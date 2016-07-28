package com.example.ershoudaili;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Store_product extends ActionBarActivity implements AbsListView.OnScrollListener {
    private ImageButton back;
    private TextView Top;
    private static final String TAG = "Product_list";
    private ListView listView;
    private View moreView; //加载更多页面
    private View view;
    private Button confirm;
    private SimpleAdapter adapter;
    private ArrayList<HashMap<String, String>> listData;
    private ArrayList<HashMap<String,String>> list;
    private int lastItem;
    private int count;
    private Bitmap[] array_map;
    private String userName;
    private String json_string;
    private String json_string1;
    private String pic;
    private int acount;
    private int pos;
    private String delivery_product_name;
    private String delivery_store_name;
    private String delivery_buyer_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_product);
        getSupportActionBar().hide();
        Top = (TextView)findViewById(R.id.top2);
        TextPaint tp1 = Top.getPaint();
        tp1.setFakeBoldText(true);


        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.product_pic);
        array_map = new Bitmap[1000];
        for(int i=0;i<1000;i++)
        {
            array_map[i] =bmp;
        }
        Intent intent1 = getIntent();
        userName = intent1.getStringExtra("name");

        delivery_store_name = userName;
        //返回上一页
        back = (ImageButton)findViewById(R.id.Bacbutton1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = (ListView)findViewById(R.id.listView);
        moreView = getLayoutInflater().inflate(R.layout.load, null);
        listData = new ArrayList<>();

        prepareData(); //准备数据
        count = listData.size();

        adapter = new MySimpleAdapter(this,listData,R.layout.item_page1);
        listView.addFooterView(moreView); //添加底部view，一定要在setAdapter之前添加，否则会报错。

//        listView.setAdapter(adapter); //设置adapter
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this); //设置listview的滚动事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
//                Intent intent =new Intent();
//                intent.setClass(Store_product.this,Product_show.class);
//                startActivity(intent);
            }
        });
        send_photo load = new send_photo();
        load.execute();
    }

    class send_photo extends AsyncTask<Integer, Integer, String>
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
                map.put("a_name",userName);
                json_string = HttpUtil.postListMap(HttpUtil.BASE_URL1 + "getAgentOrders", map);
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
        TextView detail;
        TextView buyer; TextView price;
        TextView condition;ImageView picture;}
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
            ViewHolder viewHolder;
            if(convertView==null) {
                convertView = getLayoutInflater().inflate(R.layout.item_page1, null);
                viewHolder = new ViewHolder();
            }else
                viewHolder = (ViewHolder)convertView.getTag();
            viewHolder.detail = (TextView) convertView.findViewById(R.id.detail);
            viewHolder.buyer = (TextView) convertView.findViewById(R.id.buyer);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            viewHolder.condition = (TextView) convertView.findViewById(R.id.conditon);
            viewHolder.picture = (ImageView)convertView.findViewById(R.id.order_pic);
            viewHolder.detail.setText(listData.get(position).get("g_name"));
            //delivery_product_name = listData.get(position).get("g_name");
            viewHolder.buyer.setText("买家："+listData.get(position).get("u_name"));
            //delivery_buyer_name = listData.get(position).get("u_name");
            viewHolder.condition.setText(listData.get(position).get("o_status"));
            viewHolder.picture.setImageBitmap(array_map[position]);
            confirm = (Button)convertView.findViewById(R.id.product_confirm);
            confirm.setTag(position);
            confirm.setOnClickListener(new Buttonlisteren_confirm());
            convertView.setTag(viewHolder);
            return convertView;
        }
    }
    class Buttonlisteren_confirm implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Toast.makeText(Store_product.this, "发货已确认！", Toast.LENGTH_SHORT).show();
            System.out.println(Integer.parseInt(v.getTag().toString()));
            if(listData.get(Integer.parseInt(v.getTag().toString())).get("o_status").equals("未发货")) {
                listData.get(Integer.parseInt(v.getTag().toString())).put("o_status", "已发货");
                delivery_product_name = listData.get(Integer.parseInt(v.getTag().toString())).get("g_name");
                delivery_buyer_name = listData.get(Integer.parseInt(v.getTag().toString())).get("u_name");
                Toast.makeText(Store_product.this, "发货已确认！", Toast.LENGTH_SHORT).show();
                send_status load1 = new send_status();
                load1.execute();
            }
            else
            Toast.makeText(Store_product.this,"商品已发货!",Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();

            //listData.get(condition.getTag())



        }
    }
    class send_status extends AsyncTask<Integer, Integer, String>
    {
        @Override
        protected void onPreExecute() {
            //第一个执行方法
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Integer... params) {
            try {
                Map<String, String> map2 = new HashMap<>();
                map2.put("g_name",delivery_product_name);
                map2.put("u_name",delivery_buyer_name);
                map2.put("a_name",delivery_store_name);
                json_string1 = HttpUtil.postListMap(HttpUtil.BASE_URL1 + "updateO_status", map2);

            } catch (Exception e) {
                System.out.println(e+" 02");
            }
            return json_string1;
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }
        @Override
        protected void onPostExecute(String get) {

        }
    }
    private void prepareData(){  //准备数据
        for(int i=0;i<10;i++){
            HashMap<String, String> map = new HashMap<>();
            map.put("g_name", "秋冬时尚韩版毛呢修身中长假两件套 纯色韩修身弹力打底衣");
            map.put("u_name","买家：XXX");
            map.put("o_status","已购买" );
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
//            HashMap<String, String> map = new HashMap<>();
            listData.add(i,list.get(i));
//            map.put("detail", "秋冬时尚韩版毛呢修身中长假两件套 纯色韩修身弹力打底衣");
//            map.put("buyer","买家：XXX");
//            map.put("price","￥10");
//            map.put("conditon","已购买");
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
                        Toast.makeText(Store_product.this, "木有更多数据！", Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.menu_store_product, menu);
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
