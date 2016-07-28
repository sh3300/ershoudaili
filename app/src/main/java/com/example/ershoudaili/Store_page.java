package com.example.ershoudaili;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class Store_page extends FragmentActivity implements View.OnClickListener {
    private TextView top;
    private TextView tv1 = null;
    private TextView tv2 = null;
    private Button store_product;
    private Button store_order;
    private FragmentManager fm = null;
    private FragmentTransaction ft = null;
    private ImageButton back;
    private ImageButton pic;
    private TextView name;
    private TextView activity;
    private TextView agent;
    private HashMap<String,String> map3;
    private HashMap<String,String> map2;
    private HashMap<String,String> map1;
    private String json_string;
    private ArrayList<HashMap<String,String>> list;
    private byte[] get;
    private String picture;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_page);
        //getSupportActionBar().hide();
        top = (TextView)findViewById(R.id.top4);
        TextPaint tp1 = top.getPaint();
        tp1.setFakeBoldText(true);


        LinearLayout content = (LinearLayout) findViewById(R.id.content_show);
        store_product = (Button)findViewById(R.id.button_product);
        store_product.setOnClickListener(new Buttonlistener_product());
        store_order = (Button)findViewById(R.id.button_order);
        store_order.setOnClickListener(new Buttonlistener_order());
        back = (ImageButton)findViewById(R.id.Bacbutton4);
        back.setOnClickListener(new Buttonlistener1_ba());
        agent = (TextView)findViewById(R.id.name_show);
        name = (TextView)findViewById(R.id.store_name);
        activity = (TextView)findViewById(R.id.activity_store);
        pic = (ImageButton)findViewById(R.id.shangchuan_show);
        Intent intent1= getIntent();
        userName = intent1.getStringExtra("name");
        load_msg load =new load_msg();
        load.execute();

    }
    class load_msg extends AsyncTask<Integer, Integer, Bitmap> {
        @Override
        protected void onPreExecute() {
            //第一个执行方法
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            try {
                map2 = new HashMap<>();
                map2.put("a_name",userName);
                json_string = HttpUtil.postListMap(HttpUtil.BASE_URL1+"getStoreSerlvet",map2);
                list = JSONutils.getStore(json_string);
                picture = list.get(0).get("s_mark");
                map1 = new HashMap<>();
                map1.put("picture", picture);
                get = HttpUtil.postPicture(map1);
                map3 = list.get(0);
            } catch (Exception e) {
                System.out.println(e + "1");
            }
            Bitmap photo = JSONutils.Bytes2Bimap(get);
            return photo;
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            agent.setText(map3.get("a_name"));
            name.setText(map3.get("s_name"));
           activity.setText(map3.get("s_active"));
            pic.setImageBitmap(result);
        }
    }
    class Buttonlistener_product implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent2 = new Intent();
            intent2.putExtra("name",userName);
            intent2.setClass(Store_page.this,Store_product.class);
            startActivity(intent2);
        }
    }
    class Buttonlistener_order implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent1 = new Intent();
            intent1.putExtra("name",userName);
            intent1.setClass(Store_page.this,Store_order.class);
            startActivity(intent1);
        }
    }
    class Buttonlistener1_ba implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            finish();
        }
    }
    @Override
    public void onClick(View v)
    {
        ft = fm.beginTransaction();
        switch (v.getId())
        {
            case R.id.tb1_show:

                ft.replace(R.id.content_show, new Fragment1_store());

                break;
            case R.id.tb2_show:
                ft.replace(R.id.content_show, new Fragment2_store());
                break;
            default:
                break;
        }
        ft.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_store_page, menu);
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
