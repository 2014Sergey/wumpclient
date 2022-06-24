package ru.p3m3.wumpusgame;

import android.util.AttributeSet;
import android.widget.NumberPicker;
import android.content.Context;

public class RoomPicker extends NumberPicker {
  final String[] values;

  public RoomPicker(final Context context) {
    super(context);

    String[] strs = new String[21]; strs[0] = "-";
    for (int i = 1; i <= 20; i++) strs[i] = String.valueOf(i);
    this.values = strs;

    this.setMinValue(0);
    this.setMaxValue(this.values.length - 1);
    this.setDisplayedValues(this.values);
    this.setWrapSelectorWheel(true);
  }

  public RoomPicker(final Context context, final AttributeSet set) {
    super(context, set);

    String[] strs = new String[21]; strs[0] = "-";
    for (int i = 1; i <= 20; i++) strs[i] = String.valueOf(i);
    this.values = strs;

    this.setMinValue(0);
    this.setMaxValue(this.values.length - 1);
    this.setDisplayedValues(this.values);
    this.setWrapSelectorWheel(true);
  }
}
