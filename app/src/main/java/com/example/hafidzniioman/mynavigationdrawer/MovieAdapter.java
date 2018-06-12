package com.example.hafidzniioman.mynavigationdrawer;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MovieAdapter extends BaseAdapter {
    private ArrayList<MovieItems> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;
    private String final_overview;

    public MovieAdapter(Context context) {
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<MovieItems> items) {
        mData = items;
        notifyDataSetChanged();
    }

    public void addItem(final MovieItems item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void clearData() {
        mData.clear();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getCount() {
        if (mData == null)
            return 0;
        return mData.size();
    }

    @Override
    public MovieItems getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.movie_items, null);
            holder.tvMovieName = (TextView) convertView.findViewById(R.id.tv_movie_name);
            holder.tvMovieDescription = (TextView) convertView.findViewById(R.id.tv_movie_description);
            holder.tvMovieDate = (TextView) convertView.findViewById(R.id.tv_movie_date);
            holder.imgMoviePoster = (ImageView) convertView.findViewById(R.id.img_movie_poster);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvMovieName.setText(mData.get(position).getMovieName());
        String retrievedDate = mData.get(position).getMovieDate();
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = date_format.parse(retrievedDate);
            SimpleDateFormat new_date_format = new SimpleDateFormat("EEEE, dd/MM/yyyy");
            String date_of_release = new_date_format.format(date);
            holder.tvMovieDate.setText(date_of_release);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String overview = mData.get(position).getMovieDescription();
        if (TextUtils.isEmpty(overview)) {
            final_overview = "Null";
        } else {
            final_overview = overview;
        }
        holder.tvMovieDescription.setText(final_overview);
        Glide.with(context).load("http://image.tmdb.org/t/p/w185/" + mData.get(position).getmoviePoster()).into(holder.imgMoviePoster);
        return convertView;
    }

    private static class ViewHolder {
        TextView tvMovieName;
        TextView tvMovieDescription;
        TextView tvMovieDate;
        ImageView imgMoviePoster;
    }
}
