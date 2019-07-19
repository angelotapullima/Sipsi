package com.bufeotec.sipcsi.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.bufeotec.sipcsi.R;

public class PartePDF extends AppCompatActivity {

    WebView browser;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parte_pdf);

        browser=(WebView)findViewById(R.id.webviewParte);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setBuiltInZoomControls(true); // Habilita el Zoom
        browser.getSettings().setDisplayZoomControls(false); // Oculta los botones de zoom, haciendo que solo funcione con gestos.
        browser.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        // Cargamos la web
        browser.loadUrl("http://www.guabba.com/accidentestransito/index.php?c=Robo&a=PDF&id=25&key_mobile=123456asdfgh");

        browser.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
                PartePDF.this.setProgress(progress * 1000);

                progressBar.incrementProgressBy(progress);

                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
