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

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

import jp.realglobe.android.util.view.EditTexts;

public class EditTextUtilsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text_utils);

        final EditText editInt = (EditText) findViewById(R.id.edit_int);
        final EditText editLong = (EditText) findViewById(R.id.edit_long);
        final EditText editFloat = (EditText) findViewById(R.id.edit_float);
        final EditText editDouble = (EditText) findViewById(R.id.edit_double);
        final EditText editString = (EditText) findViewById(R.id.edit_string);
        final View buttonParse = findViewById(R.id.button_parse);

        buttonParse.setOnClickListener((View v) -> {
            final DialogFragment dialog = Dialog.newInstance(
                    EditTexts.parseInt(editInt, -9999),
                    EditTexts.parseLong(editLong, -99999999L),
                    EditTexts.parseFloat(editFloat, -0.9999F),
                    EditTexts.parseDouble(editDouble, -0.99999999),
                    EditTexts.getNonEmptyString(editString, getString(R.string.dummy_empty_string))
            );
            dialog.show(getFragmentManager(), "dialog");
        });
    }

    public static class Dialog extends DialogFragment {

        private static final String KEY_INT = "int";
        private static final String KEY_LONG = "long";
        private static final String KEY_FLOAT = "float";
        private static final String KEY_DOUBLE = "double";
        private static final String KEY_STRING = "string";

        static Dialog newInstance(int intValue, long longValue, float floatValue, double doubleValue, String stringValue) {
            final Dialog dialog = new Dialog();

            final Bundle args = new Bundle();
            args.putInt(KEY_INT, intValue);
            args.putLong(KEY_LONG, longValue);
            args.putFloat(KEY_FLOAT, floatValue);
            args.putDouble(KEY_DOUBLE, doubleValue);
            args.putString(KEY_STRING, stringValue);
            dialog.setArguments(args);

            return dialog;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            final View content = inflater.inflate(R.layout.dialog_edit_text_utils, container, false);

            final TextView textInt = content.findViewById(R.id.text_int);
            final TextView textLong = content.findViewById(R.id.text_long);
            final TextView textFloat = content.findViewById(R.id.text_float);
            final TextView textDouble = content.findViewById(R.id.text_double);
            final TextView textString = content.findViewById(R.id.text_string);

            textInt.setText(String.format(Locale.US, "%d", getArguments().getInt(KEY_INT)));
            textLong.setText(String.format(Locale.US, "%d", getArguments().getLong(KEY_LONG)));
            textFloat.setText(String.format(Locale.US, "%.4f", getArguments().getFloat(KEY_FLOAT)));
            textDouble.setText(String.format(Locale.US, "%.8f", getArguments().getDouble(KEY_DOUBLE)));
            textString.setText(getArguments().getString(KEY_STRING));
            return content;
        }
    }

}
