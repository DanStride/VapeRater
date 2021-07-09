package com.upskilled.vaperater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RandomJokeActivity extends AppCompatActivity {

    private TextView mSetupText;
    private TextView mPunchlineText;
    private TextView mSetupLabel;
    private TextView mPunchlineLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set up screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_joke);

        mSetupText = findViewById(R.id.rj_setup_multiline);
        mPunchlineText = findViewById(R.id.rj_punchline_multiline);
        mSetupLabel = findViewById(R.id.rj_setup_label);
        mPunchlineLabel = findViewById(R.id.rj_punchline_label);


        setFontSize();

        // Establish connection to web server and build request
        OkHttpClient client = new OkHttpClient();

        String url = "https://official-joke-api.appspot.com/random_joke";

        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                // Use Gson to create Java object out of the response from web server
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Gson gson = new Gson();
                    final RandomJoke newJoke = gson.fromJson(myResponse, RandomJoke.class);
                    RandomJokeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Set views to display joke data
                            mSetupText.setText(newJoke.GetSetup());
                            mPunchlineText.setText(newJoke.GetPunchline());
                        }
                    });
                } else {

                }
            }
        });
    }

    private void setFontSize() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean switchPref = sharedPref.getBoolean(SettingsActivity.KEY_PREF_LARGE_FONT_SWITCH, false);

        if (switchPref) {
            mSetupText.setTextSize(22);
            mPunchlineText.setTextSize(22);
            mSetupLabel.setTextSize(26);
            mPunchlineLabel.setTextSize(26);
        } else {
            mSetupText.setTextSize(18);
            mPunchlineText.setTextSize(18);
            mSetupLabel.setTextSize(22);
            mPunchlineLabel.setTextSize(22);
        }
    }
}