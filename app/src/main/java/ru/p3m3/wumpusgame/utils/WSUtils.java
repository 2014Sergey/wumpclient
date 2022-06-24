package ru.p3m3.wumpusgame.utils;

import android.app.Activity;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.HashMap;
import java.util.function.Consumer;

import ru.p3m3.wumpusgame.C;

public class WSUtils {
    public static MyWSC wsc;
    public static void on(Activity a, String eventName, Consumer<Object[]> callback) {
        wsc.events.put(eventName, objects -> {a.runOnUiThread(() -> callback.accept(objects));});
    }
    public static void once(Activity a, String eventName, Consumer<Object[]> callback) {
        if (wsc.events.containsKey(eventName)) return;
        wsc.events.put(eventName, objects -> {
            a.runOnUiThread(() -> callback.accept(objects));
            wsc.events.remove(eventName);
        });
    }
    public static void send(String message) {
        wsc.send(message);
    }
    public static void connect() {
        try {
            wsc = new MyWSC();
            wsc.connectBlocking();
        } catch (InterruptedException e) {Log.e("CONNECTION", e.toString());}
    }
    public static void close() {
        if (wsc != null)
        wsc.close();
    }
}
class MyWSC extends WebSocketClient {
    public HashMap<String, Consumer<Object[]>> events = new HashMap<>();

    public void emit(String eventName, Object json) {
        events.get(eventName).accept(new Object[] {json});
    }

    public MyWSC() {
        super(URI.create(C.WS_SERVER));
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.e("CONNECTED","");
    }

    @Override
    public void onMessage(String message) {
        try {
            JSONObject json = new JSONObject(message);
            switch (json.getString("act")) {
                case "OK":
                    Log.d("act/OK", message);
                    break;
                case "ERROR":
                    Log.d("act/ERROR", message);
                    break;
            }
            emit(json.getString("act"), json);
        } catch (JSONException e) {}
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        WSUtils.wsc = null;
    }

    @Override
    public void onError(Exception ex) { }
}