package com.example.client;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class MainActivity extends FragmentActivity {

    EditText eUserName = null;
    EditText ePassword = null;

    private String message = "";
    private String title = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

        new ChecAuthentication().execute("http://192.168.12.122:8002/chek/");
	}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("info"," -- MainActivity [ onSavedInstanceState ]");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("info"," -- MainActivity [ onRestoreInstanceState ]");
    }

    class ChecAuthentication extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... urlString) {
            Boolean result = false;
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 25000);
            HttpConnectionParams.setSoTimeout(httpParams, 25000);

            DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(urlString[0]);
            Log.i("info",urlString[0]);

            HttpContext httpContext = new BasicHttpContext();
            httpContext.setAttribute(ClientContext.COOKIE_STORE, SApplication.cookieStore);

            try {
                HttpResponse httpResponse = httpClient.execute(httpPost, httpContext);
                HttpEntity httpEntity = httpResponse.getEntity();
                String message = EntityUtils.toString(httpEntity);
                if (message.equals("Error")){
                    result = false;
                }else{
                    result = true;
                }
                Log.i("info", message);

            } catch (IOException e) {
                result = false;
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            boolean value = result.booleanValue();
            if (value){
                Log.i("info","Success login");
                Intent intent = new Intent(getApplicationContext(), NavigationDrawer.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }else {
                Log.i("info","Error login");
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
    }

}