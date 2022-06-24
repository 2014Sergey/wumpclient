package ru.p3m3.wumpusgame;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import ru.p3m3.wumpusgame.utils.HTTPUtils;
import ru.p3m3.wumpusgame.utils.WSUtils;

public class FindRoomActivity extends MyActivity {
  private Timer refreshTimer;
  private String[] uuids;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_find_room);
    final ListView list = findViewById(R.id.list);
    list.setOnItemClickListener(this::joinRoomClick);
  }

  private void actJoin(int position) {
    try {
      String uuid = uuids[position];
      Log.d("DUNG", uuid);
      JSONObject json = new JSONObject();
      json.put("act", "JOIN");
      json.put("session", getSession());
      json.put("dung", uuid);
      WSUtils.connect();
      WSUtils.once(this, "OKJOIN", this::onOkJoin);
      WSUtils.send(json.toString());
    } catch (Exception e) {Log.e("ERROR", e.toString());}
  }

  @Override
  protected void onResume() {
    Consumer<String[][]> callback = this::refreshListView;
    super.onResume();
    refreshTimer = new Timer();
    refreshTimer.schedule(new TimerTask() {
      public void run() {
        (new FindRoomActivity.RefreshAsyncTask(callback)).execute();
      }
    }, 0, 10000);
  }

  @Override
  protected void onPause() {
    super.onPause();
    refreshTimer.cancel();
  }

  public void onOkJoin(Object[] o) {
    goAllowBack(LobbyActivity.class);
  }

  public void refreshListView(String[][] dungs) {
    final ListView list = findViewById(R.id.list);
    uuids = new String[dungs.length];
    String[] texts = new String[dungs.length];
    for (int i = 0; i < dungs.length; i++) {
      uuids[i] = dungs[i][0];
      texts[i] = dungs[i][1];
    }
    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.activity_list_item, texts);
    list.setAdapter(adapter);
  }

  public void joinRoomClick(AdapterView<?> parent, View view, int position, long id) {
    actJoin(position);
  }

  public void onBackButtonClick(View v) {
    finish();
  }

  private static class RefreshAsyncTask extends AsyncTask<Void, Void, Void> {
    private final Consumer<String[][]> callback;
    private String[][] dungs;

    public RefreshAsyncTask(Consumer<String[][]> callback) {
      super();
      this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... args) {
      this.dungs = HTTPUtils.dungs();
      return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
      this.callback.accept(this.dungs);
    }
  }
}