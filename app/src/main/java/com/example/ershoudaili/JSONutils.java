package com.example.ershoudaili;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class JSONutils {
    //url:http://192.168.191.1:8080/AgentServlet
    public static ArrayList<HashMap<String, String>> getGoodList(String listString) throws JSONException {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        String s = null,g_name = null,g_price = null,g_sales = null,s_name = null,g_picture = null;
        JSONObject jsonObject1 = new JSONObject(listString);
        JSONArray jsonarray = jsonObject1.getJSONArray("list");

        for(int i = 0; i < jsonarray.length() ; i++){
            HashMap<String, String> map = new HashMap<String, String>();
            JSONObject jsonObject = jsonarray.getJSONObject(i);
            g_name = jsonObject.getString("g_name");
            g_price = jsonObject.getString("g_price");
            g_sales = jsonObject.getString("g_sales");
            s_name = jsonObject.getString("s_name");
            g_picture = jsonObject.getString("g_picture");

            map.put("g_name", g_name);
            map.put("g_price", g_price);
            map.put("g_sales", g_sales);
            map.put("s_name", s_name);
            map.put("g_picture", g_picture);
            list.add(map);
        }
        return list;
    }
    public static ArrayList<HashMap<String, String>> getAgentGoodList(String listString) throws JSONException {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        String s = null,g_name = null,g_price = null,g_sales = null,g_picture = null;
        JSONObject jsonObject1 = new JSONObject(listString);
        JSONArray jsonarray = jsonObject1.getJSONArray("list");

        for(int i = 0; i < jsonarray.length() ; i++){
            HashMap<String, String> map = new HashMap<>();
            JSONObject jsonObject = jsonarray.getJSONObject(i);
            g_name = jsonObject.getString("g_name");
            g_price = jsonObject.getString("g_price");
            g_sales = jsonObject.getString("g_sales");
            g_picture = jsonObject.getString("g_picture");

            map.put("g_name", g_name);
            map.put("g_price", g_price);
            map.put("g_sales", g_sales);
            map.put("g_picture", g_picture);
            list.add(map);
        }
        return list;
    }
    public static  ArrayList<HashMap<String, String>> getOrder(String listString) throws JSONException {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        String s = null,g_name = null,u_name = null,a_name = null,o_status = null,o_date = null,o_review = null;
        JSONObject jsonObject1 = new JSONObject(listString);
        JSONArray jsonarray = jsonObject1.getJSONArray("list");

        for(int i = 0; i < jsonarray.length() ; i++){
            HashMap<String, String> map = new HashMap<String, String>();
            JSONObject jsonObject = jsonarray.getJSONObject(i);
            g_name = jsonObject.getString("g_name");
            u_name = jsonObject.getString("u_name");
            a_name = jsonObject.getString("a_name");
            o_status = jsonObject.getString("o_status");
            if(o_status.equals("1"))
                o_status = "未发货";
            else if(o_status.equals("2"))
            o_status = "已发货";
            else if(o_status.equals("3"))
            o_status = "交易完成";
            else
            o_status ="系统维护中";
            o_date = jsonObject.getString("o_date");
            o_review = jsonObject.getString("o_review");


            map.put("g_name", g_name);
            map.put("u_name", u_name);
            map.put("a_name", a_name);
            map.put("o_status", o_status);
            map.put("o_date", o_date);
            map.put("o_review", o_review);
            list.add(map);
        }
        return list;
    }
    public static ArrayList<HashMap<String, String>> getUser(String listString) throws JSONException {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        JSONObject jsonObject1 = new JSONObject(listString);
        JSONArray jsonarray = jsonObject1.getJSONArray("list");
        JSONObject jsonObject = jsonarray.getJSONObject(0);
        String s = null,u_name = null,u_password = null,u_tel = null,u_address = null,u_picture = null;

        u_name = jsonObject.getString("u_name");
        u_password = jsonObject.getString("u_password");
        u_tel = jsonObject.getString("u_tel");
        u_address = jsonObject.getString("u_address");
        u_picture = jsonObject.getString("u_picture");

        map.put("u_name", u_name);
        map.put("u_password", u_password);
        map.put("u_tel", u_tel);
        map.put("u_address", u_address);
        map.put("u_picture", u_picture);
        list.add(map);
        return list;
    }

    public static ArrayList<HashMap<String, String>> getAgent(String listString) throws JSONException {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        JSONObject jsonObject1 = new JSONObject(listString);
        JSONArray jsonarray = jsonObject1.getJSONArray("list");
        JSONObject jsonObject = jsonarray.getJSONObject(0);
        String s = null,a_name = null,a_password = null,a_picture = null,a_ID = null,a_scl_num = null,a_tel = null,a_address = null;

        a_name = jsonObject.getString("a_name");
        a_password = jsonObject.getString("a_password");
        a_picture = jsonObject.getString("a_picture");
        a_ID = jsonObject.getString("a_ID");
        a_scl_num = jsonObject.getString("a_scl_num");
        a_tel = jsonObject.getString("a_tel");
        a_address = jsonObject.getString("a_address");

        map.put("a_name", a_name);
        map.put("a_password", a_password);
        map.put("a_picture", a_picture);
        map.put("a_ID", a_ID);
        map.put("a_scl_num", a_scl_num);
        map.put("a_tel", a_tel);
        map.put("a_address", a_address);
        list.add(map);
        return list;
    }

    public  static ArrayList<HashMap<String, String>> getGood(String listString) throws JSONException {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        JSONObject jsonObject1 = new JSONObject(listString);
        JSONArray jsonarray = jsonObject1.getJSONArray("list");
        JSONObject jsonObject = jsonarray.getJSONObject(0);
        String s = null,g_name = null,g_price = null,g_sales = null,s_name = null,g_picture = null;
        String g_colour,g_weight,g_material,g_size,a_name;

        g_name = jsonObject.getString("g_name");
        g_price = jsonObject.getString("g_price");
        g_sales = jsonObject.getString("g_sales");
        s_name = jsonObject.getString("s_name");
        g_picture = jsonObject.getString("g_picture");
        g_colour = jsonObject.getString("g_colour");
        g_weight = jsonObject.getString("g_weight");
        g_material = jsonObject.getString("g_material");
        g_size = jsonObject.getString("g_size");
        a_name = jsonObject.getString("a_name");

        map.put("g_name", g_name);
        map.put("g_price", g_price);
        map.put("g_sales", g_sales);
        map.put("s_name", s_name);
        map.put("g_picture", g_picture);
        map.put("g_colour", g_colour);
        map.put("g_weight", g_weight);
        map.put("g_material", g_material);
        map.put("g_size", g_size);
        map.put("a_name", a_name);
        list.add(map);
        return list;
    }

    public static ArrayList<HashMap<String, String>> getStore(String listString) throws JSONException {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        JSONObject jsonObject1 = new JSONObject(listString);
        JSONArray jsonarray = jsonObject1.getJSONArray("list");
        JSONObject jsonObject = jsonarray.getJSONObject(0);
        String s = null,s_name = null,s_mark = null,s_active = null,a_name = null;

        s_name = jsonObject.getString("s_name");
        s_mark = jsonObject.getString("s_mark");
        s_active = jsonObject.getString("s_active");
        a_name = jsonObject.getString("a_name");

        map.put("s_name", s_name);
        map.put("s_mark", s_mark);
        map.put("s_active", s_active);
        map.put("a_name", a_name);
        list.add(map);
        return list;
    }
    public static ArrayList<HashMap<String, String>> getReview(String listString) throws JSONException {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        //HashMap<String, String> map = new HashMap<String, String>();
        JSONObject jsonObject1 = new JSONObject(listString);
        JSONArray jsonarray = jsonObject1.getJSONArray("list");
        //JSONObject jsonObject = jsonarray.getJSONObject(0);
        String s = null,u_name = null,o_review = null,g_date = null;

        for(int i = 0; i < jsonarray.length() ; i++){
            HashMap<String, String> map = new HashMap<String, String>();
            JSONObject jsonObject = jsonarray.getJSONObject(i);
            u_name = jsonObject.getString("u_name");
            o_review = jsonObject.getString("o_review");
            g_date = jsonObject.getString("g_date");


            map.put("u_name", u_name);
            map.put("o_review", o_review);
            map.put("g_date", g_date);
            list.add(map);
        }
        return list;
    }
    public static Bitmap Bytes2Bimap(byte[] b) {
        if(b==null)
            System.out.println("bNull");
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
}
/**
 * Created by Junyuan Gu on 2015/11/16.
 */
//public class JSONutils {
//    //url:http://192.168.191.1:8080/AgentServlet
//    public static  ArrayList<HashMap<String, String>> getGoodList(String listString) throws JSONException {
//        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
//        HashMap<String, String> map = new HashMap<String, String>();
//        String s = null,g_name = null,g_price = null,g_sales = null,s_name = null,g_picture = null;
//        JSONArray jsonarray = null;
//        JSONObject jsonObject1 = new JSONObject(listString);
//        jsonarray = jsonObject1.getJSONArray("list");
//
//        for(int i = 0; i < jsonarray.length() ; i++){
//            JSONObject jsonObject = jsonarray.getJSONObject(i);
//            g_name = jsonObject.getString("g_name");
//            System.out.println("@@@@g_name="+g_name);
//            g_price = jsonObject.getString("g_price");
//            //g_sales = jsonObject.getString("g_sales");
//            s_name = jsonObject.getString("s_name");
//            g_picture = jsonObject.getString("g_picture");
//
//            map.put("g_name", g_name);
//            System.out.println("~~~~~~~~~~g_name = "+map.get("g_name"));
//            map.put("g_price", g_price);
//            //map.put("g_sales", g_sales);
//            map.put("s_name", s_name);
//            map.put("g_picture", g_picture);
//            list.add(map);
//           // System.out.println(list.get(1).get("g_name"));
//        }
//        return list;
//    }
//
//    public static ArrayList<HashMap<String, String>> getUser(String listString) throws JSONException {
//        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
//        HashMap<String, String> map = new HashMap<String, String>();
//        JSONArray jsonarray = null;
//        jsonarray = new JSONArray(listString);
//        JSONObject jsonObject = jsonarray.getJSONObject(0);
//        String s = null,u_name = null,u_password = null,u_tel = null,u_address = null,u_picture = null;
//
//        u_name = jsonObject.getString("u_name");
//        u_password = jsonObject.getString("u_password");
//        u_tel = jsonObject.getString("u_tel");
//        u_address = jsonObject.getString("u_address");
//        u_picture = jsonObject.getString("u_picture");
//
//        map.put("u_name", u_name);
//        map.put("u_password", u_password);
//        map.put("u_tel", u_tel);
//        map.put("u_address", u_address);
//        map.put("u_picture1", u_picture);
//        list.add(map);
//        return list;
//    }
//
//    public static ArrayList<Map<String, String>> getAgent(String listString) throws JSONException {
//        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
//        Map<String, String> map = new HashMap<String, String>();
//        JSONArray jsonarray = null;
//        jsonarray = new JSONArray(listString);
//        JSONObject jsonObject = jsonarray.getJSONObject(0);
//        String s = null,a_name = null,a_password = null,a_picture = null,a_ID = null,a_scl_num = null,a_tel = null,a_address = null;
//
//        a_name = jsonObject.getString("a_name");
//        a_password = jsonObject.getString("a_password");
//        a_picture = jsonObject.getString("a_picture");
//        a_ID = jsonObject.getString("a_ID");
//        a_scl_num = jsonObject.getString("a_scl_num");
//        a_tel = jsonObject.getString("a_tel");
//        a_address = jsonObject.getString("a_address");
//
//        map.put("a_name", a_name);
//        map.put("a_password", a_password);
//        map.put("a_picture", a_picture);
//        map.put("a_ID", a_ID);
//        map.put("a_scl_num", a_scl_num);
//        map.put("a_tel", a_tel);
//        map.put("a_address", a_address);
//        list.add(map);
//        return list;
//    }
//
//    public static ArrayList<Map<String, String>> getGood(String listString) throws JSONException {
//        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
//        Map<String, String> map = new HashMap<String, String>();
//        JSONArray jsonarray = null;
//        jsonarray = new JSONArray(listString);
//        JSONObject jsonObject = jsonarray.getJSONObject(0);
//        String s = null,g_name = null,g_price = null,g_sales = null,s_name = null,g_picture = null;
//        String g_colour,g_weight,g_material,g_size;
//
//        g_name = jsonObject.getString("g_name");
//        g_price = jsonObject.getString("g_price");
//        g_sales = jsonObject.getString("g_sales");
//        s_name = jsonObject.getString("s_name");
//        g_picture = jsonObject.getString("g_picture");
//        g_colour = jsonObject.getString("g_colour");
//        g_weight = jsonObject.getString("g_weight");
//        g_material = jsonObject.getString("g_material");
//        g_size = jsonObject.getString("g_size");
//
//        map.put("g_name", g_name);
//        map.put("g_price", g_price);
//        map.put("g_sales", g_sales);
//        map.put("s_name", s_name);
//        map.put("g_picture", g_picture);
//        map.put("g_colour", g_colour);
//        map.put("g_weight", g_weight);
//        map.put("g_material", g_material);
//        map.put("g_size", g_size);
//        list.add(map);
//        return list;
//    }
//
//    public static ArrayList<Map<String, String>> getStore(String listString) throws JSONException {
//        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
//        Map<String, String> map = new HashMap<String, String>();
//        JSONArray jsonarray = null;
//        jsonarray = new JSONArray(listString);
//        JSONObject jsonObject = jsonarray.getJSONObject(0);
//        String s = null,s_name = null,s_mark = null,s_active = null,a_name = null;
//
//        s_name = jsonObject.getString("s_name");
//        s_mark = jsonObject.getString("s_mark");
//        s_active = jsonObject.getString("s_active");
//        a_name = jsonObject.getString("a_name");
//
//        map.put("s_name", s_name);
//        map.put("s_mark", s_mark);
//        map.put("s_active", s_active);
//        map.put("a_name", a_name);
//        list.add(map);
//        return list;
//    }
//}
