package me.t0rr3sp3dr0.confectionery.example;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.Predicate;

import java.util.List;
import java.util.Map;

import me.t0rr3sp3dr0.confectionery.abstracts.CandyActivity;
import me.t0rr3sp3dr0.confectionery.abstracts.CandyFragment;
import me.t0rr3sp3dr0.confectionery.abstracts.CandyListFragment;
import me.t0rr3sp3dr0.confectionery.example.databinding.FragmentItemBinding;
import me.t0rr3sp3dr0.confectionery.example.databinding.FragmentItemListBinding;
import me.t0rr3sp3dr0.confectionery.example.dummy.DummyContent;

/**
 * A simple {@link CandyFragment} subclass.
 * Activities that contain this fragment must extend the
 * {@link CandyActivity} class to handle interaction events.
 * Use the {@link ItemFragment#ItemFragment()}
 * or {@link ItemFragment#ItemFragment(Map)}
 * or {@link ItemFragment#ItemFragment(int, List, boolean)}
 * or {@link ItemFragment#ItemFragment(Map, int, List, boolean)}
 * constructor methods to create an instance of this fragment.
 */
public class ItemFragment extends CandyListFragment<FragmentItemListBinding, FragmentItemBinding, DummyContent.DummyItem> {
    public ItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this constructor method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param map A mapping from String keys to various {@link Object} values.
     */
    @SuppressLint("ValidFragment")
    public ItemFragment(@NonNull Map<String, Object> map) {
        super(map);
    }

    /**
     * Use this constructor method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param columnCount           Number of columns in {@link RecyclerView}.
     * @param values                List with rows' {@link DummyContent.DummyItem} of {@link RecyclerView}.
     * @param dividerItemDecoration Add {@link DividerItemDecoration} to {@link RecyclerView}.
     */
    @SuppressLint("ValidFragment")
    public ItemFragment(int columnCount, @NonNull List<DummyContent.DummyItem> values, boolean dividerItemDecoration) {
        super(columnCount, values, dividerItemDecoration);
    }

    /**
     * Use this constructor method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param map                   A mapping from String keys to various {@link Object} values.
     * @param columnCount           Number of columns in {@link RecyclerView}.
     * @param values                List with rows' {@link DummyContent.DummyItem} of {@link RecyclerView}.
     * @param dividerItemDecoration Add {@link DividerItemDecoration} to {@link RecyclerView}.
     */
    @SuppressLint("ValidFragment")
    public ItemFragment(@NonNull Map<String, Object> map, int columnCount, @NonNull List<DummyContent.DummyItem> values, boolean dividerItemDecoration) {
        super(map, columnCount, values, dividerItemDecoration);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        filterDataSet(new Predicate<DummyContent.DummyItem>() {
            @Override
            public boolean apply(DummyContent.DummyItem input) {
                return Integer.parseInt(input.id) % 2 == 0;
            }
        });

        return v;
    }
}
