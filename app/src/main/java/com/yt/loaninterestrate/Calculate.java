package com.yt.loaninterestrate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yang on 2015/3/31.
 */
public class Calculate {

    public Double loanAmount;//贷款本金
    public Double monthInterestRate;//月利率
    public Double monthCount;//还款月数
    public Double yearInterestRate;//年利率

    public Double repayMonthMoney;//月还款本息
    public Double repayAllMoney;//合计还款
    public Double repayInterest;//合计利息
    public Double repayLiminishing;//每月递减

    public Double returnedMoney;//已经还钱


    public List<Double> returnAllMonth  = new ArrayList<>();//递减还款数组

    public Calculate(Double yearInterestRate, Double loanAmount, Double repaymentYears ) {
        this.yearInterestRate = yearInterestRate;
        this.loanAmount = loanAmount;
        this.monthCount = repaymentYears * 12;
        this.monthInterestRate = yearInterestRate / 12;
        this.returnedMoney = 0.0;
        this.repayAllMoney = 0.0;
        this.repayInterest = 0.0;
        this.repayLiminishing = 0.0;
        this.repayMonthMoney = 0.0;
    }
  /*
        等额本息计算公式：〔贷款本金×月利率×（1＋月利率）＾还款月数〕÷〔（1＋月利率）＾还款月数－1〕
        等额本金计算公式：每月还款金额 = （贷款本金 ÷ 还款月数）+（本金 — 已归还本金累计额）×每月利率

        其中＾符号表示乘方,
        例子：
            举例说明假设以10000元为本金、在银行贷款10年、基准利率是6.65％
            等额本息还款法（每月等额还款）：
                月利率=年利率÷12=0.0665÷12=0.005541667
                月还款本息=〔10000×0.005541667×（1＋0.005541667）＾120〕÷〔（1＋0.005541667）＾120－1〕=114.3127元
                合计还款 13717.52元  合计利息 3717.52万元

            等额本金还款法（逐月递减还款） ：
                每月还款金额 = （贷款本金÷还款月数）+（本金 — 已归还本金累计额）×每月利率=（10000  ÷120）+（10000— 已归还本金累计额）×0.005541667
                首月还款 138.75元
                每月递减0.462元
                合计还款 13352.71元
                利息 3352.71元
     */


    /*
          等额本息还款法（每月等额还款）：
              月利率=年利率÷12=0.0665÷12=0.005541667
              月还款本息=〔10000×0.005541667×（1＋0.005541667）＾120〕÷〔（1＋0.005541667）＾120－1〕=114.3127元
              合计还款 13717.52元  合计利息 3717.52万元
     */
    public  void evenMoney() {

        repayMonthMoney = loanAmount * monthInterestRate * (Math.pow((1 + monthInterestRate), monthCount)) / (Math.pow((1.0 + monthInterestRate), monthCount) - 1);
        repayAllMoney = repayMonthMoney * monthCount;
        repayInterest = repayAllMoney - loanAmount;

    }


    /*
      等额本金还款法（逐月递减还款） ：
      每月还款金额 = （贷款本金÷还款月数）+（本金 — 已归还本金累计额）×每月利率
                   = （10000  ÷120）+（10000— 已归还本金累计额）×0.005541667
      首月还款 138.75元
      每月递减0.462元
      合计还款 13352.71元
      利息 3352.71元
  */
  public void diminishingMoney(){

      System.out.printf("----------------");
      for(int i = 0;i<monthCount;i++){
          returnedMoney = i * loanAmount / monthCount;
          repayMonthMoney = (loanAmount / monthCount ) + ( loanAmount - returnedMoney ) * monthInterestRate;
          //System.out.printf(i+"月"+repayMonthMoney+"\n");
          returnAllMonth.add(i,repayMonthMoney);
          repayMonthMoney += repayMonthMoney;

      }

      repayLiminishing = returnAllMonth.get(1)- returnAllMonth.get(0);
      repayInterest = repayAllMoney - loanAmount;

  }

    public static void main(String args[]) {
        Calculate t1 = new Calculate(0.0590, 10000.0, 10.0);
        //t1.evenMoney();
        t1.diminishingMoney();




    }
}
