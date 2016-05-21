package charlesli.com.personalvocabbuilder.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import charlesli.com.personalvocabbuilder.R;
import charlesli.com.personalvocabbuilder.sqlDatabase.VocabDbContract;
import charlesli.com.personalvocabbuilder.sqlDatabase.VocabDbHelper;


public class MainActivity extends AppCompatActivity {

    // Review Mode
    private final int WORDTODEF = 0;
    private final int DEFTOWORD = 1;
    private String reviewCategory = VocabDbContract.CATEGORY_NAME_MY_VOCAB;
    private Integer reviewMode = WORDTODEF;
    private Integer reviewNumOfWords = 0;

    private List<String> categoriesList = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.mainListView);

        categoriesList.add(VocabDbContract.CATEGORY_NAME_MY_VOCAB);
        categoriesList.add(VocabDbContract.CATEGORY_NAME_MY_WORD_BANK);
        categoriesList.add(VocabDbContract.CATEGORY_NAME_GMAT);
        categoriesList.add(VocabDbContract.CATEGORY_NAME_GRE);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, categoriesList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(MainActivity.this, MyVocab.class);
                    startActivity(intent);
                }
                else if (position == 1) {
                    Intent intent = new Intent(MainActivity.this, MyWordBank.class);
                    startActivity(intent);
                }
                else if (position == 2) {
                    Intent intent = new Intent(MainActivity.this, GMAT.class);
                    startActivity(intent);
                }
                else if (position == 3) {
                    Intent intent = new Intent(MainActivity.this, GRE.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
                }
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
        int id = item.getItemId();

        if (id == R.id.review_button) {
            createReviewDialog();
        }
        if (id == R.id.add_category_button) {
            createAddCategoryDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Category");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText categoryInput = new EditText(this);
        categoryInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        categoryInput.setHint("Category name");
        layout.addView(categoryInput);

        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String category = categoryInput.getText().toString();
                categoriesList.add(category);
                adapter.notifyDataSetChanged();
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

    private void createReviewDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Review Vocab");

        VocabDbHelper dbHelper = VocabDbHelper.getDBHelper(MainActivity.this);
        Cursor cursor = dbHelper.getVocabCursor(VocabDbContract.CATEGORY_NAME_MY_VOCAB);
        final Integer maxRow = cursor.getCount();
        reviewNumOfWords = maxRow;

        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.alert_dialog_review, null);

        final TextView numText = (TextView) promptsView.findViewById(R.id.numberText);
        Spinner spinner = (Spinner) promptsView.findViewById(R.id.spinner);
        final RadioButton wordDef = (RadioButton) promptsView.findViewById(R.id.wordDef);
        final RadioButton defWord = (RadioButton) promptsView.findViewById(R.id.defWord);
        final SeekBar seekBar = (SeekBar) promptsView.findViewById(R.id.seekBar);


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
                    reviewCategory = VocabDbContract.CATEGORY_NAME_MY_VOCAB;
                    VocabDbHelper dbHelper = VocabDbHelper.getDBHelper(MainActivity.this);
                    Cursor cursor = dbHelper.getVocabCursor(VocabDbContract.CATEGORY_NAME_MY_VOCAB);
                    Integer maxRow = cursor.getCount();
                    numText.setText(String.valueOf(maxRow));
                    seekBar.setMax(maxRow);
                    seekBar.setProgress(maxRow);
                }
                else if (table.equals("My Word Bank")) {
                    reviewCategory = VocabDbContract.CATEGORY_NAME_MY_WORD_BANK;
                    VocabDbHelper dbHelper = VocabDbHelper.getDBHelper(MainActivity.this);
                    Cursor cursor = dbHelper.getVocabCursor(VocabDbContract.CATEGORY_NAME_MY_WORD_BANK);
                    Integer maxRow = cursor.getCount();
                    numText.setText(String.valueOf(maxRow));
                    seekBar.setMax(maxRow);
                    seekBar.setProgress(maxRow);
                }
                else if (table.equals("GMAT")) {
                    reviewCategory = VocabDbContract.CATEGORY_NAME_GMAT;
                    VocabDbHelper dbHelper = VocabDbHelper.getDBHelper(MainActivity.this);
                    Cursor cursor = dbHelper.getVocabCursor(VocabDbContract.CATEGORY_NAME_GRE);
                    Integer maxRow = cursor.getCount();
                    numText.setText(String.valueOf(maxRow));
                    seekBar.setMax(maxRow);
                    seekBar.setProgress(maxRow);
                }
                else if (table.equals("GRE")) {
                    reviewCategory = VocabDbContract.CATEGORY_NAME_GRE;
                    VocabDbHelper dbHelper = VocabDbHelper.getDBHelper(MainActivity.this);
                    Cursor cursor = dbHelper.getVocabCursor(VocabDbContract.CATEGORY_NAME_GRE);
                    Integer maxRow = cursor.getCount();
                    numText.setText(String.valueOf(maxRow));
                    seekBar.setMax(maxRow);
                    seekBar.setProgress(maxRow);
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
                    Intent intent = new Intent(MainActivity.this, Review.class);
                    intent.putExtra("Mode", reviewMode);
                    intent.putExtra("Category", reviewCategory);
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
