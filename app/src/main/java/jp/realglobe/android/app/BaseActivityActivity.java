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
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.realglobe.android.util.BaseActivity;

public class BaseActivityActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_activity);

        final View button = findViewById(R.id.button_permission_check);
        button.setOnClickListener((View v) -> checkPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                () -> showToast(getString(R.string.notification_permitted)), (String[] denied) -> showToast(getString(R.string.notification_denied))));
        findViewById(R.id.show_toast).setOnClickListener((View v) -> showToast("メインスレッドからです"));
        findViewById(R.id.show_toast_from_background).setOnClickListener((View v) -> (new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                showToast("バックグラウンドスレッドからです");
                return null;
            }
        }).execute());
        findViewById(R.id.show_dialog).setOnClickListener((View v) -> showDialog(SampleDialog.newInstance()));
        findViewById(R.id.show_dialog_from_background).setOnClickListener((View v) -> (new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                showDialog(SampleDialog.newInstance());
                return null;
            }
        }).execute());
    }

    public static class SampleDialog extends DialogFragment {

        public static SampleDialog newInstance() {
            return new SampleDialog();
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.dialog_sample, container);
        }
    }

}
