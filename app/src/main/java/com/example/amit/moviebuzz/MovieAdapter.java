package com.example.amit.moviebuzz;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private Context context;
    private List<Search> movieList;

    public MovieAdapter(Context context, List<Search> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_single_row, null);
        return new MovieViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        final Search movie = movieList.get(position);
        holder.mTitle.setText(movie.getTitle());
        holder.mYear.setText(movie.getYear());
        holder.mType.setText(movie.getType());
        //Picasso.get().load(movie.getPoster()).into((Target) holder.mSingleImage);
        Picasso.with(context).load(movie.getPoster()).placeholder(R.drawable.confused).into(holder.mSingleImage);
        final String imdbId = movie.getImdbID();

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "clicked on "+imdbId+" "+movie.getTitle(), Toast.LENGTH_SHORT).show();
                goToDetails(imdbId);
            }
        });


    }

    private void goToDetails(String imdbId) {
        Intent i = new Intent(context, DetailsActivity.class);
        i.putExtra("selectedimdbid", imdbId);
        context.startActivity(i);

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView mSingleImage;
        TextView mTitle, mYear, mType;
        ConstraintLayout parentLayout;

        public MovieViewHolder(View itemView) {
            super(itemView);

            mSingleImage = itemView.findViewById(R.id.single_image_iv);
            mTitle = itemView.findViewById(R.id.single_title_tv);
            mYear = itemView.findViewById(R.id.single_year_tv);
            mType = itemView.findViewById(R.id.single_type_tv);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }

    }
}
