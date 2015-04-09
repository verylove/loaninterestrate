package com.yt.loaninterestrate.tools;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.Handler;
import android.widget.Toast;

import com.yt.loaninterestrate.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.LogRecord;

import static java.lang.Thread.sleep;

/**
 * Created by Administrator on 2015-04-05.
 */
public class CheckUpdate {
    private Context context;
    private ProgressDialog dialog;
    static private Boolean init;

    public  CheckUpdate(Context context){
        new  CheckUpdate(context, false);
    }

    public CheckUpdate(final Context context ,Boolean init){
        this.context = context;
        this.init = init;

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


        dialog = new ProgressDialog(context);
        if(!this.init) {
            dialog.setMessage("检查最新利率...");
        }else{
            dialog.setMessage("正在初始化...");
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setIndeterminate(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();

        checkVersion();
    }


    public void downRate(){
        dialog.setMessage("更新到最新利率...");
        dialog.show();
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


                // 执行完毕后给handler发送一个空消息
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("result", str);
                data.putBoolean("isInit",init);
                msg.setData(data);
                handler.sendMessage(msg);
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
            if (dialog.isShowing())
                dialog.dismiss();


            try {
                JSONArray data = new JSONArray(str);
                SQLiteDatabase db = new DataBaseHelp(context).getWritableDatabase();
                for(int i=0;i<data.length();i++){
                    JSONObject rate = data.getJSONObject(i);
                    ContentValues cv = new ContentValues();
                    cv.put("_id",rate.getString("id"));
                    cv.put("date",rate.getString("date"));
                    cv.put("mon6",rate.getString("mon6"));
                    cv.put("year1",rate.getString("year1"));
                    cv.put("year3",rate.getString("year3"));
                    cv.put("yaer5",rate.getString("yaer5"));
                    cv.put("more5",rate.getString("more5"));
                    cv.put("yeardown5",rate.getString("yeardown5"));
                    cv.put("yearup5",rate.getString("yearup5"));
                    db.insert("rate",null,cv);
                }
                db.close();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            dialog.dismiss();
            Toast.makeText(context,"最新利率更新成功",Toast.LENGTH_LONG).show();
            if(init){
                ((MainActivity)context).initRate();
                ((MainActivity)context).initTab();
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
            if (dialog.isShowing())
                dialog.hide();

            String newVersion = "";
            JSONTokener data = new JSONTokener(str);
            try {
                JSONObject date = (JSONObject) data.nextValue();
                newVersion = date.getString("date");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if(Compar_date(newVersion,getVersion()) > 0){
                Toast.makeText(context,"开始下载最新利率",Toast.LENGTH_LONG).show();
                downRate();
            }else{
                Toast.makeText(context, "已经是最新的利率！", Toast.LENGTH_LONG).show();
                dialog.dismiss();
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
                Log.d("YT","新的时间最新");
                return 1;
            }else if(dtNew.getTime()<dtOld.getTime()){
                Log.d("YT","旧时间最新的");
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
        return sb.toString();
    }

    /**
     * 获取利率版本最新事间
     *
     * @return 当前利率最新时间
     */
    public String getVersion() {
        try {
            SQLiteDatabase db = new DataBaseHelp(context).getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT date FROM rate ORDER BY date DESC LIMIT 1", null);
            if(cursor.moveToFirst()){
                String date = cursor.getString(cursor.getColumnIndex("date"));
                return date;
            }
            db.close();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}

