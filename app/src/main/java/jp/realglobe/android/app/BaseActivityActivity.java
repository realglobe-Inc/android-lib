package jp.realglobe.android.app;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import jp.realglobe.android.util.BaseActivity;

public class BaseActivityActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_activity);

        final View button = findViewById(R.id.button_permission_check);
        button.setOnClickListener((View v) -> checkPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                makePermissionRequestCallback(() -> showToast(getString(R.string.notification_permitted)), (String[] denied) -> showToast(getString(R.string.notification_denied)))));
    }
}
