package com.yt.loaninterestrate.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yt.loaninterestrate.R;


public class ResultTax extends Dialog {
    private Double Qisui,Yinhuasui,Ggwxjj,Yingyeshui,Gerensuode;

    private TextView textViewHouseAllPrice, textViewYinhuashui, textViewPublicWXJJ, textViewQishui,
                textViewYingyeshui,textViewGerensuode,textViewCountAllShui;



    private Integer OldNew, HouseType, AllOff;
    private Double HouseArea , HousePrice, AGoHousePrice;
    private Boolean isOver5, isFrist, isOnly;

    private Button btnClose;

    private LinearLayout HiddenPart;
    private Activity parentActivity;

    public ResultTax(Context context, int theme,Integer OldNew,Integer
            HouseType,Integer AllOff,Double HouseArea ,Double  HousePrice,
            Double  AGoHousePrice,Boolean isOver5, Boolean isFrist,Boolean isOnly) {
        super(context, theme);

        this.OldNew = OldNew;
        this.HouseType = HouseType;
        this.AllOff = AllOff;
        this.HouseArea = HouseArea;
        this.HousePrice = HousePrice;
        this.AGoHousePrice = AGoHousePrice;

        this.isOver5 = isOver5;
        this.isFrist = isFrist;
        this.isOnly = isOnly;


        Qisui = 0.0;
        Yinhuasui = 0.0;
        Ggwxjj = 0.0;
        Yingyeshui = 0.0;
        Gerensuode = 0.0;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_tax);

        if(HiddenPart!=null) HiddenPart.setVisibility(View.GONE);

        btnClose = (Button)findViewById(R.id.btnClose);


        textViewHouseAllPrice = (TextView)findViewById(R.id.HouseAllPrice);
        textViewYinhuashui = (TextView)findViewById(R.id.Yinhuashui);
        textViewPublicWXJJ = (TextView)findViewById(R.id.PublicWXJJ);
        textViewQishui = (TextView)findViewById(R.id.Qishui);

        textViewYingyeshui = (TextView)findViewById(R.id.Yingyeshui);
        textViewGerensuode = (TextView)findViewById(R.id.Gerensuode);
        textViewCountAllShui = (TextView)findViewById(R.id.CountAllShui);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(HiddenPart!=null) HiddenPart.setVisibility(View.VISIBLE);
            }
        });



        if(OldNew==1) {
            if (HouseType < 3 && isFrist) {
                Qisui = get4s5r(HousePrice * 0.015,2);
            } else {
                Qisui = get4s5r(HousePrice * 0.3,2);
            }

            Yinhuasui = get4s5r(HousePrice * 0.0005,2);

            Ggwxjj = get4s5r(HouseArea * 133, 2);

            if(!isOnly){
                Gerensuode = get4s5r(HousePrice*0.01,2);
            }

            if(!isOver5){
                Yingyeshui = get4s5r(HousePrice*0.056,2);
                Gerensuode = get4s5r(HousePrice*0.01,2);
            }


            textViewHouseAllPrice.setText(get4s5r(HousePrice,2)+"");
            textViewYinhuashui.setText(Yinhuasui+"");
            textViewPublicWXJJ.setText(Ggwxjj+"");
            textViewQishui.setText(Qisui+"");

            textViewGerensuode.setText(Gerensuode+"");
            textViewYingyeshui.setText(Yingyeshui+"");
            textViewCountAllShui.setText(Yinhuasui + Ggwxjj + Qisui + Gerensuode + Yingyeshui+"");
        }else{

            Yinhuasui = get4s5r(HousePrice * 0.0005,2);
            Ggwxjj = get4s5r(HouseArea *133 ,2 );
            if(HouseArea>144){
                Qisui = get4s5r(HousePrice * 0.03, 2);
            }else {
                Qisui = get4s5r(HousePrice * 0.015, 2);
            }

            textViewHouseAllPrice.setText(get4s5r(HousePrice,2)+"");
            textViewYinhuashui.setText(Yinhuasui+"");
            textViewPublicWXJJ.setText(Ggwxjj+"");
            textViewQishui.setText(Qisui+"");

            textViewGerensuode.setText(0+"");
            textViewYingyeshui.setText(0+"");
            textViewCountAllShui.setText(get4s5r(Yinhuasui + Ggwxjj + Qisui + Gerensuode + Yingyeshui,2)+"");
        }


    }


    public  void setHiddenPart(LinearLayout tmp){
        this.HiddenPart = tmp;
    }

    public void setActivity(Activity v){
        parentActivity = v;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(HiddenPart!=null) HiddenPart.setVisibility(View.VISIBLE);
        }
        return super.onKeyDown(keyCode, event);

    }


    public static Double get4s5r(Double data, Integer c) {
        if(data!=0) {
            data = new java.math.BigDecimal(Double.toString(data)).setScale(c, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return data;
    }

}
