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


public class MyWordBank extends ActionBarActivity {

    private VocabCursorAdapter mVocabAdapter;
    private ListView mWordBankListView;
    private TextView mEmptyTextView;
    private Cursor mCursor;
    private String mSelectedVocab;

    private VocabDbHelper mDbHelper = new VocabDbHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_word_bank);

        mWordBankListView = (ListView) findViewById(R.id.mWordBankList);
        mEmptyTextView = (TextView) findViewById(android.R.id.empty);
        mWordBankListView.setEmptyView(mEmptyTextView);
        mCursor = mDbHelper.getCursorMyVocab(mDbHelper, VocabDbContract.DatabaseInfo.TABLE_NAME_MY_WORD_BANK);
        mVocabAdapter = new VocabCursorAdapter(this, mCursor, 0);
        mWordBankListView.setAdapter(mVocabAdapter);
        mWordBankListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
        getMenuInflater().inflate(R.menu.menu_my_word_bank, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_vocab_word_bank_button) {
            addVocabAlertDialog();
        }
        else if (id == R.id.del_my_vocab_button) {
            boolean checkBoxSelected = false;
            for (int i = 0; i < mWordBankListView.getChildCount(); i++) {
                CheckBox checkBox = (CheckBox) mWordBankListView.getChildAt(i).findViewById(R.id.editCheckbox);
                if (checkBox.isChecked()) {
                    checkBoxSelected = true;
                    TextView vocab = (TextView) mWordBankListView.getChildAt(i).findViewById(R.id.vocabName);
                    String vocabText = (String) vocab.getText();

                    // Delete Vocab from Database*****************************************
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    // Define 'where' part of query
                    String selection = VocabDbContract.DatabaseInfo.COLUMN_NAME_VOCAB + " LIKE ?";
                    // Specify arguments in placeholder order
                    String[] selectionArgs = {vocabText};
                    // Issue SQL statement
                    db.delete(VocabDbContract.DatabaseInfo.TABLE_NAME_MY_WORD_BANK, selection, selectionArgs);
                }
            }
            if (checkBoxSelected) {
                // Update Cursor
                mCursor = mDbHelper.getCursorMyVocab(mDbHelper, VocabDbContract.DatabaseInfo.TABLE_NAME_MY_WORD_BANK);
                mVocabAdapter.changeCursor(mCursor);
            }
            else {
                Toast.makeText(this, "No words are selected", Toast.LENGTH_SHORT).show();
            }
            for (int i = 0; i < mWordBankListView.getChildCount(); i++) {
                CheckBox checkBox = (CheckBox) mWordBankListView.getChildAt(i).findViewById(R.id.editCheckbox);
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                }
            }
        }
        else if (id == R.id.label_my_vocab_button) {
            boolean checkBoxSelected = false;
            for (int i = 0; i < mWordBankListView.getChildCount(); i++) {
                CheckBox checkBox = (CheckBox) mWordBankListView.getChildAt(i).findViewById(R.id.editCheckbox);
                if (checkBox.isChecked()) {
                    checkBoxSelected = true;
                    TextView vocab = (TextView) mWordBankListView.getChildAt(i).findViewById(R.id.vocabName);
                    String vocabText = (String) vocab.getText();
                    TextView definition = (TextView) mWordBankListView.getChildAt(i).findViewById(R.id.vocabDefinition);
                    String definitionText = (String) definition.getText();
                    ImageView level = (ImageView) mWordBankListView.getChildAt(i).findViewById(R.id.vocabLevel);
                    int levelNum = (int) level.getTag();
                    mDbHelper.insertVocab(mDbHelper, VocabDbContract.DatabaseInfo.TABLE_NAME_MY_VOCAB, vocabText, definitionText, levelNum);
                }
            }
            if (!checkBoxSelected) {
                Toast.makeText(this, "No words are selected", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Added to My Vocab", Toast.LENGTH_SHORT).show();
            }
            for (int i = 0; i < mWordBankListView.getChildCount(); i++) {
                CheckBox checkBox = (CheckBox) mWordBankListView.getChildAt(i).findViewById(R.id.editCheckbox);
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                }
            }
        }

        return super.onOptionsItemSelected(item);
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
                mDbHelper.insertVocab(mDbHelper, VocabDbContract.DatabaseInfo.TABLE_NAME_MY_WORD_BANK, vocab, definition, 0);
                // Update Cursor
                mCursor = mDbHelper.getCursorMyVocab(mDbHelper, VocabDbContract.DatabaseInfo.TABLE_NAME_MY_WORD_BANK);
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
                values.put(VocabDbContract.DatabaseInfo.COLUMN_NAME_DEFINITION, definition);

                // which row to update, based on the ID
                String selection = VocabDbContract.DatabaseInfo._ID + " LIKE ?";
                String[] selectionArgs = {String.valueOf(id)};

                int count = db.update(
                        VocabDbContract.DatabaseInfo.TABLE_NAME_MY_WORD_BANK,
                        values,
                        selection,
                        selectionArgs
                );

                // which row to update, based on the VOCAB
                String selectionMyVocab = VocabDbContract.DatabaseInfo.COLUMN_NAME_VOCAB + " LIKE ?";
                String[] selectionArgsMyVocab = {selectedVocab};

                int countMyVocab = db.update(
                        VocabDbContract.DatabaseInfo.TABLE_NAME_MY_VOCAB,
                        values,
                        selectionMyVocab,
                        selectionArgsMyVocab
                );

                // Update Cursor
                mCursor = mDbHelper.getCursorMyVocab(mDbHelper, VocabDbContract.DatabaseInfo.TABLE_NAME_MY_WORD_BANK);
                mVocabAdapter.changeCursor(mCursor);
            }
        });
        builder.setNegativeButton("Delete Vocab", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete Vocab from Database*****************************************
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                // Define 'where' part of query
                String selection = VocabDbContract.DatabaseInfo._ID + " LIKE ?";
                // Specify arguments in placeholder order
                String[] selectionArgs = {String.valueOf(id)};
                // Issue SQL statement
                db.delete(VocabDbContract.DatabaseInfo.TABLE_NAME_MY_WORD_BANK, selection, selectionArgs);

                // Update Cursor
                mCursor = mDbHelper.getCursorMyVocab(mDbHelper, VocabDbContract.DatabaseInfo.TABLE_NAME_MY_WORD_BANK);
                mVocabAdapter.changeCursor(mCursor);
            }
        });

        builder.show();
    }

}