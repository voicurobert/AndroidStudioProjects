package rvo.com.book.main_app;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import rvo.com.book.R;

public class FirmDetailsMasterActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firm_master_details_activity);

        activateFragment(new FirmDetailsFragment());

        ImageButton closeActivityButton = findViewById(R.id.closeFirmDetailsButtonId);
        closeActivityButton.setOnClickListener(v -> finish());

        ImageButton backButton = findViewById(R.id.backButtonId);
        backButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getFragments().size() == 1) {
                finish();
            }
            fragmentManager.popBackStackImmediate();
        });
    }

    protected void activateFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.firmDetailsFragmentLayoutId, fragment)
                                   .addToBackStack(getClass().getSimpleName()).commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
