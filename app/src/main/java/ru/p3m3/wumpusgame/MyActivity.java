package ru.p3m3.wumpusgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MyActivity extends AppCompatActivity {
  private boolean isPrevActivityExists;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    isPrevActivityExists = getIntent().hasCategory(C.PREV);
    super.onCreate(savedInstanceState);
  }

  protected final String getSession() {
    return getSharedPreferences(C.SHARED_PREFERENCES, MODE_PRIVATE)
        .getString("session", C.ERROR_PLACEHOLDER);
  }

  @Override
  public void onBackPressed() {
    if (isPrevActivityExists)
      super.onBackPressed();
    overridePendingTransition(0, 0);
  }
  protected <T> void goAllowBack(Class<T> cls) {
    Intent intent = new Intent(this, cls);
    intent.addCategory(C.PREV);
    startActivity(intent);
    overridePendingTransition(0, 0);
  }
  protected <T> void goDenyBack(Class<T> cls) {
    Intent intent = new Intent(this, cls);
    startActivity(intent);
    overridePendingTransition(0, 0);
    finish();
    overridePendingTransition(0, 0);
  }
}