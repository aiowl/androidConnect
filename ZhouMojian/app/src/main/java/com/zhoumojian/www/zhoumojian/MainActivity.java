package com.zhoumojian.www.zhoumojian;

import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;



public class MainActivity extends ActionBarActivity {

    private Button open;
    private EditText url;
    private TextView message;
    String url2 = "http://wpa.qq.com/msgrd?v=1&uin=343605167&site=qq&menu=yes";
    String url3 = "http://www.baidu.com";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String m = (String)msg.obj;
            message.setText(m);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        open = (Button)findViewById(R.id.open);
        url = (EditText)findViewById(R.id.url);
        message = (TextView)findViewById(R.id.message);

        open.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();//调用连网方法

            }
        });
    }

    private void connect() {
        new Thread() {
            public void  run() {
                try {
                    HttpClient client = new DefaultHttpClient();
                    //params[0]代表连接的url
                    HttpGet get = new HttpGet(url3);
                    HttpResponse response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    long length = entity.getContentLength();
                    InputStream is = entity.getContent();
                    String s = null;
                    if(is != null) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        byte[] buf = new byte[128];
                        int ch = -1;
                        int count = 0;
                        while ((ch = is.read(buf)) != -1 ) {
                            byteArrayOutputStream.write(buf, 0, ch);
                            count += ch;
                        }
                        s = new String(byteArrayOutputStream.toByteArray());
                        Message msg = Message.obtain();
                        msg.obj = s;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
