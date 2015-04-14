package com.yt.loaninterestrate.activity;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yt.loaninterestrate.Main;
import com.yt.loaninterestrate.MainActivity;
import com.yt.loaninterestrate.R;
import com.yt.loaninterestrate.tools.HistoryDataBasesHelp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class History extends ActionBarActivity {



    private final List<HistoryDataClass> historyDatas = new ArrayList<HistoryDataClass>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        initData();

    }


    public void initData(){
        SQLiteDatabase db = new HistoryDataBasesHelp(getApplicationContext()).getReadableDatabase();
        Cursor cursor = db.query("history",null,null,null,null,null,"date desc");

        HistoryDataClass historyData;

        Double rate,price;
        Integer year;
        String date;
        while(cursor.moveToNext()){
            historyData = new HistoryDataClass();

            year = cursor.getInt(cursor.getColumnIndex("loanYear"));
            //if(cursor.getInt(cursor.getColumnIndex("type"))==1){
                rate = cursor.getDouble(cursor.getColumnIndex("loanRate"));
                price = cursor.getDouble(cursor.getColumnIndex("loanMoney"));
            //}
            date = cursor.getString(cursor.getColumnIndex("date"));
            historyData.setDate(date);
            historyData.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            historyData.setLoatRate(rate);
            historyData.setLoatPrice(price);
            historyData.setLoatYear(year);
            historyDatas.add(historyData);
            historyData = null;
        }

        cursor.close();
        db.close();


    }

    @Override
    protected void onResume() {
        super.onResume();

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
    public class PlaceholderFragment extends Fragment {
        private ListView listViewHistory;

        private Button buttonDel;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_history, container, false);

            Main.initSkin(getActivity(),rootView);

            buttonDel = (Button)rootView.findViewById(R.id.buttonDel);

            if(historyDatas.size()==0){
                buttonDel.setVisibility(View.GONE);
            }
            buttonDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SQLiteDatabase db = new HistoryDataBasesHelp(getApplicationContext()).getWritableDatabase();
                    db.delete("history","1",null);
                    db.close();
                    historyDatas.clear();
                    listViewHistory.setAdapter(new adapterHistoryData());
                    buttonDel.setVisibility(View.GONE);
                }
            });

            listViewHistory = (ListView)rootView.findViewById(R.id.listViewData);
            listViewHistory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                   // Toast.makeText(getActivity(),position+":"+id+"",Toast.LENGTH_SHORT).show();;
                    SQLiteDatabase db = new HistoryDataBasesHelp(getApplicationContext()).getWritableDatabase();
                    db.delete("history","_id="+id,null);
                    db.close();

                    historyDatas.clear();
                    initData();
                    if(historyDatas.size()==0) {
                        buttonDel.setVisibility(View.GONE);
                    }
                    listViewHistory.setAdapter(new adapterHistoryData());
                    return false;
                }
            });


            listViewHistory.setAdapter(new adapterHistoryData());
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
            return historyDatas.get(i).getId();
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
                view = inflater.inflate(R.layout.history_item, null);
                history.textViewDate = (TextView)view.findViewById(R.id.textViewHistoryDate);
                history.textViewPrice = (TextView)view.findViewById(R.id.textViewHistoryPrice);
                history.textViewRate = (TextView)view.findViewById(R.id.textViewHistoryRate);
                history.textViewYear = (TextView)view.findViewById(R.id.textViewHistoryYear);
                view.setTag(history);
            }else{
                history = (HistoryView)view.getTag();
            }
            HistoryDataClass historyData = historyDatas.get(i);
            history.textViewYear.setText(historyData.getLoatYear()+"");
            history.textViewPrice.setText(historyData.getLoatPrice()+"");
            history.textViewRate.setText(historyData.getLoatRate()+"");
           // DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            history.textViewDate.setText(historyData.getDate()+"");

            return view;
        }

        @Override
        public int getItemViewType(int i) {
            return 1;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
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
            return true;
        }
    }

    class HistoryView{
        private TextView textViewDate;
        private TextView textViewRate;
        private TextView textViewPrice;
        private TextView textViewYear;

        private Button buttonDel;
    }

    class HistoryDataClass{
        private String date;
        private Double loatRate;
        private Double loatPrice;
        private Integer loatYear;
        private Integer id;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

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
