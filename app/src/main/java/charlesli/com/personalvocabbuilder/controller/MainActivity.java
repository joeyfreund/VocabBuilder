package charlesli.com.personalvocabbuilder.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import charlesli.com.personalvocabbuilder.R;
import charlesli.com.personalvocabbuilder.sqlDatabase.VocabDbContract;


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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Review Vocab");
                // Set up the input
                LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText vocabInput = new EditText(MainActivity.this);
                vocabInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
                vocabInput.setHint("Vocab");
                layout.addView(vocabInput);

                final EditText definitionInput = new EditText(MainActivity.this);
                definitionInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
                definitionInput.setHint("Definition");
                layout.addView(definitionInput);

                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View promptsView = li.inflate(R.layout.alert_dialog_review, null);

                // Spinner
                Spinner spinner = (Spinner) promptsView.findViewById(R.id.spinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.this,
                        R.array.table_array, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                // Radio Button
                final RadioButton wordDef = (RadioButton) promptsView.findViewById(R.id.wordDef);
                final RadioButton defWord = (RadioButton) promptsView.findViewById(R.id.defWord);

                wordDef.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wordDef.setChecked(true);
                        defWord.setChecked(false);
                    }
                });

                defWord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wordDef.setChecked(false);
                        defWord.setChecked(true);
                    }
                });


                builder.setView(promptsView);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
        });

    }

}
