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
public class GMAT extends CategoryItem {

    private VocabCursorAdapter mVocabAdapter;
    private ListView mGMATListView;
    private String mSelectedVocab;
    private VocabDbHelper mDbHelper = VocabDbHelper.getDBHelper(GMAT.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmat);

        mGMATListView = (ListView) findViewById(R.id.mGMATList);
        TextView emptyTextView = (TextView) findViewById(android.R.id.empty);
        mGMATListView.setEmptyView(emptyTextView);
        Cursor mCursor = mDbHelper.getCursorMyVocab(VocabDbContract.TABLE_NAME_GMAT);
        mVocabAdapter = new VocabCursorAdapter(this, mCursor, 0);
        mGMATListView.setAdapter(mVocabAdapter);
        mGMATListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedVocab = (String) ((TextView) view.findViewById(R.id.vocabName)).getText();
                editVocabAlertDialog(mSelectedVocab, view, position, id, mDbHelper,
                        VocabDbContract.TABLE_NAME_GMAT, mVocabAdapter);
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
            addVocabAlertDialog(mDbHelper, VocabDbContract.TABLE_NAME_GMAT, mVocabAdapter);
        }
        else if (id == R.id.del_gmat_button) {
            deleteVocab(mGMATListView, mDbHelper, VocabDbContract.TABLE_NAME_GMAT, mVocabAdapter);
        }
        else if (id == R.id.label_gmat_button) {
            addVocabToMyVocab(mGMATListView, mDbHelper);
        }

        return super.onOptionsItemSelected(item);
    }

}
