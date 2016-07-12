package charlesli.com.personalvocabbuilder.controller;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;

import charlesli.com.personalvocabbuilder.R;
import charlesli.com.personalvocabbuilder.sqlDatabase.LanguageOptions;
import charlesli.com.personalvocabbuilder.sqlDatabase.VocabCursorAdapter;
import charlesli.com.personalvocabbuilder.sqlDatabase.VocabDbContract;
import charlesli.com.personalvocabbuilder.sqlDatabase.VocabDbHelper;


public class MyVocab extends AppCompatActivity {

    private VocabCursorAdapter mVocabAdapter;
    private ListView mVocabListView;
    private VocabDbHelper mDbHelper = VocabDbHelper.getDBHelper(MyVocab.this);
    private String mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vocab);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Get Category Information
        Intent intent = getIntent();
        mCategory = intent.getStringExtra("Category");
        setTitle(mCategory);

        mVocabListView = (ListView) findViewById(R.id.mVocabList);
        TextView emptyTextView = (TextView) findViewById(android.R.id.empty);
        mVocabListView.setEmptyView(emptyTextView);
        Cursor cursor = mDbHelper.getVocabCursor(mCategory);
        mVocabAdapter = new VocabCursorAdapter(this, cursor, 0);
        mVocabListView.setAdapter(mVocabAdapter);
        mVocabListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedVocab = (String) ((TextView) view.findViewById(R.id.vocabName)).getText();
                String selectedDefinition = (String) ((TextView) view.findViewById(R.id.vocabDefinition)).getText();
                editVocabAlertDialog(selectedVocab, selectedDefinition, id, mDbHelper,
                        mCategory, mVocabAdapter);
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_vocab, menu);

        implementSearchBar(menu, R.id.search_my_vocab_button, mCategory,
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
        if (id == R.id.add_vocab_my_vocab_button) {
            addVocabAlertDialog(mDbHelper, mCategory, mVocabAdapter);
        }
        else if (id == R.id.del_my_vocab_button) {
            deleteVocab(mDbHelper, mCategory, mVocabAdapter);
        }
        else if (id == R.id.label_my_vocab_button) {
            selectTableToAddVocabTo(mVocabAdapter, mDbHelper, mCategory);
        }
        else if (id == R.id.select_all_my_vocab_button) {
            selectAll(mVocabAdapter, mDbHelper, mCategory);
        }

        return super.onOptionsItemSelected(item);
    }

    protected void selectAll(VocabCursorAdapter cursorAdapter, VocabDbHelper dbHelper,
                             String category) {
        Cursor cursor = dbHelper.getVocabCursor(category);
        int numOfRows = cursor.getCount();
        for (int i = 0; i < numOfRows; i++) {
            cursorAdapter.selectedItemsPositions.add(i);
        }
        cursorAdapter.changeCursor(cursor);
    }

    protected void deleteVocab(VocabDbHelper dbHelper, String category, VocabCursorAdapter cursorAdapter) {
        Iterator<Integer> posIt = cursorAdapter.selectedItemsPositions.iterator();
        if (cursorAdapter.selectedItemsPositions.isEmpty()) {
            Toast.makeText(this, "No words are selected", Toast.LENGTH_SHORT).show();
        }
        else {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            while (posIt.hasNext()) {
                Integer posInt = posIt.next();
                // Define 'where' part of query
                String selection = VocabDbContract._ID + " LIKE ?" + " AND " +
                        VocabDbContract.COLUMN_NAME_CATEGORY + " LIKE ?";
                // Specify arguments in placeholder order
                String[] selectionArgs = {String.valueOf(cursorAdapter.getItemId(posInt)), category};
                // Issue SQL statement
                db.delete(VocabDbContract.TABLE_NAME_MY_VOCAB, selection, selectionArgs);
            }
            Cursor cursor = dbHelper.getVocabCursor(category);
            cursorAdapter.changeCursor(cursor);

            cursorAdapter.selectedItemsPositions.clear();
        }
    }

    protected void selectTableToAddVocabTo(final VocabCursorAdapter cursorAdapter, final VocabDbHelper dbHelper,
                                           final String fromCategory) {
        if (cursorAdapter.selectedItemsPositions.isEmpty()) {
            Toast.makeText(this, "No words are selected", Toast.LENGTH_SHORT).show();
        }
        else {
            // Set up alert dialog
            final String[] selectedCategory = new String[1];
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Vocab To...");
            // Set up the input
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);

            // Add Spinner to alert dialog
            final Spinner spinner = new Spinner(this);
            String[] from = {VocabDbContract.COLUMN_NAME_CATEGORY};
            int[] to = {android.R.id.text1};
            final Cursor categoryCursor = dbHelper.getCategoryCursor();
            SimpleCursorAdapter spinnerAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item,
                    categoryCursor, from, to, 0);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    categoryCursor.moveToPosition(position);
                    selectedCategory[0] = categoryCursor.getString(categoryCursor.getColumnIndex(VocabDbContract.COLUMN_NAME_CATEGORY));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            layout.addView(spinner);
            builder.setView(layout);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    addVocabToSelectedTable(cursorAdapter, dbHelper, fromCategory, selectedCategory[0]);
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            final AlertDialog dialog = builder.create();

            dialog.show();

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.app_icon_color));
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.app_icon_color));
            dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(this, R.color.app_icon_color));
        }
    }

    protected void addVocabToSelectedTable(VocabCursorAdapter cursorAdapter, VocabDbHelper dbHelper,
                                           String fromCategory, String toCategory) {
        Iterator<Integer> posIt = cursorAdapter.selectedItemsPositions.iterator();
        if (cursorAdapter.selectedItemsPositions.isEmpty()) {
            Toast.makeText(this, "No words are selected", Toast.LENGTH_SHORT).show();
        }
        else {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            while (posIt.hasNext()) {
                Integer posInt = posIt.next();
                Integer idInt = (int) cursorAdapter.getItemId(posInt);
                String[] projection = {
                        VocabDbContract._ID,
                        VocabDbContract.COLUMN_NAME_VOCAB,
                        VocabDbContract.COLUMN_NAME_DEFINITION,
                        VocabDbContract.COLUMN_NAME_LEVEL
                };
                String[] selectionArg = {
                        String.valueOf(idInt)
                };
                Cursor cursor = db.query(
                        VocabDbContract.TABLE_NAME_MY_VOCAB,
                        projection,
                        VocabDbContract._ID + "=?",
                        selectionArg,
                        null,
                        null,
                        null
                );
                cursor.moveToFirst();
                String vocab = cursor.getString(cursor.getColumnIndex(VocabDbContract.COLUMN_NAME_VOCAB));
                String definition = cursor.getString(cursor.getColumnIndex(VocabDbContract.COLUMN_NAME_DEFINITION));
                Integer level = cursor.getInt(cursor.getColumnIndex(VocabDbContract.COLUMN_NAME_LEVEL));
                dbHelper.insertVocab(toCategory, vocab, definition, level);
            }
            cursorAdapter.selectedItemsPositions.clear();
            Cursor cursor = dbHelper.getVocabCursor(fromCategory);
            cursorAdapter.changeCursor(cursor);

            Toast.makeText(this, "Vocab added successfully", Toast.LENGTH_SHORT).show();
        }
    }

    protected void addVocabAlertDialog(final VocabDbHelper dbHelper, final String category,
                                       final VocabCursorAdapter cursorAdapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Vocab");

        LayoutInflater li = LayoutInflater.from(MyVocab.this);
        View promptsView = li.inflate(R.layout.alert_dialog_add_vocab, null);
        final EditText vocabInput = (EditText) promptsView.findViewById(R.id.vocabInput);
        final EditText definitionInput = (EditText) promptsView.findViewById(R.id.definitionInput);
        final ProgressBar progressBar = (ProgressBar) promptsView.findViewById(R.id.progressBar);
        builder.setView(promptsView);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String vocab = vocabInput.getText().toString();
                String definition = definitionInput.getText().toString();
                dbHelper.insertVocab(category, vocab, definition, 0);
                if (!category.equals(VocabDbContract.CATEGORY_NAME_MY_WORD_BANK)) {
                    dbHelper.insertVocab(VocabDbContract.CATEGORY_NAME_MY_WORD_BANK, vocab, definition, 0);
                }
                // Update Cursor
                Cursor cursor = dbHelper.getVocabCursor(category);
                cursorAdapter.changeCursor(cursor);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNeutralButton("Translate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        final AlertDialog dialog = builder.create();

        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.app_icon_color));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.app_icon_color));
        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(this, R.color.app_icon_color));

        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleTranslate googleTranslate = new GoogleTranslate();
                googleTranslate.setProgressBar(progressBar);
                String vocab = vocabInput.getText().toString();

                SharedPreferences sharedPreferences = getSharedPreferences("Translation", MODE_PRIVATE);
                int sourcePos = sharedPreferences.getInt("Source", 0); // 0 is for Detect Language
                int targetPos = sharedPreferences.getInt("Target", 19); // 19 is for English

                String source = LanguageOptions.FROM_LANGUAGE_CODE[sourcePos];
                String target = LanguageOptions.TO_LANGUAGE_CODE[targetPos];

                final AlertDialog.Builder builder = new AlertDialog.Builder(MyVocab.this);
                builder.setMessage("Network is unavailable. Please try again later.");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();

                if (isNetworkAvailable()) {
                    progressBar.setVisibility(View.VISIBLE);
                    AsyncTask<String, Void, String> asyncTask = googleTranslate.execute(vocab, source, target);
                    try {
                        String translatedJSON = asyncTask.get();
                        JSONParser jsonParser = new JSONParser();
                        String translatedText = jsonParser.parseJSONForTranslation(translatedJSON);
                        definitionInput.setText(translatedText);
                    } catch (Exception e) {
                        dialog.show();

                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(MyVocab.this, R.color.app_icon_color));
                        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(MyVocab.this, R.color.app_icon_color));
                        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(MyVocab.this, R.color.app_icon_color));
                    }
                }
                else {
                    dialog.show();

                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(MyVocab.this, R.color.app_icon_color));
                    dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(MyVocab.this, R.color.app_icon_color));
                    dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(MyVocab.this, R.color.app_icon_color));
                }
            }
        });

    }

    protected void editVocabAlertDialog(final String selectedVocab, final String selectedDefinition,
                                        final long id, final VocabDbHelper dbHelper,
                                        final String category, final VocabCursorAdapter cursorAdapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Vocab");
        builder.setMessage(selectedVocab);
        // Set up the input
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText definitionInput = new EditText(this);
        definitionInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        definitionInput.setHint("New Definition");
        definitionInput.setText(selectedDefinition);
        layout.addView(definitionInput);

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
                String definition = definitionInput.getText().toString();
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                // new value for one column
                ContentValues values = new ContentValues();
                values.put(VocabDbContract.COLUMN_NAME_DEFINITION, definition);

                // which row to update, based on the VOCAB
                String selectionMyVocab = VocabDbContract.COLUMN_NAME_VOCAB + " = ? AND " +
                        VocabDbContract.COLUMN_NAME_DEFINITION + " = ?";
                String[] selectionArgsMyVocab = {selectedVocab, selectedDefinition};

                db.update(
                        VocabDbContract.TABLE_NAME_MY_VOCAB,
                        values,
                        selectionMyVocab,
                        selectionArgsMyVocab
                );

                // Update Cursor
                cursorAdapter.changeCursor(dbHelper.getVocabCursor(category));
            }
        });
        builder.setNegativeButton("Delete Vocab", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete Vocab from Database*****************************************
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                // Define 'where' part of query
                String selection = VocabDbContract._ID + " LIKE ?";
                // Specify arguments in placeholder order
                String[] selectionArgs = {String.valueOf(id)};
                // Issue SQL statement
                db.delete(VocabDbContract.TABLE_NAME_MY_VOCAB, selection, selectionArgs);

                // Update Cursor
                cursorAdapter.changeCursor(dbHelper.getVocabCursor(category));
            }
        });

        final AlertDialog dialog = builder.create();

        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.app_icon_color));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.app_icon_color));
        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(this, R.color.app_icon_color));

    }

    protected void implementSearchBar(Menu menu, int menuItemId, final String category,
                                      final VocabCursorAdapter cursorAdapter, final VocabDbHelper dbHelper) {
        MenuItem search = menu.findItem(menuItemId);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Cursor cursor = dbHelper.getVocabCursorWithStringPattern(category, s);
                cursorAdapter.changeCursor(cursor);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Cursor cursor = dbHelper.getVocabCursorWithStringPattern(category, s);
                cursorAdapter.changeCursor(cursor);
                return true;
            }
        });

        // Detect when search bar collapses
        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                cursorAdapter.changeCursor(dbHelper.getVocabCursor(category));
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

}
