package me.t0rr3sp3dr0.confectionery.examples;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import me.t0rr3sp3dr0.confectionery.abstracts.CandyActivity;
import me.t0rr3sp3dr0.confectionery.abstracts.CandyFragment;
import me.t0rr3sp3dr0.confectionery.databinding.FragmentBlankBinding;

/**
 * A simple {@link CandyFragment} subclass.
 * Activities that contain this fragment must extend the
 * {@link CandyActivity} class to handle interaction events.
 * Use the {@link BlankFragment#BlankFragment(Map)}
 * constructor method to create an instance of this fragment.
 */
public class BlankFragment extends CandyFragment<FragmentBlankBinding> {
    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this constructor method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param map A mapping from String keys to various {@link Object} values.
     */
    @SuppressLint("ValidFragment")
    public BlankFragment(@NonNull Map<String, Object> map) {
        super(map);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        getBinding().textView.setText((String) getObject("str"));

        return view;
    }
}
