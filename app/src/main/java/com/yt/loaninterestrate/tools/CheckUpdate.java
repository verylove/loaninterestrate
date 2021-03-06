package com.yt.loaninterestrate.tools;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.Handler;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yt.loaninterestrate.Main;
import com.yt.loaninterestrate.MainActivity;
import com.yt.loaninterestrate.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.LogRecord;

import static java.lang.Thread.sleep;

/**
 * Created by Administrator on 2015-04-05.
 */
public class CheckUpdate {
    private Context context;
    private ProgressDialog dialog;
    static private Boolean init;
    private View view;
    private Integer hiddenTip;

    public  CheckUpdate(Context context){
        this(context, false);
    }


    public CheckUpdate(final Context context,String type,View v ){
        this.context = context;
        this.view = v;
        this.hiddenTip = 0;
        this.init = false;
        if(type=="Tip"){
            Network network = new Network();
            if(network.isNetworkConnected(context)){
                checkVersion();
            }else {

            }
        }else{

        }

    }
    //点击处理
    public CheckUpdate(final Context context ,Boolean init){
        this.context = context;
        this.init = init;
        this.hiddenTip = 1;

        Network network = new Network();
        if(!network.isNetworkConnected(context)){
            if(this.init) {

                Toast.makeText(context, "请检查网络连接,初始化失败", Toast.LENGTH_LONG).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)context).finish();
                    }
                },3000);

                return;
            }else{
                Toast.makeText(context, "请检查网络连接", Toast.LENGTH_LONG).show();
                return ;
            }
        }

        if(hiddenTip==1) {
            dialog = new ProgressDialog(context);
            if (!this.init) {
                dialog.setMessage("检查最新利率...");
            } else {
                dialog.setMessage("正在初始化...");
            }
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
            dialog.setIndeterminate(true);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }
        checkVersion();
    }



    public void downRate(){
        dialog.setMessage("更新到最新利率...");
        if(hiddenTip==1) dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String str = "";
                try {
                    str = getJsonString("http://www.jblv.com/com.yt.loaninterestate/loaninterestrate.php?type=data");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }catch (Exception e) {
                    e.printStackTrace();
                }

                // 执行完毕后给handler发送一个空消息
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("result", str);
                data.putBoolean("isInit",init);
                msg.setData(data);
                handlerData.sendMessage(msg);
            }
        }).start();
    }

    public void checkVersion(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String str = "";
                try {
                    str = getJsonString("http://www.jblv.com/com.yt.loaninterestate/loaninterestrate.php?type=version");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    // 执行完毕后给handler发送一个空消息
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("result", str);
                    data.putBoolean("isInit", init);
                    msg.setData(data);
                    handler.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //定义Handler对象
    Handler handlerData = new Handler() {
        @Override
        // 当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 处理UI
            Bundle bundle = msg.getData();
            String str = bundle.getString("result");
            Boolean init = bundle.getBoolean("isInit");
            if(hiddenTip==1 && dialog!=null) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
            SQLiteDatabase db = null;
            try {
                JSONArray data = new JSONArray(str);
                db = new DataBaseHelp(context).getWritableDatabase();
                db.delete("rate","1",null);
                for(int i=0;i<data.length();i++){
                    JSONObject rate = data.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("_id",rate.getString("id"));
                    cv.put("date",rate.getString("date"));
                    cv.put("mon6",EncodeData(rate.getString("mon6")));
                    cv.put("year1",EncodeData(rate.getString("year1")));
                    cv.put("year3",EncodeData(rate.getString("year3")));
                    cv.put("yaer5",EncodeData(rate.getString("yaer5")));
                    cv.put("more5",EncodeData(rate.getString("more5")));
                    cv.put("yeardown5",EncodeData(rate.getString("yeardown5")));
                    cv.put("yearup5",EncodeData(rate.getString("yearup5")));
                    db.insert("rate",null,cv);
                }
                db.close();
            } catch (JSONException e) {
                db.close();
                e.printStackTrace();
            }


            if(hiddenTip==1) dialog.dismiss();
            if(hiddenTip==1) Toast.makeText(context,"最新利率更新成功",Toast.LENGTH_LONG).show();
            if(init){
                ((MainActivity)context).initRate();
                ((MainActivity)context).initTab();
            }else{
                ((MainActivity)context).initRate();
            }
        }
    };

    //定义Handler对象
    Handler handler = new Handler() {
        @Override
        // 当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 处理UI
            Bundle bundle = msg.getData();
            String str = bundle.getString("result");
            Boolean init = bundle.getBoolean("isInit");

            if(dialog!=null) {
                if(hiddenTip==1) {
                    if (dialog.isShowing())
                        dialog.hide();
                }
            }

            String newVersion = "";
            JSONTokener data = new JSONTokener(str);
            try {
                JSONObject date = (JSONObject) data.nextValue();
                newVersion = date.getString("date");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(Compar_date(newVersion,getVersion()) > 0){
                if(hiddenTip==1) Toast.makeText(context,"开始下载最新利率",Toast.LENGTH_SHORT).show();
                if(hiddenTip==0) {
                    if(view!=null) {
                        View vUpdate = view.findViewById(R.id.toolBar);
                        BadgeView badge = new BadgeView(context, vUpdate);
                        badge.setText("New!");
                        badge.setTextSize(12);
                        badge.setBadgePosition(BadgeView.POSITION_TOP_UPDATE);

                        TranslateAnimation anim = new TranslateAnimation(30, 0, 0, 0);
                        anim.setInterpolator(new BounceInterpolator());
                        anim.setDuration(1000);
                        anim.setRepeatCount(10);
                        TranslateAnimation anim2 = new TranslateAnimation(0, 30, 0, 0);
                        anim2.setDuration(500);
                        anim2.setRepeatCount(10);
                        badge.toggle(anim, anim2);
                    }
                }else {
                    downRate();
                }
            }else{
                if(hiddenTip==1) Toast.makeText(context, "已经是最新的利率！", Toast.LENGTH_LONG).show();
                if(hiddenTip==1)
                    if(dialog!=null){
                        dialog.dismiss();
                    }
            }
        }
    };

    public int Compar_date(String newDate,String oldDate){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if(oldDate=="")oldDate = "1770-01-01";
            if(newDate=="")newDate = "1770-01-01";
            Date dtNew = df.parse(newDate);
            Date dtOld = df.parse(oldDate);

            if(dtNew.getTime()>dtOld.getTime()){
                return 1;
            }else if(dtNew.getTime()<dtOld.getTime()){
                return -1;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getJsonString(String urlPath) throws Exception {

        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream inputStream = connection.getInputStream();

        Reader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);

        String str = null;
        StringBuffer sb = new StringBuffer();
        while (((str = bufferedReader.readLine()) != null)) {
            sb.append(str);
        }
        reader.close();
        connection.disconnect();
        String tmp  = new String(Base64.decode(sb.toString(), Base64.DEFAULT));
        String tmp1  = new String(Base64.decode(tmp,Base64.DEFAULT));
        String tmp2  = new String(Base64.decode(tmp1,Base64.DEFAULT));
        return tmp2;
    }

    /**
     * 获取利率版本最新事间
     *
     * @return 当前利率最新时间
     */
    public String getVersion() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {

            db = new DataBaseHelp(context).getReadableDatabase();
            cursor = db.rawQuery("SELECT date FROM rate ORDER BY date DESC LIMIT 1", null);
            if(cursor.moveToFirst()){
                String date = cursor.getString(cursor.getColumnIndex("date"));
                cursor.close();
                db.close();
                return date;
            }
            cursor.close();
            db.close();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            if(cursor!=null)  cursor.close();
            if(db!=null) db.close();
            return "";
        }
    }


    public static String EncodeData(String str){
        String tmp = "";
        tmp = String.valueOf((int)(Math.random() * 999+100))+str+String.valueOf((int)(Math.random() * 999+100));
        return tmp;
    }

    public static Float DecodeData(String str){
        String tmp = str.substring(3,str.length()-3);
        return Float.parseFloat(tmp);
    }

}

