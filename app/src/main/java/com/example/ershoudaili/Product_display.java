package com.example.ershoudaili;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by junyuan on 2015/8/4.
 */
public class Product_display extends ActionBarActivity implements View.OnClickListener {
    private TextView Top;
    private HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
    private TextView tv1 = null;
    private TextView tv2 = null;
    private TextView tv3 = null;
    private TextView Product_name = null;
    private TextView price = null;
    private TextView activity = null;
    private Button biaoqian1 = null;
    private Button biaoqian2 = null;
    private Button biaoqian3 = null;
    private ImageButton product_picture;
    private FragmentManager fm = null;
    private FragmentTransaction ft = null;
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
        getSupportActionBar().hide();
        Top = (TextView)findViewById(R.id.top2);
        TextPaint tp1 = Top.getPaint();
        tp1.setFakeBoldText(true);
        System.out.println("1");



        //获得前个页面传递的数据
        Intent intent1 = getIntent();
        Bundle bundle = intent1.getExtras();
        if(bundle.get("1-1") != null) {
            hashMap.put(0, bundle.get("1-1").toString());
            System.out.print(hashMap.get(0));
        }
        if(bundle.get("1-2") != null) {
            hashMap.put(1, bundle.get("1-2").toString());
        }
        if(bundle.get("1-3") != null) {
            hashMap.put(2, bundle.get("1-3").toString());
        }
        if(bundle.get("1-4")!= null) {
            hashMap.put(9, bundle.get("1-4").toString());
        }
        if(bundle.get("1-5") != null) {
            hashMap.put(3, bundle.get("1-5").toString());
        }
        if(bundle.get("2-1") != null) {
            hashMap.put(4, bundle.get("2-1").toString());
        }
        if(bundle.get("2-2")!= null) {
            hashMap.put(5, bundle.get("2-2").toString());
        }
        if(bundle.get("2-3") != null) {
            hashMap.put(6, bundle.get("2-3").toString());
        }
        if(bundle.get("2-4") != null) {
            hashMap.put(7, bundle.get("2-4").toString());
        }
        if(bundle.get("2-5") != null) {
            hashMap.put(8, bundle.get("2-5").toString());
        }



//        if(bundle.get("picture") != null){
//            Bitmap photo = intent1.getParcelableExtra("picture");
//            product_picture = (ImageButton)findViewById(R.id.shangchuan);
//            product_picture.setImageBitmap(photo);
//        }
        if(intent1 !=null)
        {
            product_picture = (ImageButton)findViewById(R.id.shangchuan);
            byte [] bis=intent1.getByteArrayExtra("bitmap");
            Bitmap bitmap= BitmapFactory.decodeByteArray(bis, 0, bis.length);
            product_picture.setImageBitmap(bitmap);
        }
        //导入数据
        System.out.println("2");
        Product_name =(TextView)findViewById(R.id.nam);
        price = (TextView)findViewById(R.id.price);
        activity = (TextView)findViewById(R.id.activity);
        biaoqian1 = (Button)findViewById(R.id.biaoqian1);
        biaoqian2 = (Button)findViewById(R.id.biaoqian2);
        biaoqian3 = (Button)findViewById(R.id.biaoqian3);
        if(hashMap.get(0) != null) {
            Product_name.setText(hashMap.get(0));
        }
        if(hashMap.get(1) != null){
            price.setText(hashMap.get(1));
        }
        if(hashMap.get(2) != null){

        }
        LinearLayout content = (LinearLayout) findViewById(R.id.content1);
        tv1 = (Button) findViewById(R.id.tb1);
        tv2 = (Button) findViewById(R.id.tb2);
        tv3 = (Button) findViewById(R.id.tb3);

        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);

        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.content1, new com.example.ershoudaili.Fragment1());
        ft.commit();
        back = (ImageButton)findViewById(R.id.Bacbutton1);
        back.setOnClickListener(new Buttonlistener_ba());
    }
    public HashMap<Integer, String> getmap(){
        return hashMap;
    }
    class Buttonlistener_ba implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            finish();
            System.out.println("1");
        }
    }
    @Override
    public void onClick(View v)
    {
        ft = fm.beginTransaction();
        switch (v.getId())
        {
            case R.id.tb1:

                ft.replace(R.id.content1, new Fragment1());

                break;
            case R.id.tb2:
                ft.replace(R.id.content1, new Fragment2());
                break;
            case R.id.tb3:
                ft.replace(R.id.content1, new Fragment3());
                break;
            default:
                break;
        }
        ft.commit();
    }
}
