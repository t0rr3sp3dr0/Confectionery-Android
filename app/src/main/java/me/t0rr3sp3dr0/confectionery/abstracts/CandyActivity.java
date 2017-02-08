package me.t0rr3sp3dr0.confectionery.abstracts;

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
import android.support.v7.app.AppCompatActivity;

import com.google.common.base.CaseFormat;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.t0rr3sp3dr0.confectionery.interfaces.OnFragmentInteractionListener;
import me.t0rr3sp3dr0.confectionery.interfaces.OnListFragmentInteractionListener;
import me.t0rr3sp3dr0.confectionery.singletons.Mailbox;

/**
 * Created by pedro on 2/4/17.
 */

public abstract class CandyActivity<T extends ViewDataBinding> extends AppCompatActivity implements OnFragmentInteractionListener, OnListFragmentInteractionListener {
    public final FragmentManager fragmentManager = getSupportFragmentManager();
    private final Map<String, Object> map = new HashMap<>();
    private T binding;
    private
    @AnimRes
    int enter = android.R.anim.slide_in_left;
    private
    @AnimRes
    int exit = android.R.anim.slide_out_right;
    private
    @AnimRes
    int popEnter = android.R.anim.slide_in_left;
    private
    @AnimRes
    int popExit = android.R.anim.slide_out_right;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class)
            throw new RuntimeException("Missing type parameter.");
        final Type type = ((ParameterizedType) superclass).getActualTypeArguments()[0];

        String typeName = type.toString();
        String layoutName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, typeName.substring(typeName.lastIndexOf('.') + 1, typeName.length() - 7));
        int layoutId = getResources().getIdentifier(layoutName, "layout", getPackageName());
        binding = DataBindingUtil.setContentView(this, layoutId);

        if (savedInstanceState != null) {
            String hash = savedInstanceState.getString("this$$hash");
            savedInstanceState.remove("this$$hash");
            String[] keys = savedInstanceState.getStringArray("this$$keys");
            savedInstanceState.remove("this$$keys");
            if (keys != null)
                for (String key : keys)
                    map.put(key, Mailbox.getInstance().remove(String.format("%s$$%s", hash, key)));
        } else {
            String hash = getIntent().getStringExtra("this$$hash");
            getIntent().removeExtra("this$$hash");
            String[] keys = getIntent().getStringArrayExtra("this$$keys");
            getIntent().removeExtra("this$$keys");
            if (keys != null)
                for (String key : keys)
                    map.put(key, Mailbox.getInstance().remove(String.format("%s$$%s", hash, key)));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String hash = Integer.toString(System.identityHashCode(this));
        outState.putString("this$$hash", hash);

        String[] keys = new String[map.size()];
        Iterator<String> iterator = map.keySet().iterator();
        for (int i = 0; iterator.hasNext(); i++)
            Mailbox.getInstance().put(String.format("%s$$%s", hash, keys[i] = iterator.next()), map.get(keys[i]));
        outState.putStringArray("this$$keys", keys);
    }

    public void startActivity(@NonNull Class<? extends CandyActivity<? extends ViewDataBinding>> clazz, @NonNull Map<String, Object> map) {
        Intent intent = new Intent(getApplicationContext(), clazz);

        String hash = Integer.toString(System.identityHashCode(map));
        intent.putExtra("this$$hash", hash);

        String[] keys = new String[map.size()];
        Iterator<String> iterator = map.keySet().iterator();
        for (int i = 0; iterator.hasNext(); i++)
            Mailbox.getInstance().put(String.format("%s$$%s", hash, keys[i] = iterator.next()), map.get(keys[i]));
        intent.putExtra("this$$keys", keys);

        startActivity(intent);
    }

    @NonNull
    public T getBinding() {
        return binding;
    }

    @Nullable
    public Object getObject(String key) {
        return map.get(key);
    }

    public void setCustomAnimations(@AnimRes int enter, @AnimRes int exit, @AnimRes int popEnter, @AnimRes int popExit) {
        this.enter = enter;
        this.exit = exit;
        this.popEnter = popEnter;
        this.popExit = popExit;
    }

    public void addFragment(@IdRes int containerViewId, Fragment fragment, boolean animated) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (animated)
            fragmentTransaction.setCustomAnimations(enter, exit, popEnter, popExit);
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    public void replaceFragment(@IdRes int containerViewId, Fragment fragment, boolean toBackStack, boolean animated) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (toBackStack)
            fragmentTransaction.addToBackStack(Integer.toString(System.identityHashCode(this)));
        if (animated)
            fragmentTransaction.setCustomAnimations(enter, exit, popEnter, popExit);
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.commit();
    }
}
