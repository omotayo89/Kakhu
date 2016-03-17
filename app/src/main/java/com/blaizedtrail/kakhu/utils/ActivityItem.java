package com.blaizedtrail.kakhu.utils;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;

/**
 * Created by McLeroy on 2/10/2016.
 */
@Table(database = KakhuDB.class)
public class ActivityItem extends BaseModel{

    @PrimaryKey(autoincrement = true)
    private int id;

    @Column
    private String eventTime;

    @Column
    private String eventInfo;

    @ForeignKey(saveForeignKeyModel = false)
    ForeignKeyContainer<Agent>agentForeignKeyContainer;

    private void associateAgent(Agent agent){
        agentForeignKeyContainer= FlowManager.getContainerAdapter(Agent.class).toForeignKeyContainer(agent);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventInfo() {
        return eventInfo;
    }

    public void setEventInfo(String eventInfo) {
        this.eventInfo = eventInfo;
    }
}
