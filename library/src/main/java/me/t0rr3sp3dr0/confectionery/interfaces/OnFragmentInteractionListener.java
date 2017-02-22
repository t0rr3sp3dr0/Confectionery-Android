package me.t0rr3sp3dr0.confectionery.interfaces;

import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * This interface must be implemented by activities that contain this
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 *
 * @author Pedro Tôrres
 * @since 0.0.1
 */
public interface OnFragmentInteractionListener {
    void onFragmentInteraction(Uri uri, @Nullable Object object);
}
