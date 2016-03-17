package com.blaizedtrail.kakhu.utils;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
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
public class Company extends BaseModel{

    @PrimaryKey(autoincrement = true)
    private long id;

    @Column
    private String company_name;

    @Column
    private String company_code;

    @Column
    private String company_address;

    @Column
    private String company_branch;

    @Column
    private String company_email;

    @Column
    @ForeignKey(saveForeignKeyModel = false)
    Agent agent;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_code() {
        return company_code;
    }

    public void setCompany_code(String company_code) {
        this.company_code = company_code;
    }

    public String getCompany_address() {
        return company_address;
    }

    public void setCompany_address(String company_address) {
        this.company_address = company_address;
    }

    public String getCompany_branch() {
        return company_branch;
    }

    public void setCompany_branch(String company_branch) {
        this.company_branch = company_branch;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public String getCompany_email() {
        return company_email;
    }

    public void setCompany_email(String company_email) {
        this.company_email = company_email;
    }

    List<CartItem>cart;

    @OneToMany(methods = {OneToMany.Method.DELETE,OneToMany.Method.SAVE},variableName = "cart")
    public List<CartItem>getCart(){
        return SQLite.select()
                .from(CartItem.class)
                .where(CartItem_Table.companyForeignKeyContainer_id.eq(id))
                .queryList();
    }

    public Company getCompany(){
        return SQLite.select().from(Company.class).querySingle();
    }

    public CartItem getCartItemById(int id){
        return SQLite.select()
                .from(CartItem.class)
                .where(CartItem_Table.id.eq(id))
                .querySingle();
    }
}
