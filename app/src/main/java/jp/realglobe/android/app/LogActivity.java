/*----------------------------------------------------------------------
 * Copyright 2017 realglobe Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *----------------------------------------------------------------------*/

package jp.realglobe.android.app;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import jp.realglobe.android.logger.simple.Log;

public class LogActivity extends Activity {

    private static final String TAG = LogActivity.class.getName();

    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private static final int PERMISSION_REQUEST_CODE = 15228;

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        final EditText editPath = findViewById(R.id.edit_path);
        final View buttonStart = findViewById(R.id.button_start);
        final EditText editMessage = findViewById(R.id.edit_message);
        final View buttonVerbose = findViewById(R.id.button_verbose);
        final View buttonDebug = findViewById(R.id.button_debug);
        final View buttonInfo = findViewById(R.id.button_info);
        final View buttonWarn = findViewById(R.id.button_warn);
        final View buttonError = findViewById(R.id.button_error);
        final View buttonAssert = findViewById(R.id.button_assert);

        editPath.setText((new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), getClass().getSimpleName() + ".log")).getAbsolutePath());
        editMessage.setText(R.string.dummy_log_message);

        buttonStart.setOnClickListener((View v) -> {
            this.file = new File(editPath.getText().toString());
            this.file.delete();
            try {
                Log.setLogFile(this.file);
                Toast.makeText(this, file + "への出力を開始します", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(this, file + "の作成に失敗しました: " + e, Toast.LENGTH_LONG).show();
            }
        });
        buttonVerbose.setOnClickListener((View v) -> Log.v(TAG, editMessage.getText().toString()));
        buttonDebug.setOnClickListener((View v) -> Log.d(TAG, editMessage.getText().toString()));
        buttonInfo.setOnClickListener((View v) -> Log.i(TAG, editMessage.getText().toString()));
        buttonWarn.setOnClickListener((View v) -> Log.w(TAG, editMessage.getText().toString()));
        buttonError.setOnClickListener((View v) -> Log.e(TAG, editMessage.getText().toString()));
        buttonAssert.setOnClickListener((View v) -> Log.wtf(TAG, editMessage.getText().toString()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != PERMISSION_REQUEST_CODE) {
            return;
        }

        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.notification_permissions, Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    @Override
    protected void onPause() {
        Log.flushLogFile();
        if (this.file != null) {
            this.file.setReadable(true, false);
        }
        super.onPause();
    }

}
