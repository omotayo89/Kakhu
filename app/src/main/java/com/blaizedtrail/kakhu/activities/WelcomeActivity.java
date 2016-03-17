package com.blaizedtrail.kakhu.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.blaizedtrail.kakhu.R;

public class WelcomeActivity extends AppCompatActivity implements TextWatcher {
    private FloatingActionButton fab;
    private EditText editText;
    private static boolean isProceed=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        instantiateVariables();
    }

    private void instantiateVariables(){
        fab=(FloatingActionButton)findViewById(R.id.fab);
        editText=(EditText)findViewById(R.id.edit_text);
        editText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(editText.getText())){
            fab.setImageResource(R.drawable.check);
        }else {
            fab.setImageResource(R.drawable.chevron_up);
        }
    }


    public void registerInstead(View view) {
        Intent intent=new Intent(this,RegisterActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.abc_slide_in_bottom,R.anim.abc_slide_out_top);
    }
}
