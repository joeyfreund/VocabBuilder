package charlesli.com.personalvocabbuilder.controller;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import charlesli.com.personalvocabbuilder.R;
import charlesli.com.personalvocabbuilder.sqlDatabase.VocabCursorAdapter;
import charlesli.com.personalvocabbuilder.sqlDatabase.VocabDbContract;
import charlesli.com.personalvocabbuilder.sqlDatabase.VocabDbHelper;

/**
 * Created by charles on 2016-04-28.
 */
public abstract class CategoryItem extends ActionBarActivity {

    private void deleteVocab(ListView listView, VocabDbHelper dbHelper, String tableName,
                             VocabCursorAdapter cursorAdapter, Cursor cursor) {
        boolean checkBoxSelected = false;
        for (int i = 0; i < listView.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) listView.getChildAt(i).findViewById(R.id.editCheckbox);
            if (checkBox.isChecked()) {
                checkBoxSelected = true;
                TextView vocab = (TextView) listView.getChildAt(i).findViewById(R.id.vocabName);
                String vocabText = (String) vocab.getText();

                // Delete Vocab from Database*****************************************
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                // Define 'where' part of query
                String selection = VocabDbContract.COLUMN_NAME_VOCAB + " LIKE ?";
                // Specify arguments in placeholder order
                String[] selectionArgs = {vocabText};
                // Issue SQL statement
                db.delete(tableName, selection, selectionArgs);
            }
        }
        if (checkBoxSelected) {
            // Update Cursor
            cursor = dbHelper.getCursorMyVocab(tableName);
            cursorAdapter.changeCursor(cursor);
        }
        else {
            Toast.makeText(this, "No words are selected", Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < listView.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) listView.getChildAt(i).findViewById(R.id.editCheckbox);
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
            }
        }
    }

    private void addVocabToMyVocab(ListView listView, VocabDbHelper dbHelper) {
        boolean checkBoxSelected = false;
        for (int i = 0; i < listView.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) listView.getChildAt(i).findViewById(R.id.editCheckbox);
            if (checkBox.isChecked()) {
                checkBoxSelected = true;
                TextView vocab = (TextView) listView.getChildAt(i).findViewById(R.id.vocabName);
                String vocabText = (String) vocab.getText();
                TextView definition = (TextView) listView.getChildAt(i).findViewById(R.id.vocabDefinition);
                String definitionText = (String) definition.getText();
                ImageView level = (ImageView) listView.getChildAt(i).findViewById(R.id.vocabLevel);
                int levelNum = (int) level.getTag();
                dbHelper.insertVocab(VocabDbContract.TABLE_NAME_MY_VOCAB, vocabText, definitionText, levelNum);
            }
        }
        if (!checkBoxSelected) {
            Toast.makeText(this, "No words are selected", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Added to My Vocab", Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < listView.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) listView.getChildAt(i).findViewById(R.id.editCheckbox);
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
            }
        }
    }

    private void addVocabAlertDialog(final VocabDbHelper dbHelper, final String tableName,
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
                // Update Cursor
                Cursor cursor = dbHelper.getCursorMyVocab(tableName);
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

    private void editVocabAlertDialog(final String selectedVocab, View view, int position,
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

                // which row to update, based on the ID
                String selection = VocabDbContract._ID + " LIKE ?";
                String[] selectionArgs = {String.valueOf(id)};

                int count = db.update(
                        tableName,
                        values,
                        selection,
                        selectionArgs
                );

                // which row to update, based on the VOCAB
                String selectionMyVocab = VocabDbContract.COLUMN_NAME_VOCAB + " LIKE ?";
                String[] selectionArgsMyVocab = {selectedVocab};

                int countMyVocab = db.update(
                        VocabDbContract.TABLE_NAME_MY_VOCAB,
                        values,
                        selectionMyVocab,
                        selectionArgsMyVocab
                );

                // Update Cursor
                cursorAdapter.changeCursor(dbHelper.getCursorMyVocab(tableName));
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
                cursorAdapter.changeCursor(dbHelper.getCursorMyVocab(tableName));
            }
        });

        builder.show();
    }

}
