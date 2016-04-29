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


public class MyWordBank extends CategoryItem {

    private VocabCursorAdapter mVocabAdapter;
    private ListView mWordBankListView;
    private Cursor mCursor;
    private String mSelectedVocab;

    private VocabDbHelper mDbHelper = VocabDbHelper.getDBHelper(MyWordBank.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_word_bank);

        mWordBankListView = (ListView) findViewById(R.id.mWordBankList);
        TextView emptyTextView = (TextView) findViewById(android.R.id.empty);
        mWordBankListView.setEmptyView(emptyTextView);
        mCursor = mDbHelper.getCursorMyVocab(VocabDbContract.TABLE_NAME_MY_WORD_BANK);
        mVocabAdapter = new VocabCursorAdapter(this, mCursor, 0);
        mWordBankListView.setAdapter(mVocabAdapter);
        mWordBankListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedVocab = (String) ((TextView) view.findViewById(R.id.vocabName)).getText();
                editVocabAlertDialog(mSelectedVocab, view, position, id, mDbHelper,
                        VocabDbContract.TABLE_NAME_MY_WORD_BANK, mVocabAdapter);
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
            addVocabAlertDialog(mDbHelper, VocabDbContract.TABLE_NAME_MY_WORD_BANK, mVocabAdapter);
        }
        else if (id == R.id.del_my_vocab_button) {
            deleteVocab(mWordBankListView, mDbHelper, VocabDbContract.TABLE_NAME_MY_WORD_BANK,
                    mVocabAdapter);
        }
        else if (id == R.id.label_my_vocab_button) {
            addVocabToMyVocab(mWordBankListView, mDbHelper);
        }

        return super.onOptionsItemSelected(item);
    }

}
