package com.jinsen.xuexibao.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.test.mock.MockApplication;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.jinsen.xuexibao.AppData;
import com.jinsen.xuexibao.R;

import java.util.HashMap;
import java.util.Map;

public class FilterActivity extends Activity {

    public static final String TAG = "FilterActivity";
    //UI references
    private EditText mSchool;
    private EditText mTeacher;
    private EditText mQuestion;
    private Switch mEmergency;
    private boolean isEmerg;
    private boolean isChanged;

    //Global map
    private HashMap globalMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        init();
    }

    private void init() {
        globalMap = ((AppData) this.getApplication()).getHashmap(TAG);

        mSchool = ((EditText) findViewById(R.id.school_filter));
        mTeacher = ((EditText) findViewById(R.id.teacher_filter));
        mQuestion = ((EditText) findViewById(R.id.questionId_filter));
        mEmergency = ((Switch) findViewById(R.id.emerg_filter));

        mEmergency.setChecked(false);
        mEmergency.setTextOff("全部");
        mEmergency.setTextOn("是");
        isChanged = false;
        mEmergency.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isEmerg = true;
                    isChanged = true;
                }else {
                    mEmergency.setTextOff("否");
                    mEmergency.postInvalidate();
                    mEmergency.invalidate();
                    isEmerg = false;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filter, menu);
//        menu.getItem(R.id.action_ok);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_ok:
                startActivity(sendToMain());
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Intent sendToMain() {
        String school = mSchool.getText().toString();
        String teacher = mTeacher.getText().toString();
        String questionId = mQuestion.getText().toString();
        Map<String, String> params = new HashMap<String, String>();
        params.put("school",school);
        params.put("teacher",teacher);
        params.put("status","0");
        if (isEmerg) params.put("emergencyStatus","1");
        if ((isEmerg == false) && isChanged) params.put("emergencyStatus","0");
        globalMap.put("audioSearchParams", params);

        Intent intent = new Intent(FilterActivity.this, MainActivity.class);
        return intent;
    }
}
