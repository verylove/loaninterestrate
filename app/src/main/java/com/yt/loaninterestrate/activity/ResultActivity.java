package com.yt.loaninterestrate.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yt.loaninterestrate.tools.Calculate;
import com.yt.loaninterestrate.R;
import com.yt.loaninterestrate.tools.DataBaseHelp;
import com.yt.loaninterestrate.tools.HistoryDataBasesHelp;
import com.yt.loaninterestrate.tools.InterestRate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Yang on 2015/4/2.
 */
public class ResultActivity extends Dialog {

    private double loanMoney, loanYear, loanRate ,loanGJJRate,loanGJJMoney;

    private TextView textViewDiminishingMoney, textViewDiminishingMonth, textViewDiminishingFristMonthMoney, textViewDiminishingOffMoney, textViewDiminishingInterestMoney, textViewDiminishingAllMoney;

    private TextView textViewEvenMoney, textViewEvenMonth, textViewEvenMonthMoney, textViewEvenInterestMoney, textViewEvenAllMoney;

    private Activity parentActivity;

    private Point startPos,endPos;

    private Button btnClose;

    private Boolean ZH = false;

    private LinearLayout HiddenPart;

    public ResultActivity(Context context, int theme,double loanRate, double loanMoney, double loanYear) {
        super(context, theme);
        this.loanMoney = loanMoney;
        this.loanRate = loanRate;
        this.loanYear = loanYear;
        this.HiddenPart = null;

        SQLiteDatabase db = new HistoryDataBasesHelp(context).getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("loanRate",formatFloatNumber(loanRate));
        cv.put("loanMoney",formatFloatNumber(loanMoney));
        cv.put("loanYear",loanYear);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        cv.put("date",df.format(new Date()));
        cv.put("type",0);

        db.insert("history",null,cv);

        db.close();
    }

    public ResultActivity(Context context, int theme,double loanRate, double loanMoney,double loanGJJRate, double loanGJJMoney, double loanYear) {
        super(context, theme);
        this.loanMoney = loanMoney;
        this.loanRate = loanRate;
        this.loanYear = loanYear;
        this.loanGJJMoney = loanGJJMoney;
        this.loanGJJRate = loanGJJRate;
        ZH = true;
        this.HiddenPart = null;

        SQLiteDatabase db = new HistoryDataBasesHelp(context).getWritableDatabase();


        ContentValues cv = new ContentValues();
        cv.put("loanRate",formatFloatNumber(loanRate));
        cv.put("loanMoney",formatFloatNumber(loanMoney));
        cv.put("loanYear",loanYear);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        cv.put("date",df.format(new Date()));
        cv.put("type",0);

        db.insert("history",null,cv);

        cv.clear();
        cv.put("loanRate",formatFloatNumber(loanGJJRate));
        cv.put("loanMoney",formatFloatNumber(loanGJJMoney));
        cv.put("loanYear",loanYear);
        cv.put("date",df.format(new Date()));
        cv.put("type",1);

        db.insert("history",null,cv);

        db.close();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_result);

        if(this.HiddenPart!=null) this.HiddenPart.setVisibility(View.GONE);

