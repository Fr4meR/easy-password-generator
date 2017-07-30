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

/**
 * Contains a text and a corresponding subtext
 * @author Tobias Hess
 * @since 30.07.2017
 */

public class TextSubtext {

    /**
     * the text
     * @author Tobias Hess
     * @since 30.07.2017
     */
    private final String text;

    /**
     * the subtext corresponding to the text
     * @author Tobias Hess
     * @since 30.07.2017
     */
    private final String subtext;

    /**
     * constructor
     * @author Tobias Hess
     * @since 30.07.2017
     * @param text
     *      the text to add as main text
     * @param subtext
     *      the text to add as subtext
     */
    public TextSubtext(String text, String subtext){
        this.text = text;
        this.subtext = subtext;
    }

    /**
     * @author Tobias Hess
     * @since 30.07.2017
     * @return
     *      the main text
     */
    public String getText(){
        return this.text;
    }

    /**
     * @author Tobias Hess
     * @since 30.07.2017
     * @return
     *      the subtext
     */
    public String getSubtext(){
        return this.subtext;
    }
}
