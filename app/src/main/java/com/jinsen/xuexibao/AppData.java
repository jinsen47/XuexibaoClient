package com.jinsen.xuexibao;

import android.app.Application;
import android.content.Context;

import com.jinsen.xuexibao.activities.LoginActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jinsen on 2015/2/12.
 */
public class AppData extends Application {
    private static Context sCxt;
    private HashMap<String, Object> sHashmap;

    //一些固定的url
    private static final String veriImgUrl = "http://ops.xuexibao.cn/report3/verifyCodeManager/validationCode.do";
    private static final String loginUrl = "http://ops.xuexibao.cn/report3/servlet/LoginServlet";
    private static final String audioSearchUrl = "http://ops.xuexibao.cn/report3/audio/audioSearch.do";
    private static Map<String, String>headers;


    @Override
    public void onCreate() {
        super.onCreate();
        sCxt = getApplicationContext();
        init();

    }

    public static Context getsCxt() {
        return sCxt;
    }

    public HashMap<String, Object> getHashmap(String tag) {
        Object tmp = sHashmap.get(tag);
        if (tmp == null) {
            return sHashmap;
        }else {
            return (HashMap) tmp;
        }
    }
    public Map<String, String> getHeaders() {return headers;}

    private void init() {
        sHashmap = new HashMap<String, Object>();

        //LoginActivity
        sHashmap.put("veriImgUrl", veriImgUrl);
        sHashmap.put("loginUrl", loginUrl);

        //FilterActivity
        sHashmap.put("audioSearchUrl", audioSearchUrl);

        //创建包头
        headers = new HashMap<String, String>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
        headers.put("Host", "ops.xuexibao.cn");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headers.put("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
        headers.put("Connection", "keep-alive");
//        headers.put("Accept-Encoding", "gzip, deflate");
        sHashmap.put("headers", headers);
    }
}
