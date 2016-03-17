package com.blaizedtrail.kakhu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.blaizedtrail.kakhu.R;
import com.blaizedtrail.kakhu.utils.CartItem;
import com.blaizedtrail.kakhu.utils.Company;
import com.blaizedtrail.kakhu.utils.CompanyHandler;

public class ViewCart extends AppCompatActivity {
    private static final String DEBUG_TAG=ViewCart.class.getSimpleName();
    public static final String EXTRA_CART_ID="com.blaizedtrail.kakhu.activities.ViewCart.EXTRA_CART_ID";
    private CartItem cartItem;
    private Company company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        instantiateVariables();
    }
    private void instantiateVariables(){
        company= CompanyHandler.get().getCompany();
        Intent intent=getIntent();
        int cartId=intent.getIntExtra(EXTRA_CART_ID,0);
        cartItem=company.getCartItemById(cartId);
        getSupportActionBar().setTitle(cartItem.getBuyerName() + "'s cart");
    }
    public void selectStock(View view){
        Intent intent=new Intent(this,SelectInventory.class);
        startActivityForResult(intent,0);
        overridePendingTransition(R.anim.abc_slide_in_bottom,R.anim.abc_slide_out_top);
    }

}
