package com.example.ershoudaili;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Junyuan Gu on 2015/11/24.
 */
public class FragmentPage2_logined extends Fragment {
    private Button zhuxiao;
    private ImageView touxiang;
    private String log_status;
    private String userName;
    private String json_string;
    private ArrayList<HashMap<String,String>>list;
    private HashMap<String,String>map1;
    private String picture;
    private byte[] get;
    private Bitmap photo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View converview = inflater.inflate(R.layout.fragment_2_loginde, null);
        zhuxiao = (Button)converview.findViewById(R.id.but_f2);
        touxiang = (ImageView)converview.findViewById(R.id.pic_f2);
        zhuxiao.setOnClickListener(new Buttonlistener_esc());
        log_status = ((Home_page)getActivity()).getLog_status1();
        userName = ((Home_page)getActivity()).getUserName1();
        json_string = ((Home_page)getActivity()).getJson_string1();
        send_photo load = new send_photo();
        load.execute();
        return converview;
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
                System.out.println("status= "+log_status);
                if(log_status.equals("agent")){
                    list = JSONutils.getAgent(json_string);
                picture = list.get(0).get("a_picture");}
                else if(log_status.equals("user")){
                    list = JSONutils.getUser(json_string);
                    picture=list.get(0).get("u_picture");}
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
    class Buttonlistener_esc implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent1 = new Intent();
            intent1.putExtra("msg",json_string);
            intent1.putExtra("name",userName);
            intent1.putExtra("status",log_status);
            if(log_status.equals("agent"))
            intent1.setClass(getActivity(),Personal_center.class);
            else if(log_status.equals("user"))
                   intent1.setClass(getActivity(),Personal_center_for_customer.class);
            startActivity(intent1);
            getActivity().finish();
        }
    }
}