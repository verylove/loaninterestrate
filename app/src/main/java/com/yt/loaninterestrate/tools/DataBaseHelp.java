package com.yt.loaninterestrate.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015-04-05.
 */
public class DataBaseHelp extends SQLiteOpenHelper {

    private Context context;
    private String tablename;

    public DataBaseHelp(Context context) {
        super(context, "rate", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS rate (_id INTEGER PRIMARY KEY, date DATE,mon6 DOUBLE,year1 DOUBLE,year3 DOUBLE,yaer5 DOUBLE,more5 DOUBLE,yeardown5 DOUBLE,yearup5  DOUBLE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
