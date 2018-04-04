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
package de.framersoft.easypasswordgenerator.adapters.help;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import de.framersoft.easypasswordgenerator.R;
import de.halfbit.pinnedsection.PinnedSectionListView;

/**
 * Adapter that is populating the List with the helps-table of contents.
 * @author Tobias Hess
 * @since 30.07.2017
 */
public class HelpAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {

    /**
     * item type: text
     * @author Tobias Hess
     * @since 08.08.2017
     */
    private static final int ITEM_TYPE_TEXT = 0;

    /**
     * item type: header
     * @author Tobias Hess
     * @since 08.08.2017
     */
    private static final int ITEM_TYPE_HEADER = 1;

    /**
     * item type count: the number of item types
     * @author Tobias Hess
     * @since 08.08.2017
     */
    private static final int ITEM_TYPE_COUNT = 2;

    /**
     * the context of the adapter
     * @author Tobias Hess
     * @since 08.08.2017
     */
    private final Context context;

    /**
     * the layout inflater
     * @author Tobias Hess
     * @since 08.08.2017
     */
    private final LayoutInflater layoutInflater;

    /**
     * list containing the items
     * @author Tobias Hess
     * @since 08.08.2017
     */
    private final List<String> items = new ArrayList<>();

    /**
     * a tree-set containing the positions of the headers
     * int the list of items
     * @author Tobias Hess
     * @since 08.08.2017
     */
    private final TreeSet<Integer> headerPositions = new TreeSet<>();

    /**
     * tree-map that maps the position of a help entry in the list of items
     * to the HelpEntry object
     * @author Tobias Hess
     * @since 08.08.2017
     */
    private final TreeMap<Integer, HelpEntry> entryPositionHelpEntry = new TreeMap<>();

    /**
     * constructor
     * @param context
     *      the context for this help adapter
     */
    public HelpAdapter(Context context){
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * adds an item to the list of items
     * @author Tobias Hess
     * @since 08.08.2017
     * @param item
     *      the item to add
     */
    public void addItem(HelpEntry item){
        entryPositionHelpEntry.put(items.size(), item);
        items.add(context.getString(item.getTitle()));
        notifyDataSetChanged();
    }

    /**
     * adds a header to the list
     * @author Tobias Hess
     * @since 08.08.2017
     * @param header
     *      the header to add
     */
    public void addHeader(String header){
        headerPositions.add(items.size());
        items.add(header);
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return ITEM_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if(headerPositions.contains(position)){
            return ITEM_TYPE_HEADER;
        }

        return ITEM_TYPE_TEXT;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == ITEM_TYPE_HEADER;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String text = (String) getItem(position);

        if (convertView == null){
            switch(getItemViewType(position)){
                case ITEM_TYPE_HEADER:
                    convertView = layoutInflater.inflate(R.layout.list_item_pinned_header, parent, false);
                    break;
                case ITEM_TYPE_TEXT:
                    convertView = layoutInflater.inflate(R.layout.list_item_pinned, parent, false);
                    convertView.setTag(entryPositionHelpEntry.get(position));
                    break;

            }
        }

        if(convertView != null) {
            TextView textViewText = convertView.findViewById(android.R.id.text1);
            textViewText.setText(text);
        }

        return convertView;
    }
}
