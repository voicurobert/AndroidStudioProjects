package rvo.com.book;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;

public class PrivacyPolicyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy_layout);
        ImageButton closeFragment = findViewById(R.id.closePrivacyPolicyFragmentButtonId);
        closeFragment.setOnClickListener(v -> finish());
    }
}
