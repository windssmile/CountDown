package net.dingjie.countdown;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Timer extends AppCompatActivity {
    private TextView display;
    private Button buttonStop;
    private long countDown;
    private long remain = 0;
    private MyTimer myTimer;
    DialogInterface.OnClickListener exitListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:
                    myTimer.cancel();
                    finish();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:
                    break;
                default:
                    break;
            }
        }
    };
    View.OnClickListener vocl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (remain == 0) {
                remain = myTimer.getRestTime();
                myTimer.cancel();
                Toast.makeText(Timer.this, R.string.pauseString, Toast.LENGTH_SHORT).show();
                buttonStop.setText(R.string.resumeString);
            } else {
                myTimer = new MyTimer(remain);
                myTimer.start();
                remain = 0;
                Toast.makeText(Timer.this, R.string.resumeString, Toast.LENGTH_SHORT).show();
                buttonStop.setText(R.string.pauseString);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        getData();
        inter(myTimer, countDown);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && buttonStop.isClickable()) {
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            isExit.setTitle(R.string.app_name);
            isExit.setMessage(getString(R.string.exit));
            isExit.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.yes), exitListener);
            isExit.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.no), exitListener);
            isExit.show();
        } else {
            finish();
        }
        return false;
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

    private void inter(MyTimer myTimer, long countDown) {
        myTimer = new MyTimer(countDown);
        myTimer.start();
        buttonStop = (Button) findViewById(R.id.Button_stop);
        buttonStop.setOnClickListener(vocl);
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
            buttonStop.setText(R.string.timeup);
            buttonStop.setClickable(false);
        }
    }

}
