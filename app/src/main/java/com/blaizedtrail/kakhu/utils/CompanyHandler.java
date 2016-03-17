package com.blaizedtrail.kakhu.utils;

import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;
import java.util.List;

/**
 * Created by McLeroy on 2/6/2016.
 */
public class CompanyHandler extends BaseModel{
    private static CompanyHandler instance = new CompanyHandler();
    private static Company company;
    private static Agent agent;

    public static CompanyHandler get() {
        company=SQLite.select().from(Company.class).querySingle();
        if (company!=null) {
            agent = company.getAgent();
        }
        return instance;
    }

    private CompanyHandler() {
    }
    public Company getCompany(){
        return SQLite.select().from(Company.class).querySingle();
    }
    public Agent getAgent(){
        return agent;
    }
    public Transaction addTransaction(String transactionType,String itemName,int quantityLeft,int quantityRemoved){
        Transaction transaction=new Transaction();
        transaction.setTransactionType(transactionType);
        transaction.setCompanyName(company.getCompany_name());
        transaction.setTransactingAgent(agent.getAgent_firstName());
        transaction.setTransactingAgentCode(agent.getAgent_code());
        transaction.setTransactionItemName(itemName);
        Date date=new Date();
        transaction.setTransactionDate(date.toString());
        transaction.setTransactionItemLeft(String.valueOf(quantityLeft));
        transaction.setTransactionQuantityRemoved(String.valueOf(quantityRemoved));
        transaction.setTransactionTimestamp(date.getTime());
        transaction.associateAgent(agent);
        transaction.save();
        return transaction;
    }

    public void addInventory(String itemName,String itemPrice,int quantity){
        long count=getInventoryItemsCountByName(itemName);
        InventoryItem inventoryItem=new InventoryItem();
        if (count==0){
            inventoryItem.setItemName(itemName);
        }else {
            inventoryItem.setItemName(itemName+"(Stock "+Math.random()*10L+")");
        }
        inventoryItem.setStockPrice(Integer.parseInt(itemPrice));
        inventoryItem.save();
        for (int i=0;i<quantity;i++){
            InventoryItemInfo inventoryItemInfo=new InventoryItemInfo();
            inventoryItemInfo.setItemName(itemName);
            inventoryItemInfo.setItemName(itemName);
            inventoryItemInfo.setItemPrice(itemPrice);
            inventoryItemInfo.setItemQuantity(String.valueOf(quantity));
            inventoryItemInfo.setInStock(true);
            Date date=new Date();
            inventoryItemInfo.setItemStockDate(date.toString());
            inventoryItemInfo.setItemStockStamp(String.valueOf(date.getTime()));
            inventoryItemInfo.associateInventoryItem(inventoryItem);
            inventoryItemInfo.save();
        }
        addTransaction(Config.TRANSACTION_TYPE_ADD_TO_INVENTORY,inventoryItem.getItemName(),(int)getInventoryItemsCountByName(itemName),quantity);
        addActivity(quantity+" units of "+itemName+" added to inventory by "+agent.getAgent_firstName());
    }

    @OneToMany(methods = {OneToMany.Method.SAVE,OneToMany.Method.DELETE},variableName = "inventoryItems")
    public long getInventoryItemsCountByName(String inventoryName){
        List<InventoryItemInfo>itemInfoList;
        itemInfoList=SQLite.select()
                .from(InventoryItemInfo.class)
                .where(InventoryItem_Table.itemName.eq(inventoryName)).queryList();
        return (long)itemInfoList.size();
    }




    List<InventoryItem>inventoryItems;
    @OneToMany(methods = {OneToMany.Method.SAVE,OneToMany.Method.DELETE},variableName = "inventoryItems")
    public List<InventoryItem>getInventoryItems(){
        inventoryItems=SQLite.select()
                .from(InventoryItem.class)
                .queryList();
        return inventoryItems;
    }
    public void addActivity(String activityInfo){
        ActivityItem activityItem=new ActivityItem();
        activityItem.setEventTime(new Date().toString());
        activityItem.setEventInfo(activityInfo);
        activityItem.save();
    }

    public InventoryItem getInventoryInfoById(int id){
        return SQLite.select()
                .from(InventoryItem.class)
                .where(InventoryItem_Table.id.eq(id))
                .querySingle();
    }

}
