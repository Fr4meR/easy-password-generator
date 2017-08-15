/*
 * Copyright (C) 2017 Tobias Hess
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.framersoft.easypasswordgenerator.fragments.passwordGeneration;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.framersoft.common.password.generator.PasswordGenerator;
import de.framersoft.easypasswordgenerator.R;
import de.framersoft.easypasswordgenerator.adapters.generatedPasswords.GeneratedPasswordsAdapter;

/**
 * This {@link Fragment} is used for password generation fragments.
 * The user can configure settings for the password generation. What
 * settings can be used and how they are translated into a password generation
 * is for the extending classes to implement.
 * @author Tobias Hess
 * @since 12.08.2017
 */
public abstract class APasswordGenerationFragment extends Fragment {

    /**
     * the maximum password length
     * @author Tobias Hess
     * @since 19.07.2017
     */
    private static final int MAX_PASSWORD_LENGTH = 32;

    /**
     * the default password length (will be used if switching modes, starting
     * the activity, ...)
     * @author Tobias Hess
     * @since 19.07.2017
     */
    private static final int DEFAULT_PASSWORD_LENGTH = 10;

    /**
     * the default number of passwords to be generated
     * @author Tobias Hess
     * @since 25.07.2017
     */
    private static final int DEFAULT_NUMBER_OF_PASSWORDS = 5;

    /**
     * the {@link TextView} of the title for the password length
     * @author Tobias Hess
     * @since 19.07.2017
     */
    private TextView textViewPasswordLengthTitle;

    /**
     * the {@link SeekBar} for the password length
     * @author Tobias Hess
     * @since 19.07.2017
     */
    private SeekBar seekBarPasswordLength;

    /**
     * the {@link TextView} displaying the value
     * of the {@link SeekBar} for the password length
     * @author Tobias Hess
     * @since 19.07.2017
     */
    private TextView textViewPasswordLength;

    /**
     * the {@link SeekBar} for the number of passwords to generate
     * @author Tobias Hess
     * @since 21.07.2017
     */
    private SeekBar seekBarNumberOfPasswords;

    /**
     * the {@link TextView} that displays the number of passwords that will be generated
     * @author Tobias Hess
     * @since 21.07.2017
     */
    private TextView textViewNumberOfPasswords;

    /**
     * determines the minimal password length that has to be
     * used to generate
     * @author Tobias Hess
     * @since 19.07.2017
     */
    private int minPasswordLength = 3;

    /**
     * the adapter that is used to populate the data of the generated passwords
     * to the list view in the activity
     * @author Tobias Hess
     * @since 25.07.2017
     */
    private GeneratedPasswordsAdapter generatedPasswordsAdapter;

    /**
     * the {@link ImageButton} to toggle the settings for password generation with
     * @author Tobias Hess
     * @since 12.08.2017
     */
    private ImageButton imageButtonToggleSettings;

    /**
     * the {@link LinearLayout} that contains the settings for password generation
     * @author Tobias Hess
     * @since 12.08.2017
     */
    private LinearLayout linearLayoutSettingsContent;

    /**
     * the divider between the settings header and the content
     * @author Tobias Hess
     * @since 12.08.2017
     */
    private View viewSettingsHeaderDivider;

    /**
     * the view containing the additional settings
     * @author Tobias Hess
     * @since 13.08.2017
     */
    private View viewAdditionalSettings;

    /**
     * is the settings CardView collapsed?
     * @author Tobias Hess
     * @since 12.08.2017
     */
    private boolean settingsCollapsed = false;

