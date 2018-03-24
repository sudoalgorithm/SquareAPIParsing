package malhotra1.kunal.squareapi;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    HttpHandler mHttpHandler;
    private static final String URL = "https://api.foursquare.com/v2/venues/explore?near=%20Dubai%20&oauth_token=EE0FZFVO5DR2PA4VPGSM2VKB0A3CAOM1I54N4HO04FNFVCDR&v=20180307";
    private LinearLayout mLinearLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    String[] nameArray, ratingArray, addressArray, photourlArray, websiteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLinearLayout = (LinearLayout) findViewById(R.id.linear_layout_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        mLayoutManager = new LinearLayoutManager(this);
        new AsyncTask().execute();
    }

    private class AsyncTask extends android.os.AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Snackbar.make(mLinearLayout, "Please Wait Connecting To Server",Snackbar.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mHttpHandler = new HttpHandler();
            String responseJSON =  mHttpHandler.apiCall(URL);
            if (responseJSON != null){
                try {
                    JSONObject jsonObject = new JSONObject(responseJSON);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                    JSONArray jsonArray = jsonObject1.getJSONArray("groups");
                    JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                    JSONArray jsonArray1 = jsonObject2.getJSONArray("items");
                    for (int i=0; i < jsonArray1.length(); i++) {
                        JSONObject jsonObject3 = jsonArray1.getJSONObject(i);
                        JSONObject jsonObject4 = jsonObject3.getJSONObject("venue");
                        JSONArray jsonArray2 = jsonObject3.getJSONArray("tips");
                        JSONObject jsonObject6 = jsonObject4.getJSONObject("location");
                        String name = jsonObject4.getString("name");
                        String rating = jsonObject4.getString("rating");
                        String address = "";
                        String photourl = "";
                        String url = "";
                        for (int j = 0; j < jsonArray2.length(); j++){
                            JSONObject jsonObject5 = jsonArray2.getJSONObject(j);
                            if (jsonObject5.has("photourl")) {
                                photourl = jsonObject5.getString("photourl");
                            }
                        }
                        if (jsonObject6.has("address")) {
                             address = jsonObject6.getString("address");
                        }
                        if (jsonObject4.has("url")){
                            url = jsonObject4.getString("url");
                        }
                        nameArray[i] = name;
                        ratingArray[i] = rating;
                        addressArray[i] = address;
                        websiteArray[i] = url;
                        photourlArray[i] = photourl;



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }
    }
}
