package charlesli.com.personalvocabbuilder.io;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import charlesli.com.personalvocabbuilder.R;


public class MainActivity extends ActionBarActivity {

    private ImageButton mMyVocabButton;
    private ImageButton mReviewButton;
    private ImageButton mTestButton;
    private ImageButton mSettingsButton;
    private ImageButton mDictionaryButton;
    private ImageButton mCategoriesButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mMyVocabButton = (ImageButton) findViewById(R.id.myVocabButton);
        mReviewButton = (ImageButton) findViewById(R.id.reviewButton);
        mTestButton = (ImageButton) findViewById(R.id.testButton);
        mSettingsButton = (ImageButton) findViewById(R.id.settings_button);
        mDictionaryButton = (ImageButton) findViewById(R.id.dictionary_button);
        mCategoriesButton = (ImageButton) findViewById(R.id.categories_button);


        mDictionaryButton.setVisibility(View.INVISIBLE);
        mSettingsButton.setVisibility(View.INVISIBLE);
        mTestButton.setVisibility(View.INVISIBLE);

        mMyVocabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyVocabActivity.class);
                startActivity(intent);
            }
        });

        mReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReviewActivity.class);
                startActivity(intent);
            }
        });

        mCategoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CategoriesActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
