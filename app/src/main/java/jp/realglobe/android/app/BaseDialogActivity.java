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

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

        private Handler backgroundHandler;

        static SampleDialog newInstance() {
            return new SampleDialog();
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            final View content = inflater.inflate(R.layout.dialog_sample, container);

            final HandlerThread thread = new HandlerThread(getClass().getName() + ":background");
            thread.start();
            this.backgroundHandler = new Handler(thread.getLooper());

            content.findViewById(R.id.item_1).setOnClickListener((View v) -> showToast("メインスレッドからです"));
            content.findViewById(R.id.item_2).setOnClickListener((View v) -> this.backgroundHandler.post(() -> showToast("バックグラウンドスレッドからです")));

            return content;
        }

        @Override
        public void onDestroyView() {
            this.backgroundHandler.getLooper().quit();
            super.onDestroyView();
        }

    }

}
