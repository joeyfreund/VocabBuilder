package charlesli.com.personalvocabbuilder.controller;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import charlesli.com.personalvocabbuilder.R;
import charlesli.com.personalvocabbuilder.sqlDatabase.VocabDbContract;
import charlesli.com.personalvocabbuilder.sqlDatabase.VocabDbHelper;


public class Review extends ActionBarActivity {

    private Button mWordDefinitionButton;
    private Button mDefinitionWordButton;

    private Cursor mCursor;
    private VocabDbHelper mDbHelper = VocabDbHelper.getDBHelper(Review.this);

    private static final int WORDTODEF = 0;
    private static final int DEFTOWORD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        mWordDefinitionButton = (Button) findViewById(R.id.word_definition_button);
        mDefinitionWordButton = (Button) findViewById(R.id.definition_word_button);

        mCursor = mDbHelper.getCursor(VocabDbContract.TABLE_NAME_MY_VOCAB);

        mWordDefinitionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCursor.getCount() > 0) {
                    startReview(WORDTODEF);
                }
                else {
                    Toast.makeText(Review.this, "Add words to My Vocab first", Toast.LENGTH_LONG).show();
                }

            }
        });

        mDefinitionWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCursor.getCount() > 0) {
                    startReview(DEFTOWORD);
                } else {
                    Toast.makeText(Review.this, "Add words to My Vocab first", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private  void startReview(int reviewOption) {
        Intent intent = new Intent(this, WordDefinition.class);
        intent.putExtra(getString(R.string.review_option_selected), reviewOption);
        startActivity(intent);
    }


}
