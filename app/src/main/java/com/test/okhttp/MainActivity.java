package com.test.okhttp;

import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    TextView tvResult;
    Button btnUpload;
    Button btnPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult = (TextView) findViewById(R.id.tv_result);
        btnUpload = (Button) findViewById(R.id.btn_upload);
        btnPost = (Button) findViewById(R.id.btn_post);
        btnUpload.setOnClickListener(this);
        btnPost.setOnClickListener(this);
    }

    private void uploadFiles() {
        String url = "http://dev-icall.telemedicinet.com/icallapi/tfs/3.0.0/savePhoto";
        OkHttpClient client = new OkHttpClient();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);

        String boundary = UUID.randomUUID().toString();
        //构建请求体
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("photo", "test.png", RequestBody.create(MediaType.parse("image/png"), bos.toByteArray()))
                .build();


        //构建请求
        Request request = new Request.Builder()
                .url(url)//地址
                .post(body)//添加请求体
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "onResponse: " + response.body().string());
                final String str = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //tvResult.setText(str);
                    }
                });
            }


        });
    }

    private void test() {
        OkHttpClient client = new OkHttpClient();
        final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain");
        final String postBody = "Hello World";

        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_TEXT,postBody.getBytes());

        Request request = new Request.Builder()
                .url("http://www.baidu.com")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "onResponse: " + response.body().string());
                final String str = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvResult.setText(str);
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_upload:
                uploadFiles();
                break;
            case R.id.btn_post:
                test();
                break;
        }
    }
}
