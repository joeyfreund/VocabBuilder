package charlesli.com.personalvocabbuilder.sqlDatabase;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import charlesli.com.personalvocabbuilder.R;

/**
 * Created by Li on 2015/4/13.
 */
public class VocabDbHelper extends SQLiteOpenHelper {

    private static VocabDbHelper dbInstance;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 4; // previously 3
    public static final String DATABASE_NAME = "VocabDatabase.db";


    // Table for My Vocab
    private String CREATE_TABLE_MY_VOCAB =
            "CREATE TABLE  " + VocabDbContract.TABLE_NAME_MY_VOCAB +
            " (" + VocabDbContract._ID + " INTEGER PRIMARY KEY," +
            VocabDbContract.COLUMN_NAME_VOCAB + " TEXT, " +
            VocabDbContract.COLUMN_NAME_DEFINITION + " TEXT, " +
            VocabDbContract.COLUMN_NAME_LEVEL + " INTEGER, " +
                    VocabDbContract.COLUMN_NAME_CATEGORY + " TEXT );";

    // Table for My Word Bank
    /*
    private String CREATE_TABLE_MY_WORD_BANK =
            "CREATE TABLE  " + VocabDbContract.TABLE_NAME_MY_WORD_BANK +
            " (" + VocabDbContract._ID + " INTEGER PRIMARY KEY," +
            VocabDbContract.COLUMN_NAME_VOCAB + " TEXT, " +
            VocabDbContract.COLUMN_NAME_DEFINITION + " TEXT, " +
            VocabDbContract.COLUMN_NAME_LEVEL + " INTEGER );";
    */
    // Table for GMAT
    /*
    private String CREATE_TABLE_GMAT =
            "CREATE TABLE  " + VocabDbContract.TABLE_NAME_GMAT +
                    " (" + VocabDbContract._ID + " INTEGER PRIMARY KEY," +
                    VocabDbContract.COLUMN_NAME_VOCAB + " TEXT, " +
                    VocabDbContract.COLUMN_NAME_DEFINITION + " TEXT, " +
                    VocabDbContract.COLUMN_NAME_LEVEL + " INTEGER );";
    */
    // Table for GRE
    /*
    private String CREATE_TABLE_GRE =
            "CREATE TABLE  " + VocabDbContract.TABLE_NAME_GRE +
                    " (" + VocabDbContract._ID + " INTEGER PRIMARY KEY," +
                    VocabDbContract.COLUMN_NAME_VOCAB + " TEXT, " +
                    VocabDbContract.COLUMN_NAME_DEFINITION + " TEXT, " +
                    VocabDbContract.COLUMN_NAME_LEVEL + " INTEGER );";
    */


    private static final String DELETE_TABLE_MY_VOCAB =
            "DROP TABLE IF EXISTS " + VocabDbContract.TABLE_NAME_MY_VOCAB;

    private static final String DELETE_TABLE_MY_WORD_BANK =
            "DROP TABLE IF EXISTS " + VocabDbContract.TABLE_NAME_MY_WORD_BANK;

    private static final String DELETE_TABLE_GMAT =
            "DROP TABLE IF EXISTS " + VocabDbContract.TABLE_NAME_GMAT;

    private static final String DELETE_TABLE_GRE =
            "DROP TABLE IF EXISTS " + VocabDbContract.TABLE_NAME_GRE;

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

        // Creating required tables
        db.execSQL(CREATE_TABLE_MY_VOCAB);
        /*
        db.execSQL(CREATE_TABLE_MY_WORD_BANK);
        db.execSQL(CREATE_TABLE_GMAT);
        db.execSQL(CREATE_TABLE_GRE);
        */
        loadDefaultTable(db, VocabDbContract.CATEGORY_NAME_GMAT, DefaultVocab.vocabGMAT, DefaultVocab.definitionGMAT);
        loadDefaultTable(db, VocabDbContract.CATEGORY_NAME_GRE, DefaultVocab.vocabGRE, DefaultVocab.definitionGRE);
    }

    private void loadDefaultTable(SQLiteDatabase db, String category, String[] word, String[] definition) {
        for (int i = 0; i < word.length; i++) {
            loadDefaultValue(db, category, word[i], definition[i]);
        }
    }

    private void loadDefaultValue(SQLiteDatabase db, String category, String word, String definition) {
        ContentValues cv = new ContentValues();
        cv.put(VocabDbContract.COLUMN_NAME_VOCAB, word);
        cv.put(VocabDbContract.COLUMN_NAME_DEFINITION, definition);
        cv.put(VocabDbContract.COLUMN_NAME_LEVEL, 0);
        // NEW ADDITION
        cv.put(VocabDbContract.COLUMN_NAME_CATEGORY, category);
        // NEW ADDITION
        db.insert(VocabDbContract.TABLE_NAME_MY_VOCAB, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("ALTER TABLE " + VocabDbContract.TABLE_NAME_MY_VOCAB +
                    " ADD COLUMN " + VocabDbContract.COLUMN_NAME_CATEGORY + " TEXT DEFAULT '" +
                    VocabDbContract.CATEGORY_NAME_VOCAB + "'");
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


    public Cursor getCursor(String category) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
            VocabDbContract._ID,
            VocabDbContract.COLUMN_NAME_VOCAB,
            VocabDbContract.COLUMN_NAME_DEFINITION,
            VocabDbContract.COLUMN_NAME_LEVEL
        };

        String selection = VocabDbContract.COLUMN_NAME_CATEGORY + " LIKE " + "'" + category + "'";

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

    public Cursor getCursorWithStringPattern(String category, String pattern) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                VocabDbContract._ID,
                VocabDbContract.COLUMN_NAME_VOCAB,
                VocabDbContract.COLUMN_NAME_DEFINITION,
                VocabDbContract.COLUMN_NAME_LEVEL
        };

        String selection = VocabDbContract.COLUMN_NAME_CATEGORY + " LIKE " + "'" + category + "'" +
                " AND " + VocabDbContract.COLUMN_NAME_VOCAB + " LIKE " + "'%" + pattern + "%'";

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

}

























