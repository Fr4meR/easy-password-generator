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

/**
 * This class represents an entry in the InfoFragment's List
 * @author Tobias Hess
 * @since 12.08.2017
 */
public class InfoEntry {

    /**
     * entry content type: simpel text
     * @author Tobias Hess
     * @since 12.08.2017
     */
    public static final int TYPE_CONTENT_TEXT = 0;

    /**
     * entry content type: email
     * @author Tobias Hess
     * @since 12.08.2017
     */
    public static final int TYPE_CONTENT_EMAIL = 1;

    /**
     * entry content type: url
     * @author Tobias Hess
     * @since 12.08.2017
     */
    public static final int TYPE_CONTENT_URL = 2;

    /**
     * the header of the entry
     * @author Tobias Hess
     * @since 12.08.2017
     */
    private final String header;


    /**
     * the content
     * @author Tobias Hess
     * @since 12.08.2017
     */
    private final String content;

    /**
     * the type
     * @author Tobias Hess
     * @since 12.08.2017
     */
    private final int type;

    /**
     * constructor
     * @author Tobias Hess
     * @since 12.08.2017
     * @param header
     *      the header of the entry
     * @param content
     *      the content of the entry
     * @param type
     *      the type of the entry
     */
    public InfoEntry(String header, String content, int type){
        this.header = header;
        this.content = content;
        this.type = type;
    }

    /**
     * @author Tobias Hess
     * @since 12.08.2017
     * @return
     *      the header of the entry
     */
    public String getHeader(){
        return header;
    }

    /**
     * @author Tobias Hess
     * @since 12.08.2017
     * @return
     *      the content of the entry
     */
    public String getContent(){
        return content;
    }

    /**
     * @author Tobias Hesss
     * @since 12.08.2017
     * @return
     *      the type of the entry
     */
    public int getType(){
        return type;
    }
}
