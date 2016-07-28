package com.example.ershoudaili;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by junyuan on 2015/8/10.
 */
public class My_store extends ActionBarActivity{
    private TextView top;
    private ImageButton back;
    private Button publish;
    private Button enter_store;
    private Button record;
    private String userName;
    private String json_string;
    private String json_string1;
    private HashMap<String,String> map2;
    private ArrayList<HashMap<String,String>> list;
    private  String s_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store);
        getSupportActionBar().hide();
        top = (TextView)findViewById(R.id.top4);
        TextPaint tp1 = top.getPaint();
        tp1.setFakeBoldText(true);
        back = (ImageButton)findViewById(R.id.Bacbutton4);
        back.setOnClickListener(new Buttonlistener_back1());
        publish = (Button)findViewById(R.id.publish1);
        publish.setOnClickListener(new Buttonlistener_enter());
        enter_store = (Button)findViewById(R.id.enter_store);
        enter_store.setOnClickListener(new Buttonlistener_enter_store());
        Intent intent2 = getIntent();
        userName = intent2.getStringExtra("name");
        json_string = intent2.getStringExtra("msg");
        load_msg load =new load_msg();
        load.execute();
    }
    class load_msg extends AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
            //第一个执行方法
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... params) {
            try {
                map2 = new HashMap<>();
                map2.put("a_name",userName);
                json_string1 = HttpUtil.postListMap(HttpUtil.BASE_URL1+"getStoreSerlvet",map2);
                list = JSONutils.getStore(json_string1);
            } catch (Exception e) {
                System.out.println(e + "1");
            }
            s_name = list.get(0).get("s_name");
            return json_string1;
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }
        @Override
        protected void onPostExecute(String result) {
        }
    }
    class Buttonlistener_enter implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("name",userName);
            intent.putExtra("msg",json_string);
            intent.putExtra("s_name",s_name);
            System.out.println("前页："+s_name);
            intent.setClass(My_store.this,Publish_Activity.class);
            startActivity(intent);
        }
    }
    class Buttonlistener_back1 implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            finish();
        }
    }
    class Buttonlistener_enter_store implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent3 = new Intent();
            intent3.putExtra("name",userName);
            intent3.setClass(My_store.this,Store_page.class);
            startActivity(intent3);
        }
    }
}
