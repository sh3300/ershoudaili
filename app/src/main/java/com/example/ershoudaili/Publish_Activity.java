package com.example.ershoudaili;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Publish_Activity extends ActionBarActivity {
    TextView Top;
    Button liulan;
    EditText t1;
    EditText t2;
    EditText t3;
    EditText t4;
    EditText t5;
    EditText t6;
    EditText t7;
    EditText t8;
    EditText t9;
    Button public1;
    Button confirm;
    HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
    private ExpandableListView expendView;
    private int []group_click=new int[5];
    private final int VIEW_TYPE = 3;
    private final int TYPE_1 = 0;
    private final int TYPE_2 = 1;
    private final int TYPE_3 = 2;
    private ImageButton back;
    private int change = 0;//确认修改信息标志
    private int change1 = 1;
    private File f;
    private HashMap<String,String> map;
    private String s_name;
    private String userName;
    private String result;
    private HashMap<String,String> map_send;
    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";

    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;

    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 2000;
    private static int output_Y = 2000;

    private ImageView headImage = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_);
        getSupportActionBar().hide();     //隐藏actionbar
        //设置顶栏颜色、字体
        Top = (TextView)findViewById(R.id.top1);
        TextPaint tp1 = Top.getPaint();
        tp1.setFakeBoldText(true);
        Intent intent1 = getIntent();
        userName = intent1.getStringExtra("name");
        s_name =  intent1.getStringExtra("s_name");
        System.out.println("店名："+s_name);
        back = (ImageButton)findViewById(R.id.Bacbutton);
        back.setOnClickListener(new Buttonlistener_back1());

        //上传图片功能
        headImage = (ImageView)findViewById(R.id.upload);
        headImage.setOnClickListener(new ImageButtonlistener());

        final MyExpendAdapter adapter=new MyExpendAdapter();
        expendView=(ExpandableListView) findViewById(R.id.list);
        expendView.setFocusable(false);
        expendView.setGroupIndicator(null);  //设置默认图标不显示
        expendView.setAdapter(adapter);

        //一级点击事件
        expendView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                group_click[groupPosition]+=1;
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        //二级点击事件
        expendView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                //可在这里做点击事件

                adapter.notifyDataSetChanged();
                return false;
            }
        });

    }

    /**
     * 适配器
     * @author Administrator
     *
     */
        private class MyExpendAdapter extends BaseExpandableListAdapter{
        /**
         * pic state
         */
        int []group_state=new int[]{R.drawable.group_right,R.drawable.group_down};//向左、向下小图标

        /**
         * group title
         */
        String []group_title=new String[]{"基本信息","商品详情","活动"};

        /**
         * child text
         */
        String [][] child_text=new String [][]{
                {"商品名称","商品价格","确认"},
                {"商品颜色","重量","材料","尺码","确认"},
                {"张三"}};
        String[] child2_text = new String []{"商品颜色","重量","材料","尺码","确认"};


        /**
         * 获取一级标签中二级标签的内容
         */
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return child_text[groupPosition][childPosition];
        }

        public int getItemViewType(int groupPosition) {
            int p = groupPosition;
            if (p == 0) {
                return TYPE_1;
            } else if (p == 1) {
                return TYPE_2;
            } else if (p == 2) {
                return TYPE_3;
            } else {
                return TYPE_1;
            }
        }
        /**
         * 获取二级标签ID
         */
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }
        /**
         * 对一级标签下的二级标签进行设置
         */
        @SuppressLint("SimpleDateFormat")
        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            int type = getItemViewType(groupPosition);
            ViewHolder holder = null;
            switch (type) {
                case TYPE_1:

                    convertView = getLayoutInflater().inflate(R.layout.child, null);
                    TextView tv = (TextView) convertView.findViewById(R.id.tv);
                    tv.setText(child_text[groupPosition][childPosition]);
                    switch (childPosition) {
                        case 0:
                            t1 = (EditText)convertView.findViewById(R.id.edit_1);
                            //t1.setText("123");
                            t1.addTextChangedListener(new TestWatcher1());
                           // 如果hashMap不为空，就设置的editText
                            if(hashMap.get(0) != null){
                                t1.setText(hashMap.get(0)); //商品名称
                            }
                            break;
                        case 1:
                            t2 = (EditText)convertView.findViewById(R.id.edit_1);
                            t2.addTextChangedListener(new TestWatcher2());
                            //如果hashMap不为空，就设置的editText
                            if(hashMap.get(1) != null){
                                t2.setText(hashMap.get(1));  //商品价格
                            }
                            break;
//                        case 2:
//                            t3=(EditText)convertView.findViewById(R.id.edit_1);
//                            t3.addTextChangedListener(new TestWatcher3());
//                            //如果hashMap不为空，就设置的editText
//                            if(hashMap.get(2) != null){
//                                t3.setText(hashMap.get(2));     //商品标签
//                            }
//                            break;

//                        case 3:
//                            convertView = getLayoutInflater().inflate(R.layout.child1, null);
//                            TextView t3 = (TextView)convertView.findViewById(R.id.ct1);
//                            t3.setText("商品特色");
//                            EditText e1 = (EditText)convertView.findViewById(R.id.ced1);
//                            e1.setHint("最多三个");
//                            e1.addTextChangedListener(new TestWatcher5());
//                            //如果hashMap不为空，就设置的editText
//                            if(hashMap.get(9) != null) {
//                                e1.setText(hashMap.get(9));       //商品特色
//                            }
//                            break;
//                        case 4:
//                            t3=(EditText)convertView.findViewById(R.id.edit_1);
//                            t3.addTextChangedListener(new TestWatcher4());
//                            //如果hashMap不为空，就设置的editText
//                            if(hashMap.get(3) != null){
//                                t3.setText(hashMap.get(3));       //品牌
//                            }
//                            break;
                        case 2:
                            convertView = getLayoutInflater().inflate(R.layout.child2, null);
                            //确认按钮
                            confirm = (Button)convertView.findViewById(R.id.confirm);
                            confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    change=1;
                                    Toast.makeText(Publish_Activity.this, "信息已更新", Toast.LENGTH_SHORT).show();
                                }
                            });
                            //t4.addTextChangedListener(new TestWatcher5());
                            break;
                    }
                    break;
                case TYPE_2:
                    convertView = getLayoutInflater().inflate(R.layout.child, null);
                    TextView tv1 = (TextView) convertView.findViewById(R.id.tv);
                    tv1.setText(child2_text[childPosition]);
                    switch (childPosition) {
                        case 0:
                            t5=(EditText)convertView.findViewById(R.id.edit_1);
                            t5.addTextChangedListener(new TestWatcher6());
                            //如果hashMap不为空，就设置的editText
                            if(hashMap.get(4) != null){
                                t5.setText(hashMap.get(4));              //商品颜色
                            }
                            break;
                        case 1:
                            t6=(EditText)convertView.findViewById(R.id.edit_1);
                            t6.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                            t6.addTextChangedListener(new TestWatcher7());
                            //如果hashMap不为空，就设置的editText
                            if(hashMap.get(5) != null){
                                t6.setText(hashMap.get(5));           //重量
                            }
                            break;
                        case 2:
                            t7=(EditText)convertView.findViewById(R.id.edit_1);
                            t7.addTextChangedListener(new TestWatcher8());
                            //如果hashMap不为空，就设置的editText
                            if(hashMap.get(6) != null){
                                t7.setText(hashMap.get(6));              //材料
                            }
                            break;
                        case 3:
                            t8=(EditText)convertView.findViewById(R.id.edit_1);
                            t8.addTextChangedListener(new TestWatcher9());
                            //如果hashMap不为空，就设置的editText
                            if(hashMap.get(7) != null){
                                t8.setText(hashMap.get(7));             //尺码
                            }
                            break;
//                        case 4:
//                            t9=(EditText)convertView.findViewById(R.id.edit_1);
//                            t9.addTextChangedListener(new TestWatcher10());
//                            //如果hashMap不为空，就设置的editText
//                            if(hashMap.get(8) != null){
//                                t9.setText(hashMap.get(8));               //产地
//                            }
//                            break;
                        case 4:
                            convertView = getLayoutInflater().inflate(R.layout.child3, null);
                        break;
                    }
                    break;
                case TYPE_3:
                    convertView=getLayoutInflater().inflate(R.layout.child4, null);
                    liulan = (Button)convertView.findViewById(R.id.liulan);
                    liulan.setOnClickListener(new Buttonlistener1());
                    public1 = (Button)convertView.findViewById(R.id.public1);
                    public1.setOnClickListener(new Buttonlistener2());
                    break;
            }

            return convertView;
        }


        /**
         * 一级标签下二级标签的数量
         */
        @Override
        public int getChildrenCount(int groupPosition) {
            return child_text[groupPosition].length;
        }

        /**
         * 获取一级标签内容
         */
        @Override
        public Object getGroup(int groupPosition) {
            return group_title[groupPosition];
        }

        /**
         * 一级标签总数
         */
        @Override
        public int getGroupCount() {
            return group_title.length;
        }

        /**
         * 一级标签ID
         */
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }
        /**
         * 对一级标签进行设置
         */
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            convertView=getLayoutInflater().inflate(R.layout.group, null);

            ImageView iv=(ImageView) convertView.findViewById(R.id.iv);
            TextView tv=(TextView) convertView.findViewById(R.id.iv_title);

            iv.setImageResource(R.drawable.group_right);
            tv.setText(group_title[groupPosition]);

            if(group_click[groupPosition]%2==0){
                iv.setImageResource(R.drawable.group_right);
            }else{
                iv.setImageResource(R.drawable.group_down);
            }


            return convertView;
        }
        /**
         * 指定位置相应的组视图
         */
        @Override
        public boolean hasStableIds() {
            return true;
        }

        /**
         *  当选择子节点的时候，调用该方法
         */
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        /*监视文本框*/
        class TestWatcher1 implements TextWatcher {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count,int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //将editText中改变的值设置的HashMap中
                hashMap.put(0, s.toString());
            }
        }
        class TestWatcher2 implements TextWatcher {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count,int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //将editText中改变的值设置的HashMap中
                hashMap.put(1, s.toString());
            }
        }
        class TestWatcher3 implements TextWatcher {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count,int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //将editText中改变的值设置的HashMap中
                hashMap.put(2, s.toString());
            }
        }
        class TestWatcher4 implements TextWatcher {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count,int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //将editText中改变的值设置的HashMap中
                hashMap.put(3, s.toString());
            }
        }
        class TestWatcher5 implements TextWatcher {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count,int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //将editText中改变的值设置的HashMap中
                hashMap.put(9, s.toString());
            }
        }
        class TestWatcher6 implements TextWatcher {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count,int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //将editText中改变的值设置的HashMap中
                hashMap.put(4, s.toString());
            }
        }
        class TestWatcher7 implements TextWatcher {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count,int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //将editText中改变的值设置的HashMap中
                hashMap.put(5, s.toString());
            }
        }
        class TestWatcher8 implements TextWatcher {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count,int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //将editText中改变的值设置的HashMap中
                hashMap.put(6, s.toString());
            }
        }
        class TestWatcher9 implements TextWatcher {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count,int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //将editText中改变的值设置的HashMap中
                hashMap.put(7, s.toString());
            }
        }
        class TestWatcher10 implements TextWatcher {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count,int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //将editText中改变的值设置的HashMap中
                hashMap.put(8, s.toString());
            }
        }
    }

    class Buttonlistener1 implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            //System.out.println(hashMap.get(1)+hashMap.get(3));
            Intent intent = new Intent();
            //传递发布商品的数据到浏览页面
                if (hashMap.get(0) != null) {
                    intent.putExtra("1-1", hashMap.get(0));
                }
                if (hashMap.get(1) != null) {
                    intent.putExtra("1-2", hashMap.get(1));
                }
                if (hashMap.get(2) != null) {
                    intent.putExtra("1-3", hashMap.get(2));
                }
                if (hashMap.get(9) != null) {
                    intent.putExtra("1-4", hashMap.get(9));
                }
                if (hashMap.get(3) != null) {
                    intent.putExtra("1-5", hashMap.get(3));
                }
            if(hashMap.get(4) != null) {
                intent.putExtra("2-1", hashMap.get(4));
            }
            if(hashMap.get(5) != null) {
                intent.putExtra("2-2", hashMap.get(5));
            }
            if(hashMap.get(6) != null) {
                intent.putExtra("2-3", hashMap.get(6));
            }
            if(hashMap.get(7) != null) {
                intent.putExtra("2-4", hashMap.get(7));
            }
            if(hashMap.get(8) != null) {
                intent.putExtra("2-5", hashMap.get(8));
            }
            intent.putExtra("change",change);
            change = 0;
            //传商品图片到浏览页面
            Bitmap p_picture = ((BitmapDrawable)headImage.getDrawable()).getBitmap();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            p_picture.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte [] bitmapByte =baos.toByteArray();
            intent.putExtra("bitmap", bitmapByte);
            intent.setClass(Publish_Activity.this,Product_display.class);
            startActivity(intent);
        }
    }
    class Buttonlistener2 implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            map_send = new HashMap<>();
            map_send.put("a_name",userName);
            map_send.put("s_name",s_name);
            if(hashMap.get(0)==null||hashMap.get(1)==null||hashMap.get(4)==null||hashMap.get(5)==null||hashMap.get(6)==null||hashMap.get(7)==null){
                Toast.makeText(Publish_Activity.this,"请将信息填写完整",Toast.LENGTH_SHORT).show();
            }
            else
            {
                map_send.put("g_name",hashMap.get(0));
                map_send.put("g_price",hashMap.get(1));
                map_send.put("g_colour",hashMap.get(4));
                map_send.put("g_weight",hashMap.get(5));
                map_send.put("g_material",hashMap.get(6));
                map_send.put("g_size",hashMap.get(7));
                System.out.println(map_send);
                load_msg load = new load_msg();
                load.execute();
                send_pic send = new send_pic();
                send.execute();
            }

        }
    }
    class load_msg extends AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {
            //第一个执行方法
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... params) {
            try {
                result = HttpUtil.postListMap(HttpUtil.BASE_URL1+"insertGood1",map_send);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }
        @Override
        protected void onPostExecute(String result) {
            System.out.println("upload: "+result);
            if(result.equals("TTT"))
            {
                Toast.makeText(Publish_Activity.this,"商品已发布",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    class Buttonlistener_back1 implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            finish();
        }
    }
    class ImageButtonlistener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            final String[] items = new String[]{"从相册选取","拍照"};
            new AlertDialog.Builder(Publish_Activity.this).setTitle("上传照片").setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    switch (which) {
                        case 0:
                            choseHeadImageFromGallery();
                            //Toast.makeText(Publish_Activity.this, "您选中了：" + items[0], Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            //Toast.makeText(Publish_Activity.this, "您选中了："+items[1], Toast.LENGTH_SHORT).show();
                            choseHeadImageFromCameraCapture();
                            break;
                    }
                }
            }).show();
        }
    }
    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 判断存储卡是否可用，存储照片文件
        if (hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(new File(Environment
                            .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }

        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {

        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
            return;
        }

        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(intent.getData());
                break;

            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(getApplication(), "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }

                break;

            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    try {
                        setImageToHeadView(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) throws IOException {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            headImage.setImageBitmap(photo);
            saveMyBitmap(userName,photo);
        }
    }

    public void saveMyBitmap(String bitName, Bitmap mBitmap) throws IOException {
        f = new File("/sdcard/" + bitName + ".jpg");
        f.createNewFile();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("result= " + HttpAssist.uploadFile(f, HttpUtil.BASE_URL1 + "UpU_picServlet"));
    }
    class send_pic extends AsyncTask<Integer, Integer, String>
    {
        @Override
        protected void onPreExecute() {
            //第一个执行方法
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Integer... params) {


            map = new HashMap<>();
            map.put("chinese",hashMap.get(0));
            try {
                HttpUtil.postListMap(HttpUtil.BASE_URL1+"getChinese",map);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String resu =  HttpAssist.uploadFile(f, HttpUtil.BASE_URL1 + "updateG_picture");
            return resu;
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }
        @Override
        protected void onPostExecute(String result) {
            System.out.println(result);
        }
    }
    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_publish_, menu);
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

    public static class ViewHolder {
        public EditText t1;
    }
}
