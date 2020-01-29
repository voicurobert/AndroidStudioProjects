package rvo.com.book.alerts;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import rvo.com.book.R;

public class AddAlertDialog {
    private int layoutId;
    private String title;
    private Fragment fragment;
    private Activity activity;
    private LayoutInflater layoutInflater;
    private View view;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    public AddAlertDialog(int layoutId, Fragment fragment, LayoutInflater layoutInflater, String title) {
        this.fragment = fragment;
        this.layoutId = layoutId;
        this.layoutInflater = layoutInflater;
        this.title = title;
    }

    public AddAlertDialog(int layoutId, Activity activity, LayoutInflater layoutInflater, String title) {
        this.activity = activity;
        this.layoutId = layoutId;
        this.layoutInflater = layoutInflater;
        this.title = title;
    }

    public void createAlertDialog() {
        if (activity != null) {
            builder = new AlertDialog.Builder(activity);
        } else if (fragment != null) {
            Activity activity = fragment.getActivity();
            if (activity != null) {
                builder = new AlertDialog.Builder(activity);
            }
        } else {
            return;
        }

        view = layoutInflater.inflate(layoutId, null);
        TextView titleTextView = view.findViewById(R.id.layoutTitleTextView);
        builder.setView(view);
        titleTextView.setText(this.title);
        alertDialog = builder.create();
    }

    public void show() {
        alertDialog.show();
    }

    public View getView() {
        return view;
    }

    public void close() {
        alertDialog.cancel();
    }
}
