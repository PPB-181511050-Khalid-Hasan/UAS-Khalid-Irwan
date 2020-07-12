package com.example.uasmkppb;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.uasmkppb.models.Restaurants;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private List<Restaurants> restaurants;
    private Context context;
    private OnItemClickListener onItemClickListener;


    public Adapter(List<Restaurants> restaurants, Context context) {
        this.restaurants = restaurants;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holders, int position) {
        final MyViewHolder holder = holders;
        Restaurants model = (Restaurants) restaurants.toArray()[position];

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(context)
                .load(model.getRestaurant().getThumb())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);

        holder.tittle.setText(model.getRestaurant().getName());
        holder.location.setText(model.getRestaurant().getLocation().getAddress());
        holder.rating.setText(model.getRestaurant().getUserRating().getAggregateRating());
        if(Utils.isValidStr(model.getRestaurant().getCuisines()))
            holder.cuisine.setText(model.getRestaurant().getCuisines());
        holder.currency.setText(model.getRestaurant().getCurrency());
        holder.rangerPrice.setText(model.getRestaurant().getPriceRange());
        if(model.getRestaurant().getHasOnlineDelivery() == 1){
            holder.delivery.setText("Delivery Ready");
        }else {

        }

    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);

        void onItemClick(View v, int adapterPosition);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tittle, rating, cuisine, location, currency, rangerPrice, delivery;
        ImageView imageView;
        ProgressBar progressBar;
        OnItemClickListener onItemClickListener;

        public MyViewHolder(View itemView, OnItemClickListener onItemClickListener){
            super(itemView);

            itemView.setOnClickListener(this);
            tittle = itemView.findViewById(R.id.title);
            location = itemView.findViewById(R.id.location);
            rating = itemView.findViewById(R.id.textRating);
            cuisine = itemView.findViewById(R.id.cuisines);
            imageView = itemView.findViewById(R.id.img);
            currency = itemView.findViewById(R.id.currency);
            delivery = itemView.findViewById(R.id.delivery);
            rangerPrice = itemView.findViewById(R.id.price_range);
            progressBar = itemView.findViewById(R.id.progress);

            this.onItemClickListener = this.onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            //onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
