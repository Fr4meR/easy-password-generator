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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import de.framersoft.easypasswordgenerator.R;
import de.framersoft.easypasswordgenerator.fragments.AboutUsFragment;
import de.framersoft.easypasswordgenerator.fragments.HelpFragment;
import de.framersoft.easypasswordgenerator.fragments.PresetPasswordGenerationFragment;

public class MainActivity extends AppCompatActivity {

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

        //initialize admob sdk
        MobileAds.initialize(this, getString(R.string.admob_app_id));

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //initialize the drawerLayout and drawerToggle object
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout_main);
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
        navigationViewMain = (NavigationView) findViewById(R.id.navigationView_drawer_navigation);
        navigationViewMain.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                switch(item.getItemId()){
                    case R.id.nav_preset_password_generation:
                        openTemplatePasswordGeneration();
                        return true;
                    default:
                        return true;
                }
            }
        });

        navigationViewBottom = (NavigationView) findViewById(R.id.navigationView_drawer_navigation_bottom);
        navigationViewBottom.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                switch(item.getItemId()){
                    case R.id.nav_about_us:
                        openAboutUs();
                        return true;
                    case R.id.nav_help:
                        openHelp();
                        return true;
                    default:
                        return true;
                }
            }
        });

        adViewBottomBanner = (AdView) findViewById(R.id.adView_bottom_banner);

        //set ActionBar title
        final ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(getString(R.string.activity_preset_password_generation_title));
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //syncs the state of the navigation drawer with the toggle object
        drawerToggle.syncState();

        //load the startup fragment (template password generation)
        openTemplatePasswordGeneration(false);

        loadAds();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        renewAdmobSmartBanner();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //delegate the event of the Hamburger icon to
        //drawer toggle or use default behaviour
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    /**
     * replaces the smart banner at the bottom of the screen with a new
     * smart banner and loads the ad for the banner.
     * @author Tobias Hess
     * @since 22.07.2017
     *
     */
    private void renewAdmobSmartBanner(){
        //create new adview
        AdView newAdmobSmartBanner = new AdView(this);
        newAdmobSmartBanner.setAdSize(AdSize.SMART_BANNER);
        newAdmobSmartBanner.setAdUnitId(getString(R.string.admob_ad_id));
        newAdmobSmartBanner.setId(R.id.adView_bottom_banner);
        newAdmobSmartBanner.setLayoutParams(adViewBottomBanner.getLayoutParams());

        //remove old adview from layout
        LinearLayout linearLayoutMain = (LinearLayout) findViewById(R.id.linearLayout_main);
        linearLayoutMain.removeView(adViewBottomBanner);

        //add new adView into the layout
        linearLayoutMain.addView(newAdmobSmartBanner);

        adViewBottomBanner = newAdmobSmartBanner;
        loadAds();
    }

    /**
     * loads the ads in the activity
     * @author Tobias Hess
     * @since 22.07.2017
     */
    private void loadAds(){
        AdRequest adRequest = new AdRequest.Builder().build();
        adViewBottomBanner.loadAd(adRequest);
    }

    /**
     * will show the given Fragment in the main fragment container in
     * the layout
     * @author Tobias Hess
     * @since 27.07.2017
     * @param f
     *      the Fragment to show
     * @param useBackStack
     *      add this transaction to the back stack?
     */
    private void startFragment(Fragment f, boolean useBackStack){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frameLayout_main_fragment, f);
        if(useBackStack){
            transaction.addToBackStack(null);
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
        for(int i = 0; i < sizeBottom; i++){
            boolean setChecked = false;
            if(navigationViewBottom.getMenu().getItem(i).getItemId() == itemId){
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
     * @param useBackStack
     *      add this transaction to the back stack?
     */
    private void openTemplatePasswordGeneration(boolean useBackStack){
        Fragment f = new PresetPasswordGenerationFragment();
        startFragment(f, useBackStack);
        selectNavigationDrawerItem(R.id.nav_preset_password_generation);
    }

    /**
     * Display the template password generation fragment in the main
     * fragment container. Will add the transaction to the Fragment
     * to the Back Stack!
     * @author Tobias Hess
     * @since 27.07.2017
     */
    private void openTemplatePasswordGeneration(){
        openTemplatePasswordGeneration(true);
    }

    /**
     * Display the about us fragment in the main fragment container.
     * @author Tobias Hess
     * @since 27.07.2017
     * @param useBackStack
     *      add this transaction to the back stack?
     */
    private void openAboutUs(boolean useBackStack){
        Fragment f = new AboutUsFragment();
        startFragment(f, useBackStack);
        selectNavigationDrawerItem(R.id.nav_about_us);
    }

    /**
     * Display the about us fragment in the main fragment container.
     * Will add the transaction to the Fragment to the Back Stack!
     * @author Tobias Hess
     * @since 27.07.2017
     */
    private void openAboutUs(){
        openAboutUs(true);
    }

    /**
     * Display the help fragment in the main fragment container.
     * @author Tobias Hess
     * @since 27.07.2017
     * @param useBackStack
     *      add this transaction to the back stack?
     */
    private void openHelp(boolean useBackStack){
        Fragment f = new HelpFragment();
        startFragment(f, useBackStack);
        selectNavigationDrawerItem(R.id.nav_help);
    }

    /**
     * Display the help fragment in the main fragment container.
     * Will add the transaction to the Fragment to the Back Stack!
     * @author Tobias Hess
     * @since 27.07.2017
     */
    private void openHelp(){
        openHelp(true);
    }
}