package wencongchio.com.ucsibustracker.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import wencongchio.com.ucsibustracker.Model.News;
import wencongchio.com.ucsibustracker.R;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<News> newsList;

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private boolean isLoadingAdded = false;

    public NewsAdapter(Context context, List<News> newsList){
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType) {
            case ITEM:
                View view = inflater.inflate(R.layout.home_item_layout, parent, false);
                viewHolder = new NewsViewHolder(view);
                break;
            case LOADING:
                View view2 = inflater.inflate(R.layout.item_progressbar, parent, false);
                viewHolder = new LoadingViewHolder(view2);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){


        switch (getItemViewType(position)) {
            case ITEM:
                News news = newsList.get(position);

                NewsViewHolder userViewHolder = (NewsViewHolder) holder;

                userViewHolder.text_date.setText(news.getDate());
                userViewHolder.text_content.setText(String.valueOf(news.getContent()));

                break;

            case LOADING:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return newsList == null ? 0 : newsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == newsList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        newsList.add(new News());
        notifyItemInserted(newsList.size() - 1);
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = newsList.size() - 1;
        News result = newsList.get(position);;

        if (result != null) {
            newsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView text_date, text_content;

        public NewsViewHolder(View itemView) {
            super(itemView);

            text_date = itemView.findViewById(R.id.text_news_date);
            text_content = itemView.findViewById(R.id.text_news_content);
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(View view) {
            super(view);
        }
    }
}
