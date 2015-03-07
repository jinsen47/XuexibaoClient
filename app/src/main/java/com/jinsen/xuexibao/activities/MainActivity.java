package com.jinsen.xuexibao.activities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.test.mock.MockApplication;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jinsen.xuexibao.AppData;
import com.jinsen.xuexibao.R;
import com.jinsen.xuexibao.fragment.HeaderFragment;
import com.jinsen.xuexibao.fragment.WebviewFragment;
import com.jinsen.xuexibao.types.Question;
import com.jinsen.xuexibao.types.QuestionPage;
import com.jinsen.xuexibao.utils.CNStringRequest;
import com.jinsen.xuexibao.utils.RequestManager;

public class MainActivity extends Activity implements HeaderFragment.OnChoose {

    public static final String TAG = "MainActivity";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

//    RadioButton mGoodBtn;
//    RadioButton mFineBtn;
//    RadioButton mBadBtn;
//    Spinner mSpinner;

    //Global Data
    private Map globalMap;
    private String submitString;

    //QuestionQueue
    private QuestionPage questionList;

    //SubmitList
    private List<Question> submitList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        globalMap = ((AppData) getApplication()).getHashmap(TAG);
        setContentView(R.layout.activity_main);

        Map filterMap = ((HashMap) globalMap.get("audioSearchParams"));
        submitString = ((String) globalMap.get("audioSearchUrl"));
        submitString = submitString + this.toSubmitString(filterMap);
        final Map headerMap = ((HashMap) globalMap.get("headers"));
        Log.d(TAG, submitString);

        CNStringRequest questionPageRequest = new CNStringRequest(Request.Method.GET, submitString, headerMap, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d(TAG,s);
                questionList = new QuestionPage(s);
                // Create the adapter that will return a fragment for each of the three
                // primary sections of the activity.
                mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), ((Map) globalMap.get("headers")),questionList);
                // Set up the ViewPager with the sections adapter.
                mViewPager = (ViewPager) findViewById(R.id.pager);
                mViewPager.setAdapter(mSectionsPagerAdapter);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.toString());
            }
        }
        );

        RequestManager.addRequest(questionPageRequest, TAG);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChoose(String submit) {

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private Map headers;
        private QuestionPage qp;
        public SectionsPagerAdapter(FragmentManager fm, Map headers, QuestionPage qp) {

            super(fm);
            this.headers = headers;
            this.qp = qp;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return WebviewFragment.newInstance(qp.getQuestion().audioLinkUrl, ((String) headers.get("Cookie")));
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }




    private String toSubmitString(Map map) {
        StringBuffer sb = new StringBuffer();
        sb.append("?");
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = ((Map.Entry) iter.next());
            String key = ((String) entry.getKey());
            String value = ((String) entry.getValue());
            sb.append(key);
            sb.append("=");
            if (value != null) sb.append(value);
            sb.append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

}
