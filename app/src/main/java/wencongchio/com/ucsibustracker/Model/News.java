package wencongchio.com.ucsibustracker.Model;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class News {
    private String content, date;

    public News(){

    }

    public News(String createdOn, String content){
        this.content = content;

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);

        Date date = null;
        try {
            date = inputFormat.parse(createdOn);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String formattedDate = outputFormat.format(date);

        this.date = formattedDate;
    }

    public String getContent(){
        return content;
    }

    public String getDate(){
        return date;
    }

}
