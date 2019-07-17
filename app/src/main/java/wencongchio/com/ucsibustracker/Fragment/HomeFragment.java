package wencongchio.com.ucsibustracker.Fragment;


import android.os.Bundle;
import android.os.Handler;
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

import wencongchio.com.ucsibustracker.Adapter.NewsAdapter;
import wencongchio.com.ucsibustracker.Model.News;
import wencongchio.com.ucsibustracker.R;
import wencongchio.com.ucsibustracker.Request.NewsRequest;


public class HomeFragment extends Fragment {

    List<News> noticeList;
    boolean isLoading = false;
    boolean isLast = false;
    int currentItem, totalItem, scrollOutItem;
    int newsCount = 6;
    int offset = 0;

    NewsAdapter adapter;
    Response.Listener<String> responseListener;

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.home_recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.home_swipeRefreshLayout);
        linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                init();

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){

            @Override
            public void onScrolled(RecyclerView recyclerView, int x, int y){
                super.onScrolled(recyclerView, x, y);
                currentItem = linearLayoutManager.getChildCount();
                totalItem = linearLayoutManager.getItemCount();
                scrollOutItem = linearLayoutManager.findFirstVisibleItemPosition();

                if(!isLoading && !isLast && (currentItem + scrollOutItem >= totalItem) && scrollOutItem >= 0){
                    adapter.addLoadingFooter();
                    loadMoreData();
                }
            }
        });

        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);

                    isLast = (jsonResponse.length()<newsCount);

                    for (int i = 0; i < jsonResponse.length(); i++) {
                        try {
                            JSONObject json_data = jsonResponse.getJSONObject(i);
                            noticeList.add(new News(json_data.getString("createdOn"),
                                    json_data.getString("content")));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    adapter.notifyDataSetChanged();

                    isLoading = false;
                    swipeRefreshLayout.setRefreshing(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        swipeRefreshLayout.setRefreshing(true);
        init();

        return view;
    }

    public void init(){
        noticeList = new ArrayList<>();

        offset = 0;

        adapter = new NewsAdapter(getActivity(), noticeList);
        recyclerView.setAdapter(adapter);

        NewsRequest retrieveRequest = new NewsRequest(newsCount, offset, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(retrieveRequest);
    }

    public void loadMoreData(){

        isLoading = true;

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                offset++;

                adapter.removeLoadingFooter();

                NewsRequest retrieveRequest = new NewsRequest(newsCount, offset, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(retrieveRequest);
            }
        }, 1000);
    }

}
