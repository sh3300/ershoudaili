package com.example.ershoudaili;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Personal_Information extends ActionBarActivity {
    private TextView Top;
    private ImageButton back;
    private TextView name;
    private Button phone_number;
    private Button address;
    private HashMap<String,String> map;
    private String result;
    private String userName;
    private String input;
    private String input2;
    private String log_status;//登录者身份
    private File f;
    private String json_string;
    private  String resu;

    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image1.jpg";

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
        setContentView(R.layout.activity_personal__information);
        getSupportActionBar().hide();     //隐藏actionbar
        //设置顶栏颜色、字体
        Top = (TextView)findViewById(R.id.top3);
        TextPaint tp1 = Top.getPaint();
        tp1.setFakeBoldText(true);

        Intent intent1 = getIntent();
        userName = intent1.getStringExtra("name");
        log_status = intent1.getStringExtra("status");
        json_string = intent1.getStringExtra("msg");
        name = (TextView)findViewById(R.id.name1);

        phone_number = (Button)findViewById(R.id.phone);
        address = (Button)findViewById(R.id.address);
        if(log_status.equals("agent")){
            try {
                phone_number.setText("手机号           "+JSONutils.getAgent(json_string).get(0).get("a_tel"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                address.setText("店家地址       "+JSONutils.getAgent(json_string).get(0).get("a_address"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                name.setText(JSONutils.getAgent(json_string).get(0).get("a_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(log_status.equals("user")){
            try {
                phone_number.setText("手机号           "+JSONutils.getUser(json_string).get(0).get("u_tel"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                address.setText("收货地址       "+JSONutils.getUser(json_string).get(0).get("u_address"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                name.setText(JSONutils.getUser(json_string).get(0).get("u_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(Personal_Information.this);
                new AlertDialog.Builder(Personal_Information.this).setTitle("修改")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                input = et.getText().toString();
                                if (input.equals("")) {
                                }
                                else {
                                    phone_number.setText("手机号           "+input);
                                    send_tel s_tel = new send_tel();
                                    s_tel.execute();
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(Personal_Information.this);
                new AlertDialog.Builder(Personal_Information.this).setTitle("修改")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                input2 = et.getText().toString();
                                if (input2.equals("")) {
                                }
                                else {
                                    if(log_status.equals("user"))
                                    address.setText("收货地址       "+input2);
                                    else if(log_status.equals("agent"))
                                        address.setText("店家地址       "+input2);
                                    send_add s_add = new send_add();
                                    s_add.execute();
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        back = (ImageButton)findViewById(R.id.Bacbutton6);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();                      //返回上一页
            }
        });
        //上传头像
        headImage = (ImageView)findViewById(R.id.upload);
        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = new String[]{"从相册选取","拍照"};
                new AlertDialog.Builder(Personal_Information.this).setTitle("上传照片").setItems(items, new DialogInterface.OnClickListener() {
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
        });
    }
    class send_tel extends AsyncTask<Integer, Integer, String>
    {
        @Override
        protected void onPreExecute() {
            //第一个执行方法
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Integer... params) {
            try {
                map = new HashMap<>();
                if(log_status.equals("agent")){
                map.put("a_tel",input);
                map.put("a_name",userName);
                result = HttpUtil.postListMap(HttpUtil.BASE_URL1+"updateAgentTel",map);
                }
                else if(log_status.equals("user")){
                    map.put("u_name",userName);
                    map.put("u_tel",input);
                    result = HttpUtil.postListMap(HttpUtil.BASE_URL1+"updateU_tel",map);

                }
            } catch (Exception e) {
                System.out.println(e+"1");
            }
            return result;
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }
        @Override
        protected void onPostExecute(String result) {
            if(result.equals("TTT"))
            {
               Toast.makeText(Personal_Information.this,"修改成功",Toast.LENGTH_SHORT).show();
            }
        }
    }
    class send_add extends AsyncTask<Integer, Integer, String>
    {
        @Override
        protected void onPreExecute() {
            //第一个执行方法
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Integer... params) {
            try {
                map = new HashMap<>();
                if(log_status.equals("agent")) {
                    map.put("a_address", input2);
                    map.put("a_name", userName);
                    result = HttpUtil.postListMap(HttpUtil.BASE_URL1 + "updateA_address", map);
                }
                else if(log_status.equals("user")){
                    map.put("u_address", input2);
                    map.put("u_name", userName);
                    result = HttpUtil.postListMap(HttpUtil.BASE_URL1 + "updateU_address", map);
                }
            } catch (Exception e) {
                System.out.println(e+"1");
            }
            return result;
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }
        @Override
        protected void onPostExecute(String result) {
            if(result.equals("TTT"))
            {
                Toast.makeText(Personal_Information.this,"修改成功",Toast.LENGTH_SHORT).show();
            }
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
        send_pic send = new send_pic();
        send.execute();
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
            System.out.println(log_status);
            if(log_status.equals("user")) {
                System.out.println("us");
                resu = HttpAssist.uploadFile(f, HttpUtil.BASE_URL1 + "UpU_picServlet");
            }
            else if(log_status.equals("agent")){
                System.out.println("ag");
                resu = HttpAssist.uploadFile(f, HttpUtil.BASE_URL1 + "updateA_picture");
            }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_personal__information, menu);
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
