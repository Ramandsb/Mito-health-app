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
        public static final String SLEEP_HOURS = "sleep_hours";
        public static final String SLEEP_QUALITY = "sleep_quality";
        public static final String START_TIME_STAMP = "start_time_stamp";
        public static final String END_TIME_STAMP = "end_time_stamp";

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


        public static final String WATER_UNIQUE_ID = "water_unique_id";
        public static final String WATER_DATE = "water_date";
        public static final String GLASSES = "glasses";
        public static final String ML = "ml";
        public static final String GLASS_SIZE = "glass_size";
        public static final String WATER_SYNCED = "water_synced";

        public static final String TABLE_NAME_WATER = "water_log_table";
        /////////////////////chart Table/////////////////////////////////////////
        public static final String CHART_DATE = "chart_date";
        public static final String CHART_TIMESTAMP = "chart_timestamp";
        public static final String CHART_FOODCAL_CONSUMED = "chart_foodcal_cons";
        public static final String CHART_FOODCAL_REQ = "chart_foodcal_req";
        public static final String CHART_WATER_CONSUMED = "chart_water_con";
        public static final String CHART_WATER_REQ = "chart_water_req";
        public static final String CHART_SLEEP_REQ = "chart_sleep_req";
        public static final String CHART_SLEEP_CONSUMED = "chart_sleep_con";
        public static final String CHART_EXERCAL_REQ = "chart_exer_req";
        public static final String CHART_EXERCAL_BURNED = "chart_exer_burn";

        public static final String TABLE_NAME_CHART = "chart_table";
        ///////////////////////////////////////////////////////

        public static final String CHAT_USER = "chat_user";
        public static final String CHAT_NAME = "chat_name";
        public static final String CHAT_STATUS = "chat_status";
        public static final String CHAT_TYPE = "chat_type";
        public static final String CHAT_PRESENCE_STATUS = "chat_presence_status";
        public static final String CHAT_PRESENCE = "chat_presence";

        public static final String TABLE_NAME_CHAT = "chat_table";
        //////////////////////////////////////////////////////////

        public static final String CM_USER = "cm_user";
        public static final String CM_TIME = "cm_time";
        public static final String CM_MESSAGES = "cm_messages";
        public static final String CM_SOURCE = "cm_source";

        public static final String TABLE_NAME_CM = "cm_table";




    }
}
