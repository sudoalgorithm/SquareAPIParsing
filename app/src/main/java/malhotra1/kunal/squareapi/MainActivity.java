package malhotra1.kunal.squareapi;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private ListView mListView;
    HttpHandler mHttpHandler;
    private static final String URL = "https://api.foursquare.com/v2/venues/explore?near=%20Dubai%20&oauth_token=EE0FZFVO5DR2PA4VPGSM2VKB0A3CAOM1I54N4HO04FNFVCDR&v=20180307";
    ArrayList<HashMap<String, String>> itemList;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemList = new ArrayList<>();
        mListView = (ListView) findViewById(R.id.list_view_main);
        mLinearLayout = (LinearLayout) findViewById(R.id.linear_layout_main);
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
                        for (int j =0; j < jsonArray2.length(); j++){
                            JSONObject jsonObject5 = jsonArray2.getJSONObject(j);
                            if (jsonObject5.has("photourl")) {
                                photourl = jsonObject5.getString("photourl");
                            }
                        }
                        if (jsonObject6.has("address")) {
                             address = jsonObject6.getString("address");
                        }
                        String url = "";
                        if (jsonObject4.has("url")){
                            url = jsonObject4.getString("url");
                        }
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("name", name);
                        hashMap.put("rating", rating);
                        hashMap.put("url", url);
                        hashMap.put("photourl", photourl);
                        hashMap.put("address", address);
                        itemList.add(hashMap);
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
            ListAdapter listAdapter = new SimpleAdapter(MainActivity.this, itemList, R.layout.list_view_row, new String[]{"name","rating", "url","photourl", "address"}, new int[]{R.id.name, R.id.rating, R.id.website, R.id.image, R.id.address});
            mListView.setAdapter(listAdapter);
        }
    }
}
