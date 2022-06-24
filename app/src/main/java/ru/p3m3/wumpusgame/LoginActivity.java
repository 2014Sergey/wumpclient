package ru.p3m3.wumpusgame;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import ru.p3m3.wumpusgame.utils.HTTPUtils;
import ru.p3m3.wumpusgame.utils.LengthWatcher;
import ru.p3m3.wumpusgame.utils.RegexpWatcher;

public class LoginActivity extends MyActivity {
  private LoginAsyncTask task = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
  }

  public void onLoginButtonClick(View v) {
    TextInputEditText loginEditText = findViewById(R.id.loginEditText);
    TextInputEditText passwordEditText = findViewById(R.id.passwordEditText);

    String login = loginEditText.getText() == null ? "" : loginEditText.getText().toString();
    String password = passwordEditText.getText() == null ? "" : passwordEditText.getText().toString();

    task = new LoginAsyncTask(this::onResponseReceived, login, password);
    task.execute();
    setLock(false);
  }

  private void onResponseReceived(String uuid) {
    Log.d("UUID", uuid);
    task = null;
    setLock(true);
    if (uuid.equals(C.ERROR_PLACEHOLDER)) return;
    getSharedPreferences(C.SHARED_PREFERENCES, MODE_PRIVATE)
            .edit().putString("session", uuid).apply();
    goDenyBack(MainMenuActivity.class);
  }

  private void setLock(boolean lock) {
    findViewById(R.id.loginEditText).setEnabled(lock);
    findViewById(R.id.passwordEditText).setEnabled(lock);
    findViewById(R.id.loginButton).setEnabled(lock);
  }

  private static class LoginAsyncTask extends AsyncTask<Void, Void, Void> {
    private final String login;
    private final String password;
    private String uuid;

    private final Consumer<String> callback;

    public LoginAsyncTask(Consumer<String> callback, String login, String password) {
      super();
      this.login = login;
      this.password = password;
      this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... args) {
      this.uuid = HTTPUtils.login(login, password);
      return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
      this.callback.accept(this.uuid);
    }
  }
}