package com.example.wxs20;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmManagerActivity extends Activity {
    private Button btn;
    private AlarmManager alarmManager;  //闹钟管理器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_manager);

        //获取闹钟管理器
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        btn = (Button)findViewById(R.id.set_clock);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setClock(view);
            }
        });


        Button btn_back=(Button)findViewById(R.id.button_back);
        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AlarmManagerActivity.this,MenuActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setClock(View view){
        //获取当前系统时间
        Calendar calendar = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            calendar = Calendar.getInstance();
        }
        int hour = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            hour = calendar.get(Calendar.HOUR_OF_DAY);
        }
        int minute = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            minute = calendar.get(Calendar.MINUTE);
        }

        //弹出闹钟框
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar c = null;    //获取日期对象
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    c = Calendar.getInstance();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    c.set(Calendar.HOUR_OF_DAY, hourOfDay); //设置闹钟小时数
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    c.set(Calendar.MINUTE, minute); //设置闹钟分钟数
                }
                Intent intent = new Intent(AlarmManagerActivity.this, AlarmReceiver.class);
                //创建pendingIntent
                PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmManagerActivity.this,0X102, intent,0);
                //设置闹钟
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                }
                Toast.makeText(AlarmManagerActivity.this, "闹钟设置成功", Toast.LENGTH_SHORT).show();
            }
        },hour,minute,true);
        timePickerDialog.show();
    }
}
