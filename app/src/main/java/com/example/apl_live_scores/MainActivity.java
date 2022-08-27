package com.example.apl_live_scores;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LOG";
    TextView inning, target, score, overs, bat_team, ball_team, type, extras;
    RecyclerView batsmanRecycler, bowlerRecycler;
    RelativeLayout contents;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inning = (TextView) findViewById(R.id.inning);
        target = (TextView) findViewById(R.id.target);
        score = (TextView) findViewById(R.id.score);
        overs = (TextView) findViewById(R.id.over);
        bat_team = (TextView) findViewById(R.id.batingHead);
        ball_team = (TextView) findViewById(R.id.bowlingHead);
        type = (TextView) findViewById(R.id.roundType);
        extras = (TextView) findViewById(R.id.extras);
        batsmanRecycler = (RecyclerView) findViewById(R.id.batsmanList);
        bowlerRecycler = (RecyclerView) findViewById(R.id.bowlerList);
        contents = (RelativeLayout) findViewById(R.id.content);


        db.collection("apl-teams")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                HashMap teams = new HashMap();
                                for (QueryDocumentSnapshot document : value) {
                                    teams.put(document.getId(),document.getData().get("name"));
                                }
                                db.collection("apl-players")
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                HashMap players = new HashMap();
                                                for (QueryDocumentSnapshot document : value) {
                                                    players.put(document.getId(),document.getData().get("name"));
                                                }
                                                db.collection("apl-live-match")
                                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                                Log.d(TAG, "isLive: " + !value.isEmpty());
                                                                if (value.isEmpty())
                                                                    setLayoutInvisible();
                                                                else
                                                                    setLayoutVisible();

                                                                for (QueryDocumentSnapshot document : value) {
                                                                    HashMap live_data = (HashMap) document.getData();
                                                                    HashMap inning_detail = (HashMap) live_data.get("inning_detail");
                                                                    HashMap current_inning = (HashMap) inning_detail.get(live_data.get("inning").toString());
                                                                    ArrayList versions = (ArrayList) current_inning.get("versions");
                                                                    HashMap current_version = (HashMap) versions.get(Integer.parseInt(current_inning.get("cur_version").toString()));
                                                                    Log.d(TAG, "Current Inning :" + current_version);

                                                                    type.setText(live_data.get("type") + " Round");
                                                                    if (live_data.get("inning").toString().equals("1")) {
                                                                        inning.setText(live_data.get("inning")+"st Inn");
                                                                        target.setText("");
                                                                    } else {
                                                                        inning.setText(live_data.get("inning")+"nd Inn");
                                                                        target.setText("Target:" + current_version.get("target"));
                                                                    }
                                                                    score.setText(current_version.get("score") + "-" + current_version.get("wicket"));
                                                                    overs.setText("("+current_version.get("over")+"."+current_version.get("ball")+")");
                                                                    extras.setText(current_version.get("extras").toString());

                                                                    //BATSMANS
                                                                    bat_team.setText("Batting : " + teams.get(current_version.get("batting").toString()));

                                                                    ArrayList<BatsmanModel> batsmanList = new ArrayList<>();
                                                                    for (HashMap batsman : (ArrayList<HashMap>) current_version.get("bat_detail")) {
                                                                        String _batsmanName;
                                                                        if (batsman.get("id").toString().equals("select")) {
                                                                            _batsmanName = "unknown";
                                                                        } else {
                                                                            _batsmanName = players.get(batsman.get("id").toString()).toString();
                                                                        }
                                                                        batsmanList.add(new BatsmanModel(_batsmanName+" "+batsman.get("onStrike").toString(),
                                                                                batsman.get("runs").toString(),
                                                                                batsman.get("status").toString()));
                                                                    }

                                                                    BatsmanAdpater bat_adapter = new BatsmanAdpater(batsmanList);
                                                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                                                    batsmanRecycler.setLayoutManager(layoutManager);
                                                                    batsmanRecycler.setItemAnimator(new DefaultItemAnimator());
                                                                    batsmanRecycler.setAdapter(bat_adapter);

                                                                    //BOWLERS
                                                                    ball_team.setText("Bowling : " + teams.get(current_version.get("fielding").toString()));

                                                                    ArrayList<BowlerModel> bowlerList = new ArrayList<>();

                                                                    ArrayList<HashMap> raw_bowlers = (ArrayList<HashMap>) current_version.get("ball_detail");

                                                                    for (int i = raw_bowlers.size()-1; i>=0; i--) {
                                                                        String _bowlerName;
                                                                        if (raw_bowlers.get(i).get("id").toString().equals("select")) {
                                                                            _bowlerName = "unknown";
                                                                        } else {
                                                                            _bowlerName = players.get(raw_bowlers.get(i).get("id").toString()).toString();
                                                                        }
                                                                        bowlerList.add(new BowlerModel(_bowlerName+" "+raw_bowlers.get(i).get("current").toString(),
                                                                                raw_bowlers.get(i).get("wickets").toString()+" - "+raw_bowlers.get(i).get("total_runs").toString()));
                                                                    }

                                                                    BowlerAdpater ball_adapter = new BowlerAdpater(bowlerList);
                                                                    RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
                                                                    bowlerRecycler.setLayoutManager(layoutManager2);
                                                                    bowlerRecycler.setItemAnimator(new DefaultItemAnimator());
                                                                    bowlerRecycler.setAdapter(ball_adapter);
                                                                }
                                                            }
                                                        });
                                            }
                                        });
                            }
                        });

    }

    public void setLayoutInvisible() {
        contents.setVisibility(View.GONE);
    }
    public void setLayoutVisible() {
        contents.setVisibility(View.VISIBLE);
    }
}