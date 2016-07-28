package com.example.ershoudaili;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by junyuan on 2015/8/10.
 */
public class Enroll extends ActionBarActivity {
    private Button enroll;
    private ImageButton back;
    private EditText name;
    private EditText phone_number;
    private RadioGroup choice;
    private EditText code;
    private EditText code_confirm;
    private String status;
    private HashMap map;
    private String kind;
    private boolean user_check;
    private boolean code_check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enroll);
        getSupportActionBar().hide();
        enroll = (Button)findViewById(R.id.enroll1);
        TextPaint tp1 = enroll.getPaint();
        tp1.setFakeBoldText(true);
        TextPaint tp2 = enroll.getPaint();
        tp2.setFakeBoldText(true);
        enroll.setOnClickListener(new Buttonlistener6());
        back = (ImageButton)findViewById(R.id.Bacbutton3);
        back.setOnClickListener(new Buttonlistener_back());
        name = (EditText)findViewById(R.id.name);
        phone_number = (EditText)findViewById(R.id.phone_number);
        choice = (RadioGroup)findViewById(R.id.choise);
        code = (EditText)findViewById(R.id.code);
        code_confirm = (EditText)findViewById(R.id.code_confirm);
    }
    class send_enroll extends AsyncTask<Integer, Integer, String>
    {
        @Override
        protected void onPreExecute() {
            //第一个执行方法
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Integer... params) {
            map = new HashMap<String,String>();
            map.put("name",name.getText().toString());
            map.put("password",code.getText().toString());
            map.put("tel",phone_number.getText().toString());
            map.put("kind",kind);
            try {
                status = HttpUtil.postListMap(HttpUtil.BASE_URL1+"insertUserServlet",map);
            } catch (Exception e) {
                System.out.println(e+"现在");
                e.printStackTrace();
            }
            return status;
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }
        @Override
        protected void onPostExecute(String status) {
            if(status.equals("TTT")) {
                Toast.makeText(Enroll.this, "注册成功", Toast.LENGTH_SHORT).show();
                Intent intent4 = new Intent();
                intent4.setClass(Enroll.this, Success.class);
                startActivity(intent4);
            }
            else{
                Toast.makeText(Enroll.this, "注册失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
    class Buttonlistener6 implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(code.getText().toString().equals(code_confirm.getText().toString()))
                code_check=true;
            else
                code_check=false;
            System.out.println(name.getText()+" "+phone_number.getText()+" "+choice.getCheckedRadioButtonId()+" "+code.getText()+" "+code_confirm.getText());
            if(!code_check) {
                Toast.makeText(Enroll.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            }
            else if(name.getText().toString().equals("")||phone_number.getText().toString().equals("")||code.getText().toString().equals("")||code_confirm.getText().toString().equals("")||choice.getCheckedRadioButtonId()<0)
            {
                Toast.makeText(Enroll.this,"请填写完整信息",Toast.LENGTH_SHORT).show();
            }
            else {
                if (choice.getCheckedRadioButtonId() % 2 == 0)
                    kind = "2";
                else
                    kind = "1";
                System.out.println("kind= " + kind);
                send_enroll send = new send_enroll();
                send.execute();
            }
        }
    }
    class Buttonlistener_back implements View.OnClickListener{

        @Override
    public void onClick(View v) {
        finish();
    }
}
}
