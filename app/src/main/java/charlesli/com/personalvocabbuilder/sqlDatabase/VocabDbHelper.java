package charlesli.com.personalvocabbuilder.sqlDatabase;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Li on 2015/4/13.
 */
public class VocabDbHelper extends SQLiteOpenHelper {

    private Context context;
    private static VocabDbHelper dbInstance;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "VocabDatabase.db";


    // Table for My Vocab
    private String CREATE_TABLE_MY_VOCAB =
            "CREATE TABLE  " + VocabDbContract.TABLE_NAME_MY_VOCAB +
            " (" + VocabDbContract._ID + " INTEGER PRIMARY KEY," +
            VocabDbContract.COLUMN_NAME_VOCAB + " TEXT, " +
            VocabDbContract.COLUMN_NAME_DEFINITION + " TEXT, " +
            VocabDbContract.COLUMN_NAME_LEVEL + " INTEGER );";

    // Table for My Word Bank
    private String CREATE_TABLE_MY_WORD_BANK =
            "CREATE TABLE  " + VocabDbContract.TABLE_NAME_MY_WORD_BANK +
            " (" + VocabDbContract._ID + " INTEGER PRIMARY KEY," +
            VocabDbContract.COLUMN_NAME_VOCAB + " TEXT, " +
            VocabDbContract.COLUMN_NAME_DEFINITION + " TEXT, " +
            VocabDbContract.COLUMN_NAME_LEVEL + " INTEGER );";

    // Table for My Word Bank
    private String CREATE_TABLE_GMAT =
            "CREATE TABLE  " + VocabDbContract.TABLE_NAME_GMAT +
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

