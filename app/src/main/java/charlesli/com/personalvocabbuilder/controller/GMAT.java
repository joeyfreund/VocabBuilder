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
 * Created by charles on 2016-04-10.
 */
public class GMAT extends ActionBarActivity {

    private VocabCursorAdapter mVocabAdapter;
    private ListView mGMATListView;
    private Cursor mCursor;
    private String mSelectedVocab;

    private VocabDbHelper mDbHelper = new VocabDbHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmat);

        mGMATListView = (ListView) findViewById(R.id.mGMATList);
        TextView emptyTextView = (TextView) findViewById(android.R.id.empty);
        mGMATListView.setEmptyView(emptyTextView);
        mCursor = mDbHelper.getCursorMyVocab(mDbHelper, VocabDbContract.TABLE_NAME_GMAT);
        mVocabAdapter = new VocabCursorAdapter(this, mCursor, 0);
        mGMATListView.setAdapter(mVocabAdapter);
        mGMATListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedVocab = (String) ((TextView) view.findViewById(R.id.vocabName)).getText();
                editVocabAlertDialog(mSelectedVocab, view, position, id);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gmat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_vocab_gmat_button) {
            addVocabAlertDialog();
        }
        else if (id == R.id.del_gmat_button) {
            deleteVocab();
        }
        else if (id == R.id.label_gmat_button) {
            addVocabToMyVocab();
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteVocab() {
        boolean checkBoxSelected = false;
        for (int i = 0; i < mGMATListView.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) mGMATListView.getChildAt(i).findViewById(R.id.editCheckbox);
            if (checkBox.isChecked()) {
                checkBoxSelected = true;
                TextView vocab = (TextView) mGMATListView.getChildAt(i).findViewById(R.id.vocabName);
                String vocabText = (String) vocab.getText();

                // Delete Vocab from Database*****************************************
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                // Define 'where' part of query
                String selection = VocabDbContract.COLUMN_NAME_VOCAB + " LIKE ?";
                // Specify arguments in placeholder order
                String[] selectionArgs = {vocabText};
                // Issue SQL statement
                db.delete(VocabDbContract.TABLE_NAME_GMAT, selection, selectionArgs);
            }
        }
        if (checkBoxSelected) {
            // Update Cursor
            mCursor = mDbHelper.getCursorMyVocab(mDbHelper, VocabDbContract.TABLE_NAME_GMAT);
            mVocabAdapter.changeCursor(mCursor);
        }
        else {
            Toast.makeText(this, "No words are selected", Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < mGMATListView.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) mGMATListView.getChildAt(i).findViewById(R.id.editCheckbox);
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
            }
        }
    }

    private void addVocabToMyVocab() {
        boolean checkBoxSelected = false;
        for (int i = 0; i < mGMATListView.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) mGMATListView.getChildAt(i).findViewById(R.id.editCheckbox);
            if (checkBox.isChecked()) {
                checkBoxSelected = true;
                TextView vocab = (TextView) mGMATListView.getChildAt(i).findViewById(R.id.vocabName);
                String vocabText = (String) vocab.getText();
                TextView definition = (TextView) mGMATListView.getChildAt(i).findViewById(R.id.vocabDefinition);
                String definitionText = (String) definition.getText();
                ImageView level = (ImageView) mGMATListView.getChildAt(i).findViewById(R.id.vocabLevel);
                int levelNum = (int) level.getTag();
                mDbHelper.insertVocab(mDbHelper, VocabDbContract.TABLE_NAME_GMAT, vocabText, definitionText, levelNum);
            }
        }
        if (!checkBoxSelected) {
            Toast.makeText(this, "No words are selected", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Added to My Vocab", Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < mGMATListView.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) mGMATListView.getChildAt(i).findViewById(R.id.editCheckbox);
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
            }
        }
    }

    private void addVocabAlertDialog() {
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
                mDbHelper.insertVocab(mDbHelper, VocabDbContract.TABLE_NAME_GMAT, vocab, definition, 0);
                // Update Cursor
                mCursor = mDbHelper.getCursorMyVocab(mDbHelper, VocabDbContract.TABLE_NAME_GMAT);
                mVocabAdapter.changeCursor(mCursor);
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

    private void editVocabAlertDialog(final String selectedVocab, View view, int position, final long id) {
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
                SQLiteDatabase db = mDbHelper.getReadableDatabase();

                // new value for one column
                ContentValues values = new ContentValues();
                values.put(VocabDbContract.COLUMN_NAME_DEFINITION, definition);

                // which row to update, based on the ID
                String selection = VocabDbContract._ID + " LIKE ?";
                String[] selectionArgs = {String.valueOf(id)};

                int count = db.update(
                        VocabDbContract.TABLE_NAME_GMAT,
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
                mCursor = mDbHelper.getCursorMyVocab(mDbHelper, VocabDbContract.TABLE_NAME_GMAT);
                mVocabAdapter.changeCursor(mCursor);
            }
        });
        builder.setNegativeButton("Delete Vocab", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete Vocab from Database*****************************************
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                // Define 'where' part of query
                String selection = VocabDbContract._ID + " LIKE ?";
                // Specify arguments in placeholder order
                String[] selectionArgs = {String.valueOf(id)};
                // Issue SQL statement
                db.delete(VocabDbContract.TABLE_NAME_GMAT, selection, selectionArgs);

                // Update Cursor
                mCursor = mDbHelper.getCursorMyVocab(mDbHelper, VocabDbContract.TABLE_NAME_GMAT);
                mVocabAdapter.changeCursor(mCursor);
            }
        });

        builder.show();
    }
}