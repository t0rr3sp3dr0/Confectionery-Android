package me.t0rr3sp3dr0.confectionery.abstracts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.internal.util.Predicate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import me.t0rr3sp3dr0.confectionery.interfaces.OnListFragmentInteractionListener;
import me.t0rr3sp3dr0.confectionery.singletons.StringObjectMap;
import me.t0rr3sp3dr0.confectionery.utilities.CaseFormat;

/**
 * A fragment representing a list of {@link E}.
 * <p/>
 * Activities containing this fragment MUST extend the {@link CandyActivity}
 * class.
 *
 * @param <T1> The binding class of the layout inflated by this fragment
 * @param <T2> The binding class of the row layout inflated by this fragment's {@link RecyclerView}
 * @param <E>  The type of elements in this fragment's {@link RecyclerView}
 * @author Pedro Tôrres
 * @see CandyActivity
 * @see CandyFragment
 * @see RecyclerView
 * @see ViewDataBinding
 * @since 0.0.1
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class CandyListFragment<T1 extends ViewDataBinding, T2 extends ViewDataBinding, E> extends CandyFragment<T1> {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private boolean dividerItemDecoration = true;
    private List<E> mDataSet = new ArrayList<>();
    private List<E> mFilteredDataSet = new ArrayList<>(mDataSet);
    private Predicate<E> mPredicate = input -> true;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CandyListFragment() {
    }

    /**
     * Use this constructor method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param map A mapping from String keys to various {@link Object} values.
     */
    @SuppressLint("ValidFragment")
    public CandyListFragment(@NonNull Map<String, Object> map) {
        super(map);
    }

    /**
     * Use this constructor method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param columnCount           Number of columns in {@link RecyclerView}.
     * @param values                List with rows' {@link E} of {@link RecyclerView}.
     * @param dividerItemDecoration Add {@link DividerItemDecoration} to {@link RecyclerView}.
     */
    @SuppressLint("ValidFragment")
    public CandyListFragment(int columnCount, @NonNull List<E> values, boolean dividerItemDecoration) {
        Bundle args = new Bundle();

        args.putInt(ARG_COLUMN_COUNT, columnCount);

        String hash = Long.toString((long) (System.identityHashCode(values) * Math.random()));
        args.putString("this$$dataSet", hash);
        StringObjectMap.getInstance().put(String.format("%s$$dataSet", hash), values);

        args.putBoolean("this$$dividerItemDecoration", dividerItemDecoration);

        setArguments(args);
    }

    /**
     * Use this constructor method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param map                   A mapping from String keys to various {@link Object} values.
     * @param columnCount           Number of columns in {@link RecyclerView}.
     * @param values                List with rows' {@link E} of {@link RecyclerView}.
     * @param dividerItemDecoration Add {@link DividerItemDecoration} to {@link RecyclerView}.
     */
    @SuppressLint("ValidFragment")
    public CandyListFragment(@NonNull Map<String, Object> map, int columnCount, @NonNull List<E> values, boolean dividerItemDecoration) {
        super(map);

        getArguments().putInt(ARG_COLUMN_COUNT, columnCount);

        String hash = Long.toString((long) (System.identityHashCode(values) * Math.random()));
        getArguments().putString("this$$dataSet", hash);
        StringObjectMap.getInstance().put(String.format("%s$$dataSet", hash), values);

        getArguments().putBoolean("this$$dividerItemDecoration", dividerItemDecoration);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mColumnCount = savedInstanceState.getInt(ARG_COLUMN_COUNT);

            String dataSetHash = savedInstanceState.getString("this$$dataSet");
            savedInstanceState.remove("this$$dataSet");
            setDataSet((List<E>) StringObjectMap.getInstance().remove(String.format("%s$$dataSet", dataSetHash)));

            String predicateHash = savedInstanceState.getString("this$$predicate");
            savedInstanceState.remove("this$$predicate");
            filterDataSet((Predicate<E>) StringObjectMap.getInstance().remove(String.format("%s$$predicate", predicateHash)));

            dividerItemDecoration = savedInstanceState.getBoolean("this$$dividerItemDecoration");
        } else if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);

            String dataSetHash = getArguments().getString("this$$dataSet");
            getArguments().remove("this$$dataSet");
            setDataSet((List<E>) StringObjectMap.getInstance().remove(String.format("%s$$dataSet", dataSetHash)));

            String predicateHash = getArguments().getString("this$$predicate");
            getArguments().remove("this$$predicate");
            filterDataSet((Predicate<E>) StringObjectMap.getInstance().remove(String.format("%s$$predicate", predicateHash)));

            dividerItemDecoration = getArguments().getBoolean("this$$dividerItemDecoration");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (view instanceof RecyclerView) {
            if (mDataSet != null) {
                setupRecyclerView((RecyclerView) view);
            } else
                Log.w("Confectionery", "Your data source is null! Please override CandyListFragment#onCreateView and invoke, before super, CandyListFragment#setDataSet with your nonnull data source.");
        } else
            Log.w("Confectionery", "Your root view is not instanceof RecyclerView! Please override CandyListFragment#onCreateView and invoke, after super, CandyListFragment#setupRecyclerView with the correct RecyclerView.");

        return view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ARG_COLUMN_COUNT, mColumnCount);

        String dataSetHash = Long.toString((long) (System.identityHashCode(mDataSet) * Math.random()));
        outState.putString("this$$dataSet", dataSetHash);
        StringObjectMap.getInstance().put(String.format("%s$$dataSet", dataSetHash), mDataSet);

        String predicateHash = Long.toString((long) (System.identityHashCode(mPredicate) * Math.random()));
        outState.putString("this$$predicate", predicateHash);
        StringObjectMap.getInstance().put(String.format("%s$$predicate", predicateHash), mPredicate);

        outState.putBoolean("this$$dividerItemDecoration", dividerItemDecoration);
    }

    /**
     * Called by RecyclerView to allow an interaction in this fragment to be
     * communicated to the activity and potentially other fragments contained
     * in that activity.
     *
     * @param object Selected row's {@link E} object
     */
    public void onListFragmentInteraction(@NonNull E object) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onListFragmentInteraction(getClass(), object);
        }
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the itemView to reflect the
     * item at the given position.
     *
     * @param holder   The ViewHolder which represent the contents of the item
     *                 at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     * @param payloads A non-null list of payloads.
     */
    @SuppressWarnings("EmptyMethod")
    public void onBindViewHolder(final CandyItemRecyclerViewAdapter.ViewHolder holder, int position, @NonNull List<E> payloads) {
    }

    public final int getColumnCount() {
        return mColumnCount;
    }

    public final void setColumnCount(int columnCount) {
        this.mColumnCount = columnCount;
    }

    @NonNull
    public final List<E> getDataSet() {
        return mDataSet;
    }

    public final void setDataSet(@Nullable List<E> values) {
        mDataSet = values != null ? values : new ArrayList<>();

        filterDataSet(mPredicate);
    }

    @NonNull
    public final List<E> getFilteredDataSet() {
        return mFilteredDataSet;
    }

    public boolean isDividerItemDecoration() {
        return dividerItemDecoration;
    }

    public void setDividerItemDecoration(boolean dividerItemDecoration) {
        this.dividerItemDecoration = dividerItemDecoration;
    }

    public final void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        // Set the adapter
        Context context = recyclerView.getContext();
        if (mColumnCount <= 1)
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        recyclerView.setAdapter(new CandyItemRecyclerViewAdapter());

        if (dividerItemDecoration)
            recyclerView.addItemDecoration(new DividerItemDecoration(context, ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation()));
    }

    @NonNull
    public final Predicate<E> getPredicate() {
        return mPredicate;
    }

    public final void filterDataSet(@Nullable Predicate<E> predicate) {
        mPredicate = predicate != null ? predicate : input -> true;

        mFilteredDataSet.clear();

        for (E e : mDataSet)
            if (mPredicate.apply(e))
                mFilteredDataSet.add(e);
    }

    public final void notifyDataSetChanged(RecyclerView recyclerView) {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    /**
     * {@link RecyclerView.Adapter} that can display a {@link E} and makes a call to the
     * specified {@link OnListFragmentInteractionListener}.
     */
    public final class CandyItemRecyclerViewAdapter extends RecyclerView.Adapter<CandyItemRecyclerViewAdapter.ViewHolder> {

        CandyItemRecyclerViewAdapter() {
            // Package local constructor to avoid class instantiation outside this module
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Type superclass = CandyListFragment.this.getClass().getGenericSuperclass();
            if (superclass instanceof Class)
                throw new RuntimeException("Missing type parameter.");
            final Type type = ((ParameterizedType) superclass).getActualTypeArguments()[1];

            String typeName = type.toString();
            String layoutName = CaseFormat.pascalToSnake(typeName.substring(typeName.lastIndexOf('.') + 1, typeName.length() - 7));
            int layoutId = parent.getResources().getIdentifier(layoutName, "layout", parent.getContext().getPackageName());

            T2 binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutId, parent, false);
            return new ViewHolder(binding);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.setItem(mFilteredDataSet.get(position));

            CandyListFragment.this.onBindViewHolder(holder, position, Collections.unmodifiableList(mFilteredDataSet));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getItemCount() {
            return mFilteredDataSet.size();
        }

        public final class ViewHolder extends RecyclerView.ViewHolder {
            private final T2 binding;
            private final View mView;
            private E mItem;

            ViewHolder(T2 binding) {
                super(binding.getRoot());
                this.binding = binding;
                this.mView = binding.getRoot();

                mView.setOnClickListener(v -> CandyListFragment.this.onListFragmentInteraction(mItem));
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public String toString() {
                return super.toString() + " '" + mItem.toString() + "'";
            }

            public E getItem() {
                return mItem;
            }

            @SuppressWarnings("TryWithIdenticalCatches")
            public void setItem(E mItem) {
                this.mItem = mItem;

                Type superclass = CandyListFragment.this.getClass().getGenericSuperclass();
                if (superclass instanceof Class)
                    throw new RuntimeException("Missing type parameter.");
                final Type type = ((ParameterizedType) superclass).getActualTypeArguments()[2];

                String typeName = type.toString().substring(6);
                String packageName = getContext().getPackageName();
                for (Method method : binding.getClass().getDeclaredMethods())
                    if (method.getParameterTypes().length == 1 && method.getName().length() > 3 && method.getName().substring(0, 3).equals("set")) {
                        String localTypeName = method.getParameterTypes()[0].getName();
                        if (localTypeName.equals(typeName) && ((localTypeName.length() > packageName.length() && localTypeName.substring(0, packageName.length()).equals(packageName)) || !(packageName.substring(0, 7).equals("android") || packageName.substring(0, 4).equals("java") || packageName.substring(0, 3).equals("sun"))))
                            try {
                                method.invoke(binding, mItem);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                    }
            }

            public T2 getBinding() {
                return binding;
            }
        }
    }
}
