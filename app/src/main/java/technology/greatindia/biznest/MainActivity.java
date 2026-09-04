package technology.greatindia.biznest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    private static final String HOME_URL =
            "https://biznest.greatindia.technology/";

    private WebView webView;

    private ValueCallback<Uri[]> fileCallback;

    private static final int FILE_CHOOSER_REQUEST = 1001;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        webView = new WebView(this);

        setContentView(webView);


        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setDomStorageEnabled(true);

        webView.getSettings().setAllowFileAccess(true);

        webView.getSettings().setLoadWithOverviewMode(true);

        webView.getSettings().setUseWideViewPort(true);


        CookieManager cookieManager =
                CookieManager.getInstance();

        cookieManager.setAcceptCookie(true);

        CookieManager.getInstance()
                .setAcceptThirdPartyCookies(webView, true);


        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view,
                    WebResourceRequest request
            ) {

                Uri uri = request.getUrl();

                String scheme = uri.getScheme();

                String host = uri.getHost();


                if ("http".equals(scheme)
                        || "https".equals(scheme)) {

                    if (host != null
                            && host.endsWith(
                            "greatindia.technology"
                    )) {

                        view.loadUrl(uri.toString());

                    } else {

                        try {

                            Intent intent =
                                    new Intent(
                                            Intent.ACTION_VIEW,
                                            uri
                                    );

                            startActivity(intent);

                        } catch (Exception ignored) {

                        }

                    }

                    return true;

                }


                if ("tel".equals(scheme)
                        || "mailto".equals(scheme)) {

                    try {

                        Intent intent =
                                new Intent(
                                        Intent.ACTION_VIEW,
                                        uri
                                );

                        startActivity(intent);

                    } catch (Exception ignored) {

                    }

                    return true;

                }

                return false;

            }

        });


        webView.setWebChromeClient(
                new WebChromeClient() {

                    @Override
                    public boolean onShowFileChooser(

                            WebView view,

                            ValueCallback<Uri[]> callback,

                            FileChooserParams params
                    ) {

                        if (fileCallback != null) {

                            fileCallback.onReceiveValue(null);

                        }


                        fileCallback = callback;


                        try {

                            Intent intent =
                                    params.createIntent();

                            startActivityForResult(

                                    intent,

                                    FILE_CHOOSER_REQUEST

                            );

                            return true;

                        } catch (Exception e) {

                            fileCallback = null;

                            return false;

                        }

                    }

                }
        );


        if (savedInstanceState != null) {

            webView.restoreState(savedInstanceState);

        } else {

            webView.loadUrl(HOME_URL);

        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        webView.saveState(outState);

        super.onSaveInstanceState(outState);

    }


    @Override
    protected void onActivityResult(

            int requestCode,

            int resultCode,

            Intent data
    ) {

        super.onActivityResult(

                requestCode,

                resultCode,

                data
        );


        if (requestCode == FILE_CHOOSER_REQUEST
                && fileCallback != null) {

            Uri[] results =
                    WebChromeClient.FileChooserParams
                            .parseResult(
                                    resultCode,
                                    data
                            );

            fileCallback.onReceiveValue(results);

            fileCallback = null;

        }

    }


    @Override
    public void onBackPressed() {

        if (webView.canGoBack()) {

            webView.goBack();

        } else {

            super.onBackPressed();

        }

    }

}
