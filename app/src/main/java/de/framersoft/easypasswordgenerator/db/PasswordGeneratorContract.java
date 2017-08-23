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
package de.framersoft.easypasswordgenerator.db;

import android.provider.BaseColumns;
/**
 * Contract class for the Database of the Application.
 * It contains table and column names for the Tables of the Database.
 * @author Tobias Hess
 * @since 23.08.2017
 */
public final class PasswordGeneratorContract {

    /**
     * inner class for the table saved_password.
     * @author Tobias Hess
     * @since 23.08.2017
     */
    public static class SavedPassword implements BaseColumns{
        public static final String TABLE_NAME = "saved_password";
        public static final String COLUMN_PASSWORD = "password";

        public static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_PASSWORD + " TEXT NOT NULL" +
                ");";
        public static final String SQL_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    /**
     * private constructor, so the class cant be instantiated
     * @author Tobias Hess
     * @since 23.08.2017
     */
    private PasswordGeneratorContract(){}

}
