package adneom.moutons_electriques.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import adneom.moutons_electriques.game.adapter.AdapterName;
import adneom.moutons_electriques.game.model.User;
import adneom.moutons_electriques.game.util.Cache;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.name)
    AutoCompleteTextView name;
    @BindView(R.id.see_scores)
    Button seeScores;

    private ArrayList<User> users;

    private AdapterName adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (Cache.get(App.KEY_USERS) == null) {
            users = new ArrayList<>();
        } else {
            users = ((ArrayList<User>) Cache.get(App.KEY_USERS));
        }
        Cache.set(App.KEY_USERS, users);

        seeScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeScores();
            }
        });

        //sendUsers();
        adapter = new AdapterName(this, users, R.layout.simple_text_dropdown, new AdapterName.OnClickUser() {
            @Override
            public void onClick(User user) {
                Log.d("azerty", String.valueOf(user));
                enterGame(user);
            }
        });
        name.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUsers();
    }

    private void enterGame(User user) {
        Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
        intent.putExtra(App.EXTRA_USER, user);
        startActivity(intent);
    }

    private void seeScores() {
        Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
        startActivity(intent);
    }

    private void getUsers() {
        Ion.with(this)
                .load("GET", App.URL + "/users")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if (result != null) {
                            users.clear();
                            for (int i = 0; i < result.size(); i++) {
                                JsonObject userJson = result.get(i).getAsJsonObject();
                                User user = new User();
                                user.setId(userJson.get("id").getAsInt());
                                user.setFirstname(userJson.get("firstname").getAsString());
                                user.setLastname(userJson.get("lastname").getAsString());
                                if (userJson.has("photo1")) {
                                    if (!userJson.get("photo1").isJsonNull()) {
                                        user.setPhoto1(userJson.get("photo1").getAsString());
                                    }
                                }
                                if (userJson.has("photo2")) {
                                    if (!userJson.get("photo2").isJsonNull()) {
                                        user.setPhoto2(userJson.get("photo2").getAsString());
                                    }
                                }
                                if (userJson.has("score")) {
                                    if (!userJson.get("score").isJsonNull()) {
                                        user.setScore(userJson.get("score").getAsInt());
                                    } else {
                                        user.setScore(0);
                                    }
                                } else {
                                    user.setScore(0);
                                }
                                users.add(user);
                            }
                            adapter.setItems(users);
                        }
                    }
                });
    }

    //send to server users from text
    private void sendUsers() {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("peoples.txt")));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                String[] splitLine = mLine.split(";");
                addUsers(splitLine[0], splitLine[1]);
                sendToServer(splitLine[0], splitLine[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //fill the list of users
    private void addUsers(String lastName, String firstName) {
        if (users == null) {
            users = new ArrayList<User>();
        }
        User u = new User();
        u.setLastname(lastName);
        u.setFirstname(firstName);
        users.add(u);
    }

    private void sendToServer(String firstName, String lastName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("firstname", firstName);
        jsonObject.addProperty("lastname", lastName);
        Ion.with(this)
                .load(App.URL + "/users")
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        /*
                        String[] sp = result.toString().split(";");
                        User user = new User();
                        user.setFirstname(sp[0]);
                        user.setLastname(sp[1]);
                        Log.i("Adneom", result.toString() + " " + user.getFirstname() + " - " + user.getLastname());
                        */
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
