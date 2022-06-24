package ru.p3m3.wumpusgame.utils;

import android.text.Editable;
import android.text.TextWatcher;

import java.util.function.Consumer;

public class LengthWatcher implements TextWatcher {
    private final Consumer<Boolean> callback;
    private boolean valid = false;

    public LengthWatcher(Consumer<Boolean> callback) {
        this.callback = callback;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String str = charSequence.toString();
        valid = str.length() >= 1 && str.length() <= 30;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        callback.accept(valid);
    }
}
