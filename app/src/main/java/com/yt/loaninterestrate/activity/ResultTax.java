package com.yt.loaninterestrate.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.yt.loaninterestrate.R;


public class ResultTax extends ActionBarActivity {
    private Double Qisui,Yinhuasui,Ggwxjj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_tax);

        Intent intent = getIntent();
        Integer OldNew = intent.getIntExtra("OldNew",1);
        Integer HouseType = intent.getIntExtra("HouseType", 1);
        Integer AllOff = intent.getIntExtra("AllOff", 1);
        Double HouseArea = intent.getDoubleExtra("HouseArea",0.0);
        Double  HousePrice = intent.getDoubleExtra("HousePrice", 0.0);
        Double  AGoHousePrice = intent.getDoubleExtra("OldHousePrice", 0.0);
        Boolean isOver5 = intent.getBooleanExtra("isOver5", true);
        Boolean isFrist = intent.getBooleanExtra("isFrist", true);
        Boolean isOnly = intent.getBooleanExtra("isOnly", true);

        if(HouseType<3) {
            Qisui = HousePrice * 0.15;
        }else{
            Qisui = HousePrice * 0.3;
        }

        Yinhuasui = HousePrice * 0.03;

        Ggwxjj = HouseArea * 133;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result_tax, menu);
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
}
