package charlesli.com.personalvocabbuilder.sqlDatabase;

import android.provider.BaseColumns;

/**
 * Created by Li on 2015/4/13.
 */
public final class VocabDbContract {

    public VocabDbContract() {
        // Empty constructor to prevent someone from accidentally instantiating the contract class
    }

    // Inner class that defines the table contents
    public static abstract class DatabaseInfo implements BaseColumns {

        // table names
        public static final String TABLE_NAME_MY_VOCAB = "my_vocab_table";
        public static final String TABLE_NAME_MY_WORD_BANK = "my_word_bank_table";
        // column names
        public static final String COLUMN_NAME_VOCAB = "vocab";
        public static final String COLUMN_NAME_DEFINITION = "definition";
        public static final String COLUMN_NAME_LEVEL = "level";
    }
}
