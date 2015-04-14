package com.yt.loaninterestrate.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Yang on 2015/4/10.
 */
public class HistoryDataBasesHelp  extends SQLiteOpenHelper {


    public HistoryDataBasesHelp(Context context) {
        super(context, "history", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS history (_id INTEGER PRIMARY KEY, loanRate DOUBLE , loanMoney DOUBLE ,loanYear DOUBLE ,type INTEGER ,date DATE )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
