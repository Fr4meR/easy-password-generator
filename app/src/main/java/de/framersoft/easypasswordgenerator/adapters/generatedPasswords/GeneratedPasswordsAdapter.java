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
package de.framersoft.easypasswordgenerator.adapters.generatedPasswords;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import de.framersoft.common.password.strength.PasswordStrength;
import de.framersoft.easypasswordgenerator.R;
import de.framersoft.easypasswordgenerator.view.ratingBar.RatingBarView;

/**
 * This Adapter is used to populate a ListView with items that represent
 * generated passwords.
 * Following will be displayed by the items generated by this adapter:
 * <ul>
 *     <li>TextView with the password</li>
 *     <li>RatingBarView with a rating between 1 and 3</li>
 *     <li>A Button to copy the password to the clipboard.</li>
 * </ul>
 * @author Tobias Hess
 * @since 25.07.2017
 */
public class GeneratedPasswordsAdapter extends BaseAdapter {

    /**
     * the context the adapter is in
     * @author Tobias Hess
     * @since 25.07.2017
     */
    private final Context context;

    /**
     * the layout inflater used to create the views
     * @author Tobias Hess
     * @since 25.07.2017
     */
    private final LayoutInflater layoutInflater;

    /**
     * a list containing the passwords to populate the list with
     * @author Tobias Hess
     * @since 25.07.2017
     */
    private List<String> passwords;

    /**
     * constructor
     * @param context
     *      the context this adapter is used in
     * @param passwords
     *      the list of passwords this adapter will populate
     */
    public GeneratedPasswordsAdapter(Context context, List<String> passwords){
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.passwords = passwords;
    }

    /**
     * sets a new List of generated passwords. this will trigger notifyDataSetChanged().
     * @param passwords
     *      the list of generated passwords.
     */
    public void setGeneratedPasswords(List<String> passwords){
        this.passwords = passwords;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return this.passwords.size();
    }

    @Override
    public Object getItem(int i) {
        return this.passwords.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String password = (String) getItem(i);

        if(view == null) {
            view = layoutInflater.inflate(R.layout.list_item_generated_password, viewGroup, false);
        }

        //password text
        TextView textViewPassword = view.findViewById(R.id.textView_password);
        textViewPassword.setText(password);

        //password rating
        final int rating = PasswordStrength.calculatePasswordStrengthRating(password);
        RatingBarView ratingBarView = view.findViewById(R.id.ratingBarView_password_rating);
        if(rating <= 2){
            ratingBarView.setRating(1);
        }
        else if(rating == 3){
            ratingBarView.setRating(2);
        }
        else{
            ratingBarView.setRating(3);
        }

        //copy to clipboard
        ImageButton imageButtonCopyToClipboard = view.findViewById(R.id.imageButton_copy_to_clipboard);
        imageButtonCopyToClipboard.setOnClickListener(v -> {
            //put password in clipboard
            ClipData cd = ClipData.newPlainText(context.getString(R.string.clip_data_label_password), password);
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if(cm != null){
                cm.setPrimaryClip(cd);

                //inform user
                Toast.makeText(context,
                        context.getString(R.string.activity_preset_password_generation_password_copied_to_clipboard),
                        Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
