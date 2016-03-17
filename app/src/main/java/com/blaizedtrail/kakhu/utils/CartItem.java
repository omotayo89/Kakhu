package com.blaizedtrail.kakhu.utils;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;

import java.util.List;

/**
 * Created by McLeroy on 2/11/2016.
 */
@ModelContainer
@Table(database = KakhuDB.class)
public class CartItem extends BaseModel{

    @PrimaryKey(autoincrement = true)
    private int id;

    @Column
    private String buyerName;

    @Column
    private String buyerAddress;

    @Column
    private String buyerPhoneNumber;


    @Column
    private String buyerSignature;

    @Column
    private String openingDate;



    @ForeignKey(saveForeignKeyModel = false)
    Agent agent;

    @ForeignKey(saveForeignKeyModel = false)
    ForeignKeyContainer<Company>companyForeignKeyContainer;

    List<InventoryItem>inventoryItems;
    @OneToMany(methods = {OneToMany.Method.DELETE,OneToMany.Method.SAVE},variableName = "inventoryItems")
    public List<InventoryItem>getInventoryItems(){
        return SQLite.select()
                .from(InventoryItem.class)
                .where(InventoryItem_Table.cartItemForeignKeyContainer_id.eq(id))
                .queryList();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public String getBuyerPhoneNumber() {
        return buyerPhoneNumber;
    }

    public void setBuyerPhoneNumber(String buyerPhoneNumber) {
        this.buyerPhoneNumber = buyerPhoneNumber;
    }

    public String getBuyerSignature() {
        return buyerSignature;
    }

    public void setBuyerSignature(String buyerSignature) {
        this.buyerSignature = buyerSignature;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(String openingDate) {
        this.openingDate = openingDate;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
    public void associateCompany(Company company){
        companyForeignKeyContainer= FlowManager.getContainerAdapter(Company.class).toForeignKeyContainer(company);
    }


}
