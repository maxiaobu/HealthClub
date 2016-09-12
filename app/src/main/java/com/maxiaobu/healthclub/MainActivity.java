package com.maxiaobu.healthclub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.getRequestInstance().getRequestManager().cancelAll(this);
    }
}
