package com.nihalkonda.newbie.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nihalkonda.newbie.R;

public class ProcedureGroupActivity extends AppCompatActivity {
    WebView webView;
    private final String INIT_URL="https://newbie.nihalkonda.com/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        setContentView(webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                System.out.println(url);
                super.onPageFinished(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                if(consoleMessage.message().startsWith("MROXJSONMROX")){
                    Intent i = new Intent(ProcedureGroupActivity.this,ProcessInstructionsActivity.class);
                    i.putExtra(ProcessInstructionsActivity.EXTRA_PROCESS_ENCODED,consoleMessage.message().replace("MROXJSONMROX ",""));
                    startActivity(i);
                }
                return super.onConsoleMessage(consoleMessage);
            }
        });
        webView.loadUrl(INIT_URL);

    }

    @Override
    public void onBackPressed() {
        if(webView.getUrl().equals(INIT_URL))
            super.onBackPressed();
        else
            webView.goBack();
    }
}