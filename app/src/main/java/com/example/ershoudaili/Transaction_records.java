package com.example.ershoudaili;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextPaint;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Transaction_records extends ActionBarActivity implements AbsListView.OnScrollListener{
    private TextView Top;
    private ImageButton back;
    private RelativeLayout layout_add;
    private static final String TAG = "Product_list";
    private Button confirm;
    private TextView trade_position;

    private ListView listView;
    private View moreView; //加载更多页面

    private SimpleAdapter adapter;
    private ArrayList<HashMap<String, String>> listData;

    private int lastItem;
    private int count;
    private String url = HttpUtil.BASE_URL; //POST方式
    private byte[] get;
    private String who;
    private RelativeLayout layout1;
    private ArrayList<HashMap<String,String>> list;
    private String userName;
    private  String json_string;
    private Bitmap[] array_map;
    private String pic;
    private int acount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_records);
        getSupportActionBar().hide();
        Top = (TextView)findViewById(R.id.top2);
        TextPaint tp1 = Top.getPaint();
        tp1.setFakeBoldText(true);
        Intent intent1 = getIntent();
        who = intent1.getStringExtra("who");
        userName = intent1.getStringExtra("name");
        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.product_pic);
        array_map = new Bitmap[1000];
        for(int i=0;i<1000;i++)
        {
            array_map[i] =bmp;
        }

        //返回上一页
        back = (ImageButton)findViewById(R.id.Bacbutton1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //列表实现部分
        listView = (ListView)findViewById(R.id.listView);
        moreView = getLayoutInflater().inflate(R.layout.load, null);
        listData = new ArrayList<>();

        prepareData(); //准备数据
        count = listData.size();

        adapter = new MySimpleAdapter(this,listData,R.layout.item_records);

        listView.addFooterView(moreView); //添加底部view，一定要在setAdapter之前添加，否则会报错。

        listView.setAdapter(adapter); //设置adapter
        listView.setOnScrollListener(this); //设置listview的滚动事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Intent intent =new Intent();
                intent.setClass(Transaction_records.this,Product_show.class);
                startActivity(intent);
            }
        });
        load_record load = new load_record();
        load.execute();
    }
    class load_record extends AsyncTask<Integer, Integer, String>
    {
        @Override
        protected void onPreExecute() {
            //第一个执行方法
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Integer... params) {
            try {
                Map<String, String> map = new HashMap<>();
                map.put("u_name",userName);
                json_string = HttpUtil.postListMap(HttpUtil.BASE_URL1 + "getUserOrder", map);
            } catch (Exception e) {
                System.out.println(e+" 02");
            }
            listData.clear();

            try {
                list = JSONutils.getOrder(json_string);
                System.out.println(list);
                if(list.size()>10) {
                    for (int i = 0; i < 10; i++) {
                        listData.add(i, list.get(i));
                    }
                }
                else
                    listData.addAll(list);
                for(int i=0;i<list.size();i++){
                    HashMap<String,String> map2 = new HashMap<>();
                    pic = list.get(i).get("o_date");
                    map2.put("picture",pic);
                    try {
                        System.out.println(map2);
                        byte[] address = HttpUtil.postPicture(map2);
                        if(address ==null)System.out.println("nulladdre");
                        array_map[i]=JSONutils.Bytes2Bimap(address);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } catch (JSONException e1) {
                System.out.println(e1+" 8");
                e1.printStackTrace();
            }
            return json_string;
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }
        @Override
        protected void onPostExecute(String get) {
            adapter.notifyDataSetChanged();
        }
    }
    class ViewHolder {
        TextView store_name;
        TextView position1;
        TextView price;
        TextView product_name;
        TextView count; ImageView product_pic;

    }
    /**
     * * @author CZ
     * 自定义的类去继承SimpleAdapter */
    private class MySimpleAdapter extends SimpleAdapter {
        private Context context;
        private ArrayList<HashMap<String, String>> data;
        private int layoutResource;
        /**
         * @param context * @param data
         * @param resource * @param from
         * @param data 构造函数 */
        public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource) {
            super(context, data, resource,null,null);
            this.context = context;
            this.data = listData;
            this.layoutResource = resource;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if(convertView==null)
            convertView = getLayoutInflater().inflate(R.layout.item_records, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.store_name = (TextView) convertView.findViewById(R.id.store_name);
            viewHolder.position1 = (TextView) convertView.findViewById(R.id.position);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            viewHolder.count = (TextView) convertView.findViewById(R.id.count);
            viewHolder.product_name = (TextView)convertView.findViewById(R.id.product_name);
            viewHolder.product_pic = (ImageView)convertView.findViewById(R.id.pro_pic);
            viewHolder.store_name.setText(listData.get(position).get("a_name"));
            viewHolder.position1.setText(listData.get(position).get("o_status"));
            viewHolder.product_name.setText(listData.get(position).get("g_name"));
            viewHolder.price.setText(listData.get(position).get("price"));
            viewHolder.count.setText(listData.get(position).get("count"));
            viewHolder.product_pic.setImageBitmap(array_map[position]);
            layout1 = (RelativeLayout)convertView.findViewById(R.id.button_layout);
            confirm = (Button)convertView.findViewById(R.id.comment);
            if(who.equals("agent")){
                layout1.removeView(confirm);
            }
            else{
            confirm.setTag(position);
            confirm.setOnClickListener(new Button_a());}
            return convertView;
        }
        class Button_a implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                confirm = (Button)v.findViewById(R.id.comment);

                AlertDialog.Builder builder = new AlertDialog.Builder(Transaction_records.this);
                builder.setTitle("评论");
                builder.setIcon(android.R.drawable.btn_star);
                final EditText editText = new EditText(Transaction_records.this);
                builder.setView(editText);
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                Toast.makeText(Transaction_records.this,
                                        "评论已提交",
                                        Toast.LENGTH_SHORT).show();
                                System.out.println(confirm.getTag().toString() + "  "+editText.getText());
                            }
                        });
                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                Toast.makeText(Transaction_records.this, "取消",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                builder.show();



//                listData.clear();
//                for(int i=0;i<10;i++){
//                    HashMap<String, Object> map = new HashMap<>();
//                    map.put("store_name", "a商店");
//                    map.put("position","交易");
//                    map.put("price","￥50");
//                    map.put("count","n" );
//                    listData.add(map);
//                }
//                count = listData.size();
//                adapter.notifyDataSetChanged();
//                System.out.println("1");

            }
        }

    }

    private void prepareData(){  //准备数据
        for(int i=0;i<10;i++){
            HashMap<String, String> map = new HashMap<>();
            map.put("g_name","");
            map.put("a_name", "什么什么商店");
            map.put("o_status","已交易");
            map.put("price","￥150");
            map.put("count","n" );
            listData.add(map);
        }

    }

    private void loadMoreData(){ //加载更多数据
        count = adapter.getCount();
        if(list.size()>count+5)
            acount = count+5;
        else
            acount = list.size();
        for(int i=count;i<acount;i++){
            listData.add(i,list.get(i));
//            HashMap<String, String> map = new HashMap<>();
//            map.put("store_name", "什么什么商店"+i);
//            map.put("position","已交易"+i);
//            map.put("price","￥150"+i);
//            map.put("count","n"+i);
//            listData.add(map);
        }
        count = listData.size();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        Log.i(TAG, "firstVisibleItem=" + firstVisibleItem + "\nvisibleItemCount=" +
                visibleItemCount + "\ntotalItemCount" + totalItemCount);

        lastItem = firstVisibleItem + visibleItemCount - 1;  //减1是因为上面加了个addFooterView

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.i(TAG, "scrollState="+scrollState);
        //下拉到空闲是，且最后一个item的数等于数据的总数时，进行更新
        if(lastItem == count  && scrollState == this.SCROLL_STATE_IDLE){
            Log.i(TAG, "拉到最底部");
            moreView.setVisibility(view.VISIBLE);

            mHandler.sendEmptyMessage(0);

        }

    }
    //声明Handler
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    loadMoreData();  //加载更多数据，这里可以使用异步加载
                    adapter.notifyDataSetChanged();
                    moreView.setVisibility(View.GONE);

                    if(count >=list.size()){
                        Toast.makeText(Transaction_records.this, "木有更多数据！", Toast.LENGTH_SHORT).show();
                        listView.removeFooterView(moreView); //移除底部视图
                    }
                    Log.i(TAG, "加载更多数据");
                    break;
                case 1:

                    break;
                default:
                    break;
            }
        };
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transaction_records, menu);
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
