package com.example.ershoudaili;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Product_list extends ActionBarActivity implements AbsListView.OnScrollListener {
    private TextView Top;
    private TextView store_name;
    private ImageButton back;
    private static final String TAG = "Product_list";

    private ListView listView;
    private View moreView; //加载更多页面

    private SimpleAdapter adapter;
    private ArrayList<HashMap<String, String>> listData;

    private int lastItem;
    private int count;
    private int acount;
    private String json_string;
    private String search;
    private ArrayList<HashMap<String,String>> list;
    private String pic;
    private Bitmap[] array_map;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        getSupportActionBar().hide();
        Top = (TextView)findViewById(R.id.top2);
        TextPaint tp1 = Top.getPaint();
        tp1.setFakeBoldText(true);

        //返回上一页
        back = (ImageButton)findViewById(R.id.Bacbutton1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        search = intent.getStringExtra("search");
        userName = intent.getStringExtra("name");
        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.product_pic);
        array_map = new Bitmap[1000];
        for(int i=0;i<1000;i++)
        {
            array_map[i] =bmp;
        }
        //列表实现部分
        store_name = (TextView)findViewById(R.id.store_name);
        TextPaint tp2 = Top.getPaint();
        tp2.setFakeBoldText(true);
        listView = (ListView)findViewById(R.id.listView);
        moreView = getLayoutInflater().inflate(R.layout.load, null);
        listData = new ArrayList<>();

        prepareData(); //准备数据
        count = listData.size();

        adapter = new MySimpleAdapter(this,listData,R.layout.item);

        listView.addFooterView(moreView); //添加底部view，一定要在setAdapter之前添加，否则会报错。

        listView.setAdapter(adapter); //设置adapter
        listView.setOnScrollListener(this); //设置listview的滚动事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent();
                TextView g_name = (TextView)view.findViewById(R.id.pro_name);
                TextView a_name = (TextView)view.findViewById(R.id.store_name);

                intent.putExtra("g_name",g_name.getText());
                intent.putExtra("o_date",list.get(position).get("g_picture"));
                intent.putExtra("a_name",list.get(position).get("a_name"));
                intent.putExtra("u_name",userName);

                intent.setClass(Product_list.this,Product_show.class);
                startActivity(intent);
            }
        });
        load_search load = new load_search();
        load.execute();
    }
    class load_search extends AsyncTask<Integer, Integer, String>
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
                map.put("name",search);
                json_string = HttpUtil.postListMap(HttpUtil.BASE_URL1 + "SelectG_name", map);
            } catch (Exception e) {
                System.out.println(e+" 02");
            }
            listData.clear();

            try {
                list = JSONutils.getGoodList(json_string);
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
                    pic = list.get(i).get("g_picture");
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
            if(json_string.equals("FFF"))
                Toast.makeText(Product_list.this,"暂无"+search+"相关搜索结果",Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        }
    }
    class ViewHolder {
        TextView s_name;
        TextView price;
        TextView p_name;
        ImageView product_pic;
        TextView buyed;

    }
    private class MySimpleAdapter extends SimpleAdapter {
        private Context context;
        private ArrayList<HashMap<String, String>> data;
        private int layoutResource;

        /**
         * @param context  * @param data
         * @param resource * @param from
         * @param data     构造函数
         */
        public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource) {
            super(context, data, resource, null, null);
            this.context = context;
            this.data = listData;
            this.layoutResource = resource;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.item, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.s_name = (TextView) convertView.findViewById(R.id.store_name);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price1);
            viewHolder.p_name = (TextView) convertView.findViewById(R.id.pro_name);
            viewHolder.product_pic = (ImageView) convertView.findViewById(R.id.picture1);
            viewHolder.buyed = (TextView)convertView.findViewById(R.id.buyed);
            viewHolder.s_name.setText("店铺: "+listData.get(position).get("s_name"));
            viewHolder.p_name.setText(listData.get(position).get("g_name"));
            if(viewHolder.p_name==null)
                System.out.println("pyes");
            if(listData.get(position).get("g_name")==null)
                System.out.println("lyes");
            viewHolder.price.setText("￥"+listData.get(position).get("g_price"));
            viewHolder.buyed.setText(listData.get(position).get("g_sales")+"已购");
            viewHolder.product_pic.setImageBitmap(array_map[position]);
            return convertView;
        }
    }
    private void prepareData(){  //准备数据
        for(int i=0;i<10;i++){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("g_name", "洗发水"+i);
            map.put("s_name", "什么商店");
            map.put("g_price", "￥120");
            map.put("g_sales", "120人已购");
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
//            HashMap<String, String> map = new HashMap<String, String>();
//            map.put("g_name", "洗发水"+i);
//            map.put("s_name", "什么商店");
//            map.put("g_price", "￥120");
//            map.put("g_sales", "120人已购");
//            listData.add(map);
        }
        count = listData.size();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        Log.i(TAG, "firstVisibleItem="+firstVisibleItem+"\nvisibleItemCount="+
                visibleItemCount+"\ntotalItemCount"+totalItemCount);

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
                        Toast.makeText(Product_list.this, "木有更多数据！", Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.menu_product_list, menu);
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
