package com.appkrafty.evote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    LinearLayout scanYourIdCardButton,signupLoginButton;
    private static final int REQUEST_ALL_PERMISSIONS = 100;
    private final String[] permissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.mainColor));

        scanYourIdCardButton = findViewById(R.id.scanYourIdCardButton);
        signupLoginButton = findViewById(R.id.signupLoginButton);

        scanYourIdCardButton.setOnClickListener(view -> {
            if (checkPermissions()) {
                startScanIDCardActivity();
            }
        });

        signupLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ManuallySignup.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkPermissions() {
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, REQUEST_ALL_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    private void startScanIDCardActivity() {
        Intent intent = new Intent(MainActivity.this, ScanIDCard.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ALL_PERMISSIONS) {
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                startScanIDCardActivity();
            } else {
                Toast.makeText(this, "Must need allow", Toast.LENGTH_SHORT).show();
            }
        }
    }
}