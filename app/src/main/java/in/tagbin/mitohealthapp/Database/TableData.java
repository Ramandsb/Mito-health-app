package in.tagbin.mitohealthapp.Database;

import android.provider.BaseColumns;

/**
 * Created by admin pc on 15-07-2016.
 */
public class TableData {

    public TableData() {

    }

    public static abstract class Tableinfo implements BaseColumns {
        public static final String DATABASE_NAME = "dbapp";
        /////////////////////////////////////////////////////////
        public static final String ID = "id";
        public static final String FOOD_ID = "food_id";
        public static final String FOOD_NAME = "food_name";
        public static final String TIME_CONSUMED = "time_consumed";
        public static final String AMOUNT = "amount";
        public static final String DATE = "date";
        public static final String SYNCED = "synced";
        ///////////////////////////sleep///////////////
        public static final String SLEEP_UNIQUE_ID = "sleep_unique_id";
        public static final String START_TIME = "start_time";
        public static final String END_TIME = "end_time";
        public static final String SLEEP_DATE = "sleep_date";

        public static final String TABLE_NAME_FOOD = "food_log_table";
        public static final String TABLE_NAME_SLEEP = "sleep_log_table";
        //////////////////////exercise table///////////
        public static final String EXER_UNIQUE_ID = "sleep_unique_id";
        public static final String EXER_NAME = "exer_name";
        public static final String EXER_ID = "exer_id";
        public static final String EXER_DATE = "exer_date";
        public static final String WEIGHT = "weight";
        public static final String SETS = "sets";
        public static final String REPS = "reps";

        public static final String TABLE_NAME_EXERCISE = "exer_log_table";
    }
}
