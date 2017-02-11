package me.t0rr3sp3dr0.confectionery.interfaces;

import android.support.annotation.NonNull;

/**
 * This interface must be implemented by activities that contain this
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 *
 * @author Pedro Tôrres
 */
public interface OnListFragmentInteractionListener {
    void onListFragmentInteraction(Class<?> clazz, @NonNull Object object);
}
