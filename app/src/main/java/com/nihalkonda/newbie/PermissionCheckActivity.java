package com.nihalkonda.newbie;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import com.nihalkonda.newbie.app.ProcedureGroupActivity;

public class PermissionCheckActivity extends AppCompatActivity {

    private final int OVERLAY_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_check);
        requestOverlayPermission();
    }

    private void requestOverlayPermission() {
        Intent intent;
        if (isOverlayGranted()) {goAhead();return;}
        intent = new Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:"+getPackageName()));
        startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);
    }

    private void goAhead() {
        startActivity(new Intent(this, ProcedureGroupActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!isOverlayGranted()) {
                finish();
            }else
                goAhead();
        }
    }

    private boolean isOverlayGranted() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this);
    }

}