package com.example.famuchastegui.toy2;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.famuchastegui.toy2.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

   private  EditText mSearchBoxEditText;

   private  TextView mUrlDisplayTextView;

   private  TextView mSearchResults;

   private  TextView errorMessageTextView;

   //nuevo cambio
   private int numero2;
   private  int numero3;

   ProgressBar mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);

        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);

        mSearchResults = (TextView) findViewById(R.id.tv_github_search_results_json);

        errorMessageTextView = (TextView) findViewById(R.id.tv_error_message);

        mLoading = (ProgressBar) findViewById(R.id.pb_loading);


    }

    void makeGithubSearchQuery(){

        String githubQuery = mSearchBoxEditText.getText().toString();
        if(githubQuery != null){
            System.out.print(githubQuery);
        }else{
            System.out.print("Caputramos un valor nullo, da point exception");
        }


        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
        mUrlDisplayTextView.setText(githubSearchUrl.toString());

        String githubSearchResults =  null;

        new GithubQueryTask().execute(githubSearchUrl);

    }

    private void showJsonDataView(){

        errorMessageTextView.setVisibility(View.INVISIBLE);
        mSearchResults.setVisibility(View.VISIBLE);
    }

    public class GithubQueryTask extends AsyncTask<URL , Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(URL... urls){

            URL searchUrl = urls[0];
            String githubSearchResults = null;

            try{
                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);

            }catch(IOException e){

                e.printStackTrace();
            }

            return githubSearchResults;

        }

        public void showErrorMessage(){

            errorMessageTextView.setText("Error de conexón, vuelve a intentar más tarde");

        }


        protected void onPostExecute(String s){

            mLoading.setVisibility(View.INVISIBLE);

            if(s != null && !s.equals("")){

                mSearchResults.setText(s);

            }else{

                showErrorMessage();
            }



        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
         makeGithubSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
