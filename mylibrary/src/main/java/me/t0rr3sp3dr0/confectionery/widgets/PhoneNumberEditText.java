package me.t0rr3sp3dr0.confectionery.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.Locale;

import me.t0rr3sp3dr0.confectionery.R;
import me.t0rr3sp3dr0.confectionery.utilities.PhoneNumberFormattingTextWatcher;

/**
 * Created by pedro on 2/18/17.
 */

public class PhoneNumberEditText extends EditText {
    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    private String regionCode;

    public PhoneNumberEditText(Context context) {
        this(context, null, android.R.attr.editTextStyle);
    }

    public PhoneNumberEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public PhoneNumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PhoneNumberEditText);

        String regionCode = a.getString(R.styleable.PhoneNumberEditText_regionCode);
        if (regionCode != null)
            this.regionCode = regionCode;
        else
            this.regionCode = Locale.getDefault().getCountry();

        a.recycle();

        try {
            addTextChangedListener(new PhoneNumberFormattingTextWatcher(this.regionCode));
            setInputType(InputType.TYPE_CLASS_PHONE);
        } catch (IllegalStateException e) {
            e.printStackTrace();

            // Fix Rendering Problems
        }
    }

    public String getRegionCode() {
        return regionCode;
    }

    public boolean isValidNumber() {
        try {
            return phoneNumberUtil.isValidNumber(phoneNumberUtil.parse(getText().toString(), regionCode));
        } catch (NumberParseException e) {
            e.printStackTrace();

            return false;
        }
    }
}
