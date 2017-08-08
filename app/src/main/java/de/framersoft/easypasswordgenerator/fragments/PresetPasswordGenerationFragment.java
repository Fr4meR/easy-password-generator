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
package de.framersoft.easypasswordgenerator.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.framersoft.common.password.generator.PasswordGenerator;
import de.framersoft.easypasswordgenerator.R;
import de.framersoft.easypasswordgenerator.adapters.generatedPasswords.GeneratedPasswordsAdapter;

/**
 * This {@link Fragment} is used for preset password generation.
 * The user can select from a list of password templates to generate
 * his passwords, that will be displayed in a list under the settings
 * for the password generation.
 * @author Tobias Hess
 * @since 27.07.2017
 */
public class PresetPasswordGenerationFragment extends Fragment {

    /*
     * constants for the different password modes. the number of the mode
      * represents also the index of the string-array found in the strings.xml
      * with which the spinner is populated
     */
    private static final int PASSWORD_MODE_INTERNET_PASSWORDS = 0;
    private static final int PASSWORD_MODE_COMPLEX_PASSWORDS = 1;
    private static final int PASSWORD_MODE_WPE_KEY_64 = 2;
    private static final int PASSWORD_MODE_WPE_KEY_128 = 3;
    private static final int PASSWORD_MODE_WPE_KEY_256 = 4;
    private static final int PASSWORD_MODE_WPA2_KEY = 5;

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
     * the {@link Spinner} that is used to select the passwords type
     * from
     * @author Tobias Hess
     * @since 20.07.2017
     */
    private Spinner spinnerPasswordType;

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
    private int minPasswordLength;

    /**
     * the current mode the activity is in
     * @author Tobias Hess
     * @since 19.07.2017
     */
    private int currentMode;

    /**
     * the adapter that is used to populate the data of the generated passwords
     * to the list view in the activity
     * @author Tobias Hess
     * @since 25.07.2017
     */
    private GeneratedPasswordsAdapter generatedPasswordsAdapter;

    /**
     * empty constructor (required)
     */
    public PresetPasswordGenerationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(getString(R.string.activity_preset_password_generation_title));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize the adapter for the password listview
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
        View presetPasswordConfiguration = getActivity().getLayoutInflater().inflate(R.layout.list_header_preset_password_configuration,
                listViewGeneratedPasswords, false);
        listViewGeneratedPasswords.addHeaderView(presetPasswordConfiguration);
        listViewGeneratedPasswords.setAdapter(generatedPasswordsAdapter);

        //get dynamic elements of the inflated configuration view
        spinnerPasswordType = (Spinner) presetPasswordConfiguration.findViewById(R.id.spinner_password_type);
        textViewPasswordLengthTitle = (TextView) presetPasswordConfiguration.findViewById(R.id.textView_password_length_title);
        seekBarPasswordLength = (SeekBar) presetPasswordConfiguration.findViewById(R.id.seekBar_password_length);
        textViewPasswordLength = (TextView) presetPasswordConfiguration.findViewById(R.id.textView_password_length);
        seekBarNumberOfPasswords = (SeekBar) presetPasswordConfiguration.findViewById(R.id.seekBar_number_of_passwords);
        textViewNumberOfPasswords = (TextView) presetPasswordConfiguration.findViewById(R.id.textView_number_of_passwords);
        Button buttonGenerate = (Button) presetPasswordConfiguration.findViewById(R.id.button_generate_password);

