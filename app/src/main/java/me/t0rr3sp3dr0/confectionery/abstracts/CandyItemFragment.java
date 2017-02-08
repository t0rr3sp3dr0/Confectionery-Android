package me.t0rr3sp3dr0.confectionery.abstracts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.CaseFormat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.t0rr3sp3dr0.confectionery.interfaces.OnListFragmentInteractionListener;
import me.t0rr3sp3dr0.confectionery.singletons.Mailbox;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public abstract class CandyItemFragment<T1 extends ViewDataBinding, T2 extends ViewDataBinding, E> extends CandyFragment<T1> {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private List<E> mValues = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CandyItemFragment() {
    }

    /**
     * Use this constructor method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param columnCount Number of columns in {@link RecyclerView}.
     * @param values      List with rows' {@link E} of {@link RecyclerView}.
     */
    @SuppressLint("ValidFragment")
    public CandyItemFragment(int columnCount, @NonNull List<E> values) {
        Bundle args = new Bundle();

        args.putInt(ARG_COLUMN_COUNT, columnCount);

        String hash = Integer.toString(System.identityHashCode(values));
        args.putString("this$$list", hash);
        Mailbox.getInstance().put(String.format("%s$$list", hash), values);

        setArguments(args);
    }

    /**
     * Use this constructor method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param columnCount Number of columns in {@link RecyclerView}.
     * @param values      List with rows' {@link E} of {@link RecyclerView}.
     * @param map         A mapping from String keys to various {@link Object} values.
     */
    @SuppressLint("ValidFragment")
    public CandyItemFragment(int columnCount, @NonNull List<E> values, @NonNull Map<String, Object> map) {
        super(map);

        getArguments().putInt(ARG_COLUMN_COUNT, columnCount);

        String hash = Integer.toString(System.identityHashCode(values));
        getArguments().putString("this$$list", hash);
        Mailbox.getInstance().put(String.format("%s$$list", hash), values);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mColumnCount = savedInstanceState.getInt(ARG_COLUMN_COUNT);

            String hash = savedInstanceState.getString("this$$list");
            savedInstanceState.remove("this$$list");
            //noinspection unchecked
            mValues = (List<E>) Mailbox.getInstance().remove(String.format("%s$$list", hash));
        } else if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);

            String hash = getArguments().getString("this$$list");
            getArguments().remove("this$$list");
            //noinspection unchecked
            mValues = (List<E>) Mailbox.getInstance().remove(String.format("%s$$list", hash));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new CandyItemRecyclerViewAdapter());
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ARG_COLUMN_COUNT, mColumnCount);

        String hash = Integer.toString(System.identityHashCode(mValues));
        outState.putString("this$$list", hash);
        Mailbox.getInstance().put(String.format("%s$$list", hash), mValues);
    }

    public void onBindViewHolder(List<E> mValues, final CandyItemRecyclerViewAdapter.ViewHolder holder, int position) {

    }

    /**
     * {@link RecyclerView.Adapter} that can display a {@link E} and makes a call to the
     * specified {@link OnListFragmentInteractionListener}.
     */
    public final class CandyItemRecyclerViewAdapter extends RecyclerView.Adapter<CandyItemRecyclerViewAdapter.ViewHolder> {

        @Override
        public final ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Type superclass = CandyItemFragment.this.getClass().getGenericSuperclass();
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
        public final void onBindViewHolder(final ViewHolder holder, int position) {
            holder.setItem(mValues.get(position));

            CandyItemFragment.this.onBindViewHolder(mValues, holder, position);
        }

        @Override
        public final int getItemCount() {
            return mValues.size();
        }

        public final class ViewHolder extends RecyclerView.ViewHolder {
            private final T2 binding;
            private final View mView;
            private E mItem;

            public ViewHolder(T2 binding) {
                super(binding.getRoot());
                this.binding = binding;
                this.mView = binding.getRoot();

                mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mListener) {
                            // Notify the active callbacks interface (the activity, if the
                            // fragment is attached to one) that an item has been selected.
                            mListener.onListFragmentInteraction(CandyItemFragment.this.getClass(), mItem);
                        }
                    }
                });
            }

            @Override
            public final String toString() {
                return super.toString() + " '" + mItem.toString() + "'";
            }

            public final E getItem() {
                return mItem;
            }

            public final void setItem(E mItem) {
                this.mItem = mItem;

                Type superclass = CandyItemFragment.this.getClass().getGenericSuperclass();
                if (superclass instanceof Class)
                    throw new RuntimeException("Missing type parameter.");
                final Type type = ((ParameterizedType) superclass).getActualTypeArguments()[2];

                String typeName = type.toString().substring(6);
                String packageName = getContext().getPackageName();
                for (Method method : binding.getClass().getDeclaredMethods())
                    if (method.getParameterTypes().length == 1 && method.getName().length() > 3 && method.getName().substring(0, 3).equals("set")) {
                        String localTypeName = method.getParameterTypes()[0].getName();
                        if (localTypeName.equals(typeName) && localTypeName.length() > packageName.length() && localTypeName.substring(0, packageName.length()).equals(packageName))
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
