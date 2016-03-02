package cn.homecaught.airplus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import cn.homecaught.airplus.MyApplication;
import cn.homecaught.airplus.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                next();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 3000);

    }

    private void next() {
        if (!MyApplication.getInstance().isLogin()){
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

}
