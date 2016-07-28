package com.example.ershoudaili;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Junyuan Gu on 2015/11/1.
 */
public class HttpUtil {
    // 创建HttpClient对象
    public static HttpClient httpClient = new DefaultHttpClient();
    public static final String BASE_URL = "http://192.168.191.1:8080/AgentServlet/";
    public static final String BASE_URL1  ="http://192.168.191.1:8080/AgentApp/";

//    /**
//     *
//     * @param url
//     *            发送请求的URL
//     * @return 服务器响应字符串
//     * @throws Exception
//     */
//    public static byte[] getRequest(String url) throws Exception {
//        // 创建HttpGet对象。
//        HttpGet get = new HttpGet(url);
//        // 发送GET请求
//        HttpResponse httpResponse = httpClient.execute(get);
//        // 如果服务器成功地返回响应
//        if (httpResponse.getStatusLine().getStatusCode() == 200) {
//            // 获取服务器响应字符串
//           // String result = EntityUtils.toString(httpResponse.getEntity());
//            byte[] result = EntityUtils.toByteArray(httpResponse.getEntity());
//            return result;
//        } else {
//            Log.d("服务器响应代码", (new Integer(httpResponse.getStatusLine()
//                    .getStatusCode())).toString());
//            return null;
//        }
//    }
public static String getRequest(String url) throws Exception {
    // 创建HttpGet对象。
    HttpGet get = new HttpGet(url);
    // 发送GET请求
    HttpResponse httpResponse = httpClient.execute(get);
    // 如果服务器成功地返回响应
    if (httpResponse.getStatusLine().getStatusCode() == 200) {
        // 获取服务器响应字符串
        String result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
        return result;
    } else {
        Log.d("服务器响应代码", (new Integer(httpResponse.getStatusLine()
                .getStatusCode())).toString());
        return null;
    }
}
    /**
     *
     * @param url
     *            发送请求的URL
     *
     *
     * @return 服务器响应字符串
     * @throws Exception
     */
    public static byte[] postRequest(String url, Map<String, String> rawParams)
            throws Exception {
        // 创建HttpPost对象。
        HttpPost post = new HttpPost(url);
        // 如果传递参数个数比较多的话可以对传递的参数进行封装
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (String key : rawParams.keySet()) {
            // 封装请求参数
            params.add(new BasicNameValuePair(key, rawParams.get(key)));
        }
        // 设置请求参数
        post.setEntity(new UrlEncodedFormEntity(params, "GBK"));
        // 发送POST请求
        HttpResponse httpResponse = httpClient.execute(post);
        //System.out.println(httpResponse.getStatusLine().getStatusCode());
        // 如果服务器成功地返回响应
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            // 获取服务器响应字符串
            //String result = EntityUtils.toString(httpResponse.getEntity());
            byte[] result = EntityUtils.toByteArray(httpResponse.getEntity());
            return result;
        }
        return null;
    }

    /**
     *
     * @param url
     *            发送请求的URL
     *            请求参数
     * @return 服务器响应字符串
     * @throws Exception
     */
    public static String postListMap(String url, Map<String, String> rawParams)
            throws Exception {
        // 创建HttpPost对象。
        HttpPost post = new HttpPost(url);
        // 如果传递参数个数比较多的话可以对传递的参数进行封装
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (String key : rawParams.keySet()) {
            // 封装请求参数
            params.add(new BasicNameValuePair(key, rawParams.get(key)));
        }
        // 设置请求参数
        post.setEntity(new UrlEncodedFormEntity(params, "GBK"));
        // 发送POST请求

        HttpResponse httpResponse = httpClient.execute(post);
        // 如果服务器成功地返回响应
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            // 获取服务器响应字符串
            String content = EntityUtils.toString(httpResponse.getEntity(),"GBK");
            System.out.println(content);
            String result = URLDecoder.decode(content, "utf-8");
            System.out.println(result);
            return result;
        }
        System.out.println("status= "+httpResponse.getStatusLine().getStatusCode());
        return null;
    }

    public static byte[] postPicture( Map<String, String> rawParams)
            throws Exception {
        // 创建HttpPost对象。
        String url = "http://192.168.191.1:8080/AgentApp/getPictureServlet";
        HttpPost post = new HttpPost(url);
        // 如果传递参数个数比较多的话可以对传递的参数进行封装
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (String key : rawParams.keySet()) {
            // 封装请求参数
            params.add(new BasicNameValuePair(key, rawParams.get(key)));
        }
        // 设置请求参数
        post.setEntity(new UrlEncodedFormEntity(params, "GBK"));
        // 发送POST请求
        HttpResponse httpResponse = httpClient.execute(post);
        // 如果服务器成功地返回响应
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            // 获取服务器响应字符串
            byte[] result = EntityUtils.toByteArray(httpResponse.getEntity());
            return result;
        }
        return null;
    }
}
