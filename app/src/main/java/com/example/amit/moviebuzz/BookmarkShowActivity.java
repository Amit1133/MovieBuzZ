package com.example.amit.moviebuzz;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class BookmarkShowActivity extends AppCompatActivity {
    MyDBHelper myDBHelper;
    ListView mListView;
    MyAdapter myAdapter;
    ArrayList<BookmarkData> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_show);

        mListView=findViewById(R.id.m_list_view);
        arrayList=new ArrayList<>();

        if (myDBHelper == null) {
            myDBHelper = new MyDBHelper(this);
            SQLiteDatabase sqLiteDatabase = myDBHelper.getWritableDatabase();
        }
        arrayList=myDBHelper.loadData();
        myAdapter=new MyAdapter(BookmarkShowActivity.this,arrayList);
        mListView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imdbId=arrayList.get(position).getmImdb();
                Intent i=new Intent(BookmarkShowActivity.this,DetailsActivity.class);
                i.putExtra("selectedimdbid",imdbId);
                startActivity(i);
            }
        });


    }
}
