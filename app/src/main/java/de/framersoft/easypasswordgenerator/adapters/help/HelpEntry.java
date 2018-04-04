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

import android.os.Bundle;

/**
 * This class contains the data for a single help entry
 * @author Tobias Hess
 * @since 08.08.2017
 */
public class HelpEntry {

    /**
     * bundle key: title
     * @author Tobias Hess
     * @since 08.08.2017
     */
    public final static String BUNDLE_KEY_TITLE = "help_entry_title";

    /**
     * bundle key: content
     * @author Tobias Hess
     * @since 08.08.2017
     */
    public final static String BUNDLE_KEY_CONTENT = "help_entry_content";

    /**
     * resource id of the entry title
     * @author Tobias Hess
     * @since 08.08.2017
     */
    private int title;

    /**
     * resource id of the entries contents
     * @author Tobias Hess
     * @since 08.08.2017
     */
    private int content;

    /**
     * constructor
     * @author Tobias Hess
     * @since 08.08.2017
     * @param titleResource
     *      the resource id of the title
     * @param contentResource
     *      the resource id of the content
     */
    public HelpEntry(int titleResource, int contentResource){
        setTitle(titleResource);
        setContent(contentResource);
    }

    /**
     * @author Tobias Hess
     * @since 08.08.2017
     * @return
     *      the title resource id
     */
    public int getTitle() {
        return title;
    }

    /**
     * sets the title resource id
     * @author Tobias Hess
     * @since 08.08.2017
     * @param title
     *      the resource id of the title
     */
    private void setTitle(int title) {
        this.title = title;
    }

    /**
     * @author Tobias Hess
     * @since 08.08.2017
     * @return
     *      the content resource id
     */
    public int getContent() {
        return content;
    }

    /**
     * sets the content resource id
     * @author Tobias Hess
     * @since 08.08.2017
     * @param content
     *      the resource id of the content
     */
    private void setContent(int content) {
        this.content = content;
    }

    /**
     * converts the data in this entry to a {@link Bundle}
     * that can be sent via Intent
     * @return
     *      the data as {@link Bundle}
     */
    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_TITLE, getTitle());
        bundle.putInt(BUNDLE_KEY_CONTENT, getContent());

        return bundle;
    }
}
