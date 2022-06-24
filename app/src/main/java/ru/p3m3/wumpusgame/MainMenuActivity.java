package ru.p3m3.wumpusgame;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import java.util.function.Consumer;

import ru.p3m3.wumpusgame.utils.HTTPUtils;
import ru.p3m3.wumpusgame.utils.WSUtils;

public class MainMenuActivity extends MyActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_menu);
  }

  public void onFindRoomButtonClick(View v) {
    goAllowBack(FindRoomActivity.class);
  }

  public void onCreateRoomButtonClick(View v) {
    findViewById(R.id.findRoomButton).setActivated(false);
    findViewById(R.id.createRoomButton).setActivated(false);
    (new CreateLobbyAsyncTask(this::actJoin, getSession())).execute();
  }

  public void onSignOutButtonClick(View v) {
    getSharedPreferences(C.SHARED_PREFERENCES, MODE_PRIVATE).edit().clear().apply();
    goDenyBack(MainActivity.class);
  }

  public void onOkJoin(Object[] o) {
    goAllowBack(LobbyActivity.class);
  }

  private void actJoin(String uuid) {
    try {
      Log.d("DUNG", uuid);
      JSONObject json = new JSONObject();
      json.put("act", "JOIN");
      json.put("session", getSession());
      json.put("dung", uuid);
      WSUtils.connect();
      WSUtils.once(this, "OKJOIN", this::onOkJoin);
      WSUtils.send(json.toString());
    } catch (Exception e) {
      Log.e("ERROR", e.toString());
    }
  }

  private static class CreateLobbyAsyncTask extends AsyncTask<Void, Void, Void> {
    private String uuid;
    private final String session;

    private final Consumer<String> callback;

    public CreateLobbyAsyncTask(Consumer<String> callback, String session) {
      super();
      this.callback = callback;
      this.session = session;
    }

    @Override
    protected Void doInBackground(Void... args) {
      this.uuid = HTTPUtils.newdung(session);
      return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
      this.callback.accept(this.uuid);
    }
  }
}