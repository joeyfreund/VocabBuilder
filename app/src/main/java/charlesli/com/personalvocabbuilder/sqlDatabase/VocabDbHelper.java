package charlesli.com.personalvocabbuilder.sqlDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Li on 2015/4/13.
 */
public class VocabDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "VocabDatabase.db";
    private static final String DELETE_TABLE_MY_VOCAB =
            "DROP TABLE IF EXISTS " + VocabDbContract.TABLE_NAME_MY_VOCAB;
    private static final String DELETE_TABLE_MY_WORD_BANK =
            "DROP TABLE IF EXISTS " + VocabDbContract.TABLE_NAME_MY_WORD_BANK;
    private static final String DELETE_TABLE_GMAT =
            "DROP TABLE IF EXISTS " + VocabDbContract.TABLE_NAME_GMAT;
    private static final String DELETE_TABLE_GRE =
            "DROP TABLE IF EXISTS " + VocabDbContract.TABLE_NAME_GRE;
    private static final String DELETE_TABLE_CATEGORY =
            "DROP TABLE IF EXISTS " + VocabDbContract.TABLE_NAME_CATEGORY;
    private static VocabDbHelper dbInstance;
    // Table for My Vocab
    private String CREATE_TABLE_MY_VOCAB =
            "CREATE TABLE  " + VocabDbContract.TABLE_NAME_MY_VOCAB +
            " (" + VocabDbContract._ID + " INTEGER PRIMARY KEY," +
            VocabDbContract.COLUMN_NAME_VOCAB + " TEXT, " +
            VocabDbContract.COLUMN_NAME_DEFINITION + " TEXT, " +
            VocabDbContract.COLUMN_NAME_LEVEL + " INTEGER, " +
                    VocabDbContract.COLUMN_NAME_CATEGORY + " TEXT );";
    // Table for Category
    private String CREATE_TABLE_CATEGORY =
            "CREATE TABLE  " + VocabDbContract.TABLE_NAME_CATEGORY +
                    " (" + VocabDbContract._ID + " INTEGER PRIMARY KEY," +
                    VocabDbContract.COLUMN_NAME_CATEGORY + " TEXT, " +
                    VocabDbContract.COLUMN_NAME_DESCRIPTION + " TEXT );";

    private VocabDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized VocabDbHelper getDBHelper(Context context) {
        if (dbInstance == null) {
            dbInstance = new VocabDbHelper(context);
        }
        return dbInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MY_VOCAB);
        db.execSQL(CREATE_TABLE_CATEGORY);

        loadDefaultVocabTable(db, VocabDbContract.CATEGORY_NAME_GMAT, DefaultVocab.vocabGMAT, DefaultVocab.definitionGMAT);
        loadDefaultVocabTable(db, VocabDbContract.CATEGORY_NAME_GRE, DefaultVocab.vocabGRE, DefaultVocab.definitionGRE);
        loadDefaultCategoryTable(db);
    }

    private void loadDefaultVocabTable(SQLiteDatabase db, String category, String[] word, String[] definition) {
        for (int i = 0; i < word.length; i++) {
            loadDefaultVocabValue(db, category, word[i], definition[i]);
        }
    }

    private void loadDefaultVocabValue(SQLiteDatabase db, String category, String word, String definition) {
        ContentValues cv = new ContentValues();
        cv.put(VocabDbContract.COLUMN_NAME_VOCAB, word);
        cv.put(VocabDbContract.COLUMN_NAME_DEFINITION, definition);
        cv.put(VocabDbContract.COLUMN_NAME_LEVEL, 0);
        cv.put(VocabDbContract.COLUMN_NAME_CATEGORY, category);

        db.insert(VocabDbContract.TABLE_NAME_MY_VOCAB, null, cv);
    }

    private void loadDefaultCategoryTable(SQLiteDatabase db) {
        loadDefaultCategoryValue(db, VocabDbContract.CATEGORY_NAME_MY_VOCAB, "Vocab currently being learned");
        loadDefaultCategoryValue(db, VocabDbContract.CATEGORY_NAME_MY_WORD_BANK, "A collection of all your vocab");
        loadDefaultCategoryValue(db, VocabDbContract.CATEGORY_NAME_GMAT, "Graduate Management Admission Test");
        loadDefaultCategoryValue(db, VocabDbContract.CATEGORY_NAME_GRE, "Graduate Record Examination");
    }

    private void loadDefaultCategoryValue(SQLiteDatabase db, String category, String description) {
        ContentValues cv = new ContentValues();
        cv.put(VocabDbContract.COLUMN_NAME_CATEGORY, category);
        cv.put(VocabDbContract.COLUMN_NAME_DESCRIPTION, description);
        db.insert(VocabDbContract.TABLE_NAME_CATEGORY, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion <= 3) {
            db.execSQL("ALTER TABLE " + VocabDbContract.TABLE_NAME_MY_VOCAB +
                    " ADD COLUMN " + VocabDbContract.COLUMN_NAME_CATEGORY + " TEXT DEFAULT '" +
                    VocabDbContract.CATEGORY_NAME_MY_VOCAB + "'");
            db.execSQL("ALTER TABLE " + VocabDbContract.TABLE_NAME_MY_WORD_BANK +
                    " ADD COLUMN " + VocabDbContract.COLUMN_NAME_CATEGORY + " TEXT DEFAULT '" +
                    VocabDbContract.CATEGORY_NAME_MY_WORD_BANK + "'");
            db.execSQL("ALTER TABLE " + VocabDbContract.TABLE_NAME_GMAT +
                    " ADD COLUMN " + VocabDbContract.COLUMN_NAME_CATEGORY + " TEXT DEFAULT '" +
                    VocabDbContract.CATEGORY_NAME_GMAT + "'");
            db.execSQL("ALTER TABLE " + VocabDbContract.TABLE_NAME_GRE +
                    " ADD COLUMN " + VocabDbContract.COLUMN_NAME_CATEGORY + " TEXT DEFAULT '" +
                    VocabDbContract.CATEGORY_NAME_GRE + "'");
            // Combine all table data into My Vocab table
            db.execSQL("INSERT INTO " + VocabDbContract.TABLE_NAME_MY_VOCAB +
                    " (" + VocabDbContract.COLUMN_NAME_VOCAB +
                    ", " + VocabDbContract.COLUMN_NAME_DEFINITION +
                    ", " + VocabDbContract.COLUMN_NAME_LEVEL +
                    ", " + VocabDbContract.COLUMN_NAME_CATEGORY + ") " +
                    " SELECT " + VocabDbContract.COLUMN_NAME_VOCAB +
                    ", " + VocabDbContract.COLUMN_NAME_DEFINITION +
                    ", " + VocabDbContract.COLUMN_NAME_LEVEL +
                    ", " + VocabDbContract.COLUMN_NAME_CATEGORY +
                    " FROM " + VocabDbContract.TABLE_NAME_MY_WORD_BANK);
            db.execSQL("INSERT INTO " + VocabDbContract.TABLE_NAME_MY_VOCAB +
                    " (" + VocabDbContract.COLUMN_NAME_VOCAB +
                    ", " + VocabDbContract.COLUMN_NAME_DEFINITION +
                    ", " + VocabDbContract.COLUMN_NAME_LEVEL +
                    ", " + VocabDbContract.COLUMN_NAME_CATEGORY + ") " +
                    " SELECT " + VocabDbContract.COLUMN_NAME_VOCAB +
                    ", " + VocabDbContract.COLUMN_NAME_DEFINITION +
                    ", " + VocabDbContract.COLUMN_NAME_LEVEL +
                    ", " + VocabDbContract.COLUMN_NAME_CATEGORY +
                    " FROM " + VocabDbContract.TABLE_NAME_GMAT);
            db.execSQL("INSERT INTO " + VocabDbContract.TABLE_NAME_MY_VOCAB +
                    " (" + VocabDbContract.COLUMN_NAME_VOCAB +
                    ", " + VocabDbContract.COLUMN_NAME_DEFINITION +
                    ", " + VocabDbContract.COLUMN_NAME_LEVEL +
                    ", " + VocabDbContract.COLUMN_NAME_CATEGORY + ") " +
                    " SELECT " + VocabDbContract.COLUMN_NAME_VOCAB +
                    ", " + VocabDbContract.COLUMN_NAME_DEFINITION +
                    ", " + VocabDbContract.COLUMN_NAME_LEVEL +
                    ", " + VocabDbContract.COLUMN_NAME_CATEGORY +
                    " FROM " + VocabDbContract.TABLE_NAME_GRE);
            // Delete unnecessary tables
            db.execSQL(DELETE_TABLE_MY_WORD_BANK);
            db.execSQL(DELETE_TABLE_GMAT);
            db.execSQL(DELETE_TABLE_GRE);
        }
        if (oldVersion <= 4) {
            db.execSQL(CREATE_TABLE_CATEGORY);
            loadDefaultCategoryTable(db);
        }
        if (oldVersion <= 5) {
            db.execSQL("ALTER TABLE " + VocabDbContract.TABLE_NAME_CATEGORY +
                    " ADD COLUMN " + VocabDbContract.COLUMN_NAME_DESCRIPTION + " TEXT");
            db.execSQL("UPDATE " + VocabDbContract.TABLE_NAME_CATEGORY +
                    " SET " + VocabDbContract.COLUMN_NAME_DESCRIPTION +
                    " = " + "'Vocab currently being learned'" +
                    " WHERE " + VocabDbContract.COLUMN_NAME_CATEGORY +
                    " = " + "'" + VocabDbContract.CATEGORY_NAME_MY_VOCAB + "'");
            db.execSQL("UPDATE " + VocabDbContract.TABLE_NAME_CATEGORY +
                    " SET " + VocabDbContract.COLUMN_NAME_DESCRIPTION +
                    " = " + "'A collection of all your vocab'" +
                    " WHERE " + VocabDbContract.COLUMN_NAME_CATEGORY +
                    " = " + "'" + VocabDbContract.CATEGORY_NAME_MY_WORD_BANK + "'");
            db.execSQL("UPDATE " + VocabDbContract.TABLE_NAME_CATEGORY +
                    " SET " + VocabDbContract.COLUMN_NAME_DESCRIPTION +
                    " = " + "'Graduate Management Admission Test'" +
                    " WHERE " + VocabDbContract.COLUMN_NAME_CATEGORY +
                    " = " + "'" + VocabDbContract.CATEGORY_NAME_GMAT + "'");
            db.execSQL("UPDATE " + VocabDbContract.TABLE_NAME_CATEGORY +
                    " SET " + VocabDbContract.COLUMN_NAME_DESCRIPTION +
                    " = " + "'Graduate Record Examination'" +
                    " WHERE " + VocabDbContract.COLUMN_NAME_CATEGORY +
                    " = " + "'" + VocabDbContract.CATEGORY_NAME_GRE + "'");
        }

    }

    public void insertVocab(String category, String vocab, String definition, int level) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VocabDbContract.COLUMN_NAME_VOCAB, vocab);
        values.put(VocabDbContract.COLUMN_NAME_DEFINITION, definition);
        values.put(VocabDbContract.COLUMN_NAME_LEVEL, level);
        values.put(VocabDbContract.COLUMN_NAME_CATEGORY, category);

        db.insert(VocabDbContract.TABLE_NAME_MY_VOCAB, null, values);
    }

    public Cursor getVocabCursor(String category) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                VocabDbContract._ID,
                VocabDbContract.COLUMN_NAME_VOCAB,
                VocabDbContract.COLUMN_NAME_DEFINITION,
                VocabDbContract.COLUMN_NAME_LEVEL
        };

        String selection = VocabDbContract.COLUMN_NAME_CATEGORY + " = " + "'" + category + "'";

        Cursor cursor = db.query(
                VocabDbContract.TABLE_NAME_MY_VOCAB, // The table to query
                projection,                                 // The columns for the WHERE clause
                selection,                                   // The rows to return for the WHERE clause
                null,                                        // selectionArgs
                null,                                        // groupBy
                null,                                        // having
                null,                                        // orderBy
                null                                         // limit (the number of rows)
        );
        return cursor;
    }

    public Cursor getVocabCursor(String category, String orderBy) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
            VocabDbContract._ID,
            VocabDbContract.COLUMN_NAME_VOCAB,
            VocabDbContract.COLUMN_NAME_DEFINITION,
            VocabDbContract.COLUMN_NAME_LEVEL
        };

        String selection = VocabDbContract.COLUMN_NAME_CATEGORY + " = " + "'" + category + "'";

        Cursor cursor = db.query(
                VocabDbContract.TABLE_NAME_MY_VOCAB, // The table to query
                projection,                                 // The columns for the WHERE clause
                selection,                                   // The rows to return for the WHERE clause
                null,                                        // selectionArgs
                null,                                        // groupBy
                null,                                        // having
                orderBy,                                        // orderBy
                null                                         // limit (the number of rows)
        );
        return cursor;
    }

    public Cursor getVocabCursorWithStringPattern(String category, String pattern) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                VocabDbContract._ID,
                VocabDbContract.COLUMN_NAME_VOCAB,
                VocabDbContract.COLUMN_NAME_DEFINITION,
                VocabDbContract.COLUMN_NAME_LEVEL
        };

        String selection = VocabDbContract.COLUMN_NAME_CATEGORY + " = " + "'" + category + "'" +
                " AND " + "(" + VocabDbContract.COLUMN_NAME_VOCAB + " LIKE " + "'%" + pattern + "%'"
                + " OR " + VocabDbContract.COLUMN_NAME_DEFINITION + " LIKE " + "'%" + pattern + "%'" + ")";

        Cursor cursor = db.query(
                VocabDbContract.TABLE_NAME_MY_VOCAB, // The table to query
                projection,                                 // The columns for the WHERE clause
                selection,                                   // The rows to return for the WHERE clause
                null,                                        // selectionArgs
                null,                                        // groupBy
                null,                                        // having
                null,                                        // orderBy
                null                                         // limit (the number of rows)
        );
        return cursor;
    }

    public Cursor getVocabCursorWithStringPattern(String category, String pattern, String orderBy) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                VocabDbContract._ID,
                VocabDbContract.COLUMN_NAME_VOCAB,
                VocabDbContract.COLUMN_NAME_DEFINITION,
                VocabDbContract.COLUMN_NAME_LEVEL
        };

        String selection = VocabDbContract.COLUMN_NAME_CATEGORY + " = " + "'" + category + "'" +
                " AND " + "(" + VocabDbContract.COLUMN_NAME_VOCAB + " LIKE " + "'%" + pattern + "%'"
                + " OR " + VocabDbContract.COLUMN_NAME_DEFINITION + " LIKE " + "'%" + pattern + "%'" + ")";

        Cursor cursor = db.query(
                VocabDbContract.TABLE_NAME_MY_VOCAB, // The table to query
                projection,                                 // The columns for the WHERE clause
                selection,                                   // The rows to return for the WHERE clause
                null,                                        // selectionArgs
                null,                                        // groupBy
                null,                                        // having
                orderBy,                                     // orderBy
                null                                         // limit (the number of rows)
        );
        return cursor;
    }

    public void insertCategory(String name, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VocabDbContract.COLUMN_NAME_CATEGORY, name);
        values.put(VocabDbContract.COLUMN_NAME_DESCRIPTION, description);

        db.insert(VocabDbContract.TABLE_NAME_CATEGORY, null, values);
    }

    public Cursor getCategoryCursor() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                VocabDbContract._ID,
                VocabDbContract.COLUMN_NAME_CATEGORY,
                VocabDbContract.COLUMN_NAME_DESCRIPTION
        };

        Cursor cursor = db.query(
                VocabDbContract.TABLE_NAME_CATEGORY, // The table to query
                projection,                                 // The columns for the WHERE clause
                null,                                   // The rows to return for the WHERE clause
                null,                                        // selectionArgs
                null,                                        // groupBy
                null,                                        // having
                null,                                        // orderBy
                null                                         // limit (the number of rows)
        );
        return cursor;
    }

    public boolean checkIfCategoryExists(String pattern) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + VocabDbContract.TABLE_NAME_CATEGORY + " WHERE " +
                VocabDbContract.COLUMN_NAME_CATEGORY + " = " + "'" + pattern + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        return false;
    }


}

























