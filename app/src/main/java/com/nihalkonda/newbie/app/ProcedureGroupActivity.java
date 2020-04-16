package com.nihalkonda.newbie.app;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.nihalkonda.newbie.R;

public class ProcedureGroupActivity extends AppCompatActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.webView)
    WebView webView;
    private final String INIT_URL="https://newbie.nihalkonda.com/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procedure_group);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        title.setTypeface(Typeface.createFromAsset(getAssets(),
                "kellyslab.ttf"));
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
                    webView.goBack();
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