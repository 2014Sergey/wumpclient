package ru.p3m3.wumpusgame.utils;

import android.text.Editable;
import android.text.TextWatcher;

import java.util.function.Consumer;

import ru.p3m3.wumpusgame.C;

public final class RegexpWatcher implements TextWatcher {
    private final Consumer<Boolean> callback;
    private boolean valid = false;

    public RegexpWatcher(Consumer<Boolean> callback) {
        this.callback = callback;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        valid = charSequence.toString().matches(C.LOGIN_REGEXP);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        callback.accept(valid);
    }
}
