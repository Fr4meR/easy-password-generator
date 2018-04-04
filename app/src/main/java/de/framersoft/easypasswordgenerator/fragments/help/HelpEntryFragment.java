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
package de.framersoft.easypasswordgenerator.fragments.help;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.framersoft.easypasswordgenerator.R;
import de.framersoft.easypasswordgenerator.adapters.help.HelpEntry;

/**
 * A {@link Fragment} that is used to display the
 * help for a specific topic.
 * @author Tobias Hess
 * @since 08.08.2017
 */
public class HelpEntryFragment extends Fragment {

    /**
     * the resource id of the title
     * @author Tobias Hess
     * @since 08.08.2017
     */
    private int textTitle = R.string.help_header_missing;

    /**
     * the resource id of the content
     * @author Tobias Hess
     * @since 08.08.2017
     */
    private int textContent = R.string.help_content_missing;

    public HelpEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getActivity() != null) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(getString(textTitle));
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        if(args != null){
            textTitle = args.getInt(HelpEntry.BUNDLE_KEY_TITLE);
            textContent = args.getInt(HelpEntry.BUNDLE_KEY_CONTENT);
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help_entry, container, false);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textViewContent = view.findViewById(R.id.textView_help_content);

        //receive the html styled text
        Spanned spannedContent;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            spannedContent = Html.fromHtml(getString(textContent), Html.FROM_HTML_MODE_LEGACY);
        }
        else{
            spannedContent = Html.fromHtml(getString(textContent));
        }

        textViewContent.setText(spannedContent);
    }
}
