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
package de.framersoft.easypasswordgenerator.adapters.info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import de.framersoft.easypasswordgenerator.R;

/**
 * Adapter to populate a list for the info fragment
 * @author Tobias Hess
 * @since 30.07.2017
 */

public class InfoAdapter extends BaseAdapter {


    /**
     * the layout inflater used by the adapter to
     * inflate the layout files
     * @author Tobias Hess
     * @since 30.07.2017
     */
    private final LayoutInflater layoutInflater;

    /**
     * the contents that this adapter is populating
     * @author Tobias Hess
     * @since 30.07.2017
     */
    private final List<InfoEntry> contents = new LinkedList<>();

    public InfoAdapter(Context context){
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(contents == null) return 0;

        return contents.size();
    }

    @Override
    public Object getItem(int position) {
        if(contents == null) return null;

        return contents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InfoEntry item = (InfoEntry) getItem(position);

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.list_item_text_subtext, parent, false);
        }

        //header
        TextView mainText = (TextView) convertView.findViewById(R.id.textView_text);
        mainText.setText(item.getHeader());

        //content
        TextView subText = (TextView) convertView.findViewById(R.id.textView_subtext);
        subText.setText(item.getContent());

        return convertView;
    }

    /**
     * adds an entry to the adapter
     * @author Tobias Hess
     * @since 12.08.2017
     * @param entry
     *      the {@link InfoEntry} to add
     */
    private void addEntry(InfoEntry entry){
        contents.add(entry);
    }

    /**
     * adds a simple text entry
     * @author Tobias Hess
     * @since 12.08.2017
     * @param header
     *      the header to add
     * @param content
     *      the content to add
     */
    public void addTextEntry(String header, String content){
        addEntry(new InfoEntry(header, content, InfoEntry.TYPE_CONTENT_TEXT));
    }

    /**
     * adds an email entry
     * @param header
     *      the header to add
     * @param email
     *      the email email to add
     */
    public void addEmailEntry(String header, String email){
        addEntry(new InfoEntry(header, email, InfoEntry.TYPE_CONTENT_EMAIL));
    }

    /**
     * adds an url entry
     * @param header
     *      the header of the entry
     * @param url
     *      the url to add
     */
    public void addURLEntry(String header, String url){
        addEntry(new InfoEntry(header, url, InfoEntry.TYPE_CONTENT_URL));
    }
}
