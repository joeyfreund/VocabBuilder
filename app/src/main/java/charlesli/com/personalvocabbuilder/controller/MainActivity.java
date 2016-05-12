package charlesli.com.personalvocabbuilder.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import charlesli.com.personalvocabbuilder.R;
import charlesli.com.personalvocabbuilder.sqlDatabase.VocabDbContract;
import charlesli.com.personalvocabbuilder.sqlDatabase.VocabDbHelper;


public class MainActivity extends ActionBarActivity {

    private ImageButton mMyVocabButton;
    private ImageButton mReviewButton;
    private ImageButton mTestButton;
    private ImageButton mSettingsButton;
    private ImageButton mDictionaryButton;
    private ImageButton mCategoriesButton;

    // Review Mode
    private final int WORDTODEF = 0;
    private final int DEFTOWORD = 1;
    private String reviewTable = VocabDbContract.TABLE_NAME_MY_VOCAB;
    private Integer reviewMode = WORDTODEF;
    private Integer reviewNumOfWords = 0;


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
        //mTestButton.setVisibility(View.INVISIBLE);

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

        mTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createReviewDialog();
            }
        });

    }

    private void createReviewDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Review Vocab");

        VocabDbHelper dbHelper = VocabDbHelper.getDBHelper(MainActivity.this);
        Cursor cursor = dbHelper.getCursor(VocabDbContract.TABLE_NAME_MY_VOCAB);
        Integer maxRow = cursor.getCount();
        reviewNumOfWords = maxRow;

        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.alert_dialog_review, null);

        final TextView numText = (TextView) promptsView.findViewById(R.id.numberText);
        Spinner spinner = (Spinner) promptsView.findViewById(R.id.spinner);
        final RadioButton wordDef = (RadioButton) promptsView.findViewById(R.id.wordDef);
        final RadioButton defWord = (RadioButton) promptsView.findViewById(R.id.defWord);


        // TextView
        numText.setText(String.valueOf(maxRow));

        // Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.this,
                R.array.table_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String table = (String) parent.getItemAtPosition(position);
                if (table.equals("My Vocab")) {
                    reviewTable = VocabDbContract.TABLE_NAME_MY_VOCAB;
                }
                else if (table.equals("My Word Bank")) {
                    reviewTable = VocabDbContract.TABLE_NAME_MY_WORD_BANK;
                }
                else if (table.equals("GMAT")) {
                    reviewTable = VocabDbContract.TABLE_NAME_GMAT;
                }
                else if (table.equals("GRE")) {
                    reviewTable = VocabDbContract.TABLE_NAME_GRE;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Radio Button
        wordDef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordDef.setChecked(true);
                defWord.setChecked(false);
                reviewMode = WORDTODEF;
            }
        });

        defWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordDef.setChecked(false);
                defWord.setChecked(true);
                reviewMode = DEFTOWORD;
            }
        });

        // SeekBar
        SeekBar seekBar = (SeekBar) promptsView.findViewById(R.id.seekBar);
        seekBar.setMax(maxRow);
        seekBar.setProgress(maxRow);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                numText.setText(String.valueOf(progress));
                reviewNumOfWords = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        builder.setView(promptsView);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (reviewNumOfWords == 0) {
                    Toast.makeText(MainActivity.this, "There are no words to be reviewed", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, WordDefinition.class);
                    intent.putExtra("Mode", reviewMode);
                    intent.putExtra("Table", reviewTable);
                    intent.putExtra("NumOfWords", reviewNumOfWords);
                    startActivity(intent);
                }
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

}
