package com.example.ershoudaili;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by junyuan on 2015/8/9.
 */
public class Forget_code extends ActionBarActivity {
    Button con;
    Button check;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_code);
        getSupportActionBar().hide();
        con = (Button)findViewById(R.id.confirm);
        check = (Button)findViewById(R.id.check);
        con.setOnClickListener(new Buttonlistener5());
        TextPaint tp1 = con.getPaint();
        tp1.setFakeBoldText(true);
        TextPaint tp2 = check.getPaint();
        tp2.setFakeBoldText(true);
        back = (ImageButton)findViewById(R.id.Bacbutton1);
        back.setOnClickListener(new Buttonlistener_back());
    }

   class Buttonlistener5 implements View.OnClickListener{

       @Override
       public void onClick(View v) {
           Intent intent3 = new Intent();
           intent3.setClass(Forget_code.this,Success.class);
           startActivity(intent3);
       }
   }
    class Buttonlistener_back implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            finish();
        }
    }
}
