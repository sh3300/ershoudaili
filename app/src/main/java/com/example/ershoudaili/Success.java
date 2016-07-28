package com.example.ershoudaili;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by junyuan on 2015/8/9.
 */
public class Success extends ActionBarActivity {
    Button back_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success);
        getSupportActionBar().hide();
        back_home = (Button)findViewById(R.id.back);
        back_home.setOnClickListener(new Buttonlistener_back());
    }
    class Buttonlistener_back implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClass(Success.this,Home_page.class);
            startActivity(intent);
        }
    }
}
