package com.blaizedtrail.kakhu.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blaizedtrail.kakhu.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionChat extends Fragment {
    private View rootView;

    public TransactionChat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_transaction_chat, container, false);
        loadChat();
        return rootView;
    }

    private void loadChat(){
        ArrayList<Entry> barEntries=new ArrayList<>();
        barEntries.add(new BarEntry(4f,0,"One"));
        barEntries.add(new BarEntry(8f,1,"Two"));
        barEntries.add(new BarEntry(6f,2,"Three"));
        barEntries.add(new BarEntry(12f,3,"Four"));
        barEntries.add(new BarEntry(18f,4,"Five"));
        barEntries.add(new BarEntry(9f,5,"Six"));
        LineDataSet dataSet=new LineDataSet(barEntries,"Transactions");
        ArrayList<String>labels=new ArrayList<>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        ArrayList<Entry>barEntriesTwo=new ArrayList<>();
        barEntriesTwo.add(new BarEntry(8f,0,"One"));
        barEntriesTwo.add(new BarEntry(8f,1,"Two"));
        barEntriesTwo.add(new BarEntry(6f,2,"Three"));
        barEntriesTwo.add(new BarEntry(15f,3,"Four"));
        barEntriesTwo.add(new BarEntry(19f,4,"Five"));
        barEntriesTwo.add(new BarEntry(90f,5,"Six"));
        LineChart lineChart=(LineChart)rootView.findViewById(R.id.barChart);
        LineData lineData=new LineData(labels,dataSet);
        LineDataSet lineDataSet=new LineDataSet(barEntriesTwo,"Income");
        lineDataSet.setColor(Color.RED);
        lineData.addDataSet(lineDataSet);
        lineChart.setData(lineData);
        lineChart.setDescription("Chat of Income against expenditure");
        lineDataSet.getYMax();
    }


}
