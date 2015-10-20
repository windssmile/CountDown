package net.dingjie.countdown;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText editHour;
    private EditText editMinute;
    private EditText editSecond;
    private Button buttonStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //透明状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        //透明导航栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        FindItems();
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] handle =Handle();
                if(handle[0]==1) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("hour", handle[1]);
                    bundle.putInt("minute",handle[2]);
                    bundle.putInt("second",handle[3]);
                    Intent intent = new Intent(MainActivity.this,Timer.class);
                    intent.putExtra("bundle",bundle);
                    startActivityForResult(intent,1);
                }else {
                    Toast.makeText(MainActivity.this, R.string.setwrong, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void FindItems(){
        buttonStart = (Button) findViewById(R.id.Button_start);
        editHour = (EditText) findViewById(R.id.editHour);
        editMinute = (EditText) findViewById(R.id.editMinute);
        editSecond = (EditText) findViewById(R.id.editSecond);
    }
    protected int[] Handle() {
        int[] rtn = new int[4];
        try {
            rtn[0] = 1;
            rtn[1] = Integer.parseInt(editHour.getText().toString());
            rtn[2] = Integer.parseInt(editMinute.getText().toString());
            rtn[3] = Integer.parseInt(editSecond.getText().toString());
            if (rtn[2] >= 60 || rtn[2] < 0 || rtn[3] >= 60 || rtn[3] < 0 || rtn[1] < 0||rtn[1]>=24) {
                rtn[0] = 0;
                Log.d("MainActivity","something wrong");
            }
            if(rtn[1]==0&&rtn[2]==0&&rtn[3]==0){
                rtn[0]=0;
                Log.d("MainActivity","something wrong");
            }

            return rtn;
        } catch (NumberFormatException e) {
            rtn[0] = 0;
            Log.d("MainActivity","something wrong");
            return rtn;
        }
    }
}
