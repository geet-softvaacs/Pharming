package com.onetick.pharmafest.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.onetick.pharmafest.R;
import com.onetick.pharmafest.databinding.ActivityContactBinding;
import com.onetick.pharmafest.utils.SessionManager;

public class ContactActivity extends AppCompatActivity {
    ActivityContactBinding binding;
    SessionManager sessionManager;
    TextView tatDeception;
    String url= "https://pharmingo.com/site/contact-us-v1";
    ProgressBar progressBar;
    WebView webView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_contact);
//        tatDeception = binding.txtDscirtion;
//        sessionManager = new SessionManager(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            tatDeception.setText(Html.fromHtml(sessionManager.getStringData("contact"), Html.FROM_HTML_MODE_LEGACY));
//        } else {
//            tatDeception.setText(Html.fromHtml(sessionManager.getStringData("contact")));
//        }
//
//        binding.imgBack.setOnClickListener(view -> {
//            finish();
//        });

        webView = binding.webview;
        progressBar = binding.progressbar;
        binding.imgBack.setOnClickListener(v -> {
            finish();
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setAllowFileAccess(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //Make the bar disappear after URL is loaded, and changes string to Loading...
                setTitle("Loading...");
                progressBar.setProgress(progress);//Make the bar disappear after URL is loaded
                setProgress(progress * 100);

                // Return the app name after finish loading
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                    binding.webview.setVisibility(View.VISIBLE);
                }

            }
        });

        webView.loadUrl(url);

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        //show the web page in webview but not in web browser
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl (url);
            return true;
        }
    }
}