package com.example.ershoudaili;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Junyuan Gu on 2015/11/1.
 */
public class ConnectService extends IntentService {
    private static final String ACTION_RECV_MSG = "com.example.logindemo.action.RECEIVE_MESSAGE";
    private String check;
    private String url;
    private String strFlag;
    public ConnectService() {
        super("TestIntentService");
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub
        /**
         * 经测试，IntentService里面是可以进行耗时的操作的
         * IntentService使用队列的方式将请求的Intent加入队列，
         * 然后开启一个worker thread(线程)来处理队列中的Intent
         * 对于异步的startService请求，IntentService会处理完成一个之后再处理第二个
         */
        Boolean flag = false;
        //通过intent获取主线程传来的用户名和密码字符串
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        check  = intent.getStringExtra("radiobutton");
        strFlag = doLogin(username, password);
        Log.d("登录结果", flag.toString());

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(ACTION_RECV_MSG);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra("result", strFlag);
        sendBroadcast(broadcastIntent);

    }

    // 定义发送请求的方法
    private String doLogin(String username, String password)
    {
        //String strFlag = "";
        //byte[] strFlag = new byte[0];
        strFlag = "";
        // 使用Map封装请求参数
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", username);
        map.put("password", password);
        // 定义发送请求的URL
       // String url = HttpUtil.BASE_URL + "queryOrder?un=" + username + "&pw=" + password;  //GET方式
        if(check.equals("1")){
        url = HttpUtil.BASE_URL1+"getAgentServlet";}//POST方式
        else if(check.equals("2")){
          url = HttpUtil.BASE_URL1+"getUserServlet";
        }

        Log.d("url", url);
        Log.d("username", username);
        Log.d("password", password);
        try {
            // 发送请求
            strFlag = HttpUtil.postListMap(url, map);  //POST方式

//		  strFlag = HttpUtil.getRequest(url);  //GET方式
           // Log.d("服务器返回值", strFlag);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println(strFlag.trim());
        if(strFlag==null){
            System.out.println("1");
        return "error";}
//        if(strFlag.trim().equals("true")){
//            return true;
//        }
        else{
            return strFlag;
        }

    }

}
