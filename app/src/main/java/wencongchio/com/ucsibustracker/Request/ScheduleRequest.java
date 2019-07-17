package wencongchio.com.ucsibustracker.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ScheduleRequest extends StringRequest{
    private static final String REQUEST_URL = "https://easeworks.000webhostapp.com/shuttletracker/schedule.php";
    private Map<String, String> params;

    public ScheduleRequest(String destination, Response.Listener<String> listener) {
        super(Method.POST, REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("destination", destination);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
