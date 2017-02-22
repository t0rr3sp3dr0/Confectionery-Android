package me.t0rr3sp3dr0.confectionery.abstracts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.CaseFormat;

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
public abstract class CandyListFragment<T1 extends ViewDataBinding, T2 extends ViewDataBinding, E> extends CandyFragment<T1> {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private List<E> mValues = new ArrayList<>();
    private boolean dividerItemDecoration = true;

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

        String hash = Integer.toString(System.identityHashCode(values));
        args.putString("this$$list", hash);
        StringObjectMap.getInstance().put(String.format("%s$$list", hash), values);

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

        String hash = Integer.toString(System.identityHashCode(values));
        getArguments().putString("this$$list", hash);
        StringObjectMap.getInstance().put(String.format("%s$$list", hash), values);

        getArguments().putBoolean("this$$dividerItemDecoration", dividerItemDecoration);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mColumnCount = savedInstanceState.getInt(ARG_COLUMN_COUNT);

            String hash = savedInstanceState.getString("this$$list");
            savedInstanceState.remove("this$$list");
            //noinspection unchecked
            mValues = (List<E>) StringObjectMap.getInstance().remove(String.format("%s$$list", hash));

            dividerItemDecoration = savedInstanceState.getBoolean("this$$dividerItemDecoration");
        } else if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);

            String hash = getArguments().getString("this$$list");
            getArguments().remove("this$$list");
            //noinspection unchecked
            mValues = (List<E>) StringObjectMap.getInstance().remove(String.format("%s$$list", hash));

            dividerItemDecoration = getArguments().getBoolean("this$$dividerItemDecoration");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (view instanceof RecyclerView) {
            if (mValues != null) {
                reloadData((RecyclerView) view);
            } else
                Log.w("Confectionery", "Your data source is null! Please override CandyListFragment#onCreateView and invoke, before super, CandyListFragment#setValues with your nonnull data source.");
        } else
            Log.w("Confectionery", "Your root view is not instanceof RecyclerView! Please override CandyListFragment#onCreateView and invoke, after super, CandyListFragment#reloadData with the correct RecyclerView.");

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ARG_COLUMN_COUNT, mColumnCount);

        String hash = Integer.toString(System.identityHashCode(mValues));
        outState.putString("this$$list", hash);
        StringObjectMap.getInstance().put(String.format("%s$$list", hash), mValues);

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
    public void onBindViewHolder(final CandyItemRecyclerViewAdapter.ViewHolder holder, int position, @NonNull List<E> payloads) {
    }

    public final int getColumnCount() {
        return mColumnCount;
    }

    public final void setColumnCount(int columnCount) {
        this.mColumnCount = columnCount;
    }

    @NonNull
    public final List<E> getValues() {
        return mValues;
    }

    public final void setValues(@NonNull List<E> values) {
        this.mValues = values;
    }

    public final void reloadData(RecyclerView recyclerView) {
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

    /**
     * {@link RecyclerView.Adapter} that can display a {@link E} and makes a call to the
     * specified {@link OnListFragmentInteractionListener}.
     */
    public final class CandyItemRecyclerViewAdapter extends RecyclerView.Adapter<CandyItemRecyclerViewAdapter.ViewHolder> {

        CandyItemRecyclerViewAdapter() {
            // Package local constructor to avoid class instantiation outside this module
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Type superclass = CandyListFragment.this.getClass().getGenericSuperclass();
            if (superclass instanceof Class)
                throw new RuntimeException("Missing type parameter.");
            final Type type = ((ParameterizedType) superclass).getActualTypeArguments()[1];

            String typeName = type.toString();
            String layoutName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, typeName.substring(typeName.lastIndexOf('.') + 1, typeName.length() - 7));
            int layoutId = parent.getResources().getIdentifier(layoutName, "layout", parent.getContext().getPackageName());

            T2 binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutId, parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.setItem(mValues.get(position));

            CandyListFragment.this.onBindViewHolder(holder, position, Collections.unmodifiableList(mValues));
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        @SuppressWarnings("WeakerAccess")
        public final class ViewHolder extends RecyclerView.ViewHolder {
            private final T2 binding;
            private final View mView;
            private E mItem;

            ViewHolder(T2 binding) {
                super(binding.getRoot());
                this.binding = binding;
                this.mView = binding.getRoot();

                mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CandyListFragment.this.onListFragmentInteraction(mItem);
                    }
                });
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mItem.toString() + "'";
            }

            public E getItem() {
                return mItem;
            }

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
