package com.example.ershoudaili;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Junyuan Gu on 2015/11/1.
 */
public class Fragment1_show extends Fragment
{
    private  String g_name;
    private String get;
    private TextView color;
    private TextView weight;
    private TextView material;
    private TextView size;
    private TextView area;
    private ArrayList<HashMap<String,String>> list;
    private HashMap<String,String> good;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment1_show, container, false);
        color = (TextView)view.findViewById(R.id.color);
        weight = (TextView)view.findViewById(R.id.weight);
        material = (TextView)view.findViewById(R.id.weight);
        size = (TextView)view.findViewById(R.id.size);
        area = (TextView)view.findViewById(R.id.producing_area);
        load_detail load = new load_detail();
        load.execute();
        return view;
    }
    class load_detail extends AsyncTask<Integer, Integer, HashMap<String,String>>
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
                g_name = ((Product_show)getActivity()).getname();
                map.put("g_name",g_name);
                get = HttpUtil.postListMap(HttpUtil.BASE_URL1 + "getGoodServlet", map);
                //list = JSONutils.getGoodList(get);
            } catch (Exception e) {
                System.out.println(e+" 03");
            }
            try {
                list = JSONutils.getGood(get);
                System.out.println("Good= "+list);
                good = list.get(0);


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
            color.setText(good.get("g_colour"));
            size.setText(good.get("g_size"));
            material.setText(good.get("g_material"));
            weight.setText(good.get("g_weight"));
            area.setText(good.get("g_colour"));
        }
    }
}
