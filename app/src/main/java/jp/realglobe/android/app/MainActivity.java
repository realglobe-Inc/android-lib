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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_log).setOnClickListener((View v) -> startActivity(new Intent(this, LogActivity.class)));
        findViewById(R.id.button_edit_text_utils).setOnClickListener((View v) -> startActivity(new Intent(this, EditTextUtilsActivity.class)));
        findViewById(R.id.button_preference_utils).setOnClickListener((View v) -> startActivity(new Intent(this, PreferenceUtilsActivity.class)));
        findViewById(R.id.button_base_activity).setOnClickListener((View v) -> startActivity(new Intent(this, BaseActivityActivity.class)));
        findViewById(R.id.button_base_dialog).setOnClickListener((View v) -> startActivity(new Intent(this, BaseDialogActivity.class)));
    }

}
