package com.posco.samplepush;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;



public class MainActivity extends AppCompatActivity {


    private static final String TAG = "aaa";
    TextView textView;
    TextView textView2;

    NotificationManager manager;

    private static String CHANNEL_ID2 = "channel2";
    private static String CHANNEL_NAME2 = "Channel2";

    private WebView webView;

    String from = null;
    String contents = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main2);


        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {


                            Log.w("Main", "토큰 가져오는 데 실패함", task.getException());
                            return;
                        }

                        String newToken = task.getResult();
                        println("등록 id : " + newToken);

                        Log.d(TAG, "Id 확인");//등록 id 확인
                    }
                });

        /*Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        println("토큰 실패");
                        return;
                    }

                    String token = task.getResult();
                    println("확인된 인스턴스 id : " + token);
                });
                String instanceId = FirebaseInstanceId.getInstance().getId();
                println("확인된 인스턴스 id : " + instanceId);
            }
        });*/

        setWebView();//메소드 호출!

    }


    //웹뷰
    public void setWebView(){

        webView = (WebView) findViewById(R.id.webView);

        //webView.setWebViewClient(new WebViewClient());  // 새 창 띄우기 않기
        webView.setWebChromeClient(new WebChromeClient());
        //webView.setDownloadListener(new DownloadListener(){...});  // 파일 다운로드 설정

        webView.getSettings().setLoadWithOverviewMode(true);  // WebView 화면크기에 맞추도록 설정 - setUseWideViewPort 와 같이 써야함
        webView.getSettings().setUseWideViewPort(true);  // wide viewport 설정 - setLoadWithOverviewMode 와 같이 써야함

        webView.getSettings().setSupportZoom(false);  // 줌 설정 여부
        webView.getSettings().setBuiltInZoomControls(false);  // 줌 확대/축소 버튼 여부

        webView.getSettings().setJavaScriptEnabled(true); // 자바스크립트 사용여부
//        webview.addJavascriptInterface(new AndroidBridge(), "android");
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); // javascript가 window.open()을 사용할 수 있도록 설정
        webView.getSettings().setSupportMultipleWindows(true); // 멀티 윈도우 사용 여부

        webView.getSettings().setDomStorageEnabled(true);  // 로컬 스토리지 (localStorage) 사용여부


        //웹페이지 호출
        webView.loadUrl("http://203.228.62.19");

    }






    public void println(String data) {
        //textView2.append(data + "\n");
        Log.d("FMS", data);
    }




    @Override
    protected void onNewIntent(Intent intent) {
        println("onNewIntent 호출됨");
        if (intent != null) {
            processIntent(intent);

        }

        super.onNewIntent(intent);
    }




    private void processIntent(Intent intent) {
        from = intent.getStringExtra("from");
        if (from == null) {
            println("from is null.");

            return;
        }

        contents = intent.getStringExtra("contents");
        println("DATA : " + from + ", " + contents);
        textView.setText("[" + from + "]로부터 수신한 데이터 : " + contents);




    }




    //뒤로 가기
    @Override
    public void onBackPressed() {

        Log.i(TAG, "BackButtonPressed");
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed(); //종료시키는 것
        }

    }


}
