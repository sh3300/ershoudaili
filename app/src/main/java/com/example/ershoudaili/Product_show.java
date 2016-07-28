package com.example.ershoudaili;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Product_show extends FragmentActivity implements View.OnClickListener{

    private TextView Top;
    private TextView tv1 = null;
    private TextView tv2 = null;
    private TextView tv3 = null;
    private Button purchase;
    private FragmentManager fm = null;
    private FragmentTransaction ft = null;
    private ImageButton back;
    private String g_name;
    private String get;
    private String get1;
    private ArrayList<HashMap<String,String>> list;
    private HashMap<String,String> good;
    private Bitmap bitmap;
    private TextView name;
    private TextView price;
    private String u_name;
    private String a_name;
    private String o_date;
    private TextView activity;
    private ImageButton picture;
    private HashMap<String,String> map1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_show);
        //getSupportActionBar().hide();
        Intent intent1 = getIntent();
        g_name = intent1.getStringExtra("g_name");
        u_name = intent1.getStringExtra("u_name");
        a_name = intent1.getStringExtra("a_name");
        o_date = intent1.getStringExtra("o_date");
        System.out.println("    1      "+g_name+u_name+a_name+o_date);
        Top = (TextView)findViewById(R.id.top2_show);
        TextPaint tp1 = Top.getPaint();
        tp1.setFakeBoldText(true);

        purchase = (Button) findViewById(R.id.purchase);
        purchase.setOnClickListener(new Buttonlistener_purchase());


        LinearLayout content = (LinearLayout) findViewById(R.id.content_show);
        tv1 = (Button) findViewById(R.id.tb1_show);
        tv2 = (Button) findViewById(R.id.tb2_show);
        tv3 = (Button) findViewById(R.id.tb3_show);

        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);

        fm = getFragmentManager();
        ft = fm.beginTransaction();

        ft.replace(R.id.content_show, new Fragment1_show());
        ft.commit();
        back = (ImageButton)findViewById(R.id.Bacbutton_show);
        back.setOnClickListener(new Buttonlistener1_ba());
        //异步加载物品信息
        name = (TextView)findViewById(R.id.name_show);
        price = (TextView)findViewById(R.id.price_show);
        activity = (TextView)findViewById(R.id.activity_show);
        picture = (ImageButton)findViewById(R.id.shangchuan_show);
        load_product load = new load_product();
        load.execute();
    }
    public String getname()
    {return g_name;}
    class load_product extends AsyncTask<Integer, Integer, HashMap<String,String>>
    {
        @Override
        protected void onPreExecute() {
            //第一个执行方法
            super.onPreExecute();
        }
        @Override
        protected HashMap<String,String> doInBackground(Integer... params) {
            try {
                Map<String, String> map = new HashMap<>();
                map.put("g_name",g_name);
                get = HttpUtil.postListMap(HttpUtil.BASE_URL1 + "getGoodServlet", map);
                //list = JSONutils.getGoodList(get);
            } catch (Exception e) {
                System.out.println(e+" 03");
            }
            try {
                list = JSONutils.getGood(get);
               // System.out.println("Good= "+list);
                good = list.get(0);
                    HashMap<String,String> map2 = new HashMap<>();
                    String pic = good.get("g_picture");
                    a_name = good.get("a_name");
                  //  System.out.println(pic);
                    map2.put("picture",pic);
                    try {
                       // System.out.println(map2);
                        byte[] address = HttpUtil.postPicture(map2);
                      //  System.out.println(address.toString());
                        if(address ==null)System.out.println("nulladdre");
                        bitmap = Bytes2Bimap(address);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            } catch (JSONException e1) {
                System.out.println(e1+" 8");
                e1.printStackTrace();
            }
            return good;
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }
        @Override
        protected void onPostExecute(HashMap<String,String> good) {
            name.setText(good.get("g_name"));
            price.setText(good.get("g_price"));
            picture.setImageBitmap(bitmap);
        }
    }
    public Bitmap Bytes2Bimap(byte[] b) {
        if(b==null)
            System.out.println("bNull");
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
    class Buttonlistener_purchase implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(Product_show.this).setTitle("确认")//设置对话框标题

                    .setMessage("确定购买商品吗？")//设置显示的内容

                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮


                        @Override

                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                            // TODO Auto-generated method stub
                            if(u_name==null)
                            {
                                Toast.makeText(Product_show.this,"请先登录后再购买！",Toast.LENGTH_SHORT).show();
                            }
                            else {
                            Toast.makeText(Product_show.this, "你已下单，请耐心等候商家接单", Toast.LENGTH_SHORT).show();
                            buy buy1 = new buy();
                            buy1.execute();
                            finish();}

                        }

                    }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮



                @Override

                public void onClick(DialogInterface dialog, int which) {//响应事件

                    // TODO Auto-generated method stub

                    Log.i("alertdialog", " 请保存数据！");

                }

            }).show();//在按键响应事件中显示此对话框
        }
    }
    class buy extends AsyncTask<Integer, Integer, String>
    {
        @Override
        protected void onPreExecute() {
            //第一个执行方法
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Integer... params) {
            try {
                   map1 = new HashMap<>();
                map1.put("g_name",g_name);
                map1.put("u_name",u_name);
                map1.put("a_name",a_name);
                map1.put("o_date",o_date);
                get1 = HttpUtil.postListMap(HttpUtil.BASE_URL1 + "insertOrderServlet", map1);
            } catch (Exception e) {
                System.out.println(e+" 03");
            }

            return get1;
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }
        @Override
        protected void onPostExecute(String result) {
            if(result.equals("TTT"))
            {

            }
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

                ft.replace(R.id.content_show, new Fragment1_show());

                break;
            case R.id.tb2_show:
                ft.replace(R.id.content_show, new Fragment2_show());
                break;
            case R.id.tb3_show:
                ft.replace(R.id.content_show, new Fragment3_show());
                break;
            default:
                break;
        }
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_show, menu);
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
