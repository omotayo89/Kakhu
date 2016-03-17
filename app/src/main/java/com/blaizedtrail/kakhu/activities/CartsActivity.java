package com.blaizedtrail.kakhu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.blaizedtrail.kakhu.R;
import com.blaizedtrail.kakhu.adapters.CartAdapter;
import com.blaizedtrail.kakhu.utils.CartItem;
import com.blaizedtrail.kakhu.utils.Company;
import com.blaizedtrail.kakhu.utils.CompanyHandler;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class CartsActivity extends AppCompatActivity implements Action1<CartItem>,CartAdapter.CartClickCallback{
    private static final String DEBUG_TAG=CartsActivity.class.getSimpleName();
    private CartAdapter cartAdapter;
    private Company company;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        instantiateVariables();
    }
    private void instantiateVariables(){
        company= CompanyHandler.get().getCompany();
        cartAdapter=new CartAdapter();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5, GridLayoutManager.VERTICAL));
        recyclerView.setAdapter(cartAdapter);
        cartAdapter.setCartClickCallback(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadCart();
    }

    private void reloadCart(){
        cartAdapter.clearCart();
        Observable<CartItem>cartItemObservable=Observable.create(new Observable.OnSubscribe<CartItem>() {
            @Override
            public void call(Subscriber<? super CartItem> subscriber) {
                List<CartItem>cartItems=company.getCart();
                for (CartItem cartItem:cartItems){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    subscriber.onNext(cartItem);
                }
            }
        });
        cartItemObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    @Override
    public void call(CartItem cartItem) {
        Log.d(DEBUG_TAG,"Cart item: "+cartItem.getBuyerName());
        cartAdapter.addToCart(cartItem);
    }

    public void newCart(View view){
        Intent intent=new Intent(this,NewCartActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.abc_slide_in_bottom,R.anim.abc_slide_out_top);
    }

    @Override
    public void cartClicked(CartItem cartItem) {
        Intent intent=new Intent(this,ViewCart.class);
        intent.putExtra(ViewCart.EXTRA_CART_ID,cartItem.getId());
        startActivity(intent);
        overridePendingTransition(R.anim.activity_slide_in_right,R.anim.activity_slide_out_left);
    }
}
