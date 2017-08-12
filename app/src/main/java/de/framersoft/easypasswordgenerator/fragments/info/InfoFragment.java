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
package de.framersoft.easypasswordgenerator.fragments.info;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.framersoft.common.constants.Version;
import de.framersoft.easypasswordgenerator.R;
import de.framersoft.easypasswordgenerator.adapters.info.InfoAdapter;
import de.framersoft.easypasswordgenerator.adapters.info.InfoEntry;

/**
 * This {@link Fragment} is used for the about use page.
 * It will display information about the developer and
 * the used libraries.
 * @author Tobias Hess
 * @since 27.07.2017
 */
public class InfoFragment extends Fragment {

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(getString(R.string.fragment_title_info));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //build the data structure for the list view
        String versionName;
        String buildNumber;

        try {
            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(
                    getActivity().getPackageName(), 0
            );
            versionName = packageInfo.versionName;
            final String versionCode = Integer.toString(packageInfo.versionCode);
            final String versionDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(packageInfo.lastUpdateTime));

            buildNumber = versionCode + " (" + versionDate + ")";
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "---";
            buildNumber = "---";
        }

        InfoAdapter adapter = new InfoAdapter(getActivity());
        adapter.addTextEntry(getString(R.string.info_text_author), getString(R.string.info_subtext_author));
        adapter.addEmailEntry(getString(R.string.info_text_email), getString(R.string.info_subtext_email));
        adapter.addURLEntry(getString(R.string.info_text_source_code), getString(R.string.info_subtext_source_code));
        adapter.addTextEntry(getString(R.string.info_text_version_name), versionName);
        adapter.addTextEntry(getString(R.string.info_text_version_code), buildNumber);
        adapter.addTextEntry(getString(R.string.info_text_version_common), Version.VERSION_STRING);

        ListView listViewInfo = (ListView) view.findViewById(R.id.listView_info);
        listViewInfo.setAdapter(adapter);

        //set click listener
        listViewInfo.setOnItemClickListener((adapterView, view1, i, l) ->  {
            InfoEntry entry = (InfoEntry) adapterView.getItemAtPosition(i);

            switch(entry.getType()){
                case InfoEntry.TYPE_CONTENT_EMAIL:
                    String mailTo = "mailto:" + entry.getContent() + "?subject=" + getString(R.string.info_contact_email_subject);

                    Intent intentEmail = new Intent(Intent.ACTION_SENDTO);
                    intentEmail.setData(Uri.parse(mailTo));
                    startActivity(Intent.createChooser(intentEmail, getString(R.string.info_contact_email_intent_chooser)));
                    break;
                case InfoEntry.TYPE_CONTENT_URL:
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    browserIntent.setData(Uri.parse(entry.getContent()));
                    startActivity(browserIntent);
                    break;
            }
        });
    }
}
