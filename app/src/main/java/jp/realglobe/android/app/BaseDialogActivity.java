package jp.realglobe.android.app;

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

        findViewById(R.id.button_show).setOnClickListener((View v) -> showDialog(SampleDialog.newInstance()));
    }

    public static class SampleDialog extends BaseDialog {

        public static SampleDialog newInstance() {
            return new SampleDialog();
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            final View content = inflater.inflate(R.layout.dialog_sample, container);

            content.findViewById(R.id.button_show).setOnClickListener((View v) -> showToast("メッセージが表示されていれば正常です"));

            return content;
        }
    }

}
