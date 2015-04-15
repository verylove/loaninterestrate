package com.yt.loaninterestrate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.yt.loaninterestrate.activity.Tax;
import com.yt.loaninterestrate.activity.AccumulationFund;
import com.yt.loaninterestrate.activity.Busines;
import com.yt.loaninterestrate.activity.Combination;
import com.yt.loaninterestrate.activity.ExitDialog;
import com.yt.loaninterestrate.activity.Init;
import com.yt.loaninterestrate.tools.CheckUpdate;
import com.yt.loaninterestrate.tools.CheckVersion;
import com.yt.loaninterestrate.tools.DataBaseHelp;
import com.yt.loaninterestrate.tools.InterestRate;



public class MainActivity extends FragmentActivity {


    SectionsPagerAdapter mSectionsPagerAdapter;
    public static ViewPager mViewPager;
    private Fragment fg;


    //年利率
    public final static List<InterestRate> interestratess = new ArrayList<InterestRate>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnalyticsConfig.setAppkey("552d085dfd98c5f0e10008f5");
        AnalyticsConfig.setChannel("Baidu");
        MobclickAgent.updateOnlineConfig( getApplication() );
       // AnalyticsConfig.enableEncrypt(true);
       // int screenWidth = getWindowManager().getDefaultDisplay().getWidth();//真实分辨率 宽
       // int screenHeight = getWindowManager().getDefaultDisplay().getHeight();//真实分辨率 高

        //DisplayMetrics dm = new DisplayMetrics();
        //dm = getResources().getDisplayMetrics();
       // int densityDPI = dm.densityDpi;     // 屏幕密度（每寸像素：120(ldpi)/160(mdpi)/213(tvdpi)/240(hdpi)/320(xhdpi)）
       // Toast.makeText(this, "真实分辨率：" + screenWidth + "*" + screenHeight + "  每英寸:" + densityDPI, Toast.LENGTH_LONG).show();
        //CheckVersion manager = new CheckVersion(MainActivity.this);
        // 检查软件更新
        //manager.checkUpdate();


        initRate();

}

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长

        SharedPreferences sp = getApplicationContext().getSharedPreferences("Skin", getApplicationContext().MODE_PRIVATE);
        int which = sp.getInt("which", 0);
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("skin",which+"");
        MobclickAgent.onEvent(getApplicationContext(), "skin", map);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MobclickAgent.onPause(this);
    }

    public void initRate(){
        interestratess.clear();
        DataBaseHelp dbh = new DataBaseHelp(getApplicationContext());
        SQLiteDatabase db = dbh.getReadableDatabase();
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
            Float mon6 = CheckUpdate.DecodeData(cursor.getString(cursor.getColumnIndex("mon6")));
            Float year1 = CheckUpdate.DecodeData(cursor.getString(cursor.getColumnIndex("year1")));
            Float year3 = CheckUpdate.DecodeData(cursor.getString(cursor.getColumnIndex("year3")));
            Float yaer5 = CheckUpdate.DecodeData(cursor.getString(cursor.getColumnIndex("yaer5")));
            Float more5 = CheckUpdate.DecodeData(cursor.getString(cursor.getColumnIndex("more5")));
            Float yeardown5 = CheckUpdate.DecodeData(cursor.getString(cursor.getColumnIndex("yeardown5")));
            Float yearup5 = CheckUpdate.DecodeData(cursor.getString(cursor.getColumnIndex("yearup5")));

            InterestRate tmp = new InterestRate(id,date ,mon6,year1,year3,yaer5,more5,yeardown5,yearup5);
            interestratess.add(tmp);
            tmp = null;
        }
        cursor.close();
        db.close();
        dbh.close();

        if(MainActivity.interestratess.isEmpty()){
           CheckUpdate ck = new CheckUpdate(this,true);
        }else{
            initTab();
        }


    }

    public  void initTab(){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        fg = getVisibleFragment();
        if(fg!=null) {
            if (fg instanceof Busines) {
                Busines.onKeyDown(keyCode, event);
            } else if (fg instanceof AccumulationFund) {
                AccumulationFund.onKeyDown(keyCode, event);
            } else if (fg instanceof Combination) {
                Combination.onKeyDown(keyCode, event);
            } else if (fg instanceof Tax) {
                Tax.onKeyDown(keyCode, event);
            } else if (fg instanceof Main) {
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
            }
        }

        return false;
       // return super.onKeyDown(keyCode, event);
    }



    public Fragment getVisibleFragment(){
        try {
            List<Fragment> tmp = new ArrayList<Fragment>();
            FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
            List<Fragment> fragments = fragmentManager.getFragments();
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible() && fragment.isAdded()) {
                    tmp.add(fragment);
                }
                // return fragment;
            }
            if (tmp.size() <= 2) {
                if (tmp.get(1) instanceof Tax) {
                    return tmp.get(1);
                } else {
                    return tmp.get(0);
                }
            } else if (tmp.size() >= 3) {
                return tmp.get(1);
            } else {
                return null;
            }
        }catch (Exception e){

        }
        return null;
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
            if( MainActivity.interestratess.isEmpty()){
                return Init.newInstance("test1", "test2");
            }else{
                position = position + 1;
            }


            switch (position) {
                case 0:
                    ret = Init.newInstance("test1", "test2");
                    break;
                case 1:
                    ret = Main.newInstance("test1", "test2");
                    break;
                case 2:
                     ret = Busines.newInstance("test1", "test2");
                    break;
                case 3:
                    ret = AccumulationFund.newInstance("test", "test");
                    break;
                case 4:
                    ret = Combination.newInstance("test", "test");
                    break;
                case 5:
                    ret = Tax.newInstance("test", "test");
                    break;
                default:
                    ret = Main.newInstance("test1", "test2");
            }

            return ret;
            //return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            if(MainActivity.interestratess.isEmpty()) {
                return 6;
            }else{
                return 5;
            }
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


