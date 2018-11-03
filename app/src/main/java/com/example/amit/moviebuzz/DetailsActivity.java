package com.example.amit.moviebuzz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {
    SingleMovie singleMovie;
    ImageView detailsPoster;
    Button detailsTrailerBtn,detailsSaveBtn;
    TextView detailsImdbTV,detailsTomatoTV,detailsTitleTV,detailsReleasedTV,detailsRuntimeTV,detailsGenreTV,detailsDirectorTV,detailsWriterTV,detailsProductionTV;
    ProgressDialog progressDialog;
    MyDBHelper myDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        detailsPoster=findViewById(R.id.details_poster_iv);
        detailsImdbTV=findViewById(R.id.details_imdb_score_tv);
        detailsTomatoTV=findViewById(R.id.details_tomato_score_tv);
        detailsTitleTV=findViewById(R.id.details_title_tv);
        detailsReleasedTV=findViewById(R.id.details_released_tv);
        detailsRuntimeTV=findViewById(R.id.details_runtime_tv);
        detailsGenreTV=findViewById(R.id.details_genre_tv);
        detailsDirectorTV=findViewById(R.id.details_director_tv);
        detailsWriterTV=findViewById(R.id.details_writer_tv);
        detailsProductionTV=findViewById(R.id.details_production_tv);
        detailsTrailerBtn=findViewById(R.id.details_trailer_btn);
        detailsSaveBtn=findViewById(R.id.details_save_btn);

        if (myDBHelper == null) {
            myDBHelper = new MyDBHelper(this);
            SQLiteDatabase sqLiteDatabase = myDBHelper.getWritableDatabase();
        }


        detailsTrailerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTrailer();
            }
        });
        detailsSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long r = myDBHelper.insertData(singleMovie.getImdbID(), singleMovie.getTitle());
                //long r = myDBHelper.insertData("tt5647457", "superman");
                if (r == -1) {
                    Toast.makeText(DetailsActivity.this, "Not Saved!internal problem dude", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailsActivity.this, "data Saved", Toast.LENGTH_SHORT).show();
                }

            }
        });


        String selectedMovieId=getIntent().getStringExtra("selectedimdbid");

        final String key="7a8658b4";
        GetDataServices services=RetrofitClientInstances.getRetrofitClientInstance().create(GetDataServices.class);
        Call<SingleMovie> call=services.getSingleMOvieJsonData("?apikey="+key+"&i="+selectedMovieId);
        progressDialog=new ProgressDialog(DetailsActivity.this);
        progressDialog.setMessage("working on it....");
        progressDialog.show();
        call.enqueue(new Callback<SingleMovie>() {
            @Override
            public void onResponse(Call<SingleMovie> call, Response<SingleMovie> response) {
                progressDialog.dismiss();
                singleMovie=response.body();
                //Toast.makeText(DetailsActivity.this, ""+singleMovie.getGenre(), Toast.LENGTH_LONG).show();
                showDetailsData();
            }

            @Override
            public void onFailure(Call<SingleMovie> call, Throwable t) {
                progressDialog.dismiss();
                startActivity(new Intent(DetailsActivity.this,Homepage.class));
                Toast.makeText(DetailsActivity.this, "Something error", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void startTrailer() {
        Intent i=new Intent(DetailsActivity.this,MovieTrailer.class);
        i.putExtra("imdbKeyTrailer",singleMovie.getImdbID());
        startActivity(i);
    }

    private void showDetailsData() {
        Picasso.with(this).load(singleMovie.getPoster()).placeholder(R.drawable.confused).into(detailsPoster);

        if(singleMovie.getRatings().size()>1){
            detailsImdbTV.setText(singleMovie.getRatings().get(0).getValue());
            detailsTomatoTV.setText(singleMovie.getRatings().get(1).getValue());
        }else if(singleMovie.getRatings().size()==1){
            detailsImdbTV.setText(singleMovie.getRatings().get(0).getValue());
            detailsTomatoTV.setText("Not Rated");
        }
        else{
            detailsImdbTV.setText("Not Rated");
            detailsTomatoTV.setText("Not Rated");
        }
        detailsTitleTV.setText(singleMovie.getTitle());
        detailsReleasedTV.setText(singleMovie.getReleased());
        detailsRuntimeTV.setText(singleMovie.getRuntime());
        detailsGenreTV.setText(singleMovie.getGenre());
        detailsDirectorTV.setText(singleMovie.getDirector());
        detailsWriterTV.setText(singleMovie.getWriter());
        detailsProductionTV.setText(singleMovie.getProduction());




    }
}
