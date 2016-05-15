package charlesli.com.personalvocabbuilder.controller;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
        Cursor mCursor = mDbHelper.getCursor(VocabDbContract.TABLE_NAME_GMAT);
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
            deleteVocab(mDbHelper, VocabDbContract.TABLE_NAME_GMAT, mVocabAdapter);
        }
        else if (id == R.id.label_gmat_button) {
            addVocabToMyVocab(mVocabAdapter, mDbHelper, VocabDbContract.TABLE_NAME_GMAT);
        }
        else if (id == R.id.select_all_gmat_button) {
            selectAll(mVocabAdapter, mDbHelper, VocabDbContract.TABLE_NAME_GMAT);
        }

        return super.onOptionsItemSelected(item);
    }

}
