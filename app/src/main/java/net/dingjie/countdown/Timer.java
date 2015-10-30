package net.dingjie.countdown;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Timer extends AppCompatActivity {
    CoordinatorLayout container;
    DialogInterface.OnClickListener exitListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:
                    finish();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:
                    break;
                default:
                    break;
            }
        }
    };
    private TextView display;
    private Button buttonStop;
    private long countDown;
    private long remain = 0;
    private MyTimer myTimer;
    OnClickListener ocl = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (remain == 0) {
                remain = myTimer.getRestTime();
                myTimer.cancel();
                Toast.makeText(Timer.this, R.string.pausedString, Toast.LENGTH_SHORT).show();
                buttonStop.setText(R.string.resumeString);
            } else {
                myTimer = new MyTimer(remain);
                myTimer.start();
                remain = 0;
                Toast.makeText(Timer.this, R.string.resumedString, Toast.LENGTH_SHORT).show();
                buttonStop.setText(R.string.pauseString);
            }
        }
    };
    private AlertDialog isExit;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (buttonStop.isClickable()) {
            remain = myTimer.getRestTime();
            myTimer.cancel();
            buttonStop.setText(R.string.resumeString);
            isExit.show();
        } else {
            myTimer.cancel();
            finish();
        }
        return true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
//        //透明状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        //透明导航栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prepare();
        inter();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && buttonStop.isClickable()) {
            remain = myTimer.getRestTime();
            myTimer.cancel();
            buttonStop.setText(R.string.resumeString);
            isExit.show();
        } else {
            myTimer.cancel();
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

    private void prepare() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        countDown = ((long) bundle.getInt("hour") * 60 * 60 * 1000) + ((long) bundle.getInt("minute") * 60 * 1000) + ((long) bundle.getInt("second") * 1000);
        display = (TextView) findViewById(R.id.textDisplay);
        isExit = new AlertDialog.Builder(this).create();
//        isExit.setTitle(R.string.app_name);
        isExit.setMessage(getString(R.string.exit));
        isExit.setTitle(getString(R.string.app_name));
        isExit.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.yes), exitListener);
        isExit.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.no), exitListener);
    }

    private void inter() {
        myTimer = new MyTimer(countDown);
        buttonStop = (Button) findViewById(R.id.Button_stop);
        buttonStop.setOnClickListener(ocl);
        myTimer.start();
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
            container = (CoordinatorLayout) findViewById(R.id.container);
            Snackbar.make(container, R.string.timeup, Snackbar.LENGTH_LONG).show();
            makeNotification(Timer.this);
            buttonStop.setClickable(false);
            buttonStop.setTextColor(Color.parseColor("#666666"));
        }

        public void makeNotification(Timer timer) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder nbuilder = new NotificationCompat.Builder(timer);
            nbuilder.setContentText(getString(R.string.timeup));
            nbuilder.setContentTitle(getString(R.string.app_name));
            nbuilder.setTicker(getString(R.string.timeup));
            nbuilder.setWhen(System.currentTimeMillis());
            nbuilder.setOngoing(false);
            nbuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            nbuilder.setSmallIcon(R.mipmap.ic_launcher);
            Intent intent = new Intent(Timer.this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(Timer.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            nbuilder.setContentIntent(pendingIntent);
            Notification notification = nbuilder.build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(100, notification);
        }
    }

}
