package com.example.wxs20;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wxs20.bean.DayWeatherBean;
import com.example.wxs20.bean.WeatherBean;
import com.example.wxs20.utils.NetworkUtil;
import com.google.gson.Gson;


public class WeatherActivity extends AppCompatActivity {

    TextView tvTime,tvWeather,tvTemLowHigh,tvWin,tvAir;

    private DayWeatherBean dayWeather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        tvTime = (TextView)findViewById(R.id.date_val);
        tvWeather = (TextView)findViewById(R.id.weather_val);
        tvTemLowHigh = (TextView)findViewById(R.id.temperature_val);
        tvWin = (TextView)findViewById(R.id.wind_val);
        tvAir = (TextView)findViewById(R.id.airquality_rank);
        getWeather();

    }


    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String weather = (String) msg.obj;
                Log.d("Main", ">>>>>>主线程收到了天气数据>>>" + weather);
                if (TextUtils.isEmpty(weather)) {
                    Toast.makeText(WeatherActivity.this, "天气数据为空！", Toast.LENGTH_LONG).show();
                    return;
                }
                Gson gson = new Gson();
                WeatherBean weatherBean = gson.fromJson(weather, WeatherBean.class);
                if (weatherBean != null) {
                    Log.d("Main", ">>>>>>解析后的数据>>>" + weatherBean.toString());
                }

                tvTime.setText(weatherBean.getUpdate_time());
                /**
                 * 当天天气
                 */
                dayWeather = weatherBean.getData().get(0);
                tvWeather.setText(dayWeather.getWea());
                tvTemLowHigh.setText(dayWeather.getTem2()+"/"+dayWeather.getTem1());
                tvWin.setText(dayWeather.getWin()[0]+dayWeather.getWin_speed());
                tvAir.setText("空气:"+dayWeather.getAir()+" | "+dayWeather.getAir_level()+"\n"+dayWeather.getAir_tips());
            }

        }
    };

    private void getWeather() {
        // 开启子线程，请求网络
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 请求网络
                String weatherJson = NetworkUtil.getWeather();
                // 使用handler将数据传递给主线程
                Message message = Message.obtain();
                message.what = 0;
                message.obj = weatherJson;
                mHandler.sendMessage(message);

            }
        }).start();

    }

}