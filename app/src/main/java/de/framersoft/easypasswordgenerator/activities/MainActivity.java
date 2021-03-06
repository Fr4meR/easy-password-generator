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
package de.framersoft.easypasswordgenerator.activities;

import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.kobakei.ratethisapp.RateThisApp;

import de.framersoft.easypasswordgenerator.R;
import de.framersoft.easypasswordgenerator.adapters.help.HelpEntry;
import de.framersoft.easypasswordgenerator.fragments.info.InfoFragment;
import de.framersoft.easypasswordgenerator.fragments.help.HelpEntryFragment;
import de.framersoft.easypasswordgenerator.fragments.help.HelpListFragment;
import de.framersoft.easypasswordgenerator.fragments.passwordGeneration.CustomPasswordGenerationFragment;
import de.framersoft.easypasswordgenerator.fragments.passwordGeneration.PresetPasswordGenerationFragment;

/**
 * the main activity of the app. has an navigation drawer that will switch
 * the active fragment.
 * @author Tobias Hess
 * @since 30.07.2017
 */
public class MainActivity extends AppCompatActivity
        implements HelpListFragment.OnHelpEntrySelectedListener{

    /**
     * tag for the root fragment in the back stack.
     * This is used to find the root fragment in the BacksStack e.g. for popping
     * everything in the BackStack.
     * @author Tobias Hess
     * @since 17.08.2017
     */
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";

    /**
     * the drawer layout for the navigation
     * @author Tobias Hess
     * @since 08.08.2017
     */
    private DrawerLayout drawerLayout;

    /**
     * the drawer toggle object, that keeps the action bar and the
     * navigation synced.
     * @author Tobias Hess
     * @since 27.07.2017
     */
    private ActionBarDrawerToggle drawerToggle;

    /**
     * the banner at the bottom
     * @author Tobias Hess
     * @since 20.07.2017
     */
    private AdView adViewBottomBanner;

    /**
     * the main navigation list of the navigation drawer
     * @author Tobias Hess
     * @since 27.07.2017
     */
    private NavigationView navigationViewMain;

    /**
     * the footer of the navigation drawer
     * @author Tobias Hess
     * @since 27.07.2017
     */
    private NavigationView navigationViewBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize AdMob sdk
        MobileAds.initialize(this, getString(R.string.admob_app_id));

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //initialize the drawerLayout and drawerToggle object
        drawerLayout = findViewById(R.id.drawerLayout_main);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navi_drawer_open, R.string.navi_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                this.syncState();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                this.syncState();
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);

        //set listeners to the main navigation view
        navigationViewMain = findViewById(R.id.navigationView_drawer_navigation);
        navigationViewMain.setNavigationItemSelectedListener((MenuItem item) -> {
            drawerLayout.closeDrawers();
            switch(item.getItemId()){
                case R.id.nav_preset_password_generation:
                    openTemplatePasswordGeneration();
                    return true;
                case R.id.nav_custom_password_generation:
                    openCustomPasswordGeneration();
                default:
                    return true;
            }
        });

        navigationViewBottom = findViewById(R.id.navigationView_drawer_navigation_bottom);
        navigationViewBottom.setNavigationItemSelectedListener((MenuItem item) -> {
            drawerLayout.closeDrawers();
            switch(item.getItemId()){
                case R.id.nav_about_us:
                    openInfo();
                    return true;
                case R.id.nav_help:
                    openHelp();
                    return true;
                default:
                    return true;
            }
        });

        adViewBottomBanner = findViewById(R.id.adView_bottom_banner);
        adViewBottomBanner.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adViewBottomBanner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                adViewBottomBanner.setVisibility(View.GONE);
            }
        });

        rateThisAppDialog();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //syncs the state of the navigation drawer with the toggle object
        drawerToggle.syncState();

        //load the startup fragment (template password generation)
        openTemplatePasswordGeneration();

        loadAds();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        renewAdMobSmartBanner();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //delegate the event of the Hamburger icon to
        //drawer toggle or use default behaviour
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onHelpEntrySelected(HelpEntry entry) {
        HelpEntryFragment f = new HelpEntryFragment();
        f.setArguments(entry.toBundle());
        startFragment(f, false);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        super.onBackPressed();

        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.frameLayout_main_fragment);
        if(f instanceof PresetPasswordGenerationFragment){
            selectNavigationDrawerItem(R.id.nav_preset_password_generation);
        }
        else if(f instanceof CustomPasswordGenerationFragment){
            selectNavigationDrawerItem(R.id.nav_custom_password_generation);
        }
        else if(f instanceof HelpListFragment || f instanceof HelpEntryFragment){
            selectNavigationDrawerItem(R.id.nav_help);
        }
        else if(f instanceof InfoFragment){
            selectNavigationDrawerItem(R.id.nav_about_us);
        }

    }

    /**
     * replaces the smart banner at the bottom of the screen with a new
     * smart banner and loads the ad for the banner.
     * @author Tobias Hess
     * @since 22.07.2017
     *
     */
    private void renewAdMobSmartBanner(){
        //create new adView
        AdView newAdMobSmartBanner = new AdView(this);
        newAdMobSmartBanner.setAdSize(AdSize.SMART_BANNER);
        newAdMobSmartBanner.setAdUnitId(getString(R.string.admob_ad_id));
        newAdMobSmartBanner.setId(R.id.adView_bottom_banner);
        newAdMobSmartBanner.setLayoutParams(adViewBottomBanner.getLayoutParams());

        //remove old AdView from layout
        LinearLayout linearLayoutMain = findViewById(R.id.linearLayout_main);
        linearLayoutMain.removeView(adViewBottomBanner);

        //add new adView into the layout
        linearLayoutMain.addView(newAdMobSmartBanner);

        adViewBottomBanner = newAdMobSmartBanner;
        adViewBottomBanner.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adViewBottomBanner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                adViewBottomBanner.setVisibility(View.GONE);
            }
        });
        loadAds();
    }

    /**
     * loads the ads in the activity
     * @author Tobias Hess
     * @since 22.07.2017
     */
    private void loadAds(){
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewBottomBanner.setVisibility(View.GONE);
        adViewBottomBanner.loadAd(adRequest);
    }

    /**
     * will show the given Fragment in the main fragment container in
     * the layout
     * @author Tobias Hess
     * @since 27.07.2017
     * @param f
     *      the Fragment to show
     * @param root
     *      Is the Fragment f a root fragment? Meaning is f a Fragment that sits on Top-Level
     *
     */
    private void startFragment(Fragment f, boolean root){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frameLayout_main_fragment, f);
        if(root){
            fm.popBackStackImmediate(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        else{
            if (fm.getBackStackEntryCount() == 0){
                transaction.addToBackStack(BACK_STACK_ROOT_TAG);
            }
            else{
                transaction.addToBackStack(null);
            }
        }
        transaction.commit();
    }

    /**
     * Selects the item of the navigation drawer with the given ID.
     * @author Tobias Hess
     * @since 27.07.2017
     * @param itemId
     *      the id of the navigation drawer item to select
     */
    private void selectNavigationDrawerItem(int itemId){
        final int sizeMain = navigationViewMain.getMenu().size();
        for(int i = 0; i < sizeMain; i++){
            boolean setChecked = false;
            if(navigationViewMain.getMenu().getItem(i).getItemId() == itemId){
                setChecked = true;
            }
            navigationViewMain.getMenu().getItem(i).setChecked(setChecked);
        }

        final int sizeBottom = navigationViewBottom.getMenu().size();
        for(int i = 0; i < sizeBottom; i++) {
            boolean setChecked = false;
            if (navigationViewBottom.getMenu().getItem(i).getItemId() == itemId) {
                setChecked = true;
            }
            navigationViewBottom.getMenu().getItem(i).setChecked(setChecked);
        }
    }

    /**
     * Display the template password generation fragment in the main
     * fragment container.
     * @author Tobias Hess
     * @since 27.07.2017
     */
    private void openTemplatePasswordGeneration(){
        Fragment f = new PresetPasswordGenerationFragment();
        startFragment(f, true);
        selectNavigationDrawerItem(R.id.nav_preset_password_generation);
    }

    /**
     * Display the custom password generation fragment in the main fragment
     * container.
     * @author Tobias Hess
     * @since 13.08.2017
     */
    private void openCustomPasswordGeneration(){
        Fragment f = new CustomPasswordGenerationFragment();
        startFragment(f, true);
        selectNavigationDrawerItem(R.id.nav_custom_password_generation);
    }

    /**
     * Display the info fragment in the main fragment container.
     * @author Tobias Hess
     * @since 27.07.2017
     */
    private void openInfo(){
        Fragment f = new InfoFragment();
        startFragment(f, true);
        selectNavigationDrawerItem(R.id.nav_about_us);
    }

    /**
     * Display the help fragment in the main fragment container.
     * @author Tobias Hess
     * @since 27.07.2017
     */
    private void openHelp(){
        Fragment f = new HelpListFragment();
        startFragment(f, true);
        selectNavigationDrawerItem(R.id.nav_help);
    }

    /**
     * initialization code for the rate this app dialog.
     * it will open a please rate this app dialog
     * if certain open / time criteria are met.
     * @author Tobias Hess
     * @since 12.08.2017
     */
    private void rateThisAppDialog(){
        RateThisApp.Config config = new RateThisApp.Config();
        config.setTitle(R.string.rate_dialog_title);
        config.setMessage(R.string.rate_dialog_message);
        config.setYesButtonText(R.string.rate_dialog_ok);
        config.setNoButtonText(R.string.rate_dialog_no);
        config.setCancelButtonText(R.string.rate_dialog_cancel);

        RateThisApp.onCreate(this);
        RateThisApp.showRateDialogIfNeeded(this);
    }
}
