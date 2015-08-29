package charlesli.com.personalvocabbuilder.io;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import charlesli.com.personalvocabbuilder.R;
import charlesli.com.personalvocabbuilder.sqlDatabase.VocabCursorAdapter;
import charlesli.com.personalvocabbuilder.sqlDatabase.VocabDbContract;
import charlesli.com.personalvocabbuilder.sqlDatabase.VocabDbHelper;


public class MyVocabActivity extends ActionBarActivity {

    private VocabCursorAdapter mVocabAdapter;
    private ListView mVocabListView;
    private TextView mEmptyTextView;
    private Cursor mCursor;
    private String mSelectedVocab;
    private CheckBox mCheckBox;

    private VocabDbHelper mDbHelper = new VocabDbHelper(this);

    private ArrayList<String> mCheckedItems = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vocab);

        mVocabListView = (ListView) findViewById(R.id.mVocabList);
        mEmptyTextView = (TextView) findViewById(android.R.id.empty);
        mVocabListView.setEmptyView(mEmptyTextView);
        mCheckBox = (CheckBox) findViewById(R.id.editCheckbox);
        mCursor = mDbHelper.getCursorMyVocab(mDbHelper);
        mVocabAdapter = new VocabCursorAdapter(this, mCursor, 0);
        mVocabListView.setAdapter(mVocabAdapter);
        mVocabListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
        getMenuInflater().inflate(R.menu.menu_my_vocab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_vocab_my_vocab_button) {
            addVocabAlertDialog();
        }
        else if (id == R.id.del_my_vocab_button) {
            for (int i = 0; i < mVocabListView.getChildCount(); i++) {
                CheckBox checkBox = (CheckBox) mVocabListView.getChildAt(i).findViewById(R.id.editCheckbox);
                if (checkBox.isChecked()) {
                    TextView vocab = (TextView) mVocabListView.getChildAt(i).findViewById(R.id.vocabName);
                    String vocabText = (String) vocab.getText();
                    Log.d("MyVocabActivity", vocabText);
                    mCheckedItems.add(vocabText);
                }
            }
            Toast.makeText(this, "Delete Vocab", Toast.LENGTH_LONG).show();
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
                mDbHelper.insertMyVocab(mDbHelper, vocab, definition, 0);
                mDbHelper.insertMyWordBank(mDbHelper, vocab, definition, 0);
                // Update Cursor
                mCursor = mDbHelper.getCursorMyVocab(mDbHelper);
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

                int countMyVocab = db.update(
                        VocabDbContract.DatabaseInfo.TABLE_NAME_MY_VOCAB,
                        values,
                        selection,
                        selectionArgs
                );

                // Update My Word Bank based on Vocab

                // which row to update, based on the VOCAB
                String selectionWordBank = VocabDbContract.DatabaseInfo.COLUMN_NAME_VOCAB + " LIKE ?";
                String[] selectionArgsWordBank = {selectedVocab};

                int countWordBank = db.update(
                        VocabDbContract.DatabaseInfo.TABLE_NAME_MY_WORD_BANK,
                        values,
                        selectionWordBank,
                        selectionArgsWordBank
                );


                // Update Cursor
                mCursor = mDbHelper.getCursorMyVocab(mDbHelper);
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
                db.delete(VocabDbContract.DatabaseInfo.TABLE_NAME_MY_VOCAB, selection, selectionArgs);

                // Update Cursor
                mCursor = mDbHelper.getCursorMyVocab(mDbHelper);
                mVocabAdapter.changeCursor(mCursor);
            }
        });

        builder.show();
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        if (checked) {
            mCheckedItems.add("hello");
        }
    }



}
