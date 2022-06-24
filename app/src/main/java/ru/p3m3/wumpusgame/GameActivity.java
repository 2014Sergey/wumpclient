package ru.p3m3.wumpusgame;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import ru.p3m3.wumpusgame.utils.WSUtils;

@SuppressLint("DefaultLocale")
public class GameActivity extends MyActivity {
  private boolean controls = false;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_game);
    WSUtils.on(this, "LOOKUP", this::onLookup);
    WSUtils.on(this, "STEPS", this::onSteps);
    WSUtils.on(this, "HUNTERENTER", this::onHunterEnter);
    WSUtils.on(this, "HUNTERLEAVE", this::onHunterLeave);
    //WSUtils.on(this, "WUMPUSENTER", this::onWumpusEnter);
    WSUtils.on(this, "WHISTLE", this::onWhistling);
    WSUtils.on(this, "DEATH", this::onDeath);
    WSUtils.on(this, "BATHERE", this::onBatHere);
    WSUtils.on(this, "BATTRAVELEND", this::onBatTravelEnd);
    WSUtils.on(this, "PITHERE", this::onPitHere);
    WSUtils.on(this, "WIN", this::onWin);
    WSUtils.on(this, "OUTOFAMMO", this::onOutOfAmmo);
    controls = true;
  }

  @Override
  protected void onResume() {
    super.onResume();
    actLookup();
  }

  //
  public void onLookup(Object[] o) {
    Log.d("onLookup","");
    try {
      final TextView BattleLog = findViewById(R.id.battle_log);
      final Button LeftDoor = findViewById(R.id.leftDoor);
      final Button MiddleDoor = findViewById(R.id.middleDoor);
      final Button RightDoor = findViewById(R.id.rightDoor);
      JSONObject json = (JSONObject)(o[0]);
      BattleLog.append(String.format("\nВы находитесь в комнате %d", json.getInt("num")));
      final JSONArray hunters = json.getJSONArray("hunters");
      if (hunters.length() > 1) BattleLog.append("\nВ комнате стоят другие охотники");
      if (json.getBoolean("wump")) BattleLog.append("\nМерзкий запах...");
      if (json.getBoolean("breeze")) BattleLog.append("\nХолодно...");
      if (json.getBoolean("flop")) BattleLog.append("\nЧто-то пищит...");
      if (json.getBoolean("cough")) BattleLog.append("\nКто-то кашляет?");
      final JSONArray doors = json.getJSONArray("doors");
      LeftDoor.setText(doors.getString(0));
      MiddleDoor.setText(doors.getString(1));
      RightDoor.setText(doors.getString(2));
      controls = true;
    } catch (Exception e) {e.printStackTrace();}
  }
  public void onSteps(Object[] o) {
    Log.d("onSteps","");
    final TextView BattleLog = findViewById(R.id.battle_log);
    BattleLog.append("\nВы слышите чьи-то шаги");
  }
  public void onHunterEnter(Object[] o) {
    Log.d("onHunterEnter","");
    final TextView BattleLog = findViewById(R.id.battle_log);
    BattleLog.append("\nВ комнату вошёл другой охотник");
  }
  public void onHunterLeave(Object[] o) {
    Log.d("onHunterEnter","");
    final TextView BattleLog = findViewById(R.id.battle_log);
    BattleLog.append("\nДругой охотник покинул комнату");
  }
  public void onWumpusEnter(Object[] o) {
    Log.d("onWumpusEnter","");
    final TextView BattleLog = findViewById(R.id.battle_log);
    BattleLog.append("\n!!!В КОМНАТУ ВОШЁЛ ВАМПУС!!!");
    controls = false;
  }
  public void onWhistling(Object[] o) {
    Log.d("onWhistling","");
    final TextView BattleLog = findViewById(R.id.battle_log);
    BattleLog.append("\nЧто-то свистнуло...");
  }
  public void onWin(Object[] o) {
    Log.d("onWin","");
    try {
      controls = false;
      final TextView BattleLog = findViewById(R.id.battle_log);
      BattleLog.append("\nВы победили");
      BattleLog.append("\nВампус убит");
//      goDenyBack(MainMenuActivity.class);
    } catch (Exception e) {Log.e("ERROR", e.toString());}
  }
  public void onDeath(Object[] o) {
    Log.d("onDeath","");
    try {
      controls = false;
      JSONObject json = (JSONObject)(o[0]);
      final TextView BattleLog = findViewById(R.id.battle_log);
      BattleLog.append("\nВы умерли");
      BattleLog.append(String.format("\nПричина: %s", json.getString("cause")));
//      goDenyBack(MainMenuActivity.class);
    } catch (Exception e) {Log.e("ERROR", e.toString());}
  }
  public void onPitHere(Object[] o) {
    Log.d("onPitHere","");
    final TextView BattleLog = findViewById(R.id.battle_log);
    BattleLog.append("\nВнутри комнаты нет пола. Сплошная яма.");
    controls = false;
  }
  public void onBatHere(Object[] o) {
    Log.d("onBatHere","");
    final TextView BattleLog = findViewById(R.id.battle_log);
    BattleLog.append("\nГИГАНТСКИЕ ЛЕТУЧИЕ МЫШИ ОКРУЖАЮТ ВАС");
    controls = false;
    final Button LeftDoor = findViewById(R.id.leftDoor); LeftDoor.setText("");
    final Button MiddleDoor = findViewById(R.id.middleDoor); MiddleDoor.setText("");
    final Button RightDoor = findViewById(R.id.rightDoor); RightDoor.setText("");
    final Button bowButton = findViewById(R.id.bow); bowButton.setText("В полёте нельзя использовать лук)");
  }
  public void onBatTravelEnd(Object[] o) {
    Log.d("onBatTravelEnd","");
    final Button bowButton = findViewById(R.id.bow); bowButton.setText("Достать лук");
    try {
      final TextView BattleLog = findViewById(R.id.battle_log);
      JSONObject json = (JSONObject)(o[0]);
      BattleLog.append(String.format("\nЛетучие мыши перенесли вас в комнату %d", json.getInt("num")));
    } catch (Exception e) {Log.e("ERROR", e.toString());}
  }
  public void onOutOfAmmo(Object[] o) {
    Log.d("onBatHere","");
    final TextView BattleLog = findViewById(R.id.battle_log);
    BattleLog.append("\nУ вас закончились стрелы");
  }
  //
  //
  public void actLookup() {
    try {
      JSONObject json = new JSONObject();
      json.put("act", "LOOKUP");
      WSUtils.send(json.toString());
    } catch (Exception e) {Log.e("ERROR", e.toString());}
  }
  public void actGotoDoor(View v) {
    if (!controls) return;
    controls = false;
    try {
      JSONObject json = new JSONObject();
      json.put("act", "GOTODOOR");
      json.put("num", Integer.parseInt(((Button)v).getText().toString()));
      WSUtils.send(json.toString());
    } catch (Exception e) {Log.e("ERROR", e.toString());}
  }
  public void actShoot(View v) {
    if (!controls) return;
    final Button bowButton = findViewById(R.id.bow);

    try {
      JSONObject json = new JSONObject();
      json.put("act", "SHOOT");
      JSONArray arr = new JSONArray();
      final RoomPicker pick1 = findViewById(R.id.pick_1);
      final RoomPicker pick2 = findViewById(R.id.pick_2);
      final RoomPicker pick3 = findViewById(R.id.pick_3);
      if (pick1.getValue() == 0) {
        return;
      }
      arr.put(pick1.getValue());
      if (pick2.getValue() != 0) {
        arr.put(pick2.getValue());
        if (pick3.getValue() != 0)
          arr.put(pick3.getValue());
      }
      json.put("rooms", arr);
      WSUtils.send(json.toString());
    } catch (Exception e) {Log.e("ERROR", e.toString());}
    showShootLayout(bowButton);
  }
  //
  //
  public void showShootLayout(View v) {
    if (!controls) return;
    final RoomPicker pick1 = findViewById(R.id.pick_1); pick1.setValue(0);
    final RoomPicker pick2 = findViewById(R.id.pick_2); pick2.setValue(0);
    final RoomPicker pick3 = findViewById(R.id.pick_3); pick3.setValue(0);
    final ConstraintLayout picker = findViewById(R.id.picker);
    if (picker.getVisibility() != View.VISIBLE) {
      picker.setVisibility(View.VISIBLE);
      ((Button)v).setText("Убрать лук");
    }
    else {
      picker.setVisibility(View.GONE);
      ((Button)v).setText("Достать лук");
    }
  }

  public void exitGameYes(View v) {
    WSUtils.close();
    goDenyBack(MainMenuActivity.class);
  }

  public void exitGameNo(View v) {
    final ConstraintLayout popup = findViewById(R.id.exitPopup);
    popup.setVisibility(View.GONE);
  }

  @Override
  public void onBackPressed() {
    final ConstraintLayout popup = findViewById(R.id.exitPopup);
    if (popup.getVisibility() != View.VISIBLE) popup.setVisibility(View.VISIBLE);
    else popup.setVisibility(View.GONE);
  }
}
