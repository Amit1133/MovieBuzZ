package com.example.amit.moviebuzz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity {

    List<Search> movieData;
    RecyclerView mRecyclerView;
    MovieAdapter movieAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mRecyclerView=findViewById(R.id.m_recyclerview);

        //String mText="batman";
        String mText=getIntent().getStringExtra("searchKey");
        final String key="7a8658b4";

        GetDataServices services=RetrofitClientInstances.getRetrofitClientInstance().create(GetDataServices.class);
        Call<SearchedData> call=services.getJsonData("?apikey="+key+"&s="+mText);

        progressDialog=new ProgressDialog(ResultActivity.this);
        progressDialog.setMessage("working on it....");
        progressDialog.show();
        call.enqueue(new Callback<SearchedData>() {
            @Override
            public void onResponse(Call<SearchedData> call, Response<SearchedData> response) {
                    progressDialog.dismiss();

                //Toast.makeText(ResultActivity.this, ""+response.body().getResponse(), Toast.LENGTH_SHORT).show();
                if(response.body().getResponse().equals("True")) {
                    SearchedData mData = response.body();
                    //Toast.makeText(ResultActivity.this, "success :"+mData.getTotalResults()+mData.getSearch().get(0).getTitle().toString(), Toast.LENGTH_SHORT).show();
                    //Log.d("Tag",mData.getSearch().get(0).getPoster().toString());
                    movieData = mData.getSearch();
                    //Toast.makeText(ResultActivity.this, ""+movieData.get(0).getYear().toString(), Toast.LENGTH_SHORT).show();
                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(ResultActivity.this));
                    movieAdapter = new MovieAdapter(ResultActivity.this, movieData);
                    mRecyclerView.setAdapter(movieAdapter);
                }
                else{
                    Toast.makeText(ResultActivity.this, "Wrong movie name!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ResultActivity.this, Homepage.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }


            }

            @Override
            public void onFailure(Call<SearchedData> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ResultActivity.this, "Try again!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ResultActivity.this,Homepage.class));

            }
        });


    }

}
