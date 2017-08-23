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
package de.framersoft.easypasswordgenerator.db.model;
/**
 * A BaseClass for creating model classes
 * for DatabaseEntries
 * @author Tobias Hess
 * @since 23.08.2017
 */
abstract public class DatabaseEntry {

    /**
     * the id of the entry
     * @author Tobias Hess
     * @since 23.08.2017
     */
    private int _id;

    /**
     * @author Tobias Hess
     * @since 23.08.2017
     * @return
     *      the id of the database entry
     */
    public int getID(){
        return _id;
    }

    /**
     * @author Tobias Hess
     * @since 23.08.2017
     * @return
     *      the name of the database table this entry is in
     */
    protected abstract String getTableName();


}
