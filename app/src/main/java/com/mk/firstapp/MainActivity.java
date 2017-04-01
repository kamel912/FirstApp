package com.mk.firstapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    private String TAG = MainActivity.class.getSimpleName();
    private ListView listView;
    ArrayList<HashMap<String,String>> contactList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactList = new ArrayList<>();
        new GetContacts().execute();
        listView = (ListView) findViewById(R.id.list);
    }

    private class GetContacts extends AsyncTask<Void ,Void ,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Toast.makeText(MainActivity.this,
                    "Json Data isdownloading",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void ... arg0) {

            HttpHandler handler = new HttpHandler();
            String url = "http://api.androidhive.info/contacts/";
            String jsonString = handler.makeServiceCall(url);

            Log.e(TAG,"Response from url : " + jsonString);

            if (jsonString != null){
                try {

                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray contacts = jsonObject.getJSONArray("contacts");

                    for (int i = 0;i < contacts.length(); i++){

                        JSONObject object = contacts.getJSONObject(i);
                        String id = object.getString("id");
                        String name = object.getString("name");
                        String email = object.getString("email");
                        String address = object.getString("address");
                        String gender = object.getString("gender");
                        JSONObject phone = object.getJSONObject("phone");
                        String mobile = phone.getString("mobile");
                        String home = phone.getString("home");
                        String office = phone.getString("office");

                        HashMap<String,String> contact = new HashMap<>();
                        contact.put("id",id);
                        contact.put("name",name);
                        contact.put("email",email);
                        contact.put("mobile",mobile);

                        contactList.add(contact);
                    }

                } catch (final JSONException e) {

                    Log.e(TAG,"JSON Parsing error : " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {

                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void  result) {
            super.onPostExecute(result);

            ListAdapter adapter = new SimpleAdapter(MainActivity.this,
                    contactList,
                    R.layout.list_item,new String[]{"name","email" , "mobile"},new int[]{R.id.name,R.id.email , R.id.mobile});
            listView.setAdapter(adapter);
        }
    }
}
