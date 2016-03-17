package com.blaizedtrail.kakhu.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.blaizedtrail.kakhu.R;
import com.blaizedtrail.kakhu.application.App;
import com.blaizedtrail.kakhu.fragments.FragmentNewAgent;
import com.blaizedtrail.kakhu.fragments.FragmentOldAgent;
import com.blaizedtrail.kakhu.utils.Agent;
import com.blaizedtrail.kakhu.utils.Company;
import com.blaizedtrail.kakhu.utils.CompanyHandler;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class AgentInfo extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Company company;
    private File file,cameraOutput;
    private Agent agent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainView,new FragmentNewAgent()).commit();
        instantiateVariables();
    }
    private void instantiateVariables(){
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
        fragmentTransaction.replace(R.id.mainView, new FragmentNewAgent());
        fragmentTransaction.commit();
        agent=new Agent();
        company= CompanyHandler.get().getCompany();
        file=App.getAgentImageFile();
    }

    public void registerInstead(View view){
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
        fragmentTransaction.replace(R.id.mainView, new FragmentNewAgent());
        fragmentTransaction.commit();
    }
    public void loginInstead(View view){
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right);
        fragmentTransaction.replace(R.id.mainView, new FragmentOldAgent());
        fragmentTransaction.commit();
    }
    public void register(View view){
        String agentFirstName=((MaterialEditText)findViewById(R.id.agentFirstName)).getText().toString();
        String agentLastName=((MaterialEditText)findViewById(R.id.agentLastName)).getText().toString();
        String agentEmail=((MaterialEditText)findViewById(R.id.agentEmail)).getText().toString();
        String agentPassword=((MaterialEditText)findViewById(R.id.password)).getText().toString();
        agent.setAgent_firstName(agentFirstName);
        agent.setAgent_lastName(agentLastName);
        agent.setAgent_email(agentEmail);
        agent.setAgent_password(agentPassword);
        agent.setAgent_company(company.getCompany_name());
        String agentCode=String.valueOf((long) (Math.random() * (500000L)));
        agent.setAgent_code(agentCode);
        agent.save();
        company.setAgent(agent);
        company.save();
        proceedWithRegister(agent);
    }
    private void proceedWithRegister(Agent agent){
        CompanyHandler.get().addActivity(agent.getAgent_firstName() + " registered on Kakhu as an agent for " + company.getCompany_name());
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(agent.getAgent_firstName());
        builder.setMessage("Your agent code is " + agent.getAgent_code() + ". Keep this code safe as it identifies you");
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Intent intent = new Intent(AgentInfo.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top);
            }
        });
        builder.show();
    }
    public void pickImage(View view){
        String[] selections={"Gallery","Camera"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("New image from");
        builder.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, selections), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Crop.pickImage(AgentInfo.this, 0);
                        break;
                    case 1:
                        cameraOutput = new File(App.getImageCache(), new Date().toString() + ".png");
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, Uri.fromFile(cameraOutput));
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, 1);
                        } else {
                            Toast.makeText(App.getContext(),"Failed to start camera. Switching to Gallery",Toast.LENGTH_SHORT).show();
                            Crop.pickImage(AgentInfo.this, 0);
                        }
                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode!=RESULT_OK){
            return;
        }
        switch (requestCode){
            case 0:
                //file=new File(App.getImageCache(),new Date().toString()+"png");
                Crop.of(data.getData(),Uri.fromFile(file)).withMaxSize(300,300).start(this,3);
                break;
            case 1:
                //file=new File(App.getImageCache(),new Date().toString()+"png");
                Crop.of(Uri.fromFile(cameraOutput),Uri.fromFile(file)).withMaxSize(300,300).start(this,3);
                break;
            case 3:
                processImage(Uri.fromFile(file));
        }
    }

    private void processImage(final Uri uri){
        Picasso.with(this)
                .load(uri)
                .skipMemoryCache()
                .into((CircleImageView) findViewById(R.id.image_view), new Callback() {
                    @Override
                    public void onSuccess() {
                        agent.setImage_uri(uri.toString());
                    }

                    @Override
                    public void onError() {
                        agent.setImage_uri(null);
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.clearCache();
    }
}
