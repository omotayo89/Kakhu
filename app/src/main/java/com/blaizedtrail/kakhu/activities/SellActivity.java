package com.blaizedtrail.kakhu.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.blaizedtrail.kakhu.R;
import com.blaizedtrail.kakhu.utils.Agent;
import com.blaizedtrail.kakhu.utils.BariolTextView;
import com.blaizedtrail.kakhu.utils.CompanyHandler;
import com.blaizedtrail.kakhu.utils.InventoryItem;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellActivity extends AppCompatActivity {
    public static final String EXTRAS_INVENTORY_ID="com.blaizedtrail.kakhu.activities.SellActivity.INTENT_INVENTORY_ID";
    private static final String DEBUG_TAG = SellActivity.class.getSimpleName();
    private InventoryItem inventoryItem;
    private Agent agent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTransaction();
            }
        });
        instantiateVariables();
    }
    private void instantiateVariables() {
        agent = CompanyHandler.get().getCompany().getAgent();
        Intent intent = getIntent();
        int inventoryId = intent.getIntExtra(EXTRAS_INVENTORY_ID, 0);
        inventoryItem = CompanyHandler.get().getInventoryInfoById(inventoryId);
        getSupportActionBar().setTitle(inventoryItem.getItemName());
        Log.d(DEBUG_TAG, "Inventory id: " + inventoryId);
        ((BariolTextView) findViewById(R.id.agentName)).setText(agent.getAgent_firstName() + " " + agent.getAgent_lastName());
        ((BariolTextView) findViewById(R.id.agentCode)).setText(agent.getAgent_code());
        Picasso.with(this)
                .load(Uri.parse(agent.getImage_uri()))
                .skipMemoryCache()
                .into((CircleImageView) findViewById(R.id.image_view));
    }
    private void endTransaction(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Transaction incomplete");
        builder.setMessage("End transaction?");
        builder.setNegativeButton("No", null);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        endTransaction();
    }
}
