package rvo.com.book.android.main_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import rvo.com.book.R;
import rvo.com.book.android.main_app.schedule.ScheduleFragment;


public class FirmDetailsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.firm_details_fragment, container, false);
        CardView employeesCard = view.findViewById(R.id.employeesCardViewId);
        employeesCard.setOnClickListener(v -> activateFragment(new EmployeesFragment()));
        CardView servicesCard = view.findViewById(R.id.servicesCardViewId);
        servicesCard.setOnClickListener(v -> activateFragment(new CategoriesFragment()));
        CardView scheduleCard = view.findViewById(R.id.scheduleCardViewId);
        scheduleCard.setOnClickListener(v -> activateFragment(new ScheduleFragment()));
        return view;
    }

    private void activateFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                                  .add(R.id.firmDetailsFragmentLayoutId, fragment)
                                  .addToBackStack("fragment")
                                  .commit();
    }
}
