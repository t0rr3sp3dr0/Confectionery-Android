package me.t0rr3sp3dr0.confectionery.interfaces;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.DatePicker;
import android.widget.TimePicker;

/**
 * Created by pedro on 2/18/17.
 */

public interface DateTimePickerDialogDelegate {
    @Nullable
    Drawable iconForDateTimePickerDialog();

    @Nullable
    String titleForDateTimePickerDialog();

    @Nullable
    String messageForDateTimePickerDialog();

    @NonNull
    String textForDateTimePickerDialogPositiveButton();

    @NonNull
    String textForDateTimePickerDialogNegativeButton();

    @NonNull
    String textForDateTimePickerDialogDateButton();

    @NonNull
    String textForDateTimePickerDialogTimeButton();

    void onShowDateTimePickerDialog(@NonNull final DatePicker datePicker, @NonNull final TimePicker timePicker);

    void onDateTimePickerDialogCompletion(@NonNull final DatePicker datePicker, @NonNull final TimePicker timePicker);
}
