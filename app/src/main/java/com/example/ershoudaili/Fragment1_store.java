package com.example.ershoudaili;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Junyuan Gu on 2015/11/7.
 */
public class Fragment1_store extends Fragment implements AbsListView.OnScrollListener{
    private static final String TAG = "Product_list";
    private ListView listView;
    private View moreView; //加载更多页面
    private View view;
    private Button confirm;
    private SimpleAdapter adapter;
    private ArrayList<HashMap<String, Object>> listData;

    private int lastItem;
    private int count;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //列表实现部分
        view = inflater.inflate(R.layout.fragment1_store,container,false);
        listView = (ListView)view.findViewById(R.id.myList);
        moreView = inflater.inflate(R.layout.load,container,false);

        Log.i("TAG",listView == null ?"YES":"NO");
        listData = new ArrayList<>();

        prepareData(); //准备数据
        count = listData.size();

        adapter = new MySimpleAdapter(getActivity(),listData,R.layout.item_page1);
        //listView.addFooterView(moreView); //添加底部view，一定要在setAdapter之前添加，否则会报错。

//        listView.setAdapter(adapter); //设置adapter
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this); //设置listview的滚动事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Intent intent =new Intent();
                intent.setClass(getActivity(),Product_show.class);
                startActivity(intent);
            }
        });
        return view;
    }
    class ViewHolder { TextView detail;
        TextView buyer; TextView price;
        TextView conditon;}
    private class MySimpleAdapter extends SimpleAdapter {
        private Context context;
        private ArrayList<HashMap<String, Object>> data;
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
                convertView = getActivity().getLayoutInflater().inflate(R.layout.item_page1, null);
                viewHolder = new ViewHolder();
            }else
                viewHolder = (ViewHolder)convertView.getTag();
            viewHolder.detail = (TextView) convertView.findViewById(R.id.detail);
            viewHolder.buyer = (TextView) convertView.findViewById(R.id.buyer);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            viewHolder.conditon = (TextView) convertView.findViewById(R.id.conditon);
            viewHolder.detail.setText(listData.get(position).get("detail").toString());
            viewHolder.buyer.setText(listData.get(position).get("buyer").toString());
            viewHolder.price.setText(listData.get(position).get("price").toString());
            viewHolder.conditon.setText(listData.get(position).get("conditon").toString());
            confirm = (Button)convertView.findViewById(R.id.product_confirm);
            confirm.setTag(position);
            confirm.setOnClickListener(new Button_a());
            convertView.setTag(viewHolder);
            return convertView;
        }
        class Button_a implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "确认收货，交易完成，现在你可以评论咯！", Toast.LENGTH_SHORT).show();
                confirm = (Button)v.findViewById(R.id.product_confirm);
                //confirm.setText("评论");
                //ArrayList<HashMap<String, Object>> listData1 = new ArrayList<>();
                listData.clear();
                for(int i=0;i<10;i++){
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("detail", "秋冬时尚韩版毛呢修身中长假两件套");
                    map.put("buyer","买家：金正恩");
                    map.put("price","￥50");
                    map.put("conditon","未购买" );
                    listData.add(map);
                }
                count = listData.size();
                adapter.notifyDataSetChanged();
            }
        }

    }

    private void prepareData(){  //准备数据
        for(int i=0;i<10;i++){
            HashMap<String, Object> map = new HashMap<>();
            map.put("detail", "秋冬时尚韩版毛呢修身中长假两件套 纯色韩修身弹力打底衣");
            map.put("buyer","买家：XXX");
            map.put("price","￥10");
            map.put("conditon","已购买" );
            listData.add(map);
        }

    }

    private void loadMoreData(){ //加载更多数据
        count = adapter.getCount();
        for(int i=count;i<count+5;i++){
            HashMap<String, Object> map = new HashMap<>();
            map.put("detail", "秋冬时尚韩版毛呢修身中长假两件套 纯色韩修身弹力打底衣");
            map.put("buyer","买家：XXX");
            map.put("price","￥10");
            map.put("conditon","已购买");
            listData.add(map);
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

                    if(count > 30){
                        Toast.makeText(getActivity(), "木有更多数据！", Toast.LENGTH_SHORT).show();
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
}
