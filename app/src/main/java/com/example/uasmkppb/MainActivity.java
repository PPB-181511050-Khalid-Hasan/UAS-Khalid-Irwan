package com.example.uasmkppb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
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

//                    if(!restaurants.isEmpty()){
//                        Toast.makeText(MainActivity.this, "succses no empty!", Toast.LENGTH_SHORT).show();
//                    }
//                    Toast.makeText(MainActivity.this, "succses!", Toast.LENGTH_SHORT).show();
                }else {
//                    if(!response.isSuccessful()){
//                        Toast.makeText(MainActivity.this, "No succses!", Toast.LENGTH_SHORT).show();
//
//                    }else {
//                        Toast.makeText(MainActivity.this, "No Result!", Toast.LENGTH_SHORT).show();
//                    }
                }
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}