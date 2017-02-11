package me.t0rr3sp3dr0.confectionery.example;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import me.t0rr3sp3dr0.confectionery.abstracts.CandyActivity;
import me.t0rr3sp3dr0.confectionery.example.databinding.ActivityEmptyBinding;
import me.t0rr3sp3dr0.confectionery.example.dummy.DummyContent;

public class EmptyActivity extends CandyActivity<ActivityEmptyBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_empty);
        if (fragment == null)
            addFragment(R.id.activity_empty, fragment = new ItemFragment(1, DummyContent.ITEMS, true), true);
    }
}
