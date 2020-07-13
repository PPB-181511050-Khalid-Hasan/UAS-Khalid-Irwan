package com.example.uasmkppb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

public class RestaurantDetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{
    private ImageView imageView;
    private TextView rating, currency, price_range, time, title, delivery;
    private boolean isHideTolbarView = false;
    private FrameLayout date_behavior;
    private LinearLayout tittleAppbar;
    private AppBarLayout appBarLayout;
    private MapView map;
    private String mUrl, mImg, mTitle, mRating, mCuisine, mCurrency, mPrice_range;
    private int mDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);


        requestPermissionsIfNecessary(new String[] {
                // if you need to show the current location, uncomment the line below
                // Manifest.permission.ACCESS_FINE_LOCATION,
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });

        if ((getIntent().getData() != null) && ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) == 0)) {
            //Handle the url passed through the intent
        } else {
            //proceed as normal
        }

        //Map
        final Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);



        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        map=findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);
        IMapController mapController = map.getController();

        appBarLayout = findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(this);
        imageView = findViewById(R.id.backdrop);
        time = findViewById(R.id.time);
        title = findViewById(R.id.title);
        rating = findViewById(R.id.textRating);
        currency = findViewById(R.id.currency);
        price_range = findViewById(R.id.price_range);
        delivery = findViewById(R.id.delivery);

        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
        mImg = intent.getStringExtra("img");
        mTitle = intent.getStringExtra("title");
        mCuisine = intent.getStringExtra("cuisine");
        mPrice_range = intent.getStringExtra("price_range");
        mCurrency = intent.getStringExtra("currency");
        mRating = intent.getStringExtra("rating");
//        //init Map Latitude and Point
        float Latitude= Float.parseFloat(intent.getStringExtra("latitude"));
        float Longitude= Float.parseFloat(intent.getStringExtra("longitude"));

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(Utils.getRandomDrawbleColor());

        Glide.with(this)
                .load(mImg)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);

        title.setText(mTitle);
        time.setText(mCuisine);
        rating.setText(mRating);
        currency.setText(mCurrency);
        price_range.setText(mPrice_range);

        intWebView(mUrl);

        ArrayList<OverlayItem> item = new ArrayList<OverlayItem>();
        item.add(new OverlayItem("Location", "Restaurant pinpoint",new GeoPoint(Latitude,Longitude)));
        GeoPoint startPoint = new GeoPoint(Latitude,Longitude);
        Marker startMarker = new Marker(map);

        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);

        mapController.setZoom(16);
        mapController.setCenter(startPoint);

    }

    private void intWebView(String url){
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(i) / (float) maxScroll;
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    1);
        }
    }

}