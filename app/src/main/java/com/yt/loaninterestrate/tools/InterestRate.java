package com.yt.loaninterestrate.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015-04-05.
 */

//贷款利率类
public class InterestRate {

    public Integer id;
    public String title;
    public float rate;

    private Date date;
    private float mon6;
    private float year1;
    private float year3;
    private float year5;
    private float more5;

    private float yeardown5;
    private float yearup5;


    public Float getRate(Double year){
        return getRate(year,0);
    }
    public Float getRate(Double year, Integer type) {

        if(type==0){
            if(year==1){
                return this.year1;
            }else if(year<=3 && year>1){
                return this.year3;
            }else if(year<=5 && year>3){
                return year5;
            }else{
                return more5;
            }
        }else{
            if(year<=5){
                return this.yeardown5;
            }else{
                return this.yearup5;
            }
        }


    }

    public String toString(){
        DateFormat df = new SimpleDateFormat("yyy年MM月dd");
        return df.format(date).toString();
    }
    public InterestRate(Integer id, Date date,float mon6, float year1,float year3,float year5,float more5,float yeardown5,float yearup5) {
        this.id = id;
        this.date = date;
        this.mon6 = mon6;
        this.year1 = year1;
        this.year3 = year3;
        this.year5 = year5;
        this.more5 = more5;
        this.yeardown5 = yeardown5;
        this.yearup5 = yearup5;
    }

    public String getTitle() {
        return title;
    }



    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getMon6() {
        return mon6;
    }

    public void setMon6(float mon6) {
        this.mon6 = mon6;
    }

    public float getYear1() {
        return year1;
    }

    public void setYear1(float year1) {
        this.year1 = year1;
    }

    public float getYear3() {
        return year3;
    }

    public void setYear3(float year3) {
        this.year3 = year3;
    }

    public float getYear5() {
        return year5;
    }

    public void setYear5(float year5) {
        this.year5 = year5;
    }

    public float getMore5() {
        return more5;
    }

    public void setMore5(float more5) {
        this.more5 = more5;
    }

    public float getYeardown5() {
        return yeardown5;
    }

    public void setYeardown5(float yeardown5) {
        this.yeardown5 = yeardown5;
    }

    public float getYearup5() {
        return yearup5;
    }

    public void setYearup5(float yearup5) {
        this.yearup5 = yearup5;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
