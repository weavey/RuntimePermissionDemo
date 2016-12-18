package com.weavey.permission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Observable;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.wevey.selector.dialog.DialogInterface;
import com.wevey.selector.dialog.NormalAlertDialog;

import rx.functions.Action1;

public class RxRequsetActicity extends AppCompatActivity {


    private RxPermissions rxPermissions;
    private Context mContext;
    private NormalAlertDialog tipDialog;
    private NormalAlertDialog openSettingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_requset_acticity);

        mContext = this;
        rxPermissions = new RxPermissions(RxRequsetActicity.this);

        initDialog();
        if (!(rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION) && rxPermissions
                .isGranted(Manifest.permission.READ_EXTERNAL_STORAGE) && rxPermissions.isGranted
                (Manifest.permission.CAMERA) && rxPermissions.isGranted(Manifest.permission
                .RECORD_AUDIO))) {

            tipDialog.show();
        } else {

            //跳转下个act
        }

    }

    private void initDialog() {

        tipDialog = new NormalAlertDialog.Builder(this).setContentText
                ("请允许健康管家获取相关权限，拒绝将引起相关功能异常或不可用。").setCanceledOnTouchOutside(false).setSingleMode
                (true).setSingleButtonText("下一步").setSingleListener(new DialogInterface
                .OnSingleClickListener<NormalAlertDialog>() {
            @Override
            public void clickSingleButton(NormalAlertDialog dialog, View view) {

                dialog.dismiss();
                requestPermission();
            }
        }).build();

        openSettingDialog = new NormalAlertDialog.Builder(this).setContentText
                ("由于健康管家无法获取相关权限，不能正常运行，请开启权限后再使用健康管家。").setLeftButtonText("取消")
                .setRightButtonText("去开启").setOnclickListener(new DialogInterface
                        .OnLeftAndRightClickListener<NormalAlertDialog>() {


            @Override
            public void clickLeftButton(NormalAlertDialog dialog, View view) {

                dialog.dismiss();
                finish();
            }

            @Override
            public void clickRightButton(NormalAlertDialog dialog, View view) {

                dialog.dismiss();
                Uri packageURI = Uri.parse("package:" + mContext.getPackageName());
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        packageURI);
                mContext.startActivity(intent);


            }
        }).build();
    }

    private void requestPermission() {

        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
                .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission
                .RECORD_AUDIO).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {

                if (aBoolean) {

                    Toast.makeText(RxRequsetActicity.this, "权限申请成功", Toast.LENGTH_SHORT).show();
                    //跳转下个act

                } else {

                    openSettingDialog.show();
                }

            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION) && rxPermissions
                .isGranted(Manifest.permission.READ_EXTERNAL_STORAGE) && rxPermissions.isGranted
                (Manifest.permission.CAMERA) && rxPermissions.isGranted(Manifest.permission
                .RECORD_AUDIO)) {

            //跳转下个act

        } else {
            openSettingDialog.show();
        }
    }
}
