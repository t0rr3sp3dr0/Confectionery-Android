package me.t0rr3sp3dr0.confectionery.example;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

import me.t0rr3sp3dr0.confectionery.abstracts.CandyActivity;
import me.t0rr3sp3dr0.confectionery.example.databinding.ActivityBasicBinding;
import me.t0rr3sp3dr0.confectionery.example.dummy.DummyContent;

public class BasicActivity extends CandyActivity<ActivityBasicBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getBinding().fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_basic);
        if (fragment == null)
            addFragment(R.id.content_basic, fragment = new ItemFragment(1, DummyContent.ITEMS, true), true);
    }

    @Override
    public void onFragmentInteraction(Uri uri, @Nullable Object object) {

    }

    @Override
    public void onListFragmentInteraction(Class<?> clazz, @Nullable Object object) {
        if (clazz.isAssignableFrom(ItemFragment.class) && object != null)
            Snackbar.make(getBinding().getRoot(), ((DummyContent.DummyItem) object).content, Snackbar.LENGTH_LONG).show();
    }
}
