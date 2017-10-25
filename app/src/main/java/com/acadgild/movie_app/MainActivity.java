package com.acadgild.movie_app;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    ArrayList<HashMap<String, String>> movieList;
    //JSON Node Names
    private static final String TAG_NAME = "name";
    private static final String TAG_VOTES = "vote_count";
    private static final String TAG_ID = "id";
    private static final String TAG_RESULTS = "results";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        new GetMovieLists().execute();
    }

    private class GetMovieLists extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String url = "http://api.themoviedb.org/3/tv/top_rated?api_key=8496be0b2149805afa458ab8ec27560c";
            JSONParser jsonParser = new JSONParser();
            String jsonStr = jsonParser.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    Log.e(TAG, "object of json" + jsonObj);
                    Log.e(TAG, "object of json page" + jsonObj);

                    JSONArray jsonArray_results = jsonObj.getJSONArray(TAG_RESULTS);
                    Log.e(TAG, "object of json results" + jsonArray_results);

                    for (int i = 0; i <= jsonArray_results.length(); i++) {

                        JSONObject jsonObject = jsonArray_results.getJSONObject(i);
                        Log.e(TAG, "object of json in for loop" + jsonObject);

                        String movie_name = jsonObject.getString(TAG_NAME);
                        String id = jsonObject.getString(TAG_ID);
                        String vote_count = jsonObject.getString(TAG_VOTES);

                        HashMap<String, String> results = new HashMap<>();
                        results.put(TAG_ID, id);
                        Log.e(TAG, "id" + id);
                        results.put(TAG_NAME, movie_name);
                        Log.e(TAG, "movie name" + movie_name);
                        results.put(TAG_VOTES, vote_count);
                        Log.e(TAG, "vote count" + vote_count);

                        movieList.add(results);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Here Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            //Here Updating parsed JSON data into ListView

            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, movieList,
                    R.layout.item_list, new String[]{"name", "id",
                    "vote_count"}, new int[]{R.id.name,
                    R.id.vote_count, R.id.id});

            lv.setAdapter(adapter);
        }
    }
}
