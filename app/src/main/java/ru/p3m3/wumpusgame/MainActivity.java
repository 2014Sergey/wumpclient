package ru.p3m3.wumpusgame;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends MyActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void onLoginButtonClick(View v) {
    goAllowBack(LoginActivity.class);
  }
  public void onRegisterButtonClick(View v) {
    goAllowBack(RegisterActivity.class);
  }
  public void onPlayOfflineButtonClick(View v) {
    goDenyBack(MainMenuActivity.class);
  }
}