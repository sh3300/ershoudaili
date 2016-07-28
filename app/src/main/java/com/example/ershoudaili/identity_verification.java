package com.example.ershoudaili;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class identity_verification extends ActionBarActivity {
    private TextView top;
    private ImageView id_card;
    private ImageView campus_card;
    private ImageButton back;
    private Button upload;
    private File f;
    private int select;
    private String resu;
    private String userName;
    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "id_card_image.jpg";
    private static final String IMAGE_FILE_NAME1 = "campus_card_image.jpg";
    private int position =0;//确定上传身份证还是上传校园卡

    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;

    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 2000;
    private static int output_Y = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_verification);
        getSupportActionBar().hide();
        top = (TextView) findViewById(R.id.top3);
        TextPaint tp1 = top.getPaint();
        tp1.setFakeBoldText(true);
        Intent intent1 = getIntent();
        userName = intent1.getStringExtra("name");
        id_card = (ImageView) findViewById(R.id.id_card);
        campus_card = (ImageView) findViewById(R.id.campus_card);
        upload = (Button)findViewById(R.id.upload1);
        upload.setOnClickListener(new Buttonlistener_upload());
        id_card.setOnClickListener(new Upload_id_card_listener());
        campus_card.setOnClickListener(new Upload_id_campus_listener());
        back = (ImageButton)findViewById(R.id.Bacbutton6);
        back.setOnClickListener(new Buttonlistener_back());
    }
    class Buttonlistener_upload implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Toast.makeText(identity_verification.this,"已上传，我们将尽快予以答复",Toast.LENGTH_SHORT).show();
        }
    }
    class Buttonlistener_back implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            finish();
        }
    }
    class Upload_id_card_listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            position=0;
            final String[] items = new String[]{"从相册选取", "拍照"};
            new AlertDialog.Builder(identity_verification.this).setTitle("上传照片").setItems(items, new DialogInterface.OnClickListener() {
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
                    if(position==0){
                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile));}
                    else if(position==1){
                        File tempFile = new File(
                                Environment.getExternalStorageDirectory(),
                                IMAGE_FILE_NAME1);
                        cropRawPhoto(Uri.fromFile(tempFile));
                    }
                } else {
                    Toast.makeText(getApplication(), "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }

                break;

            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    if(position==0){
                        try {
                            setImageToHeadView(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }}
                    else if(position==1){
                        try {
                            setImageToHeadView1(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }}
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
            id_card.setImageBitmap(photo);
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
            if(position==0)
            resu =  HttpAssist.uploadFile(f, HttpUtil.BASE_URL1 + "updateA_ID");//身份证
            else if(position==1)
            resu = HttpAssist.uploadFile(f, HttpUtil.BASE_URL1 + "updateA_scl_num");//校园卡
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

    class Upload_id_campus_listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            position=1;
            final String[] items1 = new String[]{"从相册选取", "拍照"};
            new AlertDialog.Builder(identity_verification.this).setTitle("上传照片").setItems(items1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    switch (which) {
                        case 0:
                            choseHeadImageFromGallery1();
                            //Toast.makeText(Publish_Activity.this, "您选中了：" + items[0], Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            //Toast.makeText(Publish_Activity.this, "您选中了："+items[1], Toast.LENGTH_SHORT).show();
                            choseHeadImageFromCameraCapture1();
                            break;
                    }
                }
            }).show();
        }
    }
    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery1() {
        Intent intentFromGallery1 = new Intent();
        // 设置文件类型
        intentFromGallery1.setType("image/*");
        intentFromGallery1.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery1, CODE_GALLERY_REQUEST);
    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture1() {
        Intent intentFromCapture1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 判断存储卡是否可用，存储照片文件
        if (hasSdcard()) {
            intentFromCapture1.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(new File(Environment
                            .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }

        startActivityForResult(intentFromCapture1, CODE_CAMERA_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView1(Intent intent) throws IOException {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo1 = extras.getParcelable("data");
            campus_card.setImageBitmap(photo1);
            saveMyBitmap(userName,photo1);
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard1() {
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
        getMenuInflater().inflate(R.menu.menu_identity_verification, menu);
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
