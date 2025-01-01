package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {
    private static final String TAG="MenuActivity";
    private int lostState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent intent=getIntent();
        Button loss = (Button) findViewById(R.id.loss_button);
        String userName=intent.getStringExtra("Name");
        Log.v(TAG,userName);
        lostState=MySharedPreference.getIntValue(MenuActivity.this,"state");
        if (lostState==1){
            loss.setText("解挂");
        }
        else{
            loss.setText("挂失");
        }

        Button query=(Button)findViewById(R.id.query_button);
        Log.v(TAG,"queryButton");
        query.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder  dialog=new AlertDialog.Builder(MenuActivity.this);
                String message="用户名:";
                message+=userName;
                message+='\n';
                message+="挂失状态:";
                if (lostState==1){
                    message+="挂失";
                }
                else{
                    message+="未挂失";
                }
                dialog.setTitle("查询结果");
                dialog.setMessage(message);
                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();

            }
        });
        //挂失、解挂按键
        loss.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                lostState=MySharedPreference.getIntValue(MenuActivity.this,"state");
                Log.v(TAG, new String(String.valueOf(lostState)));
                if (lostState==1){
                    lostState=0;
                    loss.setText("挂失");
                    Toast.makeText(MenuActivity.this, "解挂成功！", Toast.LENGTH_LONG).show();
                }
                else if (lostState==0){
                    lostState=1;
                    loss.setText("解挂");
                    Toast.makeText(MenuActivity.this, "挂失成功！", Toast.LENGTH_LONG).show();
                }
                MySharedPreference.setLossState(MenuActivity.this,lostState);
            }
        });


        Button jwc=(Button)findViewById(R.id.jwc_button);
        jwc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://jwxt.ujs.edu.cn"));
                startActivity(intent);
            }
        });


        Button course=(Button)findViewById(R.id.course_button);
        course.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, CourseActivity.class);
                startActivity(intent);
            }
        });

    }
}