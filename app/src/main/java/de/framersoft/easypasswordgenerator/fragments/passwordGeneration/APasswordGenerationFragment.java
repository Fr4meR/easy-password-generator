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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
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
    private static final int DEFAULT_NUMBER_OF_PASSWORDS = 8;

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
     * the view containing the additional settings
     * @author Tobias Hess
     * @since 13.08.2017
     */
    private View viewAdditionalSettings;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialize the adapter for the password ListView
        if(getActivity() != null) {
            generatedPasswordsAdapter = new GeneratedPasswordsAdapter(getActivity(), new ArrayList<>());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preset_password_generation, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //create the ListView with the Configurations as header
        ListView listViewGeneratedPasswords = view.findViewById(R.id.listView_generated_passwords);
        View presetPasswordConfiguration = getActivity().getLayoutInflater().inflate(R.layout.list_header_preset_password_configuration,
                listViewGeneratedPasswords, false);
        listViewGeneratedPasswords.addHeaderView(presetPasswordConfiguration);
        listViewGeneratedPasswords.setAdapter(generatedPasswordsAdapter);

        //get GUI elements
        ViewStub viewStubAdditionalSettings = presetPasswordConfiguration.findViewById(R.id.viewStub_additional_settings);
        textViewPasswordLengthTitle = presetPasswordConfiguration.findViewById(R.id.textView_password_length_title);
        seekBarPasswordLength = presetPasswordConfiguration.findViewById(R.id.seekBar_password_length);
        textViewPasswordLength = presetPasswordConfiguration.findViewById(R.id.textView_password_length);
        seekBarNumberOfPasswords = presetPasswordConfiguration.findViewById(R.id.seekBar_number_of_passwords);
        textViewNumberOfPasswords = presetPasswordConfiguration.findViewById(R.id.textView_number_of_passwords);
        Button buttonGenerate = presetPasswordConfiguration.findViewById(R.id.button_generate_password);

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
            listViewGeneratedPasswords.smoothScrollToPositionFromTop(1, 16, 350);
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        loadSettings();
        loadAdditionalSettings();
    }

    @Override
    public void onPause() {
        super.onPause();

        saveSettings();
        saveAdditionalSettings();
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

    protected void setPasswordLengthVisibility(int visibility){
        textViewPasswordLengthTitle.setVisibility(visibility);
        seekBarPasswordLength.setVisibility(visibility);
        textViewPasswordLength.setVisibility(visibility);
    }

    /**
     * sets the minimal password length
     * @author Tobias Hess
     * @since 13.08.2017
     * @param minPasswordLength
     *      the minimal password length to set
     */
    protected void setMinPasswordLength(int minPasswordLength){
        int newProgress = seekBarPasswordLength.getProgress() + this.minPasswordLength - minPasswordLength;
        if(newProgress < 0){
            newProgress = 0;
        }
        this.minPasswordLength = minPasswordLength;

        seekBarPasswordLength.setMax(MAX_PASSWORD_LENGTH - minPasswordLength);
        seekBarPasswordLength.setProgress(newProgress);

        refreshPasswordLengthTextView();
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

    /**
     * Saves the selected settings in the {@link android.content.SharedPreferences}
     * @author Tobias Hess
     * @since 06.02.2018
     */
    private void saveSettings(){
        if(getContext() != null) {
            SharedPreferences prefs = getContext().getSharedPreferences(getSettingsSharedPreferencesFileName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putInt(getString(R.string.shared_pref_key_general_password_length), getPasswordLength());
            editor.putInt(getString(R.string.shared_pref_key_general_number_of_passwords), getNumberOfPasswords());

            editor.apply();
        }
    }

    /**
     * Loads the previously saved settings from the {@link android.content.SharedPreferences}
     * @author Tobias Hess
     * @since 06.02.2018
     */
    private void loadSettings(){
        if(getContext() != null) {
            SharedPreferences prefs = getContext().getSharedPreferences(getSettingsSharedPreferencesFileName(), Context.MODE_PRIVATE);

            int passwordLength = prefs.getInt(getString(R.string.shared_pref_key_general_password_length), DEFAULT_PASSWORD_LENGTH);
            seekBarPasswordLength.setProgress(passwordLength - minPasswordLength);

            int numberOfPasswords = prefs.getInt(getString(R.string.shared_pref_key_general_number_of_passwords), DEFAULT_NUMBER_OF_PASSWORDS);
            seekBarNumberOfPasswords.setProgress(numberOfPasswords - 1);
        }
    }

    /**
     * Saves the selected additional settings in the {@link android.content.SharedPreferences}
     * @author Tobias Hess
     * @since 06.02.2018
     */
    protected abstract void saveAdditionalSettings();

    /**
     * Loads the previously saved additional settings from the {@link android.content.SharedPreferences}
     * @author Tobias Hess
     * @since 06.02.2018
     */
    protected abstract void loadAdditionalSettings();

    /**
     * @author Tobias Hess
     * @since 06.02.2018
     * @return
     *      the file name of the shared preferences for the fragments settings
     */
    protected abstract String getSettingsSharedPreferencesFileName();
}
