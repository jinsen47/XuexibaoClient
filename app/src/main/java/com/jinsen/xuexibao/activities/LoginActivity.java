package com.jinsen.xuexibao.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.test.mock.MockApplication;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.jinsen.xuexibao.AppData;
import com.jinsen.xuexibao.R;
import com.jinsen.xuexibao.utils.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    public static final String TAG = "LoginActivity";

    //全局变量
    private HashMap globalMap;
    private Map<String, String>headers;

    //默认密码
    private static final String PASSWORD = "1234";



    // UI references.
    private TextView mUserNameView;
    private EditText mPasswordView;
    private View mProgressView;
    private ImageView mVerimgView;
    private EditText mVericodeView;
    private Button mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化UI
        init();

    }

    @Override
    protected void onPause() {
        super.onPause();
        RequestManager.cancelAll(TAG);
    }

    private void init() {
        globalMap = ((AppData) this.getApplication()).getHashmap(TAG);
        //Get Cookie
        getCookie();
        headers = ((AppData) this.getApplication()).getHeaders();

        // Set up the login form.
        mUserNameView = (EditText) findViewById(R.id.userName);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setText(PASSWORD);


        mVericodeView = (EditText) findViewById(R.id.veri_code);
        mVericodeView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.veri_code || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mVerimgView = (ImageView) findViewById(R.id.veri_img);
        mVerimgView.setImageDrawable(new ColorDrawable(Color.argb(255, 201, 201, 201)));
//        requestCodeImg();
        mVerimgView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCodeImg();
            }
        });

        mLoginBtn = (Button) findViewById(R.id.sign_in_button);
        mLoginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {

        // 登陆按钮不可选
        mLoginBtn.setEnabled(false);

        // Store values at the time of the login attempt.
        final String userName = mUserNameView.getText().toString();
        final String password = mPasswordView.getText().toString();
        final String vcode = mVericodeView.getText().toString();

        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        showProgress(true);
        final String loginUrl = (String) globalMap.get("loginUrl");
        StringRequest indexRequest = new StringRequest(Request.Method.POST, loginUrl,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i(TAG, "LoginRequest");
                Log.v(TAG, s);
                if (s.contains("reset")) {
                    mPasswordView.setText("");
                    requestCodeImg();
                    mProgressView.setVisibility(View.GONE);
                    mLoginBtn.setEnabled(true);
                }else {
                    Log.i(TAG, "LoginSuccess");
                    startActivity(new Intent(LoginActivity.this, FilterActivity.class));
                }
            }
        },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, "LoginError:" + volleyError.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap();
                params.put("loginId", userName);
                params.put("password", password);
                params.put("vcode",vcode);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String cookie = ((String) globalMap.get("Cookie"));
                headers.put("Cookie",cookie);
                return headers;
            }
        };
        RequestManager.addRequest(indexRequest, TAG);
    }
    private void getCookie() {
        String url = "http://ops.xuexibao.cn/report3/login.jsp";
        StringRequest cookieRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i(TAG, "getCookie:Response");
                        requestCodeImg();
                        return;
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, "getCookieError:" + volleyError.toString());
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Map headerMap = response.headers;
                String cookies = ((String) headerMap.get("Set-Cookie"));

                //提取Cookie
                cookies = cookies.substring(0,43);
                Log.w("LOG","cookie substring "+ cookies);
                globalMap.put("Cookie", cookies);
                return super.parseNetworkResponse(response);
            }
        };
        RequestManager.addRequest(cookieRequest, TAG);
    }

    private void requestCodeImg() {
        String veriImgUrl = ((String) globalMap.get("veriImgUrl"));
        ImageRequest veriImg = new ImageRequest(veriImgUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        mVerimgView.setImageBitmap(bitmap);
                    }
                },0,0,null,
                new Response.ErrorListener () {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        mVerimgView.setImageDrawable(new ColorDrawable(Color.argb(255, 201, 201, 201)));
                        Log.e(TAG, "requestCodeImgError:" + volleyError.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String cookie = ((String) globalMap.get("Cookie"));
                Log.d(TAG, "Cookie:" + cookie);
                headers.put("Cookie", cookie);
                return headers;
            }
        };
        RequestManager.addRequest(veriImg, TAG);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

}



