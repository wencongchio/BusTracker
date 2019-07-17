package wencongchio.com.ucsibustracker.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class FeedbackRequest extends StringRequest {

    private static final String REQUEST_URL = "https://easeworks.000webhostapp.com/shuttletracker/feedback.php";
    private Map<String, String> params;

    public FeedbackRequest(String firstName, String lastName, String email, String content, Response.Listener<String> listener) {
        super(Method.POST, REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("firstName", firstName);
        params.put("lastName", lastName);
        params.put("email", email);
        params.put("content", content);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
