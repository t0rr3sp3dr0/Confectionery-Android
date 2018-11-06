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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.t0rr3sp3dr0.confectionery.R;
import me.t0rr3sp3dr0.confectionery.singletons.StringObjectMap;
import me.t0rr3sp3dr0.confectionery.utilities.CaseFormat;

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
    public FragmentManager childFragmentManager;
    protected CandyActivity<? extends ViewDataBinding> mListener;
    private T binding;
    @AnimRes
    private int enter = R.anim.slide_in_right;
    @AnimRes
    private int exit = R.anim.slide_out_left;
    @AnimRes
    private int popEnter = R.anim.slide_in_left;
    @AnimRes
    private int popExit = R.anim.slide_out_right;

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

        String hash = Long.toString((long) (System.identityHashCode(map) * Math.random()));
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
            String layoutName = CaseFormat.pascalToSnake(typeName.substring(typeName.lastIndexOf('.') + 1, typeName.length() - 7));
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

        childFragmentManager = getChildFragmentManager();
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

        String hash = Long.toString((long) (System.identityHashCode(this) * Math.random()));
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

        String hash = Long.toString((long) (System.identityHashCode(map) * Math.random()));
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

        String hash = Long.toString((long) (System.identityHashCode(map) * Math.random()));
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

    public final void setChildCustomAnimations(@AnimRes int enter, @AnimRes int exit, @AnimRes int popEnter, @AnimRes int popExit) {
        this.enter = enter;
        this.exit = exit;
        this.popEnter = popEnter;
        this.popExit = popExit;
    }

    public final void addChildFragment(@IdRes int containerViewId, Fragment fragment, boolean animated) {
        FragmentTransaction fragmentTransaction = childFragmentManager.beginTransaction();
        if (animated)
            fragmentTransaction.setCustomAnimations(enter, exit, popEnter, popExit);
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    public final void replaceChildFragment(@IdRes int containerViewId, Fragment fragment, boolean toBackStack, boolean animated) {
        FragmentTransaction fragmentTransaction = childFragmentManager.beginTransaction();
        if (toBackStack)
            fragmentTransaction.addToBackStack(Long.toString((long) (System.identityHashCode(this) * Math.random())));
        else
            childFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (animated)
            fragmentTransaction.setCustomAnimations(enter, exit, popEnter, popExit);
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public final void restartChildFragment(@IdRes int containerViewId, boolean animated) {
        try {
            Fragment actualFragment = childFragmentManager.findFragmentById(containerViewId);

            // Clone actualFragment
            Fragment cloneFragment = actualFragment.getClass().newInstance();
            Bundle args = new Bundle();
            actualFragment.onSaveInstanceState(args);
            cloneFragment.setArguments(args);

            FragmentTransaction fragmentTransaction = childFragmentManager.beginTransaction();
            if (animated)
                fragmentTransaction.setCustomAnimations(enter, exit, popEnter, popExit);
            fragmentTransaction.replace(containerViewId, cloneFragment);
            if (childFragmentManager.getBackStackEntryCount() > 0) {
                childFragmentManager.popBackStack();
                fragmentTransaction.addToBackStack(Long.toString((long) (System.identityHashCode(this) * Math.random())));
            }
            fragmentTransaction.commitAllowingStateLoss();
        } catch (java.lang.InstantiationException | NullPointerException e) {
            e.printStackTrace();

            Log.e("Confectionery", "It was not possible to restart your fragment!", e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();

            Log.e("Confectionery", "It was not possible to restart your fragment!", e);
        }
    }
}
