package ru.p3m3.wumpusgame;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.p3m3.wumpusgame.utils.WSUtils;

public class LobbyActivity extends MyActivity {

  private ArrayList<String> players = new ArrayList<>(5);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_lobby);
    WSUtils.on(this, "START", this::onStart);
    WSUtils.on(this, "JOIN", this::onJoin);
    WSUtils.on(this, "LEAVE", this::onLeave);
    WSUtils.on(this, "GETHUNTERS", this::onGetHunters);
    actGetHunters();
  }

  public void startGame(View v) {
    actStart();
  }

  public void onStart(Object[] o) {
    goDenyBack(GameActivity.class);
  }

  public void onJoin(Object[] o) {
    try {
      JSONObject json = (JSONObject)(o[0]);
      String name = json.getString("name");
      players.add(name);
      final ListView list = findViewById(R.id.players);
      list.setAdapter(new ArrayAdapter<>(this, R.layout.activity_list_item, players));
    } catch (Exception e) { Log.e("onJoin", e.toString()); }
  }
  public void onLeave(Object[] o) {
    try {
      JSONObject json = (JSONObject)(o[0]);
      String name = json.getString("name");
      players.remove(name);
      final ListView list = findViewById(R.id.players);
      list.setAdapter(new ArrayAdapter<>(this, R.layout.activity_list_item, players));
    } catch (Exception e) { Log.e("onLeave", e.toString()); }
  }
  public void onGetHunters(Object[] o) {
    Log.e("onGetHunters","");
    try {
      JSONObject json = (JSONObject)(o[0]);
      JSONArray hunters = json.getJSONArray("hunters");
      players.clear();
      for (int i = 0; i < hunters.length(); i++) {
        players.add(hunters.getString(i));
      }
      final ListView list = findViewById(R.id.players);
      list.setAdapter(new ArrayAdapter<>(this, R.layout.activity_list_item, players));
    } catch (Exception e) { Log.e("onGetHunters", e.toString()); }
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    actLeave();
  }

  private void actGetHunters() {
    Log.e("actGetHunters","");
    try {
      JSONObject json = new JSONObject();
      json.put("act","GETHUNTERS");
      WSUtils.send(json.toString());
    } catch (Exception e) { Log.e("ERROR", e.toString()); }
  }
  private void actLeave() {
    try {
      JSONObject json = new JSONObject();
      json.put("act","LEAVE");
      json.put("session", getSession());
      WSUtils.send(json.toString());
    } catch (Exception e) { Log.e("ERROR", e.toString()); }
  }

  private void actStart() {
    try {
      JSONObject json = new JSONObject();
      json.put("act","START");
      json.put("session", getSession());
      WSUtils.send(json.toString());
    } catch (Exception e) { Log.e("ERROR", e.toString()); }
  }
}