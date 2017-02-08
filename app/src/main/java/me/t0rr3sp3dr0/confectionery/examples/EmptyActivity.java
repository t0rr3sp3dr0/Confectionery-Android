package me.t0rr3sp3dr0.confectionery.examples;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import me.t0rr3sp3dr0.confectionery.R;
import me.t0rr3sp3dr0.confectionery.abstracts.CandyActivity;
import me.t0rr3sp3dr0.confectionery.databinding.ActivityEmptyBinding;
import me.t0rr3sp3dr0.confectionery.examples.dummy.DummyContent;

public class EmptyActivity extends CandyActivity<ActivityEmptyBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_empty);
        if (fragment == null)
            addFragment(R.id.activity_empty, fragment = new ItemFragment(1, DummyContent.ITEMS), true);
    }

    @Override
    public void onFragmentInteraction(Uri uri, @Nullable Object object) {

    }

    @Override
    public void onListFragmentInteraction(Class<?> clazz, @Nullable Object object) {

    }
}
