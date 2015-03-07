package com.jinsen.xuexibao.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewFragment;
import android.widget.TextView;

import com.jinsen.xuexibao.R;

import java.util.HashMap;
import java.util.Map;

public class WebviewFragment extends Fragment {

    private static final String AUDIO_URL = "audioUrl";
    private static final String COOKIE = "cookie";
//    private String audioUrl;
    private WebView mWebView;
//    private Map headers;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
        public static WebviewFragment newInstance(String audioUrl, String cookie) {
            WebviewFragment fragment = new WebviewFragment();
            Bundle args = new Bundle();
            args.putString(AUDIO_URL, audioUrl);
            args.putString(COOKIE, cookie);
            fragment.setArguments(args);
//            this.headers = headers;
            return fragment;
        }

//    public WebviewFragment(String audioUrl, Map headers) {
//        this.audioUrl = audioUrl;
//        this.headers = headers;
//    }

    public WebviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //These codes are shit
        //Need to be modified some day
        Map headers = new HashMap<String, String>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
        headers.put("Host", "ops.xuexibao.cn");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headers.put("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
        headers.put("Connection", "keep-alive");
//        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Cookie", getArguments().get(COOKIE));

        mWebView = ((WebView) getActivity().findViewById(R.id.webview));
        String questionUrl = getArguments().getString(AUDIO_URL);

        mWebView.loadUrl(questionUrl, headers);


    }
}
