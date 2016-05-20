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
    private String CREATE_TABLE_MY_WORD_BANK =
            "CREATE TABLE  " + VocabDbContract.TABLE_NAME_MY_WORD_BANK +
            " (" + VocabDbContract._ID + " INTEGER PRIMARY KEY," +
            VocabDbContract.COLUMN_NAME_VOCAB + " TEXT, " +
            VocabDbContract.COLUMN_NAME_DEFINITION + " TEXT, " +
            VocabDbContract.COLUMN_NAME_LEVEL + " INTEGER );";

    // Table for GMAT
    private String CREATE_TABLE_GMAT =
            "CREATE TABLE  " + VocabDbContract.TABLE_NAME_GMAT +
                    " (" + VocabDbContract._ID + " INTEGER PRIMARY KEY," +
                    VocabDbContract.COLUMN_NAME_VOCAB + " TEXT, " +
                    VocabDbContract.COLUMN_NAME_DEFINITION + " TEXT, " +
                    VocabDbContract.COLUMN_NAME_LEVEL + " INTEGER );";

    // Table for GRE
    private String CREATE_TABLE_GRE =
            "CREATE TABLE  " + VocabDbContract.TABLE_NAME_GRE +
                    " (" + VocabDbContract._ID + " INTEGER PRIMARY KEY," +
                    VocabDbContract.COLUMN_NAME_VOCAB + " TEXT, " +
                    VocabDbContract.COLUMN_NAME_DEFINITION + " TEXT, " +
                    VocabDbContract.COLUMN_NAME_LEVEL + " INTEGER );";



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
        db.execSQL(CREATE_TABLE_MY_WORD_BANK);
        db.execSQL(CREATE_TABLE_GMAT);
        db.execSQL(CREATE_TABLE_GRE);

        loadDefaultTable(db, VocabDbContract.TABLE_NAME_GMAT, DefaultVocab.vocabGMAT, DefaultVocab.definitionGMAT);
        loadDefaultTable(db, VocabDbContract.TABLE_NAME_GRE, DefaultVocab.vocabGRE, DefaultVocab.definitionGRE);
    }

    private void loadDefaultTable(SQLiteDatabase db, String table, String[] word, String[] definition) {
        for (int i = 0; i < word.length; i++) {
            loadDefaultValue(db, table, word[i], definition[i]);
        }
    }

    private void loadDefaultValue(SQLiteDatabase db, String table, String word, String definition) {
        ContentValues cv = new ContentValues();
        cv.put(VocabDbContract.COLUMN_NAME_VOCAB, word);
        cv.put(VocabDbContract.COLUMN_NAME_DEFINITION, definition);
        cv.put(VocabDbContract.COLUMN_NAME_LEVEL, 0);
        db.insert(table, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion && oldVersion == 3) {
            db.execSQL("ALTER TABLE " + VocabDbContract.TABLE_NAME_MY_VOCAB +
                    " ADD COLUMN " + VocabDbContract.COLUMN_NAME_CATEGORY + " TEXT DEFAULT 'My Vocab'");
            db.execSQL("ALTER TABLE " + VocabDbContract.TABLE_NAME_MY_WORD_BANK +
                    " ADD COLUMN " + VocabDbContract.COLUMN_NAME_CATEGORY + " TEXT DEFAULT 'My Word Bank'");
            db.execSQL("ALTER TABLE " + VocabDbContract.TABLE_NAME_GMAT +
                    " ADD COLUMN " + VocabDbContract.COLUMN_NAME_CATEGORY + " TEXT DEFAULT 'GMAT'");
            db.execSQL("ALTER TABLE " + VocabDbContract.TABLE_NAME_GRE +
                    " ADD COLUMN " + VocabDbContract.COLUMN_NAME_CATEGORY + " TEXT DEFAULT 'GRE'");
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void insertVocab(String tableName, String vocab, String definition, int level) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new vap of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(VocabDbContract.COLUMN_NAME_VOCAB, vocab);
        values.put(VocabDbContract.COLUMN_NAME_DEFINITION, definition);
        values.put(VocabDbContract.COLUMN_NAME_LEVEL, level);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(tableName, null, values);
    }


    public Cursor getCursor(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
            VocabDbContract._ID,
            VocabDbContract.COLUMN_NAME_VOCAB,
            VocabDbContract.COLUMN_NAME_DEFINITION,
            VocabDbContract.COLUMN_NAME_LEVEL
        };

        Cursor cursor = db.query(
                tableName, // The table to query
                projection,                                 // The columns for the WHERE clause
                null,                                        // The rows to return for the WHERE clause
                null,                                        // selectionArgs
                null,                                        // groupBy
                null,                                        // having
                null,                                        // orderBy
                null                                         // limit (the number of rows)
        );
        return cursor;
    }

    public Cursor getCursorWithStringPattern(String tableName, String pattern) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                VocabDbContract._ID,
                VocabDbContract.COLUMN_NAME_VOCAB,
                VocabDbContract.COLUMN_NAME_DEFINITION,
                VocabDbContract.COLUMN_NAME_LEVEL
        };

        String selection = VocabDbContract.COLUMN_NAME_VOCAB + " LIKE " + "'%" + pattern + "%'";

        Cursor cursor = db.query(
                tableName, // The table to query
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

























