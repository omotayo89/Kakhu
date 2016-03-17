package com.blaizedtrail.kakhu.activities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blaizedtrail.kakhu.R;
import com.blaizedtrail.kakhu.application.App;
import com.blaizedtrail.kakhu.utils.Agent;
import com.blaizedtrail.kakhu.utils.Company;
import com.blaizedtrail.kakhu.utils.CompanyHandler;

public class AddInventory extends AppCompatActivity{
    private Company company;
    private Agent agent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(AddInventory.this);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });
        instantiateVariables();
    }
    private void instantiateVariables(){
        company= CompanyHandler.get().getCompany();
        agent =company.getAgent();
        ((TextView)findViewById(R.id.postingAgent)).setText(agent.getAgent_firstName());
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(AddInventory.this);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }
    public void addToInventory(View view){
        String itemName=((EditText)findViewById(R.id.itemName)).getText().toString();
        String itemPrice=((EditText)findViewById(R.id.itemPrice)).getText().toString();
        int itemQuantity=Integer.parseInt(((EditText)findViewById(R.id.itemQuantitiy)).getText().toString());
        CompanyHandler.get().addInventory(itemName,itemPrice,itemQuantity);
        NavUtils.navigateUpFromSameTask(AddInventory.this);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        Toast.makeText(App.getContext(),"Added to Inventory",Toast.LENGTH_SHORT).show();
    }
}
