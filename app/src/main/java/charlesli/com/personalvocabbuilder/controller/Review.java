package charlesli.com.personalvocabbuilder.controller;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import charlesli.com.personalvocabbuilder.R;
import charlesli.com.personalvocabbuilder.sqlDatabase.VocabDbContract;
import charlesli.com.personalvocabbuilder.sqlDatabase.VocabDbHelper;


public class Review extends AppCompatActivity {

    private int mReviewMode;
    private String mReviewCategory;
    private int mReviewNumOfWords;

    private TextView mTopTextView;
    private TextView mBottomTextView;
    private Button mRevealButton;
    private Button mDifLvlButton;
    private Button mFamLvlButton;
    private Button mEasLvlButton;
    private Button mPerLvlButton;
    private Button mAgaLvlButton;
    private Cursor mCursor;

    private static final int DIFFICULT = 0;
    private static final int FAMILIAR = 1;
    private static final int EASY = 2;
    private static final int PERFECT = 3;

    private VocabDbHelper mDbHelper = VocabDbHelper.getDBHelper(Review.this);
    private Random mRandom = new Random();
    private ArrayList<Integer> mTracker = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Intent intent = getIntent();
        // default value, 0, indicates Word -> Definition review option
        mReviewMode = intent.getIntExtra("Mode", 0);
        mReviewCategory = intent.getStringExtra("Category");
        mReviewNumOfWords = intent.getIntExtra("NumOfWords", 0);


        mTopTextView = (TextView) findViewById(R.id.topTextView);
        mBottomTextView = (TextView) findViewById(R.id.bottomTextView);
        mRevealButton = (Button) findViewById(R.id.revealButton);
        mDifLvlButton = (Button) findViewById(R.id.lvl_difficult_button);
        mFamLvlButton = (Button) findViewById(R.id.lvl_familiar_button);
        mEasLvlButton = (Button) findViewById(R.id.lvl_easy_button);
        mPerLvlButton = (Button) findViewById(R.id.lvl_perfect_button);
        mAgaLvlButton = (Button) findViewById(R.id.lvl_again_button);

        mCursor = mDbHelper.getVocabCursor(mReviewCategory);

        loadVocabInRandomOrder();
    }



    private void loadVocabInRandomOrder() {
        // Get the length / number of rows in the table
        int numOfRows = mCursor.getCount();
        // Use Random to generate a random number from length
        int randomNum = mRandom.nextInt(numOfRows);
        // Prevent a repeated number from being generated
        while (mTracker.contains(randomNum)) {
            randomNum = mRandom.nextInt(numOfRows);
        }

        mCursor.moveToPosition(randomNum);
        // Get Word from Desired Random Row
        String word = mCursor.getString(mCursor.getColumnIndexOrThrow(VocabDbContract.COLUMN_NAME_VOCAB));
        // Get Definition from Desired Random Row
        String definition = mCursor.getString(mCursor.getColumnIndexOrThrow(VocabDbContract.COLUMN_NAME_DEFINITION));

        // if 0 (word - > definition): ********
        if (mReviewMode == 0) {
            // Set mTopView to Word
            mTopTextView.setText(word);
            // Set mBottomView to Definition
            mBottomTextView.setText(definition);
        }
        // if 1, else, (definition -> word): ********
        else {
            // Set mTopView to Definition
            mTopTextView.setText(definition);
            // Set mBottomView to Word
            mBottomTextView.setText(word);
        }
        // Make level buttons and definition invisible, reveal button visible
        mDifLvlButton.setVisibility(View.INVISIBLE);
        mFamLvlButton.setVisibility(View.INVISIBLE);
        mEasLvlButton.setVisibility(View.INVISIBLE);
        mPerLvlButton.setVisibility(View.INVISIBLE);
        mAgaLvlButton.setVisibility(View.INVISIBLE);
        mBottomTextView.setVisibility(View.INVISIBLE);
        mRevealButton.setVisibility(View.VISIBLE);

        // Set OnClickListener for RevealAnswer
        final int finalRandomNum = randomNum;
        mRevealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make RevealAnswer invisible
                mRevealButton.setVisibility(View.INVISIBLE);
                // When pressed make level buttons visible
                mDifLvlButton.setVisibility(View.VISIBLE);
                mFamLvlButton.setVisibility(View.VISIBLE);
                mEasLvlButton.setVisibility(View.VISIBLE);
                mPerLvlButton.setVisibility(View.VISIBLE);
                mAgaLvlButton.setVisibility(View.VISIBLE);
                mBottomTextView.setVisibility(View.VISIBLE);
                // Set OnClickListener for level buttons
                mDifLvlButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTracker.add(finalRandomNum);
                        selectRLevel(DIFFICULT);
                    }
                });
                mFamLvlButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTracker.add(finalRandomNum);
                        selectRLevel(FAMILIAR);
                    }
                });
                mEasLvlButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTracker.add(finalRandomNum);
                        selectRLevel(EASY);
                    }
                });
                mPerLvlButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTracker.add(finalRandomNum);
                        selectRLevel(PERFECT);
                    }
                });
                mAgaLvlButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadVocabInRandomOrder();
                    }
                });
            }
        });

    }

    private void selectRLevel(int level) {
        // Update level information of the word to the SQLite database ****
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(VocabDbContract.COLUMN_NAME_LEVEL, level);

        // Which row to update, based on the ID
        String selectionWordBank = VocabDbContract.COLUMN_NAME_VOCAB + " = ? AND " +
                VocabDbContract.COLUMN_NAME_DEFINITION + " = ?";
        String word = mCursor.getString(mCursor.getColumnIndexOrThrow(VocabDbContract.COLUMN_NAME_VOCAB));
        String definition = mCursor.getString(mCursor.getColumnIndexOrThrow(VocabDbContract.COLUMN_NAME_DEFINITION));
        String[] selectionArgsWordBank = {word, definition};

        db.update(
                VocabDbContract.TABLE_NAME_MY_VOCAB,
                values,
                selectionWordBank,
                selectionArgsWordBank
        );
        // If this is not last word to be reviewed
        if (mTracker.size() < mReviewNumOfWords) {
            // Do the same thing / loadPage(int) again (recursive)
            loadVocabInRandomOrder();

        }
        // If this is last row
        else {
            // Exit the interface and go back to Review
            mTracker.clear();
            finish();
        }
    }


}
