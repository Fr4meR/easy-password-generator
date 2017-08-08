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
package de.framersoft.easypasswordgenerator.adapters.textSubtext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.framersoft.easypasswordgenerator.R;

/**
 * Adapter to populate a list with textes and subtextes.
 * @author Tobias Hess
 * @since 30.07.2017
 */

public class TextSubtextAdapter extends BaseAdapter {


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
    private final List<TextSubtext> contents;

    public TextSubtextAdapter(Context context, List<TextSubtext> contents){
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.contents = contents;
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
        TextSubtext item = (TextSubtext) getItem(position);

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.list_item_text_subtext, parent, false);
        }

        //main text
        TextView mainText = (TextView) convertView.findViewById(R.id.textView_text);
        mainText.setText(item.getText());

        //subtext
        TextView subText = (TextView) convertView.findViewById(R.id.textView_subtext);
        subText.setText(item.getSubtext());

        return convertView;
    }
}