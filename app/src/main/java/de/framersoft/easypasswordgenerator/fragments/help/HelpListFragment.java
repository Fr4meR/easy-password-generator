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


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import de.framersoft.easypasswordgenerator.R;
import de.framersoft.easypasswordgenerator.adapters.help.HelpAdapter;
import de.framersoft.easypasswordgenerator.adapters.help.HelpEntry;

/**
 * This {@link Fragment} is used to display the selection of available help
 * sections of the App.
 * The user can select to which topic he wants to view a help.
 * @author Tobias Hess
 * @since 27.07.2017
 */
public class HelpListFragment extends Fragment {

    /**
     * interface for help entry selection
     * @author Tobias Hess
     * @since 08.08.2017
     */
    public interface OnHelpEntrySelectedListener{

        /**
         * listener for help entry selected
         * @author Tobias Hess
         * @since 08.08.2017
         * @param entry
         *      the {@link HelpEntry} object that was selected
         */
        void onHelpEntrySelected(HelpEntry entry);
    }

    /**
     * contains the ressource ids of the header mapped to a list of entries for the header
     * @author Tobias Hess
     * @since 08.08.2017
     */
    private final static TreeMap<Integer, ArrayList<HelpEntry>> helpListData = new TreeMap<>();
    static{
        //menu item entries
        ArrayList<HelpEntry> entriesMenuItems = new ArrayList<>();
        entriesMenuItems.add(new HelpEntry(R.string.help_menu_items_template_passwords,
                R.string.help_content_menu_items_template_passwords));

        //template passwords entries
        ArrayList<HelpEntry> entriesTemplatePasswords = new ArrayList<>();
        entriesTemplatePasswords.add(new HelpEntry(R.string.help_templates_internet_passwords,
                R.string.help_content_templates_internet_passwords));
        entriesTemplatePasswords.add(new HelpEntry(R.string.help_templates_complex_passwords,
                R.string.help_content_templates_complex_passwords));
        entriesTemplatePasswords.add(new HelpEntry(R.string.help_templates_wep_keys,
                R.string.help_content_templates_wep_keys));
        entriesTemplatePasswords.add(new HelpEntry(R.string.help_templates_wpa2_keys,
                R.string.help_content_templates_wpa2_keys));

        //add to the treemap
        helpListData.put(R.string.help_header_menu_items, entriesMenuItems);
        helpListData.put(R.string.help_header_templates, entriesTemplatePasswords);
    }

    /**
     * the callback method for the OnHelpEntrySelectedListener
     * @author Tobias Hess
     * @since 08.08.2017
     */
    private OnHelpEntrySelectedListener callbackOnHelpEntrySelected;

    public HelpListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //make sure the containing activity implements the OnHelpEntrySelectedListener
        try{
            callbackOnHelpEntrySelected = (OnHelpEntrySelectedListener) getActivity();
        }
        catch(ClassCastException e){
            throw new ClassCastException(getActivity().toString() + " must implement " +
                    "OnHelpEntrySelectedListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(getString(R.string.fragment_title_help));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listViewHelp = (ListView) view.findViewById(R.id.listView_help);

        //create help adapter and fill with data
        HelpAdapter helpAdapter = new HelpAdapter(getActivity());
        for(Map.Entry<Integer, ArrayList<HelpEntry>> entry : helpListData.entrySet()){
            int headerRessource = entry.getKey();
            ArrayList<HelpEntry> helpEntries = entry.getValue();

            //add the header
            helpAdapter.addHeader(getString(headerRessource));

            //add the entries
            for(HelpEntry helpEntry : helpEntries){
                helpAdapter.addItem(helpEntry);
            }
        }

        listViewHelp.setAdapter(helpAdapter);
        listViewHelp.setOnItemClickListener((parent, view1, position, id) -> {
            HelpEntry entry = (HelpEntry) view1.getTag();
            if(entry != null){
                callbackOnHelpEntrySelected.onHelpEntrySelected(entry);
            }
        });
    }
}
