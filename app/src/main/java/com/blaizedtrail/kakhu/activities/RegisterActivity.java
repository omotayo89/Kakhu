package com.blaizedtrail.kakhu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blaizedtrail.kakhu.R;
import com.blaizedtrail.kakhu.utils.Company;
import com.blaizedtrail.kakhu.utils.CompanyHandler;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.regex.Pattern;

@SuppressWarnings("PrivateResource")

public class RegisterActivity extends AppCompatActivity implements TextWatcher{
    private MaterialEditText companyNameText;
    private static final String DEBUG_TAG = RegisterActivity.class.getSimpleName();
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab=(FloatingActionButton)findViewById(R.id.fab);
        Company company= CompanyHandler.get().getCompany();
        companyNameText=(MaterialEditText)findViewById(R.id.company_name);
        if (company!=null) {
            Log.d(DEBUG_TAG, "Company name: " + company.getCompany_name());
        }else {
            Log.d(DEBUG_TAG,"Company is null");
        }
        LinearLayout view=(LinearLayout)findViewById(R.id.linearLayout);
        for (int i=0;i< view.getChildCount();i++) {
            View child = view.getChildAt(i);
            if (child instanceof MaterialEditText) {
                ((MaterialEditText) child).addTextChangedListener(this);
            }
        }
    }

    public void loginInstead(View view) {
        if(!checkCompleteness()){
            Intent intent=new Intent(this,WelcomeActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
        }else {
            register(null);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,WelcomeActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
    }
    public void register(View view){
        String companyName=companyNameText.getText().toString();
        String companyEmail=((MaterialEditText)findViewById(R.id.company_email)).getText().toString();
        String companyAddress=((MaterialEditText)findViewById(R.id.company_address)).getText().toString();
        String companyBranch=((MaterialEditText)findViewById(R.id.company_branch)).getText().toString();
        if (TextUtils.isEmpty(companyName)){
            sendError(R.id.company_name,"Company name required");
            return;
        }
        if (TextUtils.isEmpty(companyEmail)){
            sendError(R.id.company_email,"Email required");
            return;
        }
        if (TextUtils.isEmpty(companyAddress)){
            sendError(R.id.company_address,"Address field is empty");
            return;
        }
        if (TextUtils.isEmpty(companyBranch)){
            sendError(R.id.company_branch,"What branch of your company are you registering?");
            return;
        }
        Company company=new Company();
        company.setCompany_name(companyName);
        company.setCompany_email(companyEmail);
        company.setCompany_address(companyAddress);
        company.setCompany_branch(companyBranch);
        company.save();
        Intent intent=new Intent(this,AgentInfo.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.abc_slide_in_bottom,R.anim.abc_slide_out_top);
    }


    private void sendError(int id,String error){
        Snackbar.make(findViewById(R.id.coordinatorLayout),error,Snackbar.LENGTH_SHORT).show();
        YoYo.with(Techniques.Shake)
                .playOn(findViewById(id));
        ((MaterialEditText)findViewById(id)).setError(error);
    }

    private boolean checkCompleteness(){
        LinearLayout view=(LinearLayout)findViewById(R.id.linearLayout);
        for (int i=0;i< view.getChildCount();i++){
            View child= view.getChildAt(i);
            if (child instanceof MaterialEditText){
                ((MaterialEditText) child).setError(null);
                if (TextUtils.isEmpty(((MaterialEditText) child).getText())) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        boolean completeness=checkCompleteness();
        if (completeness){
            fab.setImageResource(R.drawable.check);
        }else {
            fab.setImageResource(R.drawable.chevron_down);
        }
    }
}
