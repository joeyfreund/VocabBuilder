package charlesli.com.personalvocabbuilder.controller;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import charlesli.com.personalvocabbuilder.R;
import charlesli.com.personalvocabbuilder.sqlDatabase.CategoryCursorAdapter;
import charlesli.com.personalvocabbuilder.sqlDatabase.VocabDbContract;
import charlesli.com.personalvocabbuilder.sqlDatabase.VocabDbHelper;


public class MainActivity extends AppCompatActivity {

    // Review Mode
    private final int WORDTODEF = 0;
    private final int DEFTOWORD = 1;
    private String reviewCategory = VocabDbContract.CATEGORY_NAME_MY_VOCAB;
    private Integer reviewMode = WORDTODEF;
    private Integer reviewNumOfWords = 0;

    private CategoryCursorAdapter mCategoryAdapter;
    private ListView mCategoryListView;
    private VocabDbHelper mDbHelper = VocabDbHelper.getDBHelper(MainActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCategoryListView = (ListView) findViewById(R.id.mainListView);
        Cursor cursor = mDbHelper.getCategoryCursor();
        mCategoryAdapter = new CategoryCursorAdapter(this, cursor, 0);
        mCategoryListView.setAdapter(mCategoryAdapter);

        mCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor categoryCursor = mDbHelper.getCategoryCursor();
                categoryCursor.moveToPosition(position);
                String categoryName = categoryCursor.getString(categoryCursor.getColumnIndex(VocabDbContract.COLUMN_NAME_CATEGORY));
                Intent intent = new Intent(MainActivity.this, MyVocab.class);
                intent.putExtra("Category", categoryName);
                startActivity(intent);
            }
        });

        mCategoryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor categoryCursor = mDbHelper.getCategoryCursor();
                categoryCursor.moveToPosition(position);
                String categoryName = categoryCursor.getString(categoryCursor.getColumnIndex(VocabDbContract.COLUMN_NAME_CATEGORY));
                editCategoryAlertDialog(categoryName, id, mDbHelper, mCategoryAdapter);
                return true;
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
        else if (id == R.id.add_category_button) {
            createAddCategoryDialog();
        }
        else if (id == R.id.settings_button) {
            createSettingsDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Settings");

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
                if (mDbHelper.checkIfCategoryExists(category)) {
                    Toast.makeText(MainActivity.this, category + " already exists", Toast.LENGTH_SHORT).show();
                }
                else {
                    mDbHelper.insertCategory(category);
                    mCategoryAdapter.changeCursor(mDbHelper.getCategoryCursor());
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
                if (mDbHelper.checkIfCategoryExists(category)) {
                    Toast.makeText(MainActivity.this, category + " already exists", Toast.LENGTH_SHORT).show();
                }
                else {
                    mDbHelper.insertCategory(category);
                    mCategoryAdapter.changeCursor(mDbHelper.getCategoryCursor());
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
        String[] from = {VocabDbContract.COLUMN_NAME_CATEGORY};
        int[] to = {android.R.id.text1};
        final Cursor categoryCursor = mDbHelper.getCategoryCursor();
        SimpleCursorAdapter spinnerAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item,
                categoryCursor, from, to, 0);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryCursor.moveToPosition(position);
                reviewCategory = categoryCursor.getString(categoryCursor.getColumnIndex(VocabDbContract.COLUMN_NAME_CATEGORY));
                VocabDbHelper dbHelper = VocabDbHelper.getDBHelper(MainActivity.this);
                Cursor cursor = dbHelper.getVocabCursor(reviewCategory);
                Integer maxRow = cursor.getCount();
                numText.setText(String.valueOf(maxRow));
                seekBar.setMax(maxRow);
                seekBar.setProgress(maxRow);
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

    protected void editCategoryAlertDialog(final String selectedCategory, final long id, final VocabDbHelper dbHelper,
                                        final CategoryCursorAdapter cursorAdapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Category");
        builder.setMessage(selectedCategory);
        // Set up the input
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText categoryInput = new EditText(this);
        categoryInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        categoryInput.setHint("New category name");
        layout.addView(categoryInput);

        builder.setView(layout);

        // Set up the buttons
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String categoryName = categoryInput.getText().toString();
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                ContentValues values = new ContentValues();
                values.put(VocabDbContract.COLUMN_NAME_CATEGORY, categoryName);

                String selectionMyVocab = VocabDbContract.COLUMN_NAME_CATEGORY + " = ?";
                String[] selectionArgsMyVocab = {selectedCategory};

                db.update(
                        VocabDbContract.TABLE_NAME_CATEGORY,
                        values,
                        selectionMyVocab,
                        selectionArgsMyVocab
                );

                // Update Cursor
                cursorAdapter.changeCursor(dbHelper.getCategoryCursor());
            }
        });
        builder.setNegativeButton("Delete     ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete Category from Database
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                // Define 'where' part of query
                String selection = VocabDbContract._ID + " LIKE ?";
                // Specify arguments in placeholder order
                String[] selectionArgs = {String.valueOf(id)};
                // Issue SQL statement
                db.delete(VocabDbContract.TABLE_NAME_CATEGORY, selection, selectionArgs);

                // Update Cursor
                cursorAdapter.changeCursor(dbHelper.getCategoryCursor());
            }
        });

        builder.show();
    }

}
