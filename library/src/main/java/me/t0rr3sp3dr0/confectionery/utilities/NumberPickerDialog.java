package me.t0rr3sp3dr0.confectionery.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import me.t0rr3sp3dr0.confectionery.R;

/**
 * A simple dialog containing an {@link NumberPicker}.
 *
 * @author Pedro Tôrres
 * @since 0.0.2
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class NumberPickerDialog extends AlertDialog implements DialogInterface.OnClickListener, NumberPicker.OnValueChangeListener {
    private static final String VALUE = "value";
    private static final String MIN_VALUE = "minValue";
    private static final String MAX_VALUE = "maxValue";
    private final NumberPicker mNumberPicker;
    private OnValueSetListener mValueSetListener;

    /**
     * Creates a new number picker dialog using the parent context's default
     * alert dialog theme.
     *
     * @param context the parent context
     */
    public NumberPickerDialog(@NonNull Context context) {
        this(context, 0, null, 0, 0, Integer.MAX_VALUE - 1);
    }

    /**
     * Creates a new number picker dialog.
     *
     * @param context    the parent context
     * @param themeResId the resource ID of the theme against which to inflate
     *                   this dialog, or {@code 0} to use the parent
     *                   {@code context}'s default alert dialog theme
     */
    public NumberPickerDialog(@NonNull Context context, @StyleRes int themeResId) {
        this(context, themeResId, null, 0, 0, Integer.MAX_VALUE - 1);
    }

    /**
     * Creates a new date number dialog with the specified values using the
     * parent context's default alert dialog theme.
     *
     * @param context  the parent context
     * @param listener the listener to call when the user sets the date
     * @param value    the initially selected value
     * @param minValue the initially minimum value
     * @param maxValue the initially maximum value
     */
    public NumberPickerDialog(@NonNull Context context, @Nullable OnValueSetListener listener, int value, int minValue, int maxValue) {
        this(context, 0, listener, value, minValue, maxValue);
    }

    /**
     * Creates a new date number dialog with the specified values.
     *
     * @param context    the parent context
     * @param themeResId the resource ID of the theme against which to inflate
     *                   this dialog, or {@code 0} to use the parent
     *                   {@code context}'s default alert dialog theme
     * @param listener   the listener to call when the user sets the date
     * @param value      the initially selected value
     * @param minValue   the initially minimum value
     * @param maxValue   the initially maximum value
     */
    public NumberPickerDialog(@NonNull Context context, @StyleRes int themeResId, @Nullable OnValueSetListener listener, int value, int minValue, int maxValue) {
        super(context, resolveDialogTheme(context, themeResId));

        final Context themeContext = getContext();
        final LayoutInflater inflater = LayoutInflater.from(themeContext);
        @SuppressLint("InflateParams") final View view = inflater.inflate(R.layout.number_picker_dialog, null);
        setView(view);

        setButton(BUTTON_POSITIVE, themeContext.getString(android.R.string.ok), this);
        setButton(BUTTON_NEGATIVE, themeContext.getString(android.R.string.cancel), this);

        mNumberPicker = view.findViewById(R.id.numberPicker);
        mNumberPicker.setValue(value);
        mNumberPicker.setMinValue(minValue);
        mNumberPicker.setMaxValue(maxValue);
        mNumberPicker.setOnValueChangedListener(this);

        mValueSetListener = listener;
    }

    @StyleRes
    private static int resolveDialogTheme(@NonNull Context context, @StyleRes int themeResId) {
        if (themeResId >= 0x01000000)
            // start of real resource IDs.
            return themeResId;
        else {
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.alertDialogTheme, outValue, true);
            return outValue.resourceId;
        }
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Bundle onSaveInstanceState() {
        final Bundle state = super.onSaveInstanceState();

        state.putInt(VALUE, mNumberPicker.getValue());
        state.putInt(MIN_VALUE, mNumberPicker.getMinValue());
        state.putInt(MAX_VALUE, mNumberPicker.getMaxValue());

        return state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        final int value = savedInstanceState.getInt(VALUE);
        final int minValue = savedInstanceState.getInt(MIN_VALUE);
        final int maxValue = savedInstanceState.getInt(MAX_VALUE);

        mNumberPicker.setValue(value);
        mNumberPicker.setMinValue(minValue);
        mNumberPicker.setMaxValue(maxValue);
        mNumberPicker.setOnValueChangedListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                if (mValueSetListener != null) {
                    // Clearing focus forces the dialog to commit any pending
                    // changes, e.g. typed text in a NumberPicker.
                    mNumberPicker.clearFocus();
                    mValueSetListener.onNumberSet(mNumberPicker, mNumberPicker.getValue());
                }
                break;

            case BUTTON_NEGATIVE:
                cancel();
                break;

            default:
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        mNumberPicker.setValue(newVal);
    }

    /**
     * Returns the {@link NumberPicker} contained in this dialog.
     *
     * @return the number picker
     */
    @NonNull
    public NumberPicker getNumberPicker() {
        return mNumberPicker;
    }

    /**
     * Sets the listener to call when the user sets the value.
     *
     * @param listener the listener to call when the user sets the value
     */
    public void setOnValueSetListener(@Nullable OnValueSetListener listener) {
        mValueSetListener = listener;
    }

    /**
     * Sets the current value.
     *
     * @param value the value
     */
    public void updateValue(int value) {
        mNumberPicker.setValue(value);
    }

    /**
     * The listener used to indicate the user has finished selecting a date.
     */
    public interface OnValueSetListener {
        /**
         * @param view  the picker associated with the dialog
         * @param value the selected value
         */
        void onNumberSet(NumberPicker view, int value);
    }
}
