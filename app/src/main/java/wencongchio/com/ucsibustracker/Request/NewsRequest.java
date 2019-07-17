package wencongchio.com.ucsibustracker.Request;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

public class NewsRequest extends StringRequest{
    private static final String REQUEST_URL = "https://easeworks.000webhostapp.com/shuttletracker/news.php";
    private Map<String, String> params;

    public NewsRequest(int count, int offset, Response.Listener<String> listener) {
        super(Method.POST, REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("count", count + "");
        params.put("offset", offset*count + "");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
