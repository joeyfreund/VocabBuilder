package charlesli.com.personalvocabbuilder.io;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import charlesli.com.personalvocabbuilder.R;


public class CategoriesActivity extends ActionBarActivity {

    private ListView mCategoriesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        mCategoriesListView = (ListView) findViewById(R.id.categoriesListView);

        String[] categories = {"My Word Bank", "GMAT", "GRE"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, categories);

        mCategoriesListView.setAdapter(adapter);

        mCategoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(CategoriesActivity.this, MyWordBank.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(CategoriesActivity.this, "Coming soon...", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
