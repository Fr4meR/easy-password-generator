/**
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
package de.framersoft.easypasswordgenerator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import de.framersoft.common.password.generator.PasswordGenerator;
import de.framersoft.common.password.strength.PasswordStrength;
import de.framersoft.easypasswordgenerator.view.ratingBar.RatingBarView;

public class PresetPasswordGeneration extends AppCompatActivity {

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
     * the {@link Button} that is used to generate a new password
     * @author Tobias Hess
     * @since 20.07.2017
     */
    private Button buttonGenerate;

    /**
     * the {@link TextView} that is used to display the generated password
     * @author Tobias Hess
     * @since 20.07.2017
     */
    private TextView textViewPassword;

    /**
     * the {@link RatingBarView} used to display the password strength rating
     * of the generated password
     * @author Tobias Hess
     * @since 21.07.2017
     */
    private RatingBarView ratingBarViewPasswordRating;

    /**
     * the {@link ImageButton} that is used to copy the password to the clipboard
     * @author Tobias Hess
     * @since 20.07.2017
     */
    private ImageButton imageButtonCopyToClipboard;

    /**
     * the banner at the bottom
     * @author Tobias Hess
     * @since 20.07.2017
     */
    private AdView adViewBottomBanner;

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
     * the current generated password
     * @author Tobias Hess
     * @since 20.07.2017
     */
    private String generatedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preset_password_generation);

        //initialize admob sdk
        MobileAds.initialize(this, getString(R.string.admob_app_id));

        //get dynamic elements of the activity
        spinnerPasswordType = (Spinner) findViewById(R.id.spinner_password_type);
        textViewPasswordLengthTitle = (TextView) findViewById(R.id.textView_password_length_title);
        seekBarPasswordLength = (SeekBar) findViewById(R.id.seekBar_password_length);
        textViewPasswordLength = (TextView) findViewById(R.id.textView_password_length);
        buttonGenerate = (Button) findViewById(R.id.button_generate_password);
        textViewPassword = (TextView) findViewById(R.id.textView_password);
        imageButtonCopyToClipboard = (ImageButton) findViewById(R.id.imageButton_copy_to_clipboard);
        adViewBottomBanner = (AdView) findViewById(R.id.adView_bottom_banner);
        ratingBarViewPasswordRating = (RatingBarView) findViewById(R.id.ratingBarView_password_rating);

        //set ActionBar title
        final ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(getString(R.string.activity_preset_password_generation_title));
        }

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

        //set the button listeners
        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatedPassword = generatePassword();
                textViewPassword.setText(generatedPassword);

                int rating = PasswordStrength.calculatePasswordStrengthRating(generatedPassword);
                if(rating <= 2){
                    ratingBarViewPasswordRating.setRating(1);
                }
                else if(rating == 3){
                    ratingBarViewPasswordRating.setRating(2);
                }
                else{
                    ratingBarViewPasswordRating.setRating(3);
                }
            }
        });

        //set the listeners for the copy to clipboard button
        imageButtonCopyToClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(generatedPassword != null && !generatedPassword.isEmpty()) {
                    //put password in clipboard
                    ClipData cd = ClipData.newPlainText(getString(R.string.clip_data_label_password), generatedPassword);
                    ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    cm.setPrimaryClip(cd);

                    //inform user
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.activity_preset_password_generation_password_copied_to_clipboard),
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.activity_preset_password_generation_password_needed),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //load ad
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewBottomBanner.loadAd(adRequest);

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
     * refreshes the displayed value
     * @author Tobias Hess
     * @since 19.07.2017
     */
    private void refreshPasswordLengthTextView(){
        textViewPasswordLength.setText(Integer.toString(getPasswordLength()));
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

        //clear generated passwords
        generatedPassword = null;
        textViewPassword.setText("");

        //set currentmode to new one
        currentMode = mode;
    }

    /**
     * generates a password with the set password mode and the set length of the password
     * @author Tobias Hess
     * @since 20.07.2017
     * @throws UnsupportedOperationException
     *      gets thrown if there is no password generator existing for the current password mode
     * @return
     *      the generated password
     */
    private String generatePassword(){
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

        return generator.generatePassword();
    }
}
