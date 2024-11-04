package com.example.sellpicture.activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sellpicture.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.id_map);
        mapFragment.getMapAsync(this);

        // Xử lý BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, ProductList.class)); // Chuyển về màn hình danh sách sản phẩm
                return true;
            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class)); // Chuyển về CartActivity (sẽ thêm sau)
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, UserProfileActivity.class)); // Chuyển về UserProfileActivity (sẽ thêm sau)
                return true;
            } else if (itemId == R.id.nav_more) {
                showMoreOptions(); // Hiển thị thêm tùy chọn
                return true;
            }

            return false;
        });



        // Thêm xử lý cho BottomNavigationView
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            int itemId = item.getItemId();
//
//            if (itemId == R.id.nav_home) {
//                startActivity(new Intent(this, ProductList.class)); // Chuyển về màn hình danh sách sản phẩm
//                return true;
//
//            } else if (itemId == R.id.nav_cart) {
//                startActivity(new Intent(this, CartActivity.class)); // Chuyển về CartActivity (sẽ thêm sau)
//                return true;
//            } else if (itemId == R.id.nav_profile) {
//                startActivity(new Intent(this, UserProfileActivity.class)); // Chuyển về UserProfileActivity (sẽ thêm sau)
//                return true;
//            } else if (itemId == R.id.nav_more) {
//                showMoreOptions(); // Hiển thị thêm tùy chọn
//                return true;
//            }
//
//            return false;
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_logout){
            finishAffinity();
            startActivity(new Intent(this, LoginActivity.class));
        }
        return true;
    }

    private void showMoreOptions() {
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.nav_more));
        popup.getMenuInflater().inflate(R.menu.more_options_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.shop_location) {
                // Xử lý khi chọn Shop Location
                startActivity(new Intent(this, MapActivity.class));
            } else if (item.getItemId() == R.id.chat_with_shop){
                startActivity(new Intent(this, ChatActivity.class));

            }else if (item.getItemId() == R.id.support_chat ) {
                startActivity(new Intent(this,SupportChatActivity.class));
            }
            else if (item.getItemId() == R.id.call ) {
                startActivity(new Intent(this, CallActivity.class));
            }
            return false;
        });

        popup.show();
    }
//    private void showMoreOptions() {
//        PopupMenu popup = new PopupMenu(this, findViewById(R.id.nav_more));
//        popup.getMenuInflater().inflate(R.menu.more_options_menu, popup.getMenu());
//
//        popup.setOnMenuItemClickListener(item -> {
//            if (item.getItemId() == R.id.phone_shop) {
//                // Xử lý khi chọn Phone Shop
//                Toast.makeText(this, "Phone Shop được chọn", Toast.LENGTH_SHORT).show();
//                return true;
//            } else if (item.getItemId() == R.id.shop_location) {
//                // Xử lý khi chọn Shop Location
//                startActivity(new Intent(this, MapActivity.class));
//            } else if (item.getItemId() == R.id.chat_with_shop){
//                startActivity(new Intent(this, ChatActivity.class));
//
//            }
//            return false;
//        });
//
//        popup.show();
//    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng location = new LatLng(21.012934469693796, 105.52636744038298);
        googleMap.addMarker(new MarkerOptions().position(location).title("Sellpicture"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
    }
}