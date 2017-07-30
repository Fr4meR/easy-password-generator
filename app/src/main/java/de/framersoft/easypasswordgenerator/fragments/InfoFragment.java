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


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.framersoft.common.constants.Version;
import de.framersoft.easypasswordgenerator.R;
import de.framersoft.easypasswordgenerator.adapters.textSubtext.TextSubtext;
import de.framersoft.easypasswordgenerator.adapters.textSubtext.TextSubtextAdapter;

/**
 * This {@link Fragment} is used for the about use page.
 * It will display informations about the developer and
 * the used libraries.
 * @author Tobias Hess
 * @since 27.07.2017
 */
public class InfoFragment extends Fragment {

    /**
     * the {@link ListView} that is displaying the
     * info-data
     * @author Tobias Hess
     * @since 30.07.2017
     */
    private ListView listViewInfo;

    public InfoFragment() {
        // Required empty public constructor
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
            final String versionDate = new SimpleDateFormat("yyyy-mm-dd").format(new Date(packageInfo.lastUpdateTime));

            buildNumber = versionCode + " (" + versionDate + ")";
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "---";
            buildNumber = "---";
        }

        List<TextSubtext> contents = new ArrayList<>();
        contents.add(new TextSubtext(getString(R.string.info_text_author),
                getString(R.string.info_subtext_author)));
        contents.add(new TextSubtext(getString(R.string.info_text_email),
                getString(R.string.info_subtext_email)));
        contents.add(new TextSubtext(getString(R.string.info_text_source_code),
                getString(R.string.info_subtext_source_code)));
        contents.add(new TextSubtext(getString(R.string.info_text_version_name),
                versionName));
        contents.add(new TextSubtext(getString(R.string.info_text_version_code),
                buildNumber));
        contents.add(new TextSubtext(getString(R.string.info_text_version_common),
                Version.VERSION_STRING));

        TextSubtextAdapter adapter = new TextSubtextAdapter(getActivity(), contents);

        listViewInfo = (ListView) view.findViewById(R.id.listView_info);
        listViewInfo.setAdapter(adapter);
    }
}
