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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import de.framersoft.common.constants.AlphabetConstants;
import de.framersoft.common.password.alphabet.Alphabet;
import de.framersoft.common.password.generator.PasswordGenerator;
import de.framersoft.easypasswordgenerator.R;

/**
 * This Fragment is used for custom password generation.
 * The user can create a custom password generation by enabling /
 * disabling characters and features for the password to generate.
 * @author Tobias Hess
 * @since 13.08.2017
 */
public class CustomPasswordGeneration extends APasswordGenerationFragment {

    /*
     * GUI elements for the prefix settings
     */
    private CheckBox checkBoxPrefixAToZUpperCase;
    private CheckBox checkBoxPrefixAToZLowerCase;
    private CheckBox checkBoxPrefix0To9;
    private CheckBox checkBoxPrefixSpecialCharacters;
    private SeekBar seekBarPrefixLength;
    private TextView textViewPrefixLength;

    /*
     * GUI elements for the main section settings
     */
    private CheckBox checkBoxAToZUpperCase;
    private CheckBox checkBoxAToZLowerCase;
    private CheckBox checkBox0To9;
    private CheckBox checkBoxSpecialCharacters;

    /*
     * GUI elements for the postfix settings
     */
    private CheckBox checkBoxPostfixAToZUpperCase;
    private CheckBox checkBoxPostfixAToZLowerCase;
    private CheckBox checkBoxPostfix0To9;
    private CheckBox checkBoxPostfixSpecialCharacters;
    private SeekBar seekBarPostfixLength;
    private TextView textViewPostfixLength;

    /*
     * GUI elements used for the whole password
     */
    private CheckBox checkBoxSpeakingPassword;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View additionalSettings = getViewAdditionalSettings();

        //get GUI elements
        checkBoxPrefixAToZUpperCase = (CheckBox) additionalSettings.findViewById(R.id.checkBox_prefix_A_to_Z);
        checkBoxPrefixAToZLowerCase = (CheckBox) additionalSettings.findViewById(R.id.checkBox_prefix_a_to_z);
        checkBoxPrefix0To9 = (CheckBox) additionalSettings.findViewById(R.id.checkBox_prefix_0_to_9);
        checkBoxPrefixSpecialCharacters = (CheckBox) additionalSettings.findViewById(R.id.checkBox_prefix_special_characters);
        seekBarPrefixLength = (SeekBar) additionalSettings.findViewById(R.id.seekBar_prefix_length);
        textViewPrefixLength = (TextView) additionalSettings.findViewById(R.id.textView_prefix_length);


        checkBoxAToZUpperCase = (CheckBox) additionalSettings.findViewById(R.id.checkBox_A_to_Z);
        checkBoxAToZLowerCase = (CheckBox) additionalSettings.findViewById(R.id.checkBox_a_to_z);
        checkBox0To9 = (CheckBox) additionalSettings.findViewById(R.id.checkBox_0_to_9);
        checkBoxSpecialCharacters = (CheckBox) additionalSettings.findViewById(R.id.checkBox_special_characters);

        checkBoxPostfixAToZUpperCase = (CheckBox) additionalSettings.findViewById(R.id.checkBox_postfix_A_to_Z);
        checkBoxPostfixAToZLowerCase = (CheckBox) additionalSettings.findViewById(R.id.checkBox_postfix_a_to_z);
        checkBoxPostfix0To9 = (CheckBox) additionalSettings.findViewById(R.id.checkBox_postfix_0_to_9);
        checkBoxPostfixSpecialCharacters = (CheckBox) additionalSettings.findViewById(R.id.checkBox_postfix_special_characters);
        seekBarPostfixLength = (SeekBar) additionalSettings.findViewById(R.id.seekBar_postfix_length);
        textViewPostfixLength = (TextView) additionalSettings.findViewById(R.id.textView_postfix_length);

        checkBoxSpeakingPassword = (CheckBox) additionalSettings.findViewById(R.id.checkBox_speaking_password);

        //initializes the SeekBars by adding listeners / settings the
        //textview that displays the value
        initSeekBars();

