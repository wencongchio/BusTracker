package wencongchio.com.ucsibustracker.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import wencongchio.com.ucsibustracker.R;
import wencongchio.com.ucsibustracker.Request.FeedbackRequest;


public class FeedbackFragment extends Fragment {

    TextInputLayout textFeedbackFirstName, textFeedbackLastName, textFeedbackEmail, textFeedbackContent;
    FloatingActionButton btnFeedbackSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        textFeedbackFirstName = (TextInputLayout)view.findViewById(R.id.text_feedback_first_name);
        textFeedbackLastName = (TextInputLayout)view.findViewById(R.id.text_feedback_last_name);
        textFeedbackEmail = (TextInputLayout)view.findViewById(R.id.text_feedback_email);
        textFeedbackContent = (TextInputLayout)view.findViewById(R.id.text_feedback_content);
        btnFeedbackSubmit = (FloatingActionButton)view.findViewById(R.id.btn_feedback_submit);

        btnFeedbackSubmit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                final ProgressDialog dialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.feedback_submitting), true);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            boolean success = response.equals("true");

                            dialog.dismiss();

                            if(success){
                                Toast.makeText(getActivity(), R.string.feedback_submitted, Toast.LENGTH_SHORT).show();
                                textFeedbackFirstName.getEditText().getText().clear();
                                textFeedbackLastName.getEditText().getText().clear();
                                textFeedbackEmail.getEditText().getText().clear();
                                textFeedbackContent.getEditText().getText().clear();

                            }
                            else{
                                Toast.makeText(getActivity(), R.string.feedback_submit_error, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                if(validateFirstName()||validateLastName()||validateContent()){
                    String firstName = textFeedbackFirstName.getEditText().getText().toString().trim();
                    String lastName = textFeedbackLastName.getEditText().getText().toString().trim();
                    String email = textFeedbackEmail.getEditText().getText().toString().trim();
                    String content = textFeedbackContent.getEditText().getText().toString().trim();

                    FeedbackRequest submitRequest = new FeedbackRequest(firstName, lastName, email, content, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    queue.add(submitRequest);
                }
                else{
                    dialog.dismiss();
                }
            }
        });

        return view;
    }

    public boolean validateFirstName(){
        String firstName = textFeedbackFirstName.getEditText().getText().toString().trim();

        if(firstName.isEmpty()){
            textFeedbackFirstName.setError(getResources().getString(R.string.empty_field));
            return false;
        }
        else{
            textFeedbackFirstName.setError(null);
            return true;
        }
    }

    public boolean validateLastName(){
        String lastName = textFeedbackLastName.getEditText().getText().toString().trim();

        if(lastName.isEmpty()){
            textFeedbackLastName.setError(getResources().getString(R.string.empty_field));
            return false;
        }
        else{
            textFeedbackLastName.setError(null);
            return true;
        }
    }

    public boolean validateContent(){
        String content = textFeedbackContent.getEditText().getText().toString().trim();

        if(content.isEmpty()){
            textFeedbackContent.setError(getResources().getString(R.string.empty_field));
            return false;
        }
        else{
            textFeedbackContent.setError(null);
            return true;
        }
    }

}
