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

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import de.framersoft.common.password.generator.PasswordGenerator;
import de.framersoft.easypasswordgenerator.R;

/**
 * This Fragment is used for preset password generation.
 * The user can select from a list of password templates to generate
 * his passwords, that will be displayed in a list under the settings
 * for the password generation.
 * @author Tobias Hess
 * @since 27.07.2017
 */
public class PresetPasswordGenerationFragment extends APasswordGenerationFragment {

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
     * the {@link Spinner} that is used to select the passwords type
     * from
     * @author Tobias Hess
     * @since 20.07.2017
     */
    private Spinner spinnerPasswordType;

    /**
     * the current mode the activity is in
     * @author Tobias Hess
     * @since 19.07.2017
     */
    private int currentMode;

    /**
     * empty constructor (required)
     */
    public PresetPasswordGenerationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getActivity() != null) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(getString(R.string.activity_preset_password_generation_title));
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get dynamic elements of the inflated configuration view
        spinnerPasswordType = getViewAdditionalSettings().findViewById(R.id.spinner_password_type);

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
                setPasswordLengthVisibility(View.VISIBLE);
                setMinPasswordLength(5);
                break;
            case PASSWORD_MODE_COMPLEX_PASSWORDS:
                setPasswordLengthVisibility(View.VISIBLE);
                setMinPasswordLength(4);
                break;
            case PASSWORD_MODE_WPE_KEY_64:
            case PASSWORD_MODE_WPE_KEY_128:
            case PASSWORD_MODE_WPE_KEY_256:
            case PASSWORD_MODE_WPA2_KEY:
                setPasswordLengthVisibility(View.GONE);
                break;
            default:
                throw new UnsupportedOperationException("unknown password mode");
        }

        //set current mode to new one
        currentMode = mode;
    }

    @Override
    protected PasswordGenerator createPasswordGenerator() {
        switch (currentMode){
            case PASSWORD_MODE_INTERNET_PASSWORDS:
                return PasswordGenerator.getInternetPasswordGenerator(getPasswordLength());
            case PASSWORD_MODE_COMPLEX_PASSWORDS:
                return PasswordGenerator.getRandomPasswordGenerator(getPasswordLength());
            case PASSWORD_MODE_WPE_KEY_64:
                return PasswordGenerator.get64BitWEPKeyGenerator();
            case PASSWORD_MODE_WPE_KEY_128:
                return PasswordGenerator.get128BitWEPKeyGenerator();
            case PASSWORD_MODE_WPE_KEY_256:
                return PasswordGenerator.get256BitWEPKeyGenerator();
            case PASSWORD_MODE_WPA2_KEY:
                return PasswordGenerator.getWPA2KeyGenerator();
            default:
                throw new UnsupportedOperationException("unknown password generator mode");
        }
    }

    @Override
    protected int getAdditionalSettingsLayoutResource() {
        return R.layout.additional_settings_preset_password_generation;
    }

    @Override
    protected void saveAdditionalSettings() {
        if(getContext() != null) {
            SharedPreferences prefs = getContext().getSharedPreferences(getSettingsSharedPreferencesFileName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putInt(getString(R.string.shared_pref_key_preset_passwords_mode), currentMode);
            editor.apply();
        }
    }

    @Override
    protected void loadAdditionalSettings() {
        if(getContext() != null){
            SharedPreferences prefs = getContext().getSharedPreferences(getSettingsSharedPreferencesFileName(), Context.MODE_PRIVATE);

            int mode = prefs.getInt(getString(R.string.shared_pref_key_preset_passwords_mode), PASSWORD_MODE_INTERNET_PASSWORDS);
            switchMode(mode);
        }
    }

    @Override
    protected String getSettingsSharedPreferencesFileName() {
        return getString(R.string.shared_pref_file_preset_password);
    }
}
