package rvo.com.book.android.main_app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;

import rvo.com.book.R;

public class PrivacyPolicyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy_layout);
        ImageButton closeFragment = findViewById(R.id.closePrivacyPolicyFragmentButtonId);
        closeFragment.setOnClickListener(v -> finish());
    }
}
