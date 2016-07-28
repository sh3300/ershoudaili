package com.example.ershoudaili;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentPage2 extends Fragment{
    private static final String ACTION_RECV_MSG = "com.example.logindemo.action.RECEIVE_MESSAGE";
    private String userName;
    private String passWord;
    private MessageReceiver receiver ;
    private TextView log_error;
    private Button forget;
    private Button enroll;
    private EditText et_username;
    private EditText et_password;
    private RadioButton buttonA;
    private RadioButton buttonB;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View converview = inflater.inflate(R.layout.fragment_2, null);
        Button b3 = (Button)converview.findViewById(R.id.f2_b3);
        TextPaint tp = b3.getPaint();
        tp.setFakeBoldText(true);
        log_error = (TextView)converview.findViewById(R.id.log_error);
        //b3.setOnClickListener(new Buttonlistener3());
        forget = (Button)converview.findViewById(R.id.no_code);
        forget.setOnClickListener(new Buttonlistener4());
        enroll = (Button)converview.findViewById(R.id.enroll);
        enroll.setOnClickListener(new Buttonlistener_enroll());
        buttonA = (RadioButton)converview.findViewById(R.id.buttona);
        buttonB = (RadioButton)converview.findViewById(R.id.buttonb);

        //
        et_username = (EditText)converview.findViewById(R.id.et_user);
        et_password =( EditText)converview.findViewById(R.id.et_psw);
        b3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(matchLoginMsg())
                {
                    // 如果校验成功
                    Intent msgIntent = new Intent(getActivity(), ConnectService.class);
                    msgIntent.putExtra("username", et_username.getText().toString().trim());
                    msgIntent.putExtra("password", et_password.getText().toString().trim());
                    if(buttonA.isChecked())
                    msgIntent.putExtra("radiobutton","1");
                    else if(buttonB.isChecked())
                        msgIntent.putExtra("radiobutton","2");
                    getActivity().startService(msgIntent);
                }
//                FragmentManager ft = getActivity().getSupportFragmentManager();
//                ft.beginTransaction().replace(R.id.realtabcontent,new FragmentPage2_logined());
            }
        });
        //动态注册receiver
        IntentFilter filter = new IntentFilter(ACTION_RECV_MSG);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MessageReceiver();
        getActivity().registerReceiver(receiver, filter);
        return converview;
	}


    protected boolean matchLoginMsg() {
        // TODO Auto-generated method stub
        userName = et_username.getText().toString().trim();
        passWord = et_password.getText().toString().trim();
        if(userName.equals(""))
        {
            Toast.makeText(getActivity(), "账号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(passWord.equals(""))
        {
            Toast.makeText(getActivity(), "密码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!buttonA.isChecked()&&!buttonB.isChecked())
        {
            Toast.makeText(getActivity(),"需选择登录类型",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    //接收广播类
    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("result");
            Log.i("MessageReceiver", message);
            // 如果登录成功
            if (!message.equals("error")){
                // 启动Main Activity
                if(buttonA.isChecked()){
                Intent nextIntent = new Intent(getActivity(), Personal_center.class);
                    nextIntent.putExtra("msg",message);
                    nextIntent.putExtra("name",userName);
                    nextIntent.putExtra("status","agent");
                startActivity(nextIntent);
                getActivity().finish();}
                else if(buttonB.isChecked()){
                    Intent nextIntent = new Intent(getActivity(),Personal_center_for_customer.class);
                    nextIntent.putExtra("msg",message);
                    nextIntent.putExtra("name",userName);
                    nextIntent.putExtra("status","user");
                    startActivity(nextIntent);
                    getActivity().finish();
                }
                // 结束该Activity
                getActivity().finish();
                //注销广播接收器
                context.unregisterReceiver(this);
            }else{
                Toast.makeText(getActivity(), "用户名或密码错误，请重新输入!",Toast.LENGTH_SHORT).show();
            }

        }
    }
    class Buttonlistener3 implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(getActivity(),Personal_center.class);
            startActivity(intent);
            log_error.setText("登录失败，请重新输入");
        }
    }
    class Buttonlistener4 implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent2 = new Intent();
            intent2.setClass(getActivity(),Forget_code.class);
            startActivity(intent2);
        }
    }
    class Buttonlistener_enroll implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent5 = new Intent();
            intent5.setClass(getActivity(),Enroll.class);
            startActivity(intent5);
        }
    }
}