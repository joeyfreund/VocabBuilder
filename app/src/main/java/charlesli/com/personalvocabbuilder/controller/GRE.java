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
 * Created by Li on 2016/4/27.
 */
public class GRE extends CategoryItem {

    private VocabCursorAdapter mVocabAdapter;
    private ListView mGREListView;
    private VocabDbHelper mDbHelper = VocabDbHelper.getDBHelper(GRE.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gre);

        mGREListView = (ListView) findViewById(R.id.mGREList);
        TextView emptyTextView = (TextView) findViewById(android.R.id.empty);
        mGREListView.setEmptyView(emptyTextView);
        Cursor mCursor = mDbHelper.getVocabCursor(VocabDbContract.CATEGORY_NAME_GRE);
        mVocabAdapter = new VocabCursorAdapter(this, mCursor, 0);
        mGREListView.setAdapter(mVocabAdapter);
        mGREListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedVocab = (String) ((TextView) view.findViewById(R.id.vocabName)).getText();
                String selectedDefinition = (String) ((TextView) view.findViewById(R.id.vocabDefinition)).getText();
                editVocabAlertDialog(selectedVocab, selectedDefinition, id, mDbHelper,
                        VocabDbContract.CATEGORY_NAME_GRE, mVocabAdapter);
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gre, menu);

        implementSearchBar(menu, R.id.search_gre_button, VocabDbContract.CATEGORY_NAME_GRE,
                mVocabAdapter, mDbHelper);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_vocab_gre_button) {
            addVocabAlertDialog(mDbHelper, VocabDbContract.CATEGORY_NAME_GRE, mVocabAdapter);
        }
        else if (id == R.id.del_gre_button) {
            deleteVocab(mDbHelper, VocabDbContract.CATEGORY_NAME_GRE, mVocabAdapter);
        }
        else if (id == R.id.label_gre_button) {
            selectTableToAddVocabTo(mVocabAdapter, mDbHelper, VocabDbContract.CATEGORY_NAME_GRE);;
        }
        else if (id == R.id.select_all_gre_button) {
            selectAll(mVocabAdapter, mDbHelper, VocabDbContract.CATEGORY_NAME_GRE);
        }

        return super.onOptionsItemSelected(item);
    }

}