        //set the spinner listeners so a selection in the spinner will change the mode
        spinnerPasswordType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switchMode(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //not used
            }
        });

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
        buttonGenerate.setOnClickListener(v -> generatedPasswordsAdapter.setGeneratedPasswords(generatePasswords()));

        //starting mode: internet passwords
        switchMode(PASSWORD_MODE_INTERNET_PASSWORDS);
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
    private int getPasswordLength(){
        return seekBarPasswordLength.getProgress() + minPasswordLength;
    }

    /**
     * @author Tobias Hess
     * @since 22.07.2017
     * @return
     *      the number of passwords to generate
     */
    private int getNumberOfPasswords(){
        return seekBarNumberOfPasswords.getProgress() + 1;
    }

    /**
     * refreshes the displayed value
     * @author Tobias Hess
     * @since 19.07.2017
     */
    private void refreshPasswordLengthTextView(){
        textViewPasswordLength.setText(Integer.toString(getPasswordLength()));
    }

    /**
     * sets the value of the seekbar for the number of passwords to generate
     * to a textview so the user can see what he is choosing
     * @author Tobias Hess
     * @since 22.07.2017
     */
    private void refreshNumberOfPasswordsTextView(){
        textViewNumberOfPasswords.setText(Integer.toString(getNumberOfPasswords()));
    }

    /**
     * switches the mode (= types of passwords that are generated) of the activity
     * to the given mode constant
     * @param mode
     *      the mode constant to switch to
     * @throws UnsupportedOperationException
     *      gets thrown if a mode is given that is not existing
     */
    private void switchMode(int mode){
        //make sure the right item is selected in the spinner
        spinnerPasswordType.setSelection(mode);

        switch(mode){
            case PASSWORD_MODE_INTERNET_PASSWORDS:
                textViewPasswordLengthTitle.setVisibility(View.VISIBLE);
                seekBarPasswordLength.setVisibility(View.VISIBLE);
                textViewPasswordLength.setVisibility(View.VISIBLE);
                minPasswordLength = 5;
                break;
            case PASSWORD_MODE_COMPLEX_PASSWORDS:
                textViewPasswordLengthTitle.setVisibility(View.VISIBLE);
                seekBarPasswordLength.setVisibility(View.VISIBLE);
                textViewPasswordLength.setVisibility(View.VISIBLE);
                minPasswordLength = 3;
                break;
            case PASSWORD_MODE_WPE_KEY_64:
            case PASSWORD_MODE_WPE_KEY_128:
            case PASSWORD_MODE_WPE_KEY_256:
            case PASSWORD_MODE_WPA2_KEY:
                textViewPasswordLengthTitle.setVisibility(View.GONE);
                seekBarPasswordLength.setVisibility(View.GONE);
                textViewPasswordLength.setVisibility(View.GONE);
                break;
            default:
                throw new UnsupportedOperationException("unknown password mode");
        }

        //set new seekbar values
        seekBarPasswordLength.setMax(MAX_PASSWORD_LENGTH - minPasswordLength);
        seekBarPasswordLength.setProgress(DEFAULT_PASSWORD_LENGTH - minPasswordLength);
        refreshPasswordLengthTextView();
        seekBarNumberOfPasswords.setProgress(DEFAULT_NUMBER_OF_PASSWORDS - 1);
        refreshNumberOfPasswordsTextView();

        //set currentmode to new one
        currentMode = mode;
    }

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
        PasswordGenerator generator;
        switch (currentMode){
            case PASSWORD_MODE_INTERNET_PASSWORDS:
                generator = PasswordGenerator.getInternetPasswordGenerator(getPasswordLength());
                break;
            case PASSWORD_MODE_COMPLEX_PASSWORDS:
                generator = PasswordGenerator.getRandomPasswordGenerator(getPasswordLength());
                break;
            case PASSWORD_MODE_WPE_KEY_64:
                generator = PasswordGenerator.get64BitWEPKeyGenerator();
                break;
            case PASSWORD_MODE_WPE_KEY_128:
                generator = PasswordGenerator.get128BitWEPKeyGenerator();
                break;
            case PASSWORD_MODE_WPE_KEY_256:
                generator = PasswordGenerator.get256BitWEPKeyGenerator();
                break;
            case PASSWORD_MODE_WPA2_KEY:
                generator = PasswordGenerator.getWPA2KeyGenerator();
                break;
            default:
                throw new UnsupportedOperationException("unknown password generator mode");
        }

        ArrayList<String> generatedPasswords = new ArrayList<>(getNumberOfPasswords());
        for(int i = 0; i < getNumberOfPasswords(); i++){
            generatedPasswords.add(generator.generatePassword());
        }

        return generatedPasswords;
    }
}
