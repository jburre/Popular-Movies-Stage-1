package com.example.lyz.popularmoviesstage1;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGridView = findViewById(R.id.gv_main);
        /* TEST for appliance TODO remove later
        String[] myArray = new String[100];
        for (int i=0;i<myArray.length;i++){
            myArray[i]=String.valueOf(i);
        }
        ArrayAdapter<String> mListAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,myArray);
        mGridView.setAdapter(mListAdapter);
        */
    }
}
