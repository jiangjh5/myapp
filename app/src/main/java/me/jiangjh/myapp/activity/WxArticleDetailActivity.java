package me.jiangjh.myapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import me.jiangjh.myapp.BaseActivity;
import me.jiangjh.myapp.R;

/**
 * Created by Administrator on 2017/10/24.
 */

public class WxArticleDetailActivity extends BaseActivity {

    public static final String URL = "url";

    WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        initView();
        loadPage();
    }


    private void initView() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mWebView = (WebView) findViewById(R.id.webView);
        initWebView(mWebView);
    }

    private void initWebView(WebView webView) {
        WebSettings ws = webView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        ws.setJavaScriptEnabled(true);
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可

        //设置自适应屏幕，两者合用
        ws.setUseWideViewPort(true); //将图片调整到适合webview的大小
        ws.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        ws.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        ws.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        ws.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        ws.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        ws.setAllowFileAccess(true); //设置可以访问文件
        ws.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        ws.setLoadsImagesAutomatically(true); //支持自动加载图片
        ws.setDefaultTextEncodingName("utf-8");//设置编码格式

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                WxArticleDetailActivity.this.setTitle(title);
            }
        });
    }

    private void loadPage() {
        Intent intent = getIntent();
        String url = intent.getStringExtra(URL);
        loadUrl(url);
    }

    private void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadUrl(null);
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView = null;
        }
        super.onDestroy();
    }
}