        //initializes the CheckBoxes by adding listeners
        initCheckBoxes();
    }

    @Override
    public void onResume() {
        super.onResume();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(getString(R.string.activity_custom_password_generation_title));
        }
    }

    @Override
    protected PasswordGenerator createPasswordGenerator() {
        //prefix options
        String alphabet_prefix = "";
        if(checkBoxPrefixAToZUpperCase.isChecked()){
            alphabet_prefix += AlphabetConstants.ALPHABET_A_TO_Z_UPPERCASE;
        }
        if(checkBoxPrefixAToZLowerCase.isChecked()){
            alphabet_prefix += AlphabetConstants.ALPHABET_A_TO_Z_LOWERCASE;
        }
        if(checkBoxPrefix0To9.isChecked()){
            alphabet_prefix += AlphabetConstants.ALPHABET_NUMBERS;
        }
        if(checkBoxPrefixSpecialCharacters.isChecked()){
            alphabet_prefix += AlphabetConstants.ALPHABET_SPECIAL_CHARACTERS;
        }

        //main options
        String alphabet_main = "";
        if(checkBoxAToZUpperCase.isChecked()){
            alphabet_main += AlphabetConstants.ALPHABET_A_TO_Z_UPPERCASE;
        }
        if(checkBoxAToZLowerCase.isChecked()){
            alphabet_main += AlphabetConstants.ALPHABET_A_TO_Z_LOWERCASE;
        }
        if(checkBox0To9.isChecked()){
            alphabet_main += AlphabetConstants.ALPHABET_NUMBERS;
        }
        if(checkBoxSpecialCharacters.isChecked()){
            alphabet_main += AlphabetConstants.ALPHABET_SPECIAL_CHARACTERS;
        }

        //postfix options
        String alphabet_postfix = "";
        if(checkBoxPostfixAToZUpperCase.isChecked()){
            alphabet_postfix += AlphabetConstants.ALPHABET_A_TO_Z_UPPERCASE;
        }
        if(checkBoxPostfixAToZLowerCase.isChecked()){
            alphabet_postfix += AlphabetConstants.ALPHABET_A_TO_Z_LOWERCASE;
        }
        if(checkBoxPostfix0To9.isChecked()){
            alphabet_postfix += AlphabetConstants.ALPHABET_NUMBERS;
        }
        if(checkBoxPostfixSpecialCharacters.isChecked()){
            alphabet_postfix += AlphabetConstants.ALPHABET_SPECIAL_CHARACTERS;
        }

        PasswordGenerator generator = new PasswordGenerator(alphabet_main, getPasswordLength());
        if(!alphabet_prefix.isEmpty()){
            generator.setAlphabetPrefix(new Alphabet(alphabet_prefix));
            generator.setLengthPrefix(getPrefixLength());
        }
        if(!alphabet_postfix.isEmpty()){
            generator.setAlphabetPostfix(new Alphabet(alphabet_postfix));
            generator.setLengthPostfix(getPostfixLength());
        }
        generator.setSpeakingPassword(checkBoxSpeakingPassword.isChecked());

        return generator;
    }

    @Override
    protected int getAdditionalSettingsLayoutResource() {
        return R.layout.additional_settings_custom_password_generation;
    }

    /**
     * initializes the CheckBoxes by adding listeners to them.
     * This serves mainly the purpose of ensuring that no invalid
     * state with the CheckBoxes can be achieved.
     * @author Tobias Hess
     * @since 13.08.2017
     */
    private void initCheckBoxes(){
        checkBoxAToZUpperCase.setOnCheckedChangeListener((buttonView, isChecked) -> checkMainSectionCheckBoxSate(buttonView));
        checkBoxAToZLowerCase.setOnCheckedChangeListener((buttonView, isChecked) -> checkMainSectionCheckBoxSate(buttonView));
        checkBox0To9.setOnCheckedChangeListener((buttonView, isChecked) -> checkMainSectionCheckBoxSate(buttonView));
        checkBoxSpecialCharacters.setOnCheckedChangeListener((buttonView, isChecked) -> checkMainSectionCheckBoxSate(buttonView));

        //TODO create check if speaking password is possible
    }

    /**
     * Checks the state of the CheckBoxes for the passwords main
     * section. If something is wrong it will display a Toast
     * that informs the user that the State is invalid.
     * @param checkBox
     *      the CheckBox that is responsible for the state change
     * @author Tobias Hess
     * @since 13.08.2017
     */
    private void checkMainSectionCheckBoxSate(CompoundButton checkBox){
        //if no checkbox is checked -> invalid state
        if(!checkBoxAToZUpperCase.isChecked() && !checkBoxAToZLowerCase.isChecked() &&
                !checkBox0To9.isChecked() && !checkBoxSpecialCharacters.isChecked()){

            checkBox.setChecked(true);
            Toast.makeText(getActivity(), getString(R.string.custom_password_at_least_one_checkbox),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * initializes the SeekBars by adding the listeners to them.
     * @author Tobias Hess
     * @since 13.08.2017
     */
    private void initSeekBars(){
        //initialize the prefix length seekbars
        seekBarPrefixLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                refreshPrefixLengthSeekBar();
                //make sure that the minimal password length is at least the length of
                //pre- and postfix
                setMinPasswordLength(getPrefixLength() + getPostfixLength() + 1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //nothing to do
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //nothing to do
            }
        });
        refreshPrefixLengthSeekBar();

        //initialize the postfix length seekbar
        seekBarPostfixLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                refreshPostfixLengthSeekBar();
                //make sure that the minimal password length is at least the length of
                //pre- and postfix
                setMinPasswordLength(getPrefixLength() + getPostfixLength() + 1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //nothing to do
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //nothing to do
            }
        });
        refreshPostfixLengthSeekBar();
        //make sure that the minimal password length is at least the length of
        //pre- and postfix
        setMinPasswordLength(getPrefixLength() + getPostfixLength() + 1);
    }

    /**
     * refreshes the textview for the prefix length seekbar
     * @author Tobias Hess
     * @since 13.08.2017
     */
    private void refreshPrefixLengthSeekBar(){
        textViewPrefixLength.setText(Integer.toString(getPrefixLength()));
    }

    /**
     * refreshes the textview for the postfix length seekbar
     * @author Tobias Hess
     * @since 13.08.2017
     */
    private void refreshPostfixLengthSeekBar(){
        textViewPostfixLength.setText(Integer.toString(getPostfixLength()));
    }

    /**
     * @author Tobias Hess
     * @since 13.08.2017
     * @return
     *      the length of the prefix
     */
    private int getPrefixLength(){
        return seekBarPrefixLength.getProgress();
    }

    /**
     * @author Tobias Hess
     * @since 13.08.2017
     * @return
     *      the length of the postfix
     */
    private int getPostfixLength(){
        return seekBarPostfixLength.getProgress();
    }
}
