package com.weavey.permission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.wevey.selector.dialog.DialogInterface;
import com.wevey.selector.dialog.NormalAlertDialog;

public class NormalRequestActivity extends AppCompatActivity {

    private Context context;
    private final static int REQUEST_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        findViewById(R.id.request_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) ==
                        PackageManager.PERMISSION_GRANTED) {

                    //已授权

                } else {

                    //未授权
                    if (ActivityCompat.shouldShowRequestPermissionRationale(NormalRequestActivity.this,
                            Manifest.permission.CALL_PHONE)) {

                        new NormalAlertDialog.Builder(context).setContentText
                                ("由于您拒绝了拨号权限授权，导致该功能不可使用，是否重新授权？").setLeftButtonText("我就不")
                                .setRightButtonText("重新授权").setOnclickListener(new DialogInterface.OnLeftAndRightClickListener<NormalAlertDialog>() {


                            @Override
                            public void clickLeftButton(NormalAlertDialog dialog, View view) {

                                dialog.dismiss();
                            }

                            @Override
                            public void clickRightButton(NormalAlertDialog dialog, View view) {

                                dialog.dismiss();
                                ActivityCompat.requestPermissions(NormalRequestActivity.this, new
                                        String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);

                            }
                        }).build().show();

                        return;

                    } else {

                        ActivityCompat.requestPermissions(NormalRequestActivity.this, new
                                String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);

                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {

            case REQUEST_CODE: {


                if (grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED) {

                    Toast.makeText(context, "授权成功", Toast.LENGTH_SHORT).show();

                } else {

                    if (!ActivityCompat.shouldShowRequestPermissionRationale(NormalRequestActivity.this,
                            Manifest.permission.CALL_PHONE)) {

                        new NormalAlertDialog.Builder(context).setContentText
                                ("发现您拒绝了授权拨号权限且不再提示，将导致该功能不可用，是否前往设置授权？").setLeftButtonText
                                ("我就不").setRightButtonText("去设置").setOnclickListener(new DialogInterface.OnLeftAndRightClickListener<NormalAlertDialog>() {


                            @Override
                            public void clickLeftButton(NormalAlertDialog dialog, View view) {

                                dialog.dismiss();
                            }

                            @Override
                            public void clickRightButton(NormalAlertDialog dialog, View view) {

                                dialog.dismiss();
                                Uri packageURI = Uri.parse("package:" + context.getPackageName());
                                Intent intent = new Intent(Settings
                                        .ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                                context.startActivity(intent);

                            }
                        }).build().show();

                    } else {

                        Toast.makeText(context, "授权失败", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
        }
    }

}
