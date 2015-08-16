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
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "VocabDatabase.db";


    // Table for My Vocab
    public String CREATE_TABLE_MY_VOCAB =
            "CREATE TABLE  " + VocabDbContract.DatabaseInfo.TABLE_NAME_MY_VOCAB +
            " (" + VocabDbContract.DatabaseInfo._ID + " INTEGER PRIMARY KEY," +
            VocabDbContract.DatabaseInfo.COLUMN_NAME_VOCAB + " TEXT, " +
            VocabDbContract.DatabaseInfo.COLUMN_NAME_DEFINITION + " TEXT, " +
            VocabDbContract.DatabaseInfo.COLUMN_NAME_LEVEL + " INTEGER );";

    // Table for My Word Bank
    public String CREATE_TABLE_MY_WORD_BANK =
            "CREATE TABLE  " + VocabDbContract.DatabaseInfo.TABLE_NAME_MY_WORD_BANK +
            " (" + VocabDbContract.DatabaseInfo._ID + " INTEGER PRIMARY KEY," +
            VocabDbContract.DatabaseInfo.COLUMN_NAME_VOCAB + " TEXT, " +
            VocabDbContract.DatabaseInfo.COLUMN_NAME_DEFINITION + " TEXT, " +
            VocabDbContract.DatabaseInfo.COLUMN_NAME_LEVEL + " INTEGER );";



    private static final String DELETE_TABLE_MY_VOCAB =
            "DROP TABLE IF EXISTS " + VocabDbContract.DatabaseInfo.TABLE_NAME_MY_VOCAB;

    private static final String DELETE_TABLE_MY_WORD_BANK =
            "DROP TABLE IF EXISTS " + VocabDbContract.DatabaseInfo.TABLE_NAME_MY_WORD_BANK;

    public VocabDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Creating required tables
        db.execSQL(CREATE_TABLE_MY_VOCAB);
        db.execSQL(CREATE_TABLE_MY_WORD_BANK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // simply to discard the data and start over
        db.execSQL(DELETE_TABLE_MY_VOCAB);
        db.execSQL(DELETE_TABLE_MY_WORD_BANK);

        // Create new tables
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void insertMyVocab(VocabDbHelper dbHelper, String vocab, String definition, int level) {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new vap of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(VocabDbContract.DatabaseInfo.COLUMN_NAME_VOCAB, vocab);
        values.put(VocabDbContract.DatabaseInfo.COLUMN_NAME_DEFINITION, definition);
        values.put(VocabDbContract.DatabaseInfo.COLUMN_NAME_LEVEL, level);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(VocabDbContract.DatabaseInfo.TABLE_NAME_MY_VOCAB, null, values);
    }

    public void insertMyWordBank(VocabDbHelper dbHelper, String vocab, String definition, int level) {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new vap of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(VocabDbContract.DatabaseInfo.COLUMN_NAME_VOCAB, vocab);
        values.put(VocabDbContract.DatabaseInfo.COLUMN_NAME_DEFINITION, definition);
        values.put(VocabDbContract.DatabaseInfo.COLUMN_NAME_LEVEL, level);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(VocabDbContract.DatabaseInfo.TABLE_NAME_MY_WORD_BANK, null, values);
    }

    public Cursor getCursorMyVocab(VocabDbHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
            VocabDbContract.DatabaseInfo._ID,
            VocabDbContract.DatabaseInfo.COLUMN_NAME_VOCAB,
            VocabDbContract.DatabaseInfo.COLUMN_NAME_DEFINITION,
            VocabDbContract.DatabaseInfo.COLUMN_NAME_LEVEL
        };

        Cursor cursor = db.query(
                VocabDbContract.DatabaseInfo.TABLE_NAME_MY_VOCAB, // The table to query
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

    public Cursor getCursorMyWordBank(VocabDbHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                VocabDbContract.DatabaseInfo._ID,
                VocabDbContract.DatabaseInfo.COLUMN_NAME_VOCAB,
                VocabDbContract.DatabaseInfo.COLUMN_NAME_DEFINITION,
                VocabDbContract.DatabaseInfo.COLUMN_NAME_LEVEL
        };

        Cursor cursor = db.query(
                VocabDbContract.DatabaseInfo.TABLE_NAME_MY_WORD_BANK, // The table to query
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
}

























