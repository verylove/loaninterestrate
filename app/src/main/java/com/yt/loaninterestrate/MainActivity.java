package com.yt.loaninterestrate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.yt.loaninterestrate.activity.About;
import com.yt.loaninterestrate.activity.AccumulationFund;
import com.yt.loaninterestrate.activity.Busines;
import com.yt.loaninterestrate.activity.Combination;
import com.yt.loaninterestrate.activity.ExitDialog;
import com.yt.loaninterestrate.tools.DataBaseHelp;
import com.yt.loaninterestrate.tools.InterestRate;


public class MainActivity extends FragmentActivity {


    SectionsPagerAdapter mSectionsPagerAdapter;
    static ViewPager mViewPager;

    //年利率
    public final static List<InterestRate> interestratess = new ArrayList<InterestRate>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        // final ActionBar actionBar = getSupportActionBar();
        // actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // actionBar.setDisplayShowTitleEnabled(false);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //    actionBar.setSelectedNavigationItem(position);
            }
        });

        initRate();
}


    public void initRate(){

        SQLiteDatabase db = new DataBaseHelp(getApplicationContext()).getReadableDatabase();
        Cursor cursor = db.query("rate",null,null,null,null,null,"DATE DESC");
        while(cursor.moveToNext()){
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            Integer id = cursor.getInt(cursor.getColumnIndex("_id"));
            Date date = null;
            try {
                date = df.parse(cursor.getString(cursor.getColumnIndex("date")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Float mon6 = cursor.getFloat(cursor.getColumnIndex("mon6"));
            Float year1 = cursor.getFloat(cursor.getColumnIndex("year1"));
            Float year3 = cursor.getFloat(cursor.getColumnIndex("year3"));
            Float yaer5 = cursor.getFloat(cursor.getColumnIndex("yaer5"));
            Float more5 = cursor.getFloat(cursor.getColumnIndex("more5"));
            Float yeardown5 = cursor.getFloat(cursor.getColumnIndex("yeardown5"));
            Float yearup5 = cursor.getFloat(cursor.getColumnIndex("yearup5"));

            InterestRate tmp = new InterestRate(id,date ,mon6,year1,year3,yaer5,more5,yeardown5,yearup5);
            interestratess.add(tmp);
            tmp = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this).setTitle("确认退出吗？")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();

                        }
                    })
                    .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 点击“返回”后的操作,这里不设置没有任何操作
                        }
                    }).show();

        }

        return false;
        //return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment ret = null;
            switch (position) {
                case 0:
                    ret = Main.newInstance("test1", "test2");
                    break;
                case 1:
                    ret = Busines.newInstance("test1", "test2");
                    break;
                case 2:
                    ret = AccumulationFund.newInstance("test", "test");
                    break;
                case 3:
                    ret = Combination.newInstance("test", "test");
                    break;
                case 4:
                    ret = About.newInstance("test", "test");
                    break;
                default:
                    Main.newInstance("test1", "test2");
            }
            return ret;
            //return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }


}


