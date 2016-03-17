package com.blaizedtrail.kakhu.utils;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by McLeroy on 2/6/2016.
 */
@ModelContainer
@Table(database = KakhuDB.class)
public class Agent extends BaseModel{

    @PrimaryKey(autoincrement = true)
    private long id;

    @Column
    private String agent_firstName;

    @Column
    private String agent_lastName;

    @Column
    private String agent_code;

    @Column
    private String agent_company;

    @Column
    private String agent_email;

    @Column
    private String agent_password;

    @Column
    private String image_uri;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAgent_firstName() {
        return agent_firstName;
    }

    public String getAgent_lastName() {
        return agent_lastName;
    }

    public void setAgent_lastName(String agent_lastName) {
        this.agent_lastName = agent_lastName;
    }

    public void setAgent_firstName(String agent_firstName) {
        this.agent_firstName = agent_firstName;
    }

    public String getAgent_code() {
        return agent_code;
    }

    public void setAgent_code(String agent_code) {
        this.agent_code = agent_code;
    }

    public String getAgent_email() {
        return agent_email;
    }

    public void setAgent_email(String agent_email) {
        this.agent_email = agent_email;
    }

    public String getAgent_password() {
        return agent_password;
    }

    public void setAgent_password(String agent_password) {
        this.agent_password = agent_password;
    }

    public String getAgent_company() {
        return agent_company;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public void setAgent_company(String agent_company) {
        this.agent_company = agent_company;
    }

    public Agent getCurrentAgentInfo(){
        return SQLite.select().from(Company.class).querySingle().getAgent();
    }
    List<Transaction> transactions;
    @OneToMany(methods = {OneToMany.Method.SAVE,OneToMany.Method.DELETE},variableName = "transactions")
    public List<Transaction>getTransactions(){
        if (transactions==null||transactions.isEmpty()){
            transactions=SQLite.select().from(Transaction.class)
                    .where(Transaction_Table.agentForeignKeyContainer_id.eq(id))
                    .queryList();
        }
        return transactions;
    }

    List<ActivityItem> activities;
    @OneToMany(methods = {OneToMany.Method.SAVE,OneToMany.Method.DELETE},variableName = "activities")
    public List<ActivityItem> getActivities(){
        activities =SQLite.select()
                .from(ActivityItem.class)
                .queryList();
        return activities;
    }
}
