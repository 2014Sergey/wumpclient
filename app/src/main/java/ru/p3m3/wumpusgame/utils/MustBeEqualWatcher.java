package ru.p3m3.wumpusgame.utils;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;

import java.util.function.Consumer;

public class MustBeEqualWatcher implements TextWatcher {
    private final Consumer<Boolean> callback;
    private final TextInputEditText other;
    private boolean valid = false;

    public MustBeEqualWatcher(Consumer<Boolean> callback, TextInputEditText other) {
        this.callback = callback;
        this.other = other;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String str1 = charSequence.toString();
        String str2 = other.getText() == null ? "" : other.getText().toString();
        valid = str1.equals(str2);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        callback.accept(valid);
    }
}
