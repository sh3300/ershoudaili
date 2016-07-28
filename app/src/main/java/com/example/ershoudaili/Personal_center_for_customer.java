package com.example.ershoudaili;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class Personal_center_for_customer extends ActionBarActivity {
    private TextView top;
    private Button personal_information;
    private ImageButton back;
    private Button record;
    private String userName;
    private String json_string;
    private ArrayList<HashMap<String,String>> list;
    private String picture;
    private HashMap<String,String> map1;
    private byte[] get;
    private ImageView touxiang;
    private Button zhuxiao1;
    private String log_status;
    private Bitmap photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center_for_customer);
        getSupportActionBar().hide();
        top = (TextView)findViewById(R.id.top3);
        TextPaint tp1 = top.getPaint();
        tp1.setFakeBoldText(true);

        Intent intent1 = getIntent();
        userName = intent1.getStringExtra("name");
        json_string = intent1.getStringExtra("msg");
        log_status = intent1.getStringExtra("status");
        back = (ImageButton)findViewById(R.id.Bacbutton6);
        back.setOnClickListener(new Buttonlistener_back2());
        zhuxiao1 = (Button)findViewById(R.id.zhuxiao1);
        zhuxiao1.setOnClickListener(new Buttonlistener_zhuxiao1());
        personal_information = (Button)findViewById(R.id.information);
        personal_information.setOnClickListener(new Buttonlistener_information());
        touxiang = (ImageView)findViewById(R.id.user_touxiang);
        record = (Button)findViewById(R.id.record);
        record.setOnClickListener(new Buttonlistener_record());
        send_photo load = new send_photo();
        load.execute();
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
                list = JSONutils.getUser(json_string);
                picture = list.get(0).get("u_picture");
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
    class Buttonlistener_record implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent2 = new Intent();
            intent2.putExtra("who","user");
            intent2.putExtra("name",userName);
            intent2.setClass(Personal_center_for_customer.this,Transaction_records.class);
            startActivity(intent2);
        }
    }
    class Buttonlistener_zhuxiao1 implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent1 = new Intent();
            intent1.setClass(Personal_center_for_customer.this,Home_page.class);
            finish();
            startActivity(intent1);
        }
    }
    class Buttonlistener_back2 implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent1 = new Intent();
            intent1.putExtra("name",userName);
            intent1.putExtra("status",log_status);
            intent1.putExtra("msg",json_string);
            intent1.setClass(Personal_center_for_customer.this,Home_page.class);
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
            intent1.setClass(Personal_center_for_customer.this,Home_page.class);
            startActivity(intent1);
            finish();
        }
        return  false;
    }
    class Buttonlistener_information implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent1 = new Intent();
            intent1.putExtra("status",log_status);
            intent1.putExtra("name",userName);
            intent1.putExtra("msg",json_string);
            intent1.setClass(Personal_center_for_customer.this,Personal_Information.class);
            startActivity(intent1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_personal_center_for_customer, menu);
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
