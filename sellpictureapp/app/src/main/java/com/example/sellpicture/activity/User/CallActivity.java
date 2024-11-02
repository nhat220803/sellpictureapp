package com.example.sellpicture.activity.User;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sellpicture.R;

import java.util.logging.Logger;

public class CallActivity extends AppCompatActivity {
    final static int REQUEST_CODE_FOR_PHONE_CALL = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.phone), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void getCallPermission() {
        if(ActivityCompat.checkSelfPermission
                (CallActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CallActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_CODE_FOR_PHONE_CALL);
        }
        else {
            // perform call phone action
            performCallAction();
        }
    }

    @Override
    public void onRequestPermissionsResult(int reqCode, String[] permissions, int[] grantResults) {
        switch (reqCode) {
            case REQUEST_CODE_FOR_PHONE_CALL:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    performCallAction();
                }  else {
                    Toast.makeText(CallActivity.this, "You need to allow call access", Toast.LENGTH_LONG).show();
                }
                return;
        }

        super.onRequestPermissionsResult(reqCode, permissions, grantResults);
    }

    private void performCallAction() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: 0964937641"));
        startActivity(intent);
    }

    public void phoneclick(View view)
    {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M))
        {
            getCallPermission();
        }
    }




}