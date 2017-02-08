package me.t0rr3sp3dr0.confectionery.abstracts;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.CaseFormat;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.t0rr3sp3dr0.confectionery.singletons.Mailbox;

/**
 * Created by pedro on 2/5/17.
 */

public abstract class CandyFragment<T extends ViewDataBinding> extends Fragment {
    protected CandyActivity mListener;
    private T binding;
    private Map<String, Object> map = new HashMap<>();

    public CandyFragment() {
        // Required empty public constructor
    }

    public CandyFragment(@NonNull Map<String, Object> map) {
        Bundle args = new Bundle();

        String hash = Integer.toString(System.identityHashCode(map));
        args.putString("this$$hash", hash);

        String[] keys = new String[map.size()];
        Iterator<String> iterator = map.keySet().iterator();
        for (int i = 0; iterator.hasNext(); i++)
            Mailbox.getInstance().put(String.format("%s$$%s", hash, keys[i] = iterator.next()), map.get(keys[i]));
        args.putStringArray("this$$keys", keys);

        setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            String hash = savedInstanceState.getString("this$$hash");
            savedInstanceState.remove("this$$hash");
            String[] keys = savedInstanceState.getStringArray("this$$keys");
            savedInstanceState.remove("this$$keys");
            if (keys != null)
                for (String key : keys)
                    map.put(key, Mailbox.getInstance().remove(String.format("%s$$%s", hash, key)));
        } else if (getArguments() != null) {
            String hash = getArguments().getString("this$$hash");
            getArguments().remove("this$$hash");
            String[] keys = getArguments().getStringArray("this$$keys");
            getArguments().remove("this$$keys");
            if (keys != null)
                for (String key : keys)
                    map.put(key, Mailbox.getInstance().remove(String.format("%s$$%s", hash, key)));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class)
            throw new RuntimeException("Missing type parameter.");
        final Type type = ((ParameterizedType) superclass).getActualTypeArguments()[0];

        String typeName = type.toString();
        String layoutName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, typeName.substring(typeName.lastIndexOf('.') + 1, typeName.length() - 7));
        int layoutId = getResources().getIdentifier(layoutName, "layout", getContext().getPackageName());

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false);
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CandyActivity) {
            mListener = (CandyActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must extend CandyFragment");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String hash = Integer.toString(System.identityHashCode(this));
        outState.putString("this$$hash", hash);

        String[] keys = new String[map.size()];
        Iterator<String> iterator = map.keySet().iterator();
        for (int i = 0; iterator.hasNext(); i++)
            Mailbox.getInstance().put(String.format("%s$$%s", hash, keys[i] = iterator.next()), map.get(keys[i]));
        outState.putStringArray("this$$keys", keys);
    }

    @NonNull
    public T getBinding() {
        return binding;
    }

    @Nullable
    public Object getObject(String key) {
        return map.get(key);
    }
}
