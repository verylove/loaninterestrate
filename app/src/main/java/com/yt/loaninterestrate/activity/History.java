package com.yt.loaninterestrate.activity;

import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yt.loaninterestrate.R;
import com.yt.loaninterestrate.tools.HistoryDataBasesHelp;

import java.util.ArrayList;
import java.util.List;

public class History extends ActionBarActivity {

    private ListView listViewHistory;

    private List<HistoryDataClass> historyDatas = new ArrayList<HistoryDataClass>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        listViewHistory = (ListView)findViewById(R.id.listViewData);

        listViewHistory.setAdapter(new adapterHistoryData());

    }

    @Override
    protected void onResume() {
        super.onResume();

        SQLiteDatabase db = new HistoryDataBasesHelp(getApplicationContext()).getReadableDatabase();
        Cursor cursor = db.query("history",null,null,null,null,null,"date desc");

        HistoryDataClass historyData;

        Double rate,price;
        Integer year;
        while(cursor.moveToNext()){
            historyData = new HistoryDataClass();

            year = cursor.getInt(cursor.getColumnIndex("loanYear"));
            if(cursor.getInt(cursor.getColumnIndex("type"))==1){
                //公积金
                rate = cursor.getDouble(cursor.getColumnIndex("loanGJJRate"));
                price = cursor.getDouble(cursor.getColumnIndex("loanGJJMoney"));
            }else{
                rate = cursor.getDouble(cursor.getColumnIndex("loanRate"));
                price = cursor.getDouble(cursor.getColumnIndex("loanMoney"));
            }
            historyData.setLoatRate(rate);
            historyData.setLoatPrice(price);
            historyData.setLoatYear(year);
            historyDatas.add(historyData);
            historyData = null;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_history, container, false);
            return rootView;
        }
    }


    public class adapterHistoryData implements ListAdapter {

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public int getCount() {
            return historyDatas.size();
        }

        @Override
        public Object getItem(int i) {
            return historyDatas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            HistoryView history = null;

            if(view==null){
                history = new HistoryView();
                view =inflater.inflate(R.layout.history_item, null);
                history.textViewPrice = (TextView)view.findViewById(R.id.textViewHistoryPrice);
                history.textViewRate = (TextView)view.findViewById(R.id.textViewHistoryRate);
                history.textViewYear = (TextView)view.findViewById(R.id.textViewHistoryYear);
                history.buttonDel = (Button)view.findViewById(R.id.buttonHistoryDel);
            }else{
                history = (HistoryView)view.getTag();
            }

            history.textViewPrice.setText("100");
            history.textViewRate.setText("0.15");
            history.textViewYear.setText("24");

            return view;
        }

        @Override
        public int getItemViewType(int i) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int i) {
            return false;
        }
    }

    class HistoryView{
        private TextView textViewRate;
        private TextView textViewPrice;
        private TextView textViewYear;
        private Button buttonDel;
    }

    class HistoryDataClass{
        private Double loatRate;
        private Double loatPrice;
        private Integer loatYear;

        public Double getLoatRate() {
            return loatRate;
        }

        public void setLoatRate(Double loatRate) {
            this.loatRate = loatRate;
        }

        public Double getLoatPrice() {
            return loatPrice;
        }

        public void setLoatPrice(Double loatPrice) {
            this.loatPrice = loatPrice;
        }

        public Integer getLoatYear() {
            return loatYear;
        }

        public void setLoatYear(Integer loatYear) {
            this.loatYear = loatYear;
        }
    }

}
