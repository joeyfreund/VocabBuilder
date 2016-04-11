package charlesli.com.personalvocabbuilder.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
                Intent intent = new Intent(MainActivity.this, MyVocab.class);
                startActivity(intent);
            }
        });

        mReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Review.class);
                startActivity(intent);
            }
        });

        mCategoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Categories.class);
                startActivity(intent);
            }
        });

    }

}