    /**
     * the settings view
     * @author Tobias Hess
     * @since 15.08.2017
     */
    private View presetPasswordConfiguration;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialize the adapter for the password ListView
        generatedPasswordsAdapter = new GeneratedPasswordsAdapter(getActivity(), new ArrayList<>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preset_password_generation, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //create the ListView with the Configurations as header
        ListView listViewGeneratedPasswords = (ListView) view.findViewById(R.id.listView_generated_passwords);
        presetPasswordConfiguration = getActivity().getLayoutInflater().inflate(R.layout.list_header_preset_password_configuration,
                listViewGeneratedPasswords, false);
        listViewGeneratedPasswords.addHeaderView(presetPasswordConfiguration);
        listViewGeneratedPasswords.setAdapter(generatedPasswordsAdapter);

        //create toggling for the header
        imageButtonToggleSettings = (ImageButton) presetPasswordConfiguration.findViewById(R.id.imageButton_toggle_settings_collapse);
        LinearLayout linearLayoutSettingsHeader = (LinearLayout) presetPasswordConfiguration.findViewById(R.id.linearLayout_settings_header);
        linearLayoutSettingsContent = (LinearLayout) presetPasswordConfiguration.findViewById(R.id.constraintLayout_settings_content);
        viewSettingsHeaderDivider = presetPasswordConfiguration.findViewById(R.id.view_PresetPasswordSettingsDividerHeader);
        linearLayoutSettingsHeader.setOnClickListener(view1 -> toggleSettingsCard());
        imageButtonToggleSettings.setOnClickListener(view1 -> toggleSettingsCard());

        //get GUI elements
        ViewStub viewStubAdditionalSettings = (ViewStub) presetPasswordConfiguration.findViewById(R.id.viewStub_additional_settings);
        textViewPasswordLengthTitle = (TextView) presetPasswordConfiguration.findViewById(R.id.textView_password_length_title);
        seekBarPasswordLength = (SeekBar) presetPasswordConfiguration.findViewById(R.id.seekBar_password_length);
        textViewPasswordLength = (TextView) presetPasswordConfiguration.findViewById(R.id.textView_password_length);
        seekBarNumberOfPasswords = (SeekBar) presetPasswordConfiguration.findViewById(R.id.seekBar_number_of_passwords);
        textViewNumberOfPasswords = (TextView) presetPasswordConfiguration.findViewById(R.id.textView_number_of_passwords);
        Button buttonGenerate = (Button) presetPasswordConfiguration.findViewById(R.id.button_generate_password);

        //inflate the view stub with the additional settings
        viewStubAdditionalSettings.setLayoutResource(getAdditionalSettingsLayoutResource());
        viewAdditionalSettings = viewStubAdditionalSettings.inflate();


        //set the seekBar listeners so the textView for displaying the value gets
        //synced on changes
        seekBarPasswordLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                refreshPasswordLengthTextView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //not used
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //not used
            }
        });

        seekBarNumberOfPasswords.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                refreshNumberOfPasswordsTextView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //not used
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //not used
            }
        });

        //set the button listeners
        buttonGenerate.setOnClickListener(v -> {
            generatedPasswordsAdapter.setGeneratedPasswords(generatePasswords());
            collapseSettingsCard();
        });

        refreshNumberOfPasswordsTextView();
        refreshPasswordLengthTextView();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(settingsCollapsed){
            collapseSettingsCard();
        }
        else{
            expandSettingsCard();
        }
    }

    /**
     * Returns the progress of the {@link SeekBar} for the password length.
     * The password length is the progress of the {@link SeekBar} + the minimum
     * length of the password.
     * @author Tobias Hess
     * @since 19.07.2017
     * @return
     *      the length for the password
     */
    protected int getPasswordLength(){
        return seekBarPasswordLength.getProgress() + minPasswordLength;
    }

    /**
     * @author Tobias Hess
     * @since 22.07.2017
     * @return
     *      the number of passwords to generate
     */
    protected int getNumberOfPasswords(){
        return seekBarNumberOfPasswords.getProgress() + 1;
    }

    /**
     * refreshes the displayed value
     * @author Tobias Hess
     * @since 19.07.2017
     */
    @SuppressLint("SetTextI18n")
    private void refreshPasswordLengthTextView(){
        textViewPasswordLength.setText(Integer.toString(getPasswordLength()));
    }

    /**
     * sets the value of the SeekBar for the number of passwords to generate
     * to a TextView so the user can see what he is choosing
     * @author Tobias Hess
     * @since 22.07.2017
     */
    @SuppressLint("SetTextI18n")
    private void refreshNumberOfPasswordsTextView(){
        textViewNumberOfPasswords.setText(Integer.toString(getNumberOfPasswords()));
    }

    /**
     * resets the SeekBar for the number of passwords
     * @author Tobias Hess
     * @since 13.08.2017
     */
    protected void resetNumberOfPasswordsSeekBar(){
        seekBarNumberOfPasswords.setProgress(DEFAULT_NUMBER_OF_PASSWORDS - 1);
        refreshNumberOfPasswordsTextView();
    }

    protected void setPasswordLengthVisibility(int visibility){
        textViewPasswordLengthTitle.setVisibility(visibility);
        seekBarPasswordLength.setVisibility(visibility);
        textViewPasswordLength.setVisibility(visibility);
    }

    /**
     * toggles the visibility of the content section of the settings CardView
     * @author Tobias Hess
     * @since 12.08.2017
     */
    protected void toggleSettingsCard(){
        if(settingsCollapsed){
            expandSettingsCard();
        }
        else{
            collapseSettingsCard();
        }
    }

    /**
     * sets the minimal password length
     * @author Tobias Hess
     * @since 13.08.2017
     * @param minPasswordLength
     *      the minimal password length to set
     */
    protected void setMinPasswordLength(int minPasswordLength){
        this.minPasswordLength = minPasswordLength;
        seekBarPasswordLength.setMax(MAX_PASSWORD_LENGTH - minPasswordLength);
        seekBarPasswordLength.setProgress(DEFAULT_PASSWORD_LENGTH - minPasswordLength);
        refreshPasswordLengthTextView();
    }

    /**
     * collapses the settings card
     * @author Tobias Hess
     * @since 12.08.2017
     */
    protected void collapseSettingsCard(){
        imageButtonToggleSettings.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
        imageButtonToggleSettings.setContentDescription(getString(R.string.content_description_collapse_settings));
        settingsCollapsed = true;

        ValueAnimator anim = ValueAnimator.ofInt(linearLayoutSettingsContent.getHeight(), 0);
        anim.addUpdateListener(valueAnimator -> {
            int value = (Integer) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = linearLayoutSettingsContent.getLayoutParams();
            layoutParams.height = value;
            linearLayoutSettingsContent.setLayoutParams(layoutParams);
        });
        anim.setDuration(300);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                linearLayoutSettingsContent.setVisibility(View.GONE);
                viewSettingsHeaderDivider.setVisibility(View.GONE);
                ViewGroup.LayoutParams layoutParams = linearLayoutSettingsContent.getLayoutParams();
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                presetPasswordConfiguration.requestLayout();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        anim.start();
    }

    /**
     * expands the settings card
     * @author Tobias Hess
     * @since 12.08.2017
     */
    protected void expandSettingsCard(){
        linearLayoutSettingsContent.setVisibility(View.VISIBLE);
        viewSettingsHeaderDivider.setVisibility(View.VISIBLE);
        imageButtonToggleSettings.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
        imageButtonToggleSettings.setContentDescription(getString(R.string.content_description_expand_settings));
        settingsCollapsed = false;

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        linearLayoutSettingsContent.measure(widthSpec, heightSpec);

        ValueAnimator anim = ValueAnimator.ofInt(0, linearLayoutSettingsContent.getMeasuredHeight());
        anim.addUpdateListener(valueAnimator -> {
            int value = (Integer) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = linearLayoutSettingsContent.getLayoutParams();
            layoutParams.height = value;
            linearLayoutSettingsContent.setLayoutParams(layoutParams);
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                ViewGroup.LayoutParams layoutParams = linearLayoutSettingsContent.getLayoutParams();
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                presetPasswordConfiguration.requestLayout();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        anim.setDuration(300);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }

    /**
     * creates the password generator instance and returns it
     * @author Tobias Hess
     * @since 13.08.2017
     * @return
     *      the created password generator instance
     */
    protected abstract PasswordGenerator createPasswordGenerator();

    /**
     * returns the layout for the additional settings for password generation
     * @author Tobias Hess
     * @since 13.08.2017
     * @return
     *      the id of the layout resource to add as additional settings
     */
    protected abstract int getAdditionalSettingsLayoutResource();

    /**
     * generates the set number of passwords with the set password mode and the set length of the password
     * @author Tobias Hess
     * @since 20.07.2017
     * @throws UnsupportedOperationException
     *      gets thrown if there is no password generator existing for the current password mode
     * @return
     *      the generated passwords as a List
     */
    private List<String> generatePasswords(){
                PasswordGenerator generator = createPasswordGenerator();
        ArrayList<String> generatedPasswords = new ArrayList<>(getNumberOfPasswords());
        for(int i = 0; i < getNumberOfPasswords(); i++){
            generatedPasswords.add(generator.generatePassword());
        }

        return generatedPasswords;
    }

    /**
     * @author Tobias Hess
     * @since 13.08.2017
     * @return
     *      the view that contains the additional settings provided
     *      by the xml layout returned by getAdditionalSettingsLayoutResource()
     */
    protected View getViewAdditionalSettings(){
        return viewAdditionalSettings;
    }
}
