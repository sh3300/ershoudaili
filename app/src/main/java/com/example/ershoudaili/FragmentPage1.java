package com.example.ershoudaili;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.R.layout.simple_dropdown_item_1line;
import static com.example.ershoudaili.R.layout;

public class FragmentPage1 extends Fragment{
    private TextView Top;
    private ImageButton search_button;
    private String number;
    private String userName;
    AutoCompleteTextView search = null;
    private static final String[] autoStrs=new String[]{"a","abc","abcd","abcde","ba"};
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        View rootview = inflater.inflate(layout.fragment_1, null);
        Top = (TextView)rootview.findViewById(R.id.top3);
        TextPaint tp1 = Top.getPaint();
        tp1.setFakeBoldText(true);
        userName = ((Home_page)getActivity()).getUserName1();
        if(userName!=null)
        Top.setText(userName);
        search_button = (ImageButton)rootview.findViewById(R.id.search1);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(search.getText().toString());
                if(search.getText().toString().equals(""))
                    Toast.makeText(getActivity(),"请输入关键词",Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent();
                    intent.putExtra("search", search.getText().toString());
                    intent.putExtra("name", userName);
                    intent.setClass(getActivity(), Product_list.class);
                    startActivity(intent);
                }
            }
        });

        //搜索栏自动提示功能

        search = (AutoCompleteTextView)rootview.findViewById(R.id.autosearch);
        //new　ArrayAdapter对象并将autoStr字符串数组传入actv中　　
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(getActivity(),R.layout.simple_dropdown_item_1line,autoStrs);
        search.setAdapter(adapter);



        GridView gridview = (GridView) rootview.findViewById(R.id.gridview);
        String[] string1 = new String[]{"生活用品","男装","女装","鞋包","美妆","食品","电子设备","内衣","其他"};
        //生成动态数组，并且转入数据
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<9;i++)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemText",string1[i]);//按序号做ItemText
            lstImageItem.add(map);
        }
        //生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
        SimpleAdapter saImageItems = new SimpleAdapter(getActivity(), //没什么解释
                lstImageItem,//数据来源
                layout.grid_item,//night_item的XML实现

                //动态数组与ImageItem对应的子项
                new String[] {"ItemText"},

                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[] {R.id.ItemText});
        //添加并且显示
        gridview.setAdapter(saImageItems);
        //添加消息处理
        gridview.setOnItemClickListener(new ItemClickListener());
		return rootview;
	}
    //当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
    class  ItemClickListener implements AdapterView.OnItemClickListener
    {
        public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened
                                View arg1,//The view within the AdapterView that was clicked
                                int arg2,//The position of the view in the adapter
                                long arg3//The row id of the item that was clicked
        ) {
            //在本例中arg2=arg3
            HashMap<String, Object> item=(HashMap<String, Object>) arg0.getItemAtPosition(arg2);
            //显示所选Item的ItemText
            System.out.println(item.get("ItemText").toString());
            number = String.valueOf(arg3+1);
            System.out.println(number);
            Intent intent = new Intent();
            intent.putExtra("name",userName);
            intent.putExtra("ItemText",item.get("ItemText").toString());
            intent.putExtra("number",number);
            intent.setClass(getActivity(), assortment.class);
            startActivity(intent);
        }

    }

}
