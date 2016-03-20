package com.darkfire.database;

import android.provider.BaseColumns;

/**
 * Created by Siddharth on 3/15/2016.
 */
public class PaymentContract {

    public PaymentContract(){

    }

    public static abstract class PaymentEntry implements BaseColumns{

        public static final String TABLE_NAME = "payments";
        public static final String COLUMN_NAME_ENTRY_ID = "tid";
        public static final String COLUMN_NAME_PAYER_NAME = "PAYER_NAME";
        public static final String COLUMN_NAME_PAYEE_NAME = "PAYEE_NAME";
        public static final String COLUMN_NAME_PAYER_VIRTUAL_ADD = "PAYER_VIRTUAL_ADD";
        public static final String COLUMN_NAME_PAYEE_VIRTUAL_ADD = "PAYEE_VIRTUAL_ADD";
        public static final String COLUMN_NAME_PAYER_ID_NUMBER = "PAYER_ID_NUMBER";
        public static final String COLUMN_NAME_PAYEE_ID_NUMBER = "PAYEE_ID_NUMBER";
        public static final String COLUMN_NAME_TRANSACTION_DESC = "TRANSACTION_DESC";
        public static final String COLUMN_NAME_AMOUNT = "AMOUNT";

    }
}
