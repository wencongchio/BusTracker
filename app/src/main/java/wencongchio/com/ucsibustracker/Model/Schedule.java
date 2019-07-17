package wencongchio.com.ucsibustracker.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import wencongchio.com.ucsibustracker.R;

public class Schedule {
    private String time, bus, type;
    private int busColor, busImage, viewType;

    public Schedule(String type, int viewType){
        this.type = type;
        this.viewType = viewType;
    }

    public Schedule(String time, String bus, String type, int viewType){
        this.bus = bus;
        this.type = type;
        this.viewType = viewType;

        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

        Date departureTime = null;
        try {
            departureTime = inputFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.time = outputFormat.format(departureTime);

        switch(bus){
            case "b":
                busColor = R.color.colorBusB;
                busImage = R.drawable.ic_bus_b;
                break;
            case "c":
                busColor = R.color.colorBusC;
                busImage = R.drawable.ic_bus_c;
                break;
            default:
                busColor = R.color.colorBusA;
                busImage = R.drawable.ic_bus_a;
                break;
        }
    }

    public String getTime() {
        return time;
    }

    public String getBus() {
        return bus;
    }

    public String getType() {
        return type;
    }

    public int getBusColor() {
        return busColor;
    }

    public int getViewType() {
        return viewType;
    }

    public int getBusImage(){ return busImage; }
}
