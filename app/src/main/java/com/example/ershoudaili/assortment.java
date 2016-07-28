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

import org.apache.commons.io.IOUtils;
import org.json.JSONException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class assortment extends ActionBarActivity implements AbsListView.OnScrollListener{
    private TextView Top;
    private ImageButton back;
    private static final String TAG = "Product_list";

    private ListView listView;
    private View moreView; //加载更多页面

    private SimpleAdapter adapter;
    private ArrayList<HashMap<String, String>> listData;

    private int lastItem;
    private int count;
    private int acount;
    private String number;
    //private String url = HttpUtil.BASE_URL; //POST方式
    private String get;
    public Bitmap bitmap1;
    private Bitmap[] array_map;
    private String url = "http://192.168.191.1:8080/AgentApp/getGoodListServlet";
    private ArrayList<HashMap<String, String>> list;
    private TextView item_name;
    private String userName;
    private String[] array_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assortment);
        getSupportActionBar().hide();
        Top = (TextView)findViewById(R.id.top2);
        Intent intent1 = getIntent();
        Bundle bundle = intent1.getExtras();
        Top.setText(bundle.get("ItemText").toString());
        number = bundle.get("number").toString();
        userName = intent1.getStringExtra("name");
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


        //列表实现部分
        TextPaint tp2 = Top.getPaint();
        tp2.setFakeBoldText(true);
        listView = (ListView)findViewById(R.id.listView);
        moreView = getLayoutInflater().inflate(R.layout.load, null);
        listData = new ArrayList<>();
        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.ul);
        array_map = new Bitmap[1000];
        for(int i=0;i<1000;i++)
        {
            array_map[i] =bmp;
        }
        prepareData(); //准备数据
        count = listData.size();
//        send_photo a = new send_photo();

        adapter = new myadapter1(this,listData,R.layout.item_assortment);
       // m1Handler.sendEmptyMessage(0);
        listView.addFooterView(moreView); //添加底部view，一定要在setAdapter之前添加，否则会报错。

        listView.setAdapter(adapter); //设置adapter
        send_photo a = new send_photo();
        a.execute();
        listView.setOnScrollListener(this); //设置listview的滚动事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent =new Intent();
                item_name = (TextView)view.findViewById(R.id.itemText);
                System.out.println(item_name.getText());
                intent.putExtra("g_name",item_name.getText());
                intent.putExtra("u_name",userName);
                intent.putExtra("o_date",list.get(position).get("g_picture"));
                intent.setClass(assortment.this,Product_show.class);
                startActivity(intent);
            }
        });
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
                map.put("g_sort",number);
                get = HttpUtil.postListMap(url,map);
            } catch (Exception e) {
                System.out.println(e+" 02");
            }
            listData.clear();

            try {
                list = JSONutils.getGoodList(get);
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
                    String pic = list.get(i).get("g_picture");
                    map2.put("picture",pic);
                    try {
                        System.out.println(map2);
                        byte[] address = HttpUtil.postPicture(map2);
                        if(address ==null)System.out.println("nulladdre");
                        //bitmap1 = new Bitmap();
                        //bitmap1 = Bytes2Bimap(address);
                        array_map[i]=Bytes2Bimap(address);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } catch (JSONException e1) {
                System.out.println(e1+" 8");
                e1.printStackTrace();
            }
            return get;
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
    private void prepareData(){  //准备数据
        for(int i=0;i<10;i++){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("g_name", "");
            map.put("g_price","");
            map.put("s_name","");
            map.put("g_sales","");
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
//            map.put("itemText", "测试数据"+i);
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
        System.out.println("count=="+count);
        if(lastItem == count  && scrollState == this.SCROLL_STATE_IDLE){
            Log.i(TAG, "拉到最底部");
            moreView.setVisibility(view.VISIBLE);

            mHandler.sendEmptyMessage(0);

        }

    }
    private class ViewHolder { TextView g_name;
        TextView g_price; TextView s_name;
        ImageView picture;TextView g_sales;

    }
private  class  myadapter1 extends SimpleAdapter {
    private Context context;
    private ArrayList<HashMap<String, String>> data;
    private int layoutResource;
    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"

     */
    public myadapter1(Context context, List<? extends Map<String, ?>> data,  int resource) {
        super(context, data, resource, null, null);
        this.context = context;
        this.data = listData;
        this.layoutResource = resource;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if(convertView==null)
            convertView = getLayoutInflater().inflate(R.layout.item_assortment, null);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.g_name = (TextView) convertView.findViewById(R.id.itemText);
        viewHolder.g_price = (TextView) convertView.findViewById(R.id.price2);
        viewHolder.s_name = (TextView) convertView.findViewById(R.id.buyed);
        viewHolder.picture=(ImageView)convertView.findViewById(R.id.picture1);
        viewHolder.g_sales= (TextView)convertView.findViewById(R.id.sales);
        viewHolder.g_name.setText(listData.get(position).get("g_name"));
        viewHolder.g_price.setText("价格："+listData.get(position).get("g_price"));
        viewHolder.s_name.setText(listData.get(position).get("s_name"));
        viewHolder.g_sales.setText("销量："+listData.get(position).get("g_sales"));
        viewHolder.picture.setImageBitmap(array_map[position]);
        return convertView;
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

                    if(count >= list.size()){
                        Toast.makeText(assortment.this, "木有更多数据！", Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.menu_assortment, menu);
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
