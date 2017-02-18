package me.t0rr3sp3dr0.confectionery.utilities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import me.t0rr3sp3dr0.confectionery.R;
import me.t0rr3sp3dr0.confectionery.interfaces.DateTimePickerDialogDelegate;

/**
 * Created by pedro on 2/18/17.
 */

public class DateTimePickerDialogFragment extends DialogFragment {
    private DateTimePickerDialogDelegate mDelegate;
    private boolean aSwitch;

    public DateTimePickerDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(R.layout.dialog_datetime_picker);

        Drawable drawable;
        if ((drawable = mDelegate.iconForDateTimePickerDialog()) != null)
            builder.setIcon(drawable);

        String string;
        if ((string = mDelegate.titleForDateTimePickerDialog()) != null)
            builder.setTitle(string);
        if ((string = mDelegate.messageForDateTimePickerDialog()) != null)
            builder.setMessage(string);

        builder.setPositiveButton(mDelegate.textForDateTimePickerDialogPositiveButton(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final DatePicker datePicker = (DatePicker) ((AlertDialog) dialog).findViewById(R.id.datePicker);
                final TimePicker timePicker = (TimePicker) ((AlertDialog) dialog).findViewById(R.id.timePicker);

                assert datePicker != null;
                assert timePicker != null;

                mDelegate.onDateTimePickerDialogCompletion(datePicker, timePicker);
            }
        });
        builder.setNegativeButton(mDelegate.textForDateTimePickerDialogNegativeButton(), null);
        builder.setNeutralButton("\0", null);

        AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button neutral = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEUTRAL);
                final DatePicker datePicker = (DatePicker) ((AlertDialog) dialog).findViewById(R.id.datePicker);
                final TimePicker timePicker = (TimePicker) ((AlertDialog) dialog).findViewById(R.id.timePicker);

                assert datePicker != null;
                assert timePicker != null;

                neutral.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        aSwitch = !aSwitch;
                        neutral.setText(aSwitch ? mDelegate.textForDateTimePickerDialogTimeButton() : mDelegate.textForDateTimePickerDialogDateButton());
                        datePicker.setVisibility(aSwitch ? View.VISIBLE : View.GONE);
                        timePicker.setVisibility(aSwitch ? View.GONE : View.VISIBLE);
                    }
                });
                neutral.callOnClick();

                mDelegate.onShowDateTimePickerDialog(datePicker, timePicker);
            }
        });

        return alertDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DateTimePickerDialogDelegate)
            mDelegate = (DateTimePickerDialogDelegate) context;
        else
            throw new RuntimeException(context.toString() + " must implement DateTimePickerDialogDelegate");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mDelegate = null;
    }
}
