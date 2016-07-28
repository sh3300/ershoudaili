package com.example.ershoudaili;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Junyuan Gu on 2015/11/1.
 */
public class Fragment2_show extends Fragment implements XListView.IXListViewListener
{
    private XListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> items = new ArrayList<String>();
    private ArrayList<HashMap<String, String>> listData;
    private Handler mHandler;
    private SimpleAdapter adapter;
    private int start = 0;
    private static int refreshCnt = 0;
    private int count;
    private Bitmap[] array_map;
    private int acount;
    private String json_string;
    private ArrayList<HashMap<String,String>> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment2_show, container, false);
        geneItems();
        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.product_pic);
        array_map = new Bitmap[1000];
        for(int i=0;i<1000;i++)
        {
            array_map[i] =bmp;
        }
        mListView = (XListView) view.findViewById(R.id.listview1);
        mListView.setPullLoadEnable(true);
        listData = new ArrayList<>();
        prepareData();
        adapter = new MySimpleAdapter(getActivity(), listData,R.layout.list_item_test);
        //mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, items);
        mListView.setAdapter(adapter);
//		mListView.setPullLoadEnable(false);
//		mListView.setPullRefreshEnable(false);
        mListView.setXListViewListener(this);
        mHandler = new Handler();
        load_review load = new load_review();
        load.execute();
        return view;
    }
    class load_review extends AsyncTask<Integer, Integer, String>
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
                map.put("g_name",((Product_show)getActivity()).getname());
                json_string = HttpUtil.postListMap(HttpUtil.BASE_URL1 + "getReview", map);
            } catch (Exception e) {
                System.out.println(e+" 02");
            }
            listData.clear();

            try {
                list = JSONutils.getReview(json_string);
                System.out.println(list);
                if(list.size()>10) {
                    for (int i = 0; i < 10; i++) {
                        listData.add(i, list.get(i));
                    }
                }
                else
                    listData.addAll(list);
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
        TextView o_review;
        TextView g_date;
        TextView u_name;

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
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_test, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.o_review = (TextView) convertView.findViewById(R.id.comment1);
            viewHolder.g_date = (TextView) convertView.findViewById(R.id.time1);
            viewHolder.u_name = (TextView) convertView.findViewById(R.id.user1);
            viewHolder.o_review.setText(listData.get(position).get("o_review"));
            viewHolder.g_date.setText(listData.get(position).get("g_date"));
            viewHolder.u_name.setText(listData.get(position).get("u_name"));
            return convertView;
        }
    }
    private void geneItems() {
        for (int i = 0; i != 10; ++i) {
            items.add("refresh cnt " + (++start));
        }
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime("刚刚");
    }

    private void prepareData(){  //准备数据
        for(int i=0;i<10;i++){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("o_review", "评价"+i);
            map.put("g_date","11.11");
            map.put("u_name","sh3300");
            listData.add(map);
        }

    }
    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                start = ++refreshCnt;
                items.clear();
                geneItems();
                listData.clear();
                prepareData();
                adapter = new MySimpleAdapter(getActivity(), listData,R.layout.list_item_test);
                // mAdapter.notifyDataSetChanged();
                //mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, items);
                //mListView.setAdapter(mAdapter);
                onLoad();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //geneItems();
                //mAdapter.notifyDataSetChanged();
                count = adapter.getCount();
                if(list.size()>count+5)
                    acount = count+5;
                else
                    acount = list.size();
                for(int i=count;i<acount;i++){
                    listData.add(i,list.get(i));
//                    HashMap<String, String> map = new HashMap<>();
//                    map.put("comment1", "评价"+i);
//                    map.put("time1","11.11");
//                    map.put("user1","sh3300");
//                    listData.add(map);
                }
                count = listData.size();
                if(count>=list.size())
                    mListView.setPullLoadEnable(false);
                adapter.notifyDataSetChanged();
                onLoad();
            }
        }, 2000);
    }
}
