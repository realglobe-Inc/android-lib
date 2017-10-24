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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import jp.realglobe.android.util.PreferenceUtils;

public class PreferenceUtilsActivity extends Activity {

    private static final String KEY_STRING = PreferenceUtilsActivity.class.getName() + ":string";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_utils);

        final EditText editString = findViewById(R.id.edit_string);
        final View buttonSave = findViewById(R.id.button_save);
        final View buttonDelete = findViewById(R.id.button_delete);
        final View buttonReload = findViewById(R.id.button_reload);

        buttonSave.setOnClickListener((View v) -> {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            final String text = editString.getText().toString();
            preferences.edit().putString(KEY_STRING, text).apply();
            Toast.makeText(this, "\"" + text + "\"を保存しました", Toast.LENGTH_LONG).show();
        });
        buttonDelete.setOnClickListener((View v) -> {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            preferences.edit().remove(KEY_STRING).apply();
            Toast.makeText(this, R.string.notification_preferences_delete, Toast.LENGTH_LONG).show();
        });
        buttonReload.setOnClickListener((View v) -> {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editString.setText(PreferenceUtils.getNonEmptyString(preferences, KEY_STRING, getString(R.string.dummy_empty_string)));
            Toast.makeText(this, R.string.notification_preferences_load, Toast.LENGTH_LONG).show();
        });
    }

}
