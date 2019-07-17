package wencongchio.com.ucsibustracker.Fragment;


import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import wencongchio.com.ucsibustracker.R;

public class ScheduleFragment extends Fragment{

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_schedule, container, false);

        BottomNavigationView bottomNavigation = (BottomNavigationView)view.findViewById(R.id.schedule_bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navListener);

        init();
        return view;
    }

    private void init(){
        BottomNavigationView bottomNavigation = (BottomNavigationView)view.findViewById(R.id.schedule_bottom_navigation);
        Fragment fragmentSelected = new UniversityScheduleFragment();

        if (fragmentSelected != null){
            bottomNavigation.setSelectedItemId(R.id.navigation_university);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.schedule_fragment_container, fragmentSelected).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.navigation_university:
                            selectedFragment = new UniversityScheduleFragment();
                            break;
                        case R.id.navigation_college:
                            selectedFragment = new CollegeScheduleFragment();
                            break;
                        case R.id.navigation_tbs:
                            selectedFragment = new TbsScheduleFragment();
                            break;
                    }

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.schedule_fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

}
