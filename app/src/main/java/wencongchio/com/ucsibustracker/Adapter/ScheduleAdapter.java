package wencongchio.com.ucsibustracker.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wencongchio.com.ucsibustracker.Model.Schedule;
import wencongchio.com.ucsibustracker.R;

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Schedule> scheduleList;

    public static final int VIEWTYPE_GROUP = 0;
    public static final int VIEWTYPE_ITEM = 1;

    public ScheduleAdapter(Context context, List<Schedule> scheduleList){
        this.context = context;
        this.scheduleList = scheduleList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(context);

        switch(viewType){
            case VIEWTYPE_GROUP:
                View groupView = inflater.inflate(R.layout.schedule_group_layout, parent, false);
                viewHolder = new ScheduleGroupViewHolder(groupView);
                break;

            case VIEWTYPE_ITEM:
                View itemView = inflater.inflate(R.layout.schedule_item_layout, parent, false);
                viewHolder = new ScheduleItemViewHolder(itemView);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch(getItemViewType(position)){
            case VIEWTYPE_GROUP:
                ScheduleGroupViewHolder groupViewHolder = (ScheduleGroupViewHolder)holder;
                groupViewHolder.text_schedule_group_header.setText(scheduleList.get(position).getType());
                break;

            case VIEWTYPE_ITEM:
                ScheduleItemViewHolder itemViewHolder = (ScheduleItemViewHolder)holder;
                itemViewHolder.text_schedule_item_time.setText(scheduleList.get(position).getTime());
                itemViewHolder.image_schedule_bus.setImageResource(scheduleList.get(position).getBusImage());
                itemViewHolder.image_schedule_bus.setBackgroundResource(scheduleList.get(position).getBusColor());
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return scheduleList.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    class ScheduleGroupViewHolder extends RecyclerView.ViewHolder {

        TextView text_schedule_group_header;

        public ScheduleGroupViewHolder(View groupView) {
            super(groupView);

            text_schedule_group_header = itemView.findViewById(R.id.text_schedule_group_header);
        }
    }

    class ScheduleItemViewHolder extends RecyclerView.ViewHolder {

        ImageView image_schedule_bus;
        TextView text_schedule_item_time;

        public ScheduleItemViewHolder(View itemView) {
            super(itemView);

            image_schedule_bus = (ImageView)itemView.findViewById(R.id.image_schedule_bus);
            text_schedule_item_time = (TextView)itemView.findViewById(R.id.text_schedule_item_time);
        }
    }
}
