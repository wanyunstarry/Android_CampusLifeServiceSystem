package com.example.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.app.utils.DBHelper;
import com.example.app.utils.util;

public class DialogModal extends Activity {
    Button close_activity;
    Button save_activity;
    Spinner selected_time;
    Spinner selected_day;
    EditText subject;
    EditText teacher;
    //数据库名，表名
    public final String DB_NAME = "classes_db.db";
    public final String TABLE_NAME = "classes_db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_set);

        //当点击dialog之外完成此activity
        setFinishOnTouchOutside (true);

        //关闭按钮操作
        close_activity=(Button) findViewById(R.id.close_activity);
        close_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogModal.this.finish();
            }
        });

        //保存按钮操作
        save_activity = findViewById(R.id.save_activity);
        save_activity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                selected_day = findViewById(R.id.selected_day);
                String day = selected_day.getSelectedItem().toString();

                if (day.equals("--请选择--")) {
                    save_activity.setError("");
                    return;
                }
                selected_time = findViewById(R.id.selected_time);
                String time = selected_time.getSelectedItem().toString();
                if (time.equals("--请选择--")) {
                    save_activity.setError("");
                    return;
                }

                subject = findViewById(R.id.subject);
                String text = subject.getText().toString();
                if ("".equals(text)) {
                    save_activity.setError("");
                    return;
                }

                teacher = findViewById(R.id.teacher);
                String teacher = subject.getText().toString();
                if ("".equals(teacher)) {
                    save_activity.setError("");
                    return;
                }

                //创建一个数据库对象
                DBHelper dbHelper = new DBHelper(DialogModal.this, DB_NAME, null, 1);

                //把数据存在contentValues中
                ContentValues contentValues = new ContentValues();
                //combineId()方法目的是通过星期几和第几节课算出对应该课程id
                contentValues.put("c_id", combineId(day, time));
                contentValues.put("c_name", text);
                contentValues.put("c_time", time);
                contentValues.put("c_day", day);
                contentValues.put("c_teacher", teacher);

                //更新数据库记录
                update(dbHelper, contentValues);

                //清空栈内所有activity
                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);

                //启动MainActivity
                intent.setClass(DialogModal.this, CourseActivity.class);
                startActivity(intent);
            }
        });
    }

    public String combineId(String day, String time) {

        //星期几转换成int类型
        int day1 = util.getDay(day);

        //如1-2节课只取1
        int time1 = Integer.parseInt(time.substring(0, 1));

        return String.valueOf((day1 - 1) * 5 + ((time1 - 1)/2 + 1));
    }

    public void update(DBHelper dbHelper, ContentValues contentValues){

        String []a = {contentValues.get("c_id").toString()};

        // 通过DBHelper类获取一个读写的SQLiteDatabase对象
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        db.update(TABLE_NAME,contentValues,"c_id=?", a);

        // 释放连接
        db.close();
    }

}
