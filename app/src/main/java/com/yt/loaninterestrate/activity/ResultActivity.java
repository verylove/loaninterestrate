package com.yt.loaninterestrate.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.yt.loaninterestrate.Calculate;
import com.yt.loaninterestrate.R;

/**
 * Created by Yang on 2015/4/2.
 */
public class ResultActivity extends Dialog {

    private double loanMoney, loanYear, loanRate ;

    private TextView textViewDiminishingMoney, textViewDiminishingMonth, textViewDiminishingFristMonthMoney, textViewDiminishingOffMoney, textViewDiminishingInterestMoney, textViewDiminishingAllMoney;

    private TextView textViewEvenMoney, textViewEvenMonth, textViewEvenMonthMoney, textViewEvenInterestMoney, textViewEvenAllMoney;



    public ResultActivity(Context context, int theme,double loanRate, double loanMoney, double loanYear) {
        super(context, theme);
        this.loanMoney = loanMoney;
        this.loanRate = loanRate;
        this.loanYear = loanYear;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_result);

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


        Calculate t1 = new Calculate(loanRate, loanMoney, loanYear);
        t1.evenMoney();

        //等息
        textViewEvenMoney.setText(get4s5r(t1.loanAmount, 2) + "");
        textViewEvenMonth.setText(get4s5r(t1.monthCount, 0) + "");
        textViewEvenMonthMoney.setText(get4s5r(t1.repayMonthMoney, 2) + "");
        textViewEvenInterestMoney.setText(get4s5r(t1.repayInterest, 2) + "");
        textViewEvenAllMoney.setText(get4s5r(t1.repayAllMoney, 2) + "");

        Calculate t2 = new Calculate(loanRate, loanMoney, loanYear);
        t2.diminishingMoney();
        //等金
        textViewDiminishingMoney.setText(get4s5r(t2.loanAmount, 2) + "");
        textViewDiminishingMonth.setText(get4s5r(t2.monthCount, 0) + "");
        textViewDiminishingFristMonthMoney.setText(get4s5r(t2.returnAllMonth.get(0).doubleValue(), 2) + "");
        textViewDiminishingOffMoney.setText(get4s5r(t2.repayLiminishing, 2) + "");
        textViewDiminishingInterestMoney.setText(get4s5r(t2.repayInterest, 2) + "");
        textViewDiminishingAllMoney.setText(get4s5r(t2.repayAllMoney, 2) + "");

    }


    public Double get4s5r(Double data, Integer c) {
        if(data!=0) {
            data = new java.math.BigDecimal(Double.toString(data)).setScale(c, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return data;
    }

}
