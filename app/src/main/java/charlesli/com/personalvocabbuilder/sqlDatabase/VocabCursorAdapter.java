package charlesli.com.personalvocabbuilder.sqlDatabase;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import charlesli.com.personalvocabbuilder.R;

/**
 * Created by Li on 2015/4/17.
 */
public class VocabCursorAdapter extends CursorAdapter {

    private static final int DIFFICULT = 0;
    private static final int FAMILIAR = 1;
    private static final int EASY = 2;
    private static final int PERFECT = 3;

    public List<Integer> selectedItemsPositions;


    public VocabCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, 0);

        selectedItemsPositions = new ArrayList<>();
    }


    // The newView method is used to inflate a new view and return it
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vocab, parent, false);
        CheckBox box = (CheckBox) view.findViewById(R.id.editCheckbox);
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int position = (int) compoundButton.getTag();
                if (b) {
                    //check whether its already selected or not
                    if (!selectedItemsPositions.contains(position))
                        selectedItemsPositions.add(position);
                } else {
                    //remove position if unchecked checked item
                    selectedItemsPositions.remove((Object) position);
                }
            }
        });
        return view;
    }


    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvVocabName = (TextView) view.findViewById(R.id.vocabName);
        TextView tvVocabDefinition = (TextView) view.findViewById(R.id.vocabDefinition);
        ImageView tvVocabLevel = (ImageView) view.findViewById(R.id.vocabLevel);
        // Extract properties from cursor
        String vocab = cursor.getString(cursor.getColumnIndexOrThrow(VocabDbContract.COLUMN_NAME_VOCAB));
        String definition = cursor.getString(cursor.getColumnIndexOrThrow(VocabDbContract.COLUMN_NAME_DEFINITION));
        int level = cursor.getInt(cursor.getColumnIndexOrThrow(VocabDbContract.COLUMN_NAME_LEVEL));
        // Populate fields with extracted properties
        tvVocabName.setText(vocab);
        tvVocabDefinition.setText(definition);

        // Set Level images later *************************************************
        if (level == DIFFICULT) {
            tvVocabLevel.setImageResource(R.drawable.level_bars_difficult);
            tvVocabLevel.setTag(DIFFICULT);
        }
        else if (level == FAMILIAR) {
            tvVocabLevel.setImageResource(R.drawable.level_bars_familiar);
            tvVocabLevel.setTag(FAMILIAR);
        }
        else if (level == EASY) {
            tvVocabLevel.setImageResource(R.drawable.level_bars_easy);
            tvVocabLevel.setTag(EASY);
        }
        else if (level == PERFECT) {
            tvVocabLevel.setImageResource(R.drawable.level_bars_perfect);
            tvVocabLevel.setTag(PERFECT);
        }

        CheckBox box = (CheckBox) view.findViewById(R.id.editCheckbox);
        box.setTag(cursor.getPosition());

        if (selectedItemsPositions.contains(cursor.getPosition()))
            box.setChecked(true);
        else
            box.setChecked(false);
    }
}
