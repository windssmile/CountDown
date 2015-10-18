package net.dingjie.countdown;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Timer extends AppCompatActivity {
    private TextView display;
    private Button buttonStop;
    private long countDown;
    private long remain = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        getData();
        final MyTimer[] myTimer = {new MyTimer(countDown)};
        myTimer[0].start();
        Button buttonStop =(Button) findViewById(R.id.Button_stop);
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(remain==0) {
                    remain = myTimer[0].getRestTime();
                    myTimer[0].cancel();
                } else {
                    myTimer[0] = new MyTimer(remain);
                    myTimer[0].start();
                    remain =0;
                }
            }
        });

    }


    private String displayTime(long millisUntilFinished) {

        int hour = (int)(millisUntilFinished/3600000);
        int minute = (int)((millisUntilFinished-3600000*hour)/60000);
        int second = (int)((millisUntilFinished-3600000*hour-60000*minute)/1000);
        int mill = (int)(millisUntilFinished%1000);
        StringBuffer displayBuffer = new StringBuffer();
        if(hour<10){
            displayBuffer.append("0");
        }
        displayBuffer.append(hour);
        displayBuffer.append(":");
        if(minute<10){
            displayBuffer.append("0");
        }
        displayBuffer.append(minute);
        displayBuffer.append(":");
        if (second<10){
            displayBuffer.append("0");
        }
        displayBuffer.append(second);
        displayBuffer.append(".");
        if(mill<100){
            if(mill<10){
                displayBuffer.append("0");
            }
            displayBuffer.append("0");
        }
        displayBuffer.append(mill);
        return displayBuffer.toString();

    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        countDown = ((long) bundle.getInt("hour") * 60 * 60 * 1000) + ((long) bundle.getInt("minute") * 60 * 1000) + ((long) bundle.getInt("second") * 1000);
        display = (TextView) findViewById(R.id.textDisplay);
    }

    protected class MyTimer extends CountDownTimer{
        private long restTime;
        public MyTimer(long millisInFuture) {
            super(millisInFuture, 33);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            display.setText(displayTime(millisUntilFinished));
            restTime = millisUntilFinished;
        }
        public long getRestTime(){
            return restTime;
        }

        @Override
        public void onFinish() {
                display.setText("00:00:00.000");
                Toast.makeText(Timer.this, R.string.timeup, Toast.LENGTH_LONG).show();
        }
    }

}
