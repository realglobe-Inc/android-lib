package jp.realglobe.android.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_log).setOnClickListener((View v) -> startActivity(new Intent(this, LogActivity.class)));
        findViewById(R.id.button_edit_text_utils).setOnClickListener((View v) -> startActivity(new Intent(this, EditTextUtilsActivity.class)));
        findViewById(R.id.button_preference_utils).setOnClickListener((View v) -> startActivity(new Intent(this, PreferenceUtilsActivity.class)));
        findViewById(R.id.button_base_activity).setOnClickListener((View v) -> startActivity(new Intent(this, BaseActivityActivity.class)));
    }

}
