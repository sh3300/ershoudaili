package com.example.ershoudaili;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by junyuan on 2015/8/10.
 */
public class Personal_center extends ActionBarActivity {
    private TextView top;
    private Button my_store;
    private Button personal_information;
    private ImageButton back;
    private Button zhuxiao;
    private Button verification;
    private ImageView touxiang;
    private String url = HttpUtil.BASE_URL; //POST方式
    private byte[] get;
    private String picture;
    private HashMap<String,String> map1;
    private String json_string;
    private ArrayList<HashMap<String,String>> list;
    private String userName;
    private String log_status;
    private Bitmap photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal);
        getSupportActionBar().hide();
        top = (TextView)findViewById(R.id.top3);
        TextPaint tp1 = top.getPaint();
        tp1.setFakeBoldText(true);
        my_store = (Button)findViewById(R.id.my_store);
        my_store.setOnClickListener(new Buttonlistener_store());
        back = (ImageButton)findViewById(R.id.Bacbutton6);
        back.setOnClickListener(new Buttonlistener_back2());
        zhuxiao = (Button)findViewById(R.id.zhuxiao);
        zhuxiao.setOnClickListener(new Buttonlistener_zhuxiao());
        personal_information = (Button)findViewById(R.id.information);
        personal_information.setOnClickListener(new Buttonlistener_information());
        verification = (Button)findViewById(R.id.verification);
        verification.setOnClickListener(new Buttonlistener_verification());
        touxiang = (ImageView)findViewById(R.id.touxiang);
        Intent intent1 = getIntent();
        json_string = intent1.getStringExtra("msg");
        userName = intent1.getStringExtra("name");
        log_status = intent1.getStringExtra("status");
        send_photo send = new send_photo();
        send.execute();
    }
    class send_photo extends AsyncTask<Integer, Integer, Bitmap>
    {
        @Override
        protected void onPreExecute() {
            //第一个执行方法
            super.onPreExecute();
        }
        @Override
        protected Bitmap doInBackground(Integer... params) {
            try {
                list = JSONutils.getAgent(json_string);
                picture = list.get(0).get("a_picture");
                map1 =new HashMap<>();
                map1.put("picture",picture);
                get = HttpUtil.postPicture(map1);
                photo = JSONutils.Bytes2Bimap(get);
            } catch (Exception e) {
                System.out.println(e+"1");
                Resources res = getResources();
                photo = BitmapFactory.decodeResource(res, R.drawable.personal_image);
            }
            return photo;
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            touxiang.setImageBitmap(result);
        }
    }

    class Buttonlistener_verification implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent3 = new Intent();
            intent3.putExtra("name",userName);
            intent3.setClass(Personal_center.this,identity_verification.class);
            startActivity(intent3);
        }
    }
    class Buttonlistener_store implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("name",userName);
            intent.putExtra("msg",json_string);
            intent.setClass(Personal_center.this,My_store.class);
            startActivity(intent);
        }
    }
    class Buttonlistener_back2 implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent1 = new Intent();
            intent1.putExtra("name",userName);
            intent1.putExtra("status",log_status);
            intent1.putExtra("msg",json_string);
            intent1.setClass(Personal_center.this,Home_page.class);
            startActivity(intent1);
            finish();

        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            Intent intent1 = new Intent();
            intent1.putExtra("name",userName);
            intent1.putExtra("status",log_status);
            intent1.putExtra("msg",json_string);
            intent1.setClass(Personal_center.this,Home_page.class);
            startActivity(intent1);
            finish();
        }
        return  false;
    }
    class Buttonlistener_zhuxiao implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent2 = new Intent();
            intent2.setClass(Personal_center.this,Home_page.class);
            startActivity(intent2);
            finish();
        }
    }
    class Buttonlistener_information implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent1 = new Intent();
            intent1.putExtra("name",userName);
            intent1.putExtra("status",log_status);
            intent1.putExtra("msg",json_string);
            intent1.setClass(Personal_center.this,Personal_Information.class);
            startActivity(intent1);
        }
    }
}
