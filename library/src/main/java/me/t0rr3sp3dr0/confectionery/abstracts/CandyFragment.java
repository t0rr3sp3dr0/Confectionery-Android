package me.t0rr3sp3dr0.confectionery.abstracts;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
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

import me.t0rr3sp3dr0.confectionery.R;
import me.t0rr3sp3dr0.confectionery.singletons.StringObjectMap;

/**
 * A blank fragment.
 * <p/>
 * Activities containing this fragment MUST extend the {@link CandyActivity} class.
 *
 * @param <T> The binding class of the layout inflated by this fragment
 * @author Pedro Tôrres
 * @see CandyActivity
 * @see Fragment
 * @see ViewDataBinding
 * @since 0.0.1
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class CandyFragment<T extends ViewDataBinding> extends Fragment {
    private final Map<String, Object> map = new HashMap<>();
    protected CandyActivity<? extends ViewDataBinding> mListener;
    private T binding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CandyFragment() {
    }

    /**
     * Use this constructor method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param map A mapping from String keys to various {@link Object} values.
     */
    public CandyFragment(@NonNull Map<String, Object> map) {
        Bundle args = new Bundle();

        String hash = Integer.toString(System.identityHashCode(map));
        args.putString("this$$hash", hash);

        String[] keys = new String[map.size()];
        Iterator<String> iterator = map.keySet().iterator();
        for (int i = 0; iterator.hasNext(); i++)
            StringObjectMap.getInstance().put(String.format("%s$$%s", hash, keys[i] = iterator.next()), map.get(keys[i]));
        args.putStringArray("this$$keys", keys);

        setArguments(args);
    }

    /**
     * {@inheritDoc}
     */
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
                    map.put(key, StringObjectMap.getInstance().remove(String.format("%s$$%s", hash, key)));
        } else if (getArguments() != null) {
            String hash = getArguments().getString("this$$hash");
            getArguments().remove("this$$hash");
            String[] keys = getArguments().getStringArray("this$$keys");
            getArguments().remove("this$$keys");
            if (keys != null)
                for (String key : keys)
                    map.put(key, StringObjectMap.getInstance().remove(String.format("%s$$%s", hash, key)));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            Type superclass = getClass().getGenericSuperclass();
            if (superclass instanceof Class)
                throw new RuntimeException("Missing type parameter.");
            final Type type = ((ParameterizedType) superclass).getActualTypeArguments()[0];

            String typeName = type.toString();
            String layoutName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, typeName.substring(typeName.lastIndexOf('.') + 1, typeName.length() - 7));
            int layoutId = getResources().getIdentifier(layoutName, "layout", getContext().getPackageName());

            // Inflate the layout for this fragment
            binding = DataBindingUtil.inflate(inflater, layoutId, container, false);
        } catch (RuntimeException e) {
            e.printStackTrace();

            // Inflate the layout for this fragment
            binding = DataBindingUtil.inflate(inflater, R.layout.empty, container, false);
        }

        getActivity().invalidateOptionsMenu();

        return binding.getRoot();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof CandyActivity)
            mListener = (CandyActivity<? extends ViewDataBinding>) context;
        else
            throw new RuntimeException(context.toString() + " must extend CandyFragment");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;

        getActivity().invalidateOptionsMenu();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String hash = Integer.toString(System.identityHashCode(this));
        outState.putString("this$$hash", hash);

        String[] keys = new String[map.size()];
        Iterator<String> iterator = map.keySet().iterator();
        for (int i = 0; iterator.hasNext(); i++)
            StringObjectMap.getInstance().put(String.format("%s$$%s", hash, keys[i] = iterator.next()), map.get(keys[i]));
        outState.putStringArray("this$$keys", keys);
    }

    @NonNull
    public final T getBinding() {
        return binding;
    }

    @Nullable
    public final Object getObject(String key) {
        return map.get(key);
    }

    public final void startActivity(@NonNull Class<? extends CandyActivity<? extends ViewDataBinding>> clazz, @NonNull Map<String, Object> map) {
        Intent intent = new Intent(getContext(), clazz);

        String hash = Integer.toString(System.identityHashCode(map));
        intent.putExtra("this$$hash", hash);

        String[] keys = new String[map.size()];
        Iterator<String> iterator = map.keySet().iterator();
        for (int i = 0; iterator.hasNext(); i++)
            StringObjectMap.getInstance().put(String.format("%s$$%s", hash, keys[i] = iterator.next()), map.get(keys[i]));
        intent.putExtra("this$$keys", keys);

        startActivity(intent);
    }

    public final void startActivityForResult(@NonNull Class<? extends CandyActivity<? extends ViewDataBinding>> clazz, int requestCode, @NonNull Map<String, Object> map) {
        Intent intent = new Intent(getContext(), clazz);

        String hash = Integer.toString(System.identityHashCode(map));
        intent.putExtra("this$$hash", hash);

        String[] keys = new String[map.size()];
        Iterator<String> iterator = map.keySet().iterator();
        for (int i = 0; iterator.hasNext(); i++)
            StringObjectMap.getInstance().put(String.format("%s$$%s", hash, keys[i] = iterator.next()), map.get(keys[i]));
        intent.putExtra("this$$keys", keys);

        startActivityForResult(intent, requestCode);
    }

    public final void setCustomAnimations(@AnimRes int enter, @AnimRes int exit, @AnimRes int popEnter, @AnimRes int popExit) {
        ((CandyActivity) getActivity()).setCustomAnimations(enter, exit, popEnter, popExit);
    }

    public final void addFragment(@IdRes int containerViewId, Fragment fragment, boolean animated) {
        ((CandyActivity) getActivity()).addFragment(containerViewId, fragment, animated);
    }

    public final void replaceFragment(@IdRes int containerViewId, Fragment fragment, boolean toBackStack, boolean animated) {
        ((CandyActivity) getActivity()).replaceFragment(containerViewId, fragment, toBackStack, animated);
    }

    public final void restartFragment(@IdRes int containerViewId, boolean animated) {
        ((CandyActivity) getActivity()).restartFragment(containerViewId, animated);
    }
}
