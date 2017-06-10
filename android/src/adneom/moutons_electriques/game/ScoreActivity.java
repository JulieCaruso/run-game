package adneom.moutons_electriques.game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Collections;

import adneom.moutons_electriques.game.adapter.ScoreAdapter;
import adneom.moutons_electriques.game.model.User;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ScoreActivity extends AppCompatActivity {

    @BindView(R.id.recycler)
    RecyclerView recycler;

    private ScoreAdapter adapter;

    private ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        ButterKnife.bind(this);

        adapter = new ScoreAdapter(this, users);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUsers();
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
                            Collections.sort(users);
                            adapter.setItems(users);
                        }
                    }
                });
    }
}
