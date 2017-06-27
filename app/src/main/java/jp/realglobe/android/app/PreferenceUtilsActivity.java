package jp.realglobe.android.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import jp.realglobe.android.util.PreferenceUtils;

public class PreferenceUtilsActivity extends AppCompatActivity {

    private static final String KEY_STRING = PreferenceUtilsActivity.class.getName() + ":string";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_utils);

        final EditText editString = (EditText) findViewById(R.id.edit_string);
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
