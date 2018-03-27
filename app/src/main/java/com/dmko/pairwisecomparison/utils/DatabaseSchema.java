package com.dmko.pairwisecomparison.utils;


public interface DatabaseSchema {
    String DATABASE_NAME = "comparisons_db";
    int VERSION = 1;

    interface Comparisons {
        String TABLE_NAME = "comparisons";

        interface Cols {
            String ID = "id";
            String NAME = "name";
        }
    }

    interface Options {
        String TABLE_NAME = "options";

        interface Cols {
            String ID = "id";
            String COMPARISON_ID = "comparison_id";
            String NAME = "name";
        }
    }

    interface OptionComparisons {
        String TABLE_NAME = "option_comparisons";

        interface Cols {
            String ID = "id";
            String FIRST_OPTION_ID = "first_option_id";
            String SECOND_OPTION_ID = "second_option_id";
            String PROGRESS = "progress";
        }
    }
}
