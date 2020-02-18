package rvo.com.book.android.main_app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import rvo.com.book.R;
import rvo.com.book.common.Eight;
import rvo.com.book.datamodel.entities.Firm;

public class ActiveFirmsFragment extends Fragment {
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.active_firms_activity, container, false);
        ListView firmsListView = myView.findViewById(R.id.firmsListViewId);
        progressBar = myView.findViewById(R.id.firmsListProgressBarId);
        FirmsAdapter firmsAdapter = new FirmsAdapter(this, Eight.dataModel.getActiveFirms());
        firmsListView.setAdapter(firmsAdapter);
        ImageButton mapViewButton = myView.findViewById(R.id.mapViewButtonId);
        mapViewButton.setOnClickListener(v -> Eight.dataModel.initialiseActiveFirms(() -> {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayoutId, new MapViewFragment()).addToBackStack(getClass().getSimpleName()).commit();
            }
        } ));
        return myView;
    }


    protected void callFirm(Firm firm) {
        if (Build.VERSION.SDK_INT < 23) {
            doActualCall(firm);
        } else {
            Activity activity = getActivity();
            if (activity != null) {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    doActualCall(firm);
                } else {
                    String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                    ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, 9);
                }
            }
        }
    }

    private void doActualCall(Firm firm) {
        Activity activity = getActivity();
        if (activity != null) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", firm.getPhoneNumber(), null));
                this.startActivity(callIntent);
            } else {
                Toast.makeText(this.getContext(), getString(R.string.no_permission_to_call), Toast.LENGTH_SHORT).show();
            }
        }

    }


    protected void activateFirmDetailsActivity() {
        Activity activity = getActivity();
        if (activity != null) {
            Intent firmIntent = new Intent(getContext(), FirmDetailsMasterActivity.class);
            activity.startActivity(firmIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean permissionGranted = false;
        if (requestCode == 9) {
            permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        if (permissionGranted) {
            doActualCall(Eight.dataModel.getFirm());
        } else {
            Toast.makeText(this.getContext(), getString(R.string.no_permission_to_call), Toast.LENGTH_SHORT).show();
        }
    }

    public void activateProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void deactivateProgress() {
        progressBar.setVisibility(View.GONE);
    }
}
