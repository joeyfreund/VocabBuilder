package charlesli.com.personalvocabbuilder;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Li on 2015/4/17.
 */
public class VocabCursorAdapter extends CursorAdapter {

    private static final int DIFFICULT = 0;
    private static final int FAMILIAR = 1;
    private static final int EASY = 2;
    private static final int PERFECT = 3;


    public VocabCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, 0);
    }


    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_vocab, parent, false);
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
        String vocab = cursor.getString(cursor.getColumnIndexOrThrow(VocabDbContract.DatabaseInfo.COLUMN_NAME_VOCAB));
        String definition = cursor.getString(cursor.getColumnIndexOrThrow(VocabDbContract.DatabaseInfo.COLUMN_NAME_DEFINITION));
        int level = cursor.getInt(cursor.getColumnIndexOrThrow(VocabDbContract.DatabaseInfo.COLUMN_NAME_LEVEL));
        // Populate fields with extracted properties
        tvVocabName.setText(vocab);
        tvVocabDefinition.setText(definition);
        // Set Level images later *************************************************
        if (level == DIFFICULT) {
            tvVocabLevel.setImageResource(R.drawable.level_bars_difficult);
        }
        else if (level == FAMILIAR) {
            tvVocabLevel.setImageResource(R.drawable.level_bars_familiar);
        }
        else if (level == EASY) {
            tvVocabLevel.setImageResource(R.drawable.level_bars_easy);
        }
        else if (level == PERFECT) {
            tvVocabLevel.setImageResource(R.drawable.level_bars_perfect);
        }
    }
}
