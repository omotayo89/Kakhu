package com.blaizedtrail.kakhu.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blaizedtrail.kakhu.R;
import com.blaizedtrail.kakhu.application.App;
import com.blaizedtrail.kakhu.utils.Agent;
import com.blaizedtrail.kakhu.utils.BariolTextView;
import com.blaizedtrail.kakhu.utils.CartItem;
import com.blaizedtrail.kakhu.utils.Company;
import com.blaizedtrail.kakhu.utils.CompanyHandler;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewCartActivity extends AppCompatActivity implements TextWatcher {
    private static final String DEBUG_TAG=NewCartActivity.class.getSimpleName();
    private CartItem cartItem;
    private Agent agent;
    private MaterialEditText buyerNameText;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        instantiateVariables();
    }
    private void instantiateVariables(){
        cartItem=new CartItem();
        buyerNameText=(MaterialEditText)findViewById(R.id.buyerName);
        agent= CompanyHandler.get().getCompany().getAgent();
        ((BariolTextView)findViewById(R.id.agentName)).setText(agent.getAgent_firstName() + " " + agent.getAgent_lastName());
        ((BariolTextView)findViewById(R.id.agentCode)).setText(agent.getAgent_code());
        if (agent.getImage_uri()!=null){
            Picasso.with(this)
                    .load(Uri.parse(agent.getImage_uri()))
                    .skipMemoryCache()
                    .into((CircleImageView) findViewById(R.id.image_view));
        }
        linearLayout=(LinearLayout)findViewById(R.id.linearLayout);
        for (int i=0;i<linearLayout.getChildCount();i++){
            View view=linearLayout.getChildAt(i);
            if (view instanceof MaterialEditText){
                ((MaterialEditText) view).addTextChangedListener(this);
            }
        }
    }
    public void saveCart(View view){
        String buyerName=buyerNameText.getText().toString();
        String address=((MaterialEditText)findViewById(R.id.buyerAddress)).getText().toString();
        String phoneNumber=((MaterialEditText)findViewById(R.id.buyerPhoneNumber)).getText().toString();
        String buyerSignature=((MaterialEditText)findViewById(R.id.buyerSignature)).getText().toString();
        if (TextUtils.isEmpty(buyerName)){
            sendError(R.id.buyerName,"Buyer name is required");
            return;
        }
        if (TextUtils.isEmpty(address)){
            sendError(R.id.buyerAddress,"Buyer address is required");
            return;
        }
        if (TextUtils.isEmpty(phoneNumber)){
            sendError(R.id.buyerPhoneNumber,"Buyer address is needed");
            return;
        }
        if (TextUtils.isEmpty(buyerSignature)){
            sendError(R.id.buyerSignature,"This cart has to be signed by the buyer with a pin");
            return;
        }
        cartItem.setBuyerName(buyerName);
        cartItem.setBuyerAddress(address);
        cartItem.setBuyerPhoneNumber(phoneNumber);
        cartItem.setBuyerSignature(buyerSignature);
        cartItem.setAgent(agent);
        cartItem.associateCompany(CompanyHandler.get().getCompany());
        cartItem.save();
        Toast.makeText(App.getContext(),"Cart created",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,ViewCart.class);
        intent.putExtra(ViewCart.EXTRA_CART_ID,cartItem.getId());
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.activity_slide_in_right,R.anim.activity_slide_out_left);

    }
    private void sendError(int id,String error){
        ((MaterialEditText)findViewById(id)).setError(error);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        for (int i=0;i<linearLayout.getChildCount();i++){
            View view=linearLayout.getChildAt(i);
            if (view instanceof MaterialEditText){
                ((MaterialEditText) view).setError(null);
            }
        }
    }
}