        ContentValues cv = new ContentValues();
        cv.put(VocabDbContract.COLUMN_NAME_VOCAB, "Abaft");
        cv.put(VocabDbContract.COLUMN_NAME_DEFINITION, "adj: on or toward the rear of a ship");
        cv.put(VocabDbContract.COLUMN_NAME_LEVEL, 0);
        db.insert(VocabDbContract.TABLE_NAME_GMAT, null, cv);
        cv.clear();
        cv.put(VocabDbContract.COLUMN_NAME_VOCAB, "Abandon");
        cv.put(VocabDbContract.COLUMN_NAME_DEFINITION, "v: to leave behind");
        cv.put(VocabDbContract.COLUMN_NAME_LEVEL, 0);
        db.insert(VocabDbContract.TABLE_NAME_GMAT, null, cv);
        cv.clear();
        cv.put(VocabDbContract.COLUMN_NAME_VOCAB, "Abase");
        cv.put(VocabDbContract.COLUMN_NAME_DEFINITION, "v: to degrade, humiliate");
        cv.put(VocabDbContract.COLUMN_NAME_LEVEL, 0);
        db.insert(VocabDbContract.TABLE_NAME_GMAT, null, cv);
        cv.clear();
        cv.put(VocabDbContract.COLUMN_NAME_VOCAB, "Abbreviate");
        cv.put(VocabDbContract.COLUMN_NAME_DEFINITION, "v: to shorten, compress");
        cv.put(VocabDbContract.COLUMN_NAME_LEVEL, 0);
        db.insert(VocabDbContract.TABLE_NAME_GMAT, null, cv);
        cv.clear();
        cv.put(VocabDbContract.COLUMN_NAME_VOCAB, "Abdicate");
        cv.put(VocabDbContract.COLUMN_NAME_DEFINITION, "v: to reject, renounce");
        cv.put(VocabDbContract.COLUMN_NAME_LEVEL, 0);
        db.insert(VocabDbContract.TABLE_NAME_GMAT, null, cv);
        cv.clear();
        cv.put(VocabDbContract.COLUMN_NAME_VOCAB, "Abhor");
        cv.put(VocabDbContract.COLUMN_NAME_DEFINITION, "v: to hate");
        cv.put(VocabDbContract.COLUMN_NAME_LEVEL, 0);
        db.insert(VocabDbContract.TABLE_NAME_GMAT, null, cv);
        cv.clear();
        cv.put(VocabDbContract.COLUMN_NAME_VOCAB, "Abject");
        cv.put(VocabDbContract.COLUMN_NAME_DEFINITION, "v: to give up");
        cv.put(VocabDbContract.COLUMN_NAME_LEVEL, 0);
        db.insert(VocabDbContract.TABLE_NAME_GMAT, null, cv);
        cv.clear();
        cv.put(VocabDbContract.COLUMN_NAME_VOCAB, "Abnegation");
        cv.put(VocabDbContract.COLUMN_NAME_DEFINITION, "n: denial");
        cv.put(VocabDbContract.COLUMN_NAME_LEVEL, 0);
        db.insert(VocabDbContract.TABLE_NAME_GMAT, null, cv);
        cv.clear();
        cv.put(VocabDbContract.COLUMN_NAME_VOCAB, "Abominate");
        cv.put(VocabDbContract.COLUMN_NAME_DEFINITION, "v: to loathe, hate");
        cv.put(VocabDbContract.COLUMN_NAME_LEVEL, 0);
        db.insert(VocabDbContract.TABLE_NAME_GMAT, null, cv);
        cv.clear();
        cv.put(VocabDbContract.COLUMN_NAME_VOCAB, "Abridge");
        cv.put(VocabDbContract.COLUMN_NAME_DEFINITION, "v: to shorten, limit");
        cv.put(VocabDbContract.COLUMN_NAME_LEVEL, 0);
        db.insert(VocabDbContract.TABLE_NAME_GMAT, null, cv);
        cv.clear();
        cv.put(VocabDbContract.COLUMN_NAME_VOCAB, "Abrogate");
        cv.put(VocabDbContract.COLUMN_NAME_DEFINITION, "v: to cancel by authority");
        cv.put(VocabDbContract.COLUMN_NAME_LEVEL, 0);
        db.insert(VocabDbContract.TABLE_NAME_GMAT, null, cv);
        cv.clear();
        cv.put(VocabDbContract.COLUMN_NAME_VOCAB, "Abrupt");
        cv.put(VocabDbContract.COLUMN_NAME_DEFINITION, "adj: happening or ending unexpectedly");
        cv.put(VocabDbContract.COLUMN_NAME_LEVEL, 0);
        db.insert(VocabDbContract.TABLE_NAME_GMAT, null, cv);
        cv.clear();
        cv.put(VocabDbContract.COLUMN_NAME_VOCAB, "Abscond");
        cv.put(VocabDbContract.COLUMN_NAME_DEFINITION, "v: to go away hastily or secretly");
        cv.put(VocabDbContract.COLUMN_NAME_LEVEL, 0);
        db.insert(VocabDbContract.TABLE_NAME_GMAT, null, cv);
        cv.clear();
        cv.put(VocabDbContract.COLUMN_NAME_VOCAB, "Absolve");
        cv.put(VocabDbContract.COLUMN_NAME_DEFINITION, "v: to forgive");
        cv.put(VocabDbContract.COLUMN_NAME_LEVEL, 0);
        db.insert(VocabDbContract.TABLE_NAME_GMAT, null, cv);
        cv.clear();
        cv.put(VocabDbContract.COLUMN_NAME_VOCAB, "Abstemious");
        cv.put(VocabDbContract.COLUMN_NAME_DEFINITION, "adj: sparing in use of food or drinks");
        cv.put(VocabDbContract.COLUMN_NAME_LEVEL, 0);
        db.insert(VocabDbContract.TABLE_NAME_GMAT, null, cv);
        cv.clear();
        cv.put(VocabDbContract.COLUMN_NAME_VOCAB, "Abstruse");
        cv.put(VocabDbContract.COLUMN_NAME_DEFINITION, "adj: hard to understand");
        cv.put(VocabDbContract.COLUMN_NAME_LEVEL, 0);
        db.insert(VocabDbContract.TABLE_NAME_GMAT, null, cv);
        cv.clear();
        cv.put(VocabDbContract.COLUMN_NAME_VOCAB, "Abysmal");
        cv.put(VocabDbContract.COLUMN_NAME_DEFINITION, "adj: very deep");
        cv.put(VocabDbContract.COLUMN_NAME_LEVEL, 0);
        db.insert(VocabDbContract.TABLE_NAME_GMAT, null, cv);
        cv.clear();
        cv.put(VocabDbContract.COLUMN_NAME_VOCAB, "Accede");
        cv.put(VocabDbContract.COLUMN_NAME_DEFINITION, "v: to comply with");
        cv.put(VocabDbContract.COLUMN_NAME_LEVEL, 0);
        db.insert(VocabDbContract.TABLE_NAME_GMAT, null, cv);
        cv.clear();
        cv.put(VocabDbContract.COLUMN_NAME_VOCAB, "Acclaim");
        cv.put(VocabDbContract.COLUMN_NAME_DEFINITION, "v: loud approval");
        cv.put(VocabDbContract.COLUMN_NAME_LEVEL, 0);
        db.insert(VocabDbContract.TABLE_NAME_GMAT, null, cv);
        cv.clear();
        cv.put(VocabDbContract.COLUMN_NAME_VOCAB, "Accrue");
        cv.put(VocabDbContract.COLUMN_NAME_DEFINITION, "v: a natural growth; a periodic increase");
        cv.put(VocabDbContract.COLUMN_NAME_LEVEL, 0);
        db.insert(VocabDbContract.TABLE_NAME_GMAT, null, cv);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // simply to discard the data and start over
        db.execSQL(DELETE_TABLE_MY_VOCAB);
        db.execSQL(DELETE_TABLE_MY_WORD_BANK);
        db.execSQL(DELETE_TABLE_GMAT);

        // Create new tables
        onCreate(db);
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


    public Cursor getCursorMyVocab(String tableName) {
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

}

























