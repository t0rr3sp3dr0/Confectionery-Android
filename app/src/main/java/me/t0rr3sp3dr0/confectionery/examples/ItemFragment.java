package me.t0rr3sp3dr0.confectionery.examples;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import me.t0rr3sp3dr0.confectionery.abstracts.CandyActivity;
import me.t0rr3sp3dr0.confectionery.abstracts.CandyFragment;
import me.t0rr3sp3dr0.confectionery.abstracts.CandyItemFragment;
import me.t0rr3sp3dr0.confectionery.databinding.FragmentItemBinding;
import me.t0rr3sp3dr0.confectionery.databinding.FragmentItemListBinding;
import me.t0rr3sp3dr0.confectionery.examples.dummy.DummyContent;

/**
 * A simple {@link CandyFragment} subclass.
 * Activities that contain this fragment must extend the
 * {@link CandyActivity} class to handle interaction events.
 * Use the {@link ItemFragment#ItemFragment(int, List)}
 * or {@link ItemFragment#ItemFragment(int, List, Map)}
 * constructor methods to create an instance of this fragment.
 */
public class ItemFragment extends CandyItemFragment<FragmentItemListBinding, FragmentItemBinding, DummyContent.DummyItem> {
    public ItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this constructor method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param columnCount Number of columns in {@link RecyclerView}.
     * @param values      List with rows' {@link DummyContent.DummyItem} of {@link RecyclerView}.
     */
    @SuppressLint("ValidFragment")
    public ItemFragment(int columnCount, @NonNull List<DummyContent.DummyItem> values) {
        super(columnCount, values);
    }

    /**
     * Use this constructor method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param columnCount Number of columns in {@link RecyclerView}.
     * @param values      List with rows' {@link DummyContent.DummyItem} of {@link RecyclerView}.
     * @param map         A mapping from String keys to various {@link Object} values.
     */
    @SuppressLint("ValidFragment")
    public ItemFragment(int columnCount, @NonNull List<DummyContent.DummyItem> values, @NonNull Map<String, Object> map) {
        super(columnCount, values, map);
    }
}
