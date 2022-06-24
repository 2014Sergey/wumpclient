package ru.p3m3.wumpusgame.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import ru.p3m3.wumpusgame.C;

public class HTTPUtils {
  private static final String REGISTER_URL = C.API_URL+"/register";
  private static final String LOGIN_URL = C.API_URL+"/login";
  private static final String NEWDUNG_URL = C.API_URL+"/newdung";
  private static final String DUNGS_URL = C.API_URL+"/dungs";

  public static String register(String login, String password) {
    try {
      URL url = new URL(REGISTER_URL);
      HttpURLConnection conn = (HttpURLConnection)url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("content-type", C.CONTENT_TYPE_JSON);
      conn.setDoOutput(true);
      try (OutputStream os = conn.getOutputStream()) {
        JSONObject json = new JSONObject()
            .put("login", login)
            .put("password", password);
        byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
        os.write(input);
      } catch (JSONException e) {
      } catch (IOException e) { }
      boolean ok = conn.getResponseCode() == 200;
      try (DataInputStream dis = new DataInputStream(ok
          ? conn.getInputStream()
          : conn.getErrorStream())) {
        int contentLength = Integer.parseInt(conn.getHeaderField("content-length"));
        byte[] body = new byte[contentLength];
        dis.readFully(body);
        JSONObject json = new JSONObject(new String(body, StandardCharsets.UTF_8));
        return json.getString("uuid");
      } catch (IOException e) {
      } catch (JSONException e) { }
    } catch (MalformedURLException e) {
    } catch (IOException e) {
    }
    return C.ERROR_PLACEHOLDER;
  }

  public static String login(String login, String password) {
    try {
      URL url = new URL(LOGIN_URL);
      HttpURLConnection conn = (HttpURLConnection)url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("content-type", C.CONTENT_TYPE_JSON);
      conn.setDoOutput(true);
      try (OutputStream os = conn.getOutputStream()) {
        JSONObject json = new JSONObject()
            .put("login", login)
            .put("password", password);
        byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
        os.write(input);
      } catch (JSONException e) {
      } catch (IOException e) { }
      boolean ok = conn.getResponseCode() == 200;
      try (DataInputStream dis = new DataInputStream(ok
          ? conn.getInputStream()
          : conn.getErrorStream())) {
        int contentLength = Integer.parseInt(conn.getHeaderField("content-length"));
        byte[] body = new byte[contentLength];
        dis.readFully(body);
        JSONObject json = new JSONObject(new String(body, StandardCharsets.UTF_8));
        return json.getString("uuid");
      } catch (IOException e) {
      } catch (JSONException e) { }
    } catch (MalformedURLException e) {
    } catch (IOException e) {
    }
    return C.ERROR_PLACEHOLDER;
  }

  public static String newdung(String session) {
    try {
      URL url = new URL(NEWDUNG_URL);
      HttpURLConnection conn = (HttpURLConnection)url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("content-type", C.CONTENT_TYPE_JSON);
      conn.setDoOutput(true);
      try (OutputStream os = conn.getOutputStream()) {
        JSONObject json = new JSONObject()
            .put("uuid", session);
        byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
        os.write(input);
      } catch (JSONException e) {
      } catch (IOException e) { }
      boolean ok = conn.getResponseCode() == 200;
      try (DataInputStream dis = new DataInputStream(ok
          ? conn.getInputStream()
          : conn.getErrorStream())) {
        int contentLength = Integer.parseInt(conn.getHeaderField("content-length"));
        byte[] body = new byte[contentLength];
        dis.readFully(body);
        JSONObject json = new JSONObject(new String(body, StandardCharsets.UTF_8));
        return json.getString("dung");
      } catch (IOException e) {
      } catch (JSONException e) { }
    } catch (MalformedURLException e) {
    } catch (IOException e) {
    }
    return C.ERROR_PLACEHOLDER;
  }

  public static String[][] dungs() {
    try {
      URL url = new URL(DUNGS_URL);
      HttpURLConnection conn = (HttpURLConnection)url.openConnection();
      conn.setRequestMethod("POST");
      boolean ok = conn.getResponseCode() == 200;
      try (DataInputStream dis = new DataInputStream(ok
          ? conn.getInputStream()
          : conn.getErrorStream())) {
        int contentLength = Integer.parseInt(conn.getHeaderField("content-length"));
        byte[] body = new byte[contentLength];
        dis.readFully(body);
        Log.d("DUNGS", new String(body, StandardCharsets.UTF_8));
        JSONArray json = new JSONArray(new String(body, StandardCharsets.UTF_8));
        String[][] strs = new String[json.length()][2];
        for (int i = 0; i < json.length(); i++) {
          JSONObject o = json.getJSONObject(i);
          String owner = o.has("owner") ? o.getString("owner") : "No owner";
          String str = String.format("P:%d/5 O:%s", o.getInt("players"), owner);
          String uuid = o.getString("uuid");
          strs[i] = new String[] {uuid, str};
        }
        return strs;
      } catch (IOException e) { Log.e("ERR", e.toString());
      } catch (JSONException e) { Log.e("ERR", e.toString()); }
    } catch (MalformedURLException e) { Log.e("ERR", e.toString());
    } catch (IOException e) { Log.e("ERR", e.toString());
    }
    return null;
  }
}
