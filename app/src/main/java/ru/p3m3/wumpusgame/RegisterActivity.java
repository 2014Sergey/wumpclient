package ru.p3m3.wumpusgame;

import android.content.Context;
import android.content.SharedPreferences;
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
import java.util.function.Function;

import ru.p3m3.wumpusgame.utils.HTTPUtils;
import ru.p3m3.wumpusgame.utils.LengthWatcher;
import ru.p3m3.wumpusgame.utils.MustBeEqualWatcher;
import ru.p3m3.wumpusgame.utils.RegexpWatcher;

public class RegisterActivity extends MyActivity {
  private RegisterAsyncTask task;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    MaterialButton registerButton = findViewById(R.id.registerButton);

    TextInputLayout loginLayout = findViewById(R.id.loginEditTextLayout);
    TextInputLayout passwordLayout = findViewById(R.id.passwordEditTextLayout);
    TextInputLayout passwordAgainLayout = findViewById(R.id.passwordAgainEditTextLayout);

    TextInputEditText loginEditText = findViewById(R.id.loginEditText);
    TextInputEditText passwordEditText = findViewById(R.id.passwordEditText);
    TextInputEditText passwordAgainEditText = findViewById(R.id.passwordAgainEditText);


    loginEditText.addTextChangedListener(new RegexpWatcher(getEditTextCallback(registerButton, loginLayout, getString(R.string.login_helper_text))));
    passwordEditText.addTextChangedListener(new LengthWatcher(getEditTextCallback(registerButton, passwordLayout, getString(R.string.password_helper_text))));

    passwordEditText.addTextChangedListener(new MustBeEqualWatcher(getEditTextCallback(registerButton, passwordAgainLayout, getString(R.string.password_again_helper_text)), passwordAgainEditText));
    passwordAgainEditText.addTextChangedListener(new MustBeEqualWatcher(getEditTextCallback(registerButton, passwordAgainLayout, getString(R.string.password_again_helper_text)), passwordEditText));
  }

  private Consumer<Boolean> getEditTextCallback(MaterialButton registerButton, TextInputLayout layout, String helperText) {
    return valid -> {
      boolean enable = !(layout.isHelperTextEnabled() || valid);
      layout.setHelperTextEnabled(!valid);
      if (enable) layout.setHelperText(helperText);
      registerButton.setEnabled(isAllFieldsFilledCorrect());
    };
  }

  private boolean isAllFieldsFilledCorrect() {
    TextInputLayout loginEditTextLayout = findViewById(R.id.loginEditTextLayout);
    TextInputLayout passwordEditTextLayout = findViewById(R.id.passwordEditTextLayout);
    TextInputLayout passwordAgainEditTextLayout = findViewById(R.id.passwordAgainEditTextLayout);
    return !(loginEditTextLayout.isHelperTextEnabled()
            || passwordEditTextLayout.isHelperTextEnabled()
            || passwordAgainEditTextLayout.isHelperTextEnabled());
  }

  public void onRegisterButtonClick(View v) {
    TextInputEditText loginEditText = findViewById(R.id.loginEditText);
    TextInputEditText passwordEditText = findViewById(R.id.passwordEditText);

    String login = loginEditText.getText() == null ? "" : loginEditText.getText().toString();
    String password = passwordEditText.getText() == null ? "" : passwordEditText.getText().toString();

    task = new RegisterAsyncTask(this::onResponseReceived, login, password);
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
    findViewById(R.id.passwordAgainEditText).setEnabled(lock);
    findViewById(R.id.registerButton).setEnabled(lock);
  }

  private static class RegisterAsyncTask extends AsyncTask<Void, Void, Void> {

    private final String login;
    private final String password;
    private String uuid;

    private final Consumer<String> callback;

    public RegisterAsyncTask(Consumer<String> callback, String login, String password) {
      super();
      this.login = login;
      this.password = password;
      this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... args) {
      this.uuid = HTTPUtils.register(this.login, this.password);
      return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
      this.callback.accept(this.uuid);
    }
  }
}