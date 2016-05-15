package charlesli.com.personalvocabbuilder.controller;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

import charlesli.com.personalvocabbuilder.R;
import charlesli.com.personalvocabbuilder.sqlDatabase.VocabCursorAdapter;
import charlesli.com.personalvocabbuilder.sqlDatabase.VocabDbContract;
import charlesli.com.personalvocabbuilder.sqlDatabase.VocabDbHelper;

/**
 * Created by charles on 2016-04-28.
 */
public abstract class CategoryItem extends ActionBarActivity {

    protected void selectAll(VocabCursorAdapter cursorAdapter, VocabDbHelper dbHelper,
                             String tableName) {
        Cursor cursor = dbHelper.getCursor(tableName);
        int numOfRows = cursor.getCount();
        for (int i = 0; i < numOfRows; i++) {
            cursorAdapter.selectedItemsPositions.add(i);
        }
        cursorAdapter.changeCursor(cursor);
    }

    protected void deleteVocab(VocabDbHelper dbHelper, String tableName, VocabCursorAdapter cursorAdapter) {
        Iterator<Integer> posIt = cursorAdapter.selectedItemsPositions.iterator();
        if (cursorAdapter.selectedItemsPositions.isEmpty()) {
            Toast.makeText(this, "No words are selected", Toast.LENGTH_SHORT).show();
        }
        else {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            while (posIt.hasNext()) {
                Integer posInt = posIt.next();
                // Define 'where' part of query
                String selection = VocabDbContract._ID + " LIKE ?";
                // Specify arguments in placeholder order
                String[] selectionArgs = {String.valueOf(cursorAdapter.getItemId(posInt))};
                // Issue SQL statement
                db.delete(tableName, selection, selectionArgs);
            }
            Cursor cursor = dbHelper.getCursor(tableName);
            cursorAdapter.changeCursor(cursor);

            cursorAdapter.selectedItemsPositions.clear();
        }
    }

    protected void selectTableToAddVocabTo(final VocabCursorAdapter cursorAdapter, final VocabDbHelper dbHelper) {
        if (cursorAdapter.selectedItemsPositions.isEmpty()) {
            Toast.makeText(this, "No words are selected", Toast.LENGTH_SHORT).show();
        }
        else {
            // Set up alert dialog
            final String[] selectedTable = new String[1];
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Vocab To...");
            // Set up the input
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);

            // Add Spinner to alert dialog
            final Spinner spinner = new Spinner(this);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.table_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String table = (String) parent.getItemAtPosition(position);
                    if (table.equals("My Vocab")) {
                        selectedTable[0] = VocabDbContract.TABLE_NAME_MY_VOCAB;
                    } else if (table.equals("My Word Bank")) {
                        selectedTable[0] = VocabDbContract.TABLE_NAME_MY_WORD_BANK;
                    } else if (table.equals("GMAT")) {
                        selectedTable[0] = VocabDbContract.TABLE_NAME_GMAT;
                    } else if (table.equals("GRE")) {
                        selectedTable[0] = VocabDbContract.TABLE_NAME_GRE;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            layout.addView(spinner);
            builder.setView(layout);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    addVocabToSelectedTable(cursorAdapter, dbHelper, selectedTable[0]);
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }
    }


    protected void addVocabToSelectedTable(VocabCursorAdapter cursorAdapter, VocabDbHelper dbHelper,
                                           String tableName) {
        Iterator<Integer> posIt = cursorAdapter.selectedItemsPositions.iterator();
        if (cursorAdapter.selectedItemsPositions.isEmpty()) {
            Toast.makeText(this, "No words are selected", Toast.LENGTH_SHORT).show();
        }
        else {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            while (posIt.hasNext()) {
                Integer posInt = posIt.next();
                Integer idInt = (int) cursorAdapter.getItemId(posInt);
                String[] projection = {
                        VocabDbContract._ID,
                        VocabDbContract.COLUMN_NAME_VOCAB,
                        VocabDbContract.COLUMN_NAME_DEFINITION,
                        VocabDbContract.COLUMN_NAME_LEVEL
                };
                String[] selectionArg = {
                        String.valueOf(idInt)
                };
                Cursor cursor = db.query(
                        tableName,
                        projection,
                        VocabDbContract._ID + "=?",
                        selectionArg,
                        null,
                        null,
                        null
                );
                cursor.moveToFirst();
                String vocab = cursor.getString(cursor.getColumnIndex(VocabDbContract.COLUMN_NAME_VOCAB));
                String definition = cursor.getString(cursor.getColumnIndex(VocabDbContract.COLUMN_NAME_DEFINITION));
                Integer level = cursor.getInt(cursor.getColumnIndex(VocabDbContract.COLUMN_NAME_LEVEL));
                dbHelper.insertVocab(VocabDbContract.TABLE_NAME_MY_VOCAB, vocab, definition, level);
            }
            cursorAdapter.selectedItemsPositions.clear();
            Cursor cursor = dbHelper.getCursor(tableName);
            cursorAdapter.changeCursor(cursor);

            Toast.makeText(this, "Added to My Vocab", Toast.LENGTH_SHORT).show();
        }
    }

    protected void addVocabAlertDialog(final VocabDbHelper dbHelper, final String tableName,
                                     final VocabCursorAdapter cursorAdapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Vocab");
        // Set up the input
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText vocabInput = new EditText(this);
        vocabInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        vocabInput.setHint("Vocab");
        layout.addView(vocabInput);

        final EditText definitionInput = new EditText(this);
        definitionInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        definitionInput.setHint("Definition");
        layout.addView(definitionInput);

        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String vocab = vocabInput.getText().toString();
                String definition = definitionInput.getText().toString();
                dbHelper.insertVocab(tableName, vocab, definition, 0);
                if (!tableName.equals(VocabDbContract.TABLE_NAME_MY_WORD_BANK)) {
                    dbHelper.insertVocab(VocabDbContract.TABLE_NAME_MY_WORD_BANK, vocab, definition, 0);
                }
                // Update Cursor
                Cursor cursor = dbHelper.getCursor(tableName);
                cursorAdapter.changeCursor(cursor);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    protected void editVocabAlertDialog(final String selectedVocab, final String selectedDefinition,
                                        final long id, final VocabDbHelper dbHelper,
                                        final String tableName, final VocabCursorAdapter cursorAdapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Vocab");
        builder.setMessage(selectedVocab);
        // Set up the input
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText definitionInput = new EditText(this);
        definitionInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        definitionInput.setHint("New Definition");
        layout.addView(definitionInput);

        builder.setView(layout);

        // Set up the buttons
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String definition = definitionInput.getText().toString();
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                // new value for one column
                ContentValues values = new ContentValues();
                values.put(VocabDbContract.COLUMN_NAME_DEFINITION, definition);

                // which row to update, based on the VOCAB
                String selectionMyVocab = VocabDbContract.COLUMN_NAME_VOCAB + " = ? AND " +
                        VocabDbContract.COLUMN_NAME_DEFINITION + " = ?";
                String[] selectionArgsMyVocab = {selectedVocab, selectedDefinition};

                db.update(
                        VocabDbContract.TABLE_NAME_MY_VOCAB,
                        values,
                        selectionMyVocab,
                        selectionArgsMyVocab
                );

                db.update(
                        VocabDbContract.TABLE_NAME_MY_WORD_BANK,
                        values,
                        selectionMyVocab,
                        selectionArgsMyVocab
                );

                db.update(
                        VocabDbContract.TABLE_NAME_GMAT,
                        values,
                        selectionMyVocab,
                        selectionArgsMyVocab
                );

                db.update(
                        VocabDbContract.TABLE_NAME_GRE,
                        values,
                        selectionMyVocab,
                        selectionArgsMyVocab
                );

                // Update Cursor
                cursorAdapter.changeCursor(dbHelper.getCursor(tableName));
            }
        });
        builder.setNegativeButton("Delete Vocab", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete Vocab from Database*****************************************
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                // Define 'where' part of query
                String selection = VocabDbContract._ID + " LIKE ?";
                // Specify arguments in placeholder order
                String[] selectionArgs = {String.valueOf(id)};
                // Issue SQL statement
                db.delete(tableName, selection, selectionArgs);

                // Update Cursor
                cursorAdapter.changeCursor(dbHelper.getCursor(tableName));
            }
        });

        builder.show();
    }

}
