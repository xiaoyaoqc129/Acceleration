package com.example.qc.acceleration;

import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


public class MainActivity extends Activity implements SensorEventListener , View.OnClickListener {

    private SensorManager mSensorManager;

    private Sensor maccelerometer; // 加速度传感器

    private float[] accelerometerValues = new float[3];

    private Boolean doWrite = false;


    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 实例化传感器管理者
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // 初始化加速度传感器
        maccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // 初始化地磁传感器
        mTextView = (TextView) findViewById(R.id.sample_text);
        startbutton = (Button) findViewById(R.id.button);
        startbutton.setOnClickListener(this);
        stopbutton = (Button) findViewById(R.id.button2);
        stopbutton.setOnClickListener(this);
        //初始化数组
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerValues = event.values.clone();
            mTextView.setText(accelerometerValues[0]+"\n"+accelerometerValues[1]+"\n"+accelerometerValues[2]);
        }

            if (doWrite) {
                String message = new String();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
                String str = sdf.format(new Date());
                message = str+ " "+ accelerometerValues[0]+" "+accelerometerValues[1]+" "+accelerometerValues[2]+"\n";
                writeFileSdcard(message);
            }
        }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }


    private void writeFileSdcard(String message) {
        try {
            // 如果手机插入了SD卡，而且应用程序具有访问SD的权限
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // 获取SD卡的目录
                File sdCardDir = Environment.getExternalStorageDirectory();
                File targitFile = new File(sdCardDir.getCanonicalPath() + "/acc/SensorData" +  ".txt");
                // 以指定文件创建 RandomAccessFile对象
                RandomAccessFile raf = new RandomAccessFile(targitFile, "rw");
                // 将文件记录指针移动到最后
                raf.seek(targitFile.length());
                // 输出文件内容
                raf.write(message.getBytes());
                // 关闭RandomAccessFile
                raf.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            doWrite = true;
        }
        if (v.getId() == R.id.button2) {
            doWrite = false;
        }
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        // 注册监听
        mSensorManager.registerListener(this, maccelerometer, Sensor.TYPE_ACCELEROMETER);
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        // 解除注册
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

}