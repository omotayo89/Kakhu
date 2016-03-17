package com.blaizedtrail.kakhu.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.blaizedtrail.kakhu.R;
import com.blaizedtrail.kakhu.adapters.TransactionAdapter;
import com.blaizedtrail.kakhu.utils.Agent;
import com.blaizedtrail.kakhu.utils.CompanyHandler;
import com.blaizedtrail.kakhu.utils.Transaction;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class TransactionActivity extends AppCompatActivity implements Action1<Transaction>{
    private static final String DEBUG_TAG=TransactionActivity.class.getSimpleName();
    private TransactionAdapter transactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(TransactionActivity.this);
                overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right);
            }
        });
        instantiateVariables();
    }
    private void instantiateVariables(){
        transactionAdapter=new TransactionAdapter();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(transactionAdapter);
        Observable<Transaction>transactionObservable=Observable.create(new Observable.OnSubscribe<Transaction>() {
            @Override
            public void call(Subscriber<? super Transaction> subscriber) {
                Agent agent=CompanyHandler.get().getAgent();
                List<Transaction>transactions= agent.getTransactions();
                for (Transaction transaction:transactions){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    subscriber.onNext(transaction);
                }
            }
        });
        transactionObservable.subscribe(this);
    }

    @Override
    public void call(Transaction transaction) {
        Log.d(DEBUG_TAG,"Transaction: "+transaction.getTransactionType());
        transactionAdapter.addToTransactions(transaction);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(TransactionActivity.this);
        overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right);
    }
}
