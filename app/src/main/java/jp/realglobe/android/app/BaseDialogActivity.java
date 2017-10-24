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

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.realglobe.android.util.BaseActivity;
import jp.realglobe.android.util.BaseDialog;

public class BaseDialogActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_dialog);

        findViewById(R.id.item_1).setOnClickListener((View v) -> showDialog(SampleDialog.newInstance()));
    }

    public static class SampleDialog extends BaseDialog {

        public static SampleDialog newInstance() {
            return new SampleDialog();
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            final View content = inflater.inflate(R.layout.dialog_sample, container);

            content.findViewById(R.id.item_1).setOnClickListener((View v) -> showToast("メインスレッドからです"));
            content.findViewById(R.id.item_2).setOnClickListener((View v) -> (new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    showToast("バックグラウンドスレッドからです");
                    return null;
                }
            }).execute());

            return content;
        }
    }

}