        btnClose = (Button)findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(HiddenPart!=null) HiddenPart.setVisibility(View.VISIBLE);
            }
        });
        //处理显示数据
        textViewEvenMoney = (TextView) findViewById(R.id.textViewEvenMoney);
        textViewEvenMonth = (TextView) findViewById(R.id.textViewEvenMonth);
        textViewEvenMonthMoney = (TextView) findViewById(R.id.textViewEvenMonthMoney);
        textViewEvenInterestMoney = (TextView) findViewById(R.id.textViewEvenInterestMoney);
        textViewEvenAllMoney = (TextView) findViewById(R.id.textViewEvenAllMoney);

        textViewDiminishingMoney = (TextView) findViewById(R.id.textViewDiminishingMoney);
        textViewDiminishingMonth = (TextView) findViewById(R.id.textViewDiminishingMonth);
        textViewDiminishingFristMonthMoney = (TextView) findViewById(R.id.textViewDiminishingFristMonthMoney);
        textViewDiminishingOffMoney = (TextView) findViewById(R.id.textViewDiminishingOffMoney);
        textViewDiminishingInterestMoney = (TextView) findViewById(R.id.textViewDiminishingInterestMoney);
        textViewDiminishingAllMoney = (TextView) findViewById(R.id.textViewDiminishingAllMoney);


        if(ZH==true){
            Calculate t1 = new Calculate(loanRate, loanMoney, loanYear);
            t1.evenMoney();
            Calculate t11 = new Calculate(loanGJJRate, loanGJJMoney, loanYear);
            t11.evenMoney();

            //等息
            textViewEvenMoney.setText(formatFloatNumber(t1.loanAmount+t11.loanAmount));
            textViewEvenMonth.setText(t1.monthCount+"");
            textViewEvenMonthMoney.setText(formatFloatNumber(t1.repayMonthMoney+t11.repayMonthMoney));
            textViewEvenInterestMoney.setText(formatFloatNumber(t1.repayInterest+t11.repayInterest));
            textViewEvenAllMoney.setText(formatFloatNumber(t1.repayAllMoney+t11.repayAllMoney));

            Calculate t2 = new Calculate(loanRate, loanMoney, loanYear);
            t2.diminishingMoney();
            Calculate t22 = new Calculate(loanGJJRate, loanGJJMoney, loanYear);
            t22.diminishingMoney();
            //等金
            textViewDiminishingMoney.setText(formatFloatNumber(t2.loanAmount+t22.loanAmount));
            textViewDiminishingMonth.setText(t2.monthCount+"");
            textViewDiminishingFristMonthMoney.setText(formatFloatNumber(t2.returnAllMonth.get(0).doubleValue()+t22.returnAllMonth.get(0).doubleValue()));
            textViewDiminishingOffMoney.setText(ResultTax.get4s5r(t2.repayLiminishing+t22.repayLiminishing,2)+"");
            textViewDiminishingInterestMoney.setText(formatFloatNumber(t2.repayInterest+t22.repayInterest));
            textViewDiminishingAllMoney.setText(formatFloatNumber(t2.repayAllMoney+t22.repayAllMoney));
        }else {


            Calculate t1 = new Calculate(loanRate, loanMoney, loanYear);
            t1.evenMoney();

            //等息
            textViewEvenMoney.setText(formatFloatNumber(t1.loanAmount));
            textViewEvenMonth.setText(t1.monthCount+"");
            textViewEvenMonthMoney.setText(formatFloatNumber(t1.repayMonthMoney));
            textViewEvenInterestMoney.setText(formatFloatNumber(t1.repayInterest));
            textViewEvenAllMoney.setText(formatFloatNumber(t1.repayAllMoney));

            Calculate t2 = new Calculate(loanRate, loanMoney, loanYear);
            t2.diminishingMoney();
            //等金
            textViewDiminishingMoney.setText(formatFloatNumber(t2.loanAmount));
            textViewDiminishingMonth.setText(formatFloatNumber(t2.monthCount));
            textViewDiminishingFristMonthMoney.setText(formatFloatNumber(t2.returnAllMonth.get(0).doubleValue()));
            textViewDiminishingOffMoney.setText(ResultTax.get4s5r(t2.repayLiminishing,2)+"");
            textViewDiminishingInterestMoney.setText(formatFloatNumber(t2.repayInterest));
            textViewDiminishingAllMoney.setText(formatFloatNumber(t2.repayAllMoney));

        }
        startPos = new Point();
        endPos = new Point();


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(HiddenPart!=null) HiddenPart.setVisibility(View.VISIBLE);
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

//        Window dialogWindow = this.getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//
//
//        int x = (int)event.getX();
//        int y = (int)event.getY();;
//        switch(event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                startPos.x = x;
//                startPos.y = y;
//                break;
//            case MotionEvent.ACTION_MOVE:
//
//                endPos.x = x;
//                endPos.y = y;
//                lp.x += endPos.x - startPos.x;
//                lp.y += endPos.y - startPos.y;
//
//                if(lp.x<0) lp.x = 0;
//                if(lp.y<0) lp.y = 0;
//
//                WindowManager m =   parentActivity.getWindowManager();
//                Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
//                if(lp.x>(d.getWidth()-lp.width)) lp.x = (int)d.getWidth()-lp.width;
//                if(lp.y>(d.getHeight()-lp.height)) lp.x = (int)d.getHeight()-lp.height;
//                dialogWindow.setAttributes(lp);
//                break;
//            case MotionEvent.ACTION_UP:
//
//                break;
//        }
//
//
        return true;//处理了触摸消息，消息不再传递
//
//      //  return super.onTouchEvent(event);
//

    }

    public  void setHiddenPart(LinearLayout tmp){
        this.HiddenPart = tmp;
    }

    public void setActivity(Activity v){
        parentActivity = v;
    }

    /*
    public static Double get4s5r(Double data, Integer c) {
        if(data!=0.0) {
            data = new java.math.BigDecimal(Double.toString(data)).setScale(c, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
        }else{
            data = 0.0;
        }
        return data;
    }
    */


    public static String formatFloatNumber(double value) {
        if(value != 0.00){
            java.text.DecimalFormat df = new java.text.DecimalFormat("########.00");
            return df.format(value);
        }else{
            return "0.00";
        }

    }

}
