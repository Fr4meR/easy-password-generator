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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database Helper Class for the Applications Database.
 * @author Tobias Hess
 * @since 23.08.2017
 */
public class PasswordGeneratorDbHelper extends SQLiteOpenHelper {
    /**
     * The version number of the database. This is used
     * to determine if the database has to be updated.
     * @author Tobias Hess
     * @since 23.08.2017
     */
    public static final int DATABASE_VERSION = 1;

    /**
     * The name of the Database file.
     * @author Tobias Hess
     * @since 23.08.2017
     */
    public static final String DATABASE_NAME = "EasyPasswordGenerator.db";

    /**
     * constructor
     * @param context
     *      a context for the Database Helper class to use.
     */
    public PasswordGeneratorDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(PasswordGeneratorContract.SavedPassword.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //if the oldVersion is lower than the base version
        if(oldVersion < 1){
            sqLiteDatabase.execSQL(PasswordGeneratorContract.SavedPassword.SQL_DELETE);
            onCreate(sqLiteDatabase);
        }
    }
}
