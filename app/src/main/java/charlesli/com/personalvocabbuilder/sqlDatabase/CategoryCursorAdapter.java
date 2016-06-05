package charlesli.com.personalvocabbuilder.sqlDatabase;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

import charlesli.com.personalvocabbuilder.R;
import charlesli.com.personalvocabbuilder.controller.MyVocab;

/**
 * Created by charles on 2016-05-21.
 */
public class CategoryCursorAdapter extends CursorAdapter {

    private VocabDbHelper mDBHelper;
    public CategoryCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvCategory = (TextView) view.findViewById(R.id.categoryName);
        String categoryName = cursor.getString(cursor.getColumnIndexOrThrow(VocabDbContract.COLUMN_NAME_CATEGORY));
        tvCategory.setText(categoryName);
        mDBHelper = VocabDbHelper.getDBHelper(context);
        Cursor vocabCursor = mDBHelper.getVocabCursor(categoryName);
        int numOfRows = vocabCursor.getCount();
        DonutProgress donutProgress = (DonutProgress) view.findViewById(R.id.donut_progress);
        donutProgress.setMax(3 * numOfRows);
        donutProgress.setProgress(mDBHelper.getCategoryLevelSum(categoryName));
    }
}
