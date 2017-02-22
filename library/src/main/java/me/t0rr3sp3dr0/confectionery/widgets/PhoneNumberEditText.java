package me.t0rr3sp3dr0.confectionery.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.Locale;

import me.t0rr3sp3dr0.confectionery.R;
import me.t0rr3sp3dr0.confectionery.utilities.PhoneNumberFormattingTextWatcher;

/**
 * A {@link EditText} which uses Google's common library for parsing,
 * formatting, and validating international phone numbers.
 *
 * @author Pedro Tôrres
 * @since 0.0.2
 */
public class PhoneNumberEditText extends AppCompatEditText {
    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    private String regionCode;

    public PhoneNumberEditText(Context context) {
        this(context, null, R.attr.editTextStyle);
    }

    public PhoneNumberEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
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

        setInputType(InputType.TYPE_CLASS_PHONE);
        setRawInputType(InputType.TYPE_CLASS_PHONE);

        if (!isInEditMode())
            addTextChangedListener(new PhoneNumberFormattingTextWatcher(this.regionCode));
        else
            setHint("+55 81 99999-9999");
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
