package com.example.app;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
    private static EditText etUid;                  //接收用户id组件
    private static EditText etPwd;                  //接收用户密码组件
    private static CheckBox cb;                     //"记住我"复选框组件
    private static String uidstr;                   //用户帐号
    private static String pwdstr;                   //用户密码
    private int count;								//统计注册人数

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = (Button) findViewById(R.id.btnLogin);
        Button register = (Button) findViewById(R.id.btnReg);

        etUid=(EditText) findViewById(R.id.etUid);       //获得帐号EditText
        etPwd=(EditText) findViewById(R.id.etPwd);       //获得密码EditText


        cb=(CheckBox) findViewById(R.id.cbRemember);     //获得CheckBox对象

        checkIfRemember();             //从SharedPreferences中读取用户的帐号和密码

        count = MySharedPreference.getIntValue(LoginActivity.this, "count");


        //登录
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String strUsername = etUid.getText().toString();
                String strPassword = etPwd.getText().toString();
                String defaultPass = MySharedPreference.getStringValue(LoginActivity.this, strUsername);
                if(strUsername.equals("")||strPassword.equals("")){
                    Toast.makeText(LoginActivity.this, "用户名或密码为空！", Toast.LENGTH_LONG).show();
                }
                if(defaultPass.equals(strPassword)) {
                    //匹配成功登录跳转
                    if(cb.isChecked()) {
                        for(int i=1;i<=count;i++) {
                            //为了实现”记住我“功能，这里i从1开始
                            String name = String.valueOf(i-1);
                            name = MySharedPreference.getStringValue(LoginActivity.this, name);

                            if(name.equals(strUsername)) {
                                //如果勾选了”记住我“，而且用户名存在，则标记此时需要记住的用户
                                MySharedPreference.saveRemember(LoginActivity.this, i);
                            }
                        }
                    }
                    else {
                        //如果没有勾选”记住我“。那么将标志置为0
                        MySharedPreference.saveRemember(LoginActivity.this, 0);
                    }
                    //跳转到菜单
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    intent.putExtra("Name",strUsername);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, "用户名和密码不匹配！", Toast.LENGTH_LONG).show();
                    etPwd.setText("");
                }
            }
        });

        //注册
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String strUsername = etUid.getText().toString();
                String strPassword = etPwd.getText().toString();
                boolean isNameRepeat = false;
                if(!strUsername.equals("") && !strPassword.equals("")) {
                    //用户名和密码不能为空
                    String[] names = new String[10];
                    if(count > 0) {
                        for(int i = 0;i < count;i++){
                            //检测用户名是否已被注册
                            names[i] = String.valueOf(i);
                            String name = MySharedPreference.getStringValue(LoginActivity.this, names[i]);
                            if(name.equals(strUsername)) {
                                //如果用户名已存在
                                Toast.makeText(LoginActivity.this, "当前用户已被注册！", Toast.LENGTH_LONG).show();
                                isNameRepeat = true;
                                break;
                            }
                        }
                    }
                    //如果用户名还没有注册
                    if(!isNameRepeat) {
                        //键值对：<用户名，密码>
                        MySharedPreference.register(LoginActivity.this, strUsername, strPassword);
                        names[count] = String.valueOf(count);
                        //键值对：<用户号count，用户名>
                        MySharedPreference.saveNames(LoginActivity.this, names[count], strUsername);
                        count = count + 1;
                        //键值对：<count，当前注册人数>
                        MySharedPreference.saveCount(LoginActivity.this, "count", count);

                        Toast.makeText(LoginActivity.this, "注册成功！", Toast.LENGTH_LONG).show();
                    }
                }
                else
                    Toast.makeText(LoginActivity.this, "用户名或密码为空！", Toast.LENGTH_LONG).show();
            }
        });


    }

    //方法：从SharedPreferences中读取用户的帐号和密码
    public void checkIfRemember(){
        int i = MySharedPreference.getFlag(LoginActivity.this, "flag");
        //如果最近登录的用户勾选了“记住我”，则自动填充用户名和密码
        if(i!=0) {
            String flag=String.valueOf(i-1);
            uidstr=MySharedPreference.getStringValue(LoginActivity.this, flag);
            pwdstr=MySharedPreference.getStringValue(LoginActivity.this, uidstr);
            etUid.setText(uidstr);
            etPwd.setText(pwdstr);
            cb.setChecked(true);
        }

    }
}
