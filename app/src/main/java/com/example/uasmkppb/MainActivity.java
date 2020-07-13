package com.example.uasmkppb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uasmkppb.api.ApiClient;
import com.example.uasmkppb.api.ApiInterface;
import com.example.uasmkppb.models.Restaurant;
import com.example.uasmkppb.models.Restaurants;
import com.example.uasmkppb.models.Search;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String API_KEY = "77778ae6116592fd3e767445ebad15ab";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Restaurants> restaurants = new ArrayList<>();
    private Adapter adapter;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        LoadJson();
    }

    public void LoadJson(){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        TextView textView = findViewById(R.id.ayam);

        Call<Search> call;
        call = apiInterface.search(API_KEY);

        call.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                if(response.isSuccessful() && response.body().getRestaurants() != null){
                    if(!restaurants.isEmpty()){
                        restaurants.clear();
                    }

                    String content = "";
                    content += " : " + response.body().getResultsFound();
                    textView.append(content);

                    restaurants = response.body().getRestaurants();
                    adapter = new Adapter(restaurants, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    intListener();
                }else {
                    Toast.makeText(MainActivity.this, "No Result!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void intListener(){
        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int adapterPosition) {
                Intent intent = new Intent(MainActivity.this, RestaurantDetailActivity.class);

                Restaurants restaurant = restaurants.get(adapterPosition);
                intent.putExtra("url", restaurant.getRestaurant().getUrl());
                intent.putExtra("title", restaurant.getRestaurant().getName());
                intent.putExtra("img", restaurant.getRestaurant().getThumb());
                intent.putExtra("address", restaurant.getRestaurant().getLocation().getAddress());
                intent.putExtra("cuisine", restaurant.getRestaurant().getCuisines());
                intent.putExtra("latitude",restaurant.getRestaurant().getLocation().getLatitude());
                intent.putExtra("longitude",restaurant.getRestaurant().getLocation().getLongitude());
                intent.putExtra("currency",restaurant.getRestaurant().getCurrency());
                intent.putExtra("price_range", restaurant.getRestaurant().getAverageCostForTwo());
                intent.putExtra("rating", restaurant.getRestaurant().getUserRating().getAggregateRating());
                intent.putExtra("delivery", restaurant.getRestaurant().getHasOnlineDelivery());

                startActivity(intent);
            }
        });
    }
}