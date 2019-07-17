package wencongchio.com.ucsibustracker.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import wencongchio.com.ucsibustracker.Adapter.ScheduleAdapter;
import wencongchio.com.ucsibustracker.Model.Schedule;
import wencongchio.com.ucsibustracker.R;
import wencongchio.com.ucsibustracker.Request.ScheduleRequest;

public class TbsScheduleFragment extends Fragment {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    ScheduleAdapter adapter;
    Response.Listener<String> responseListener;

    List<Schedule> weekdaysScheduleList;
    List<Schedule> saturdayScheduleList;
    List<Schedule> scheduleList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_schedule_tbs, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.schedule_tbs_swipeRefreshLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.schedule_tbs_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);

                    for (int i = 0; i < jsonResponse.length(); i++) {
                        try {
                            JSONObject json_data = jsonResponse.getJSONObject(i);

                            Schedule newSchedule = new Schedule(json_data.getString("time"), json_data.getString("bus"), json_data.getString("type"), ScheduleAdapter.VIEWTYPE_ITEM);

                            if(newSchedule.getType().equals("weekdays")){
                                weekdaysScheduleList.add(newSchedule);
                            }
                            else{
                                saturdayScheduleList.add(newSchedule);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    scheduleList = new ArrayList<>();

                    scheduleList.addAll(weekdaysScheduleList);
                    scheduleList.addAll(saturdayScheduleList);

                    adapter = new ScheduleAdapter(getActivity(), scheduleList);
                    recyclerView.setAdapter(adapter);

                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setEnabled(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        init();

        return view;
    }

    public void init(){
        Schedule weekdaysHeader = new Schedule("Weekdays", ScheduleAdapter.VIEWTYPE_GROUP);
        Schedule saturdayHeader = new Schedule("Saturday",ScheduleAdapter.VIEWTYPE_GROUP);

        weekdaysScheduleList = new ArrayList<>();
        saturdayScheduleList = new ArrayList<>();

        weekdaysScheduleList.add(weekdaysHeader);
        saturdayScheduleList.add(saturdayHeader);

        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(true);

        ScheduleRequest retrieveRequest = new ScheduleRequest("tbs", responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(retrieveRequest);
    }

}
