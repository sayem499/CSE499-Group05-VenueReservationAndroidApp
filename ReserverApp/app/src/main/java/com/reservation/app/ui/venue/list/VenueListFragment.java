package com.reservation.app.ui.venue.list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reservation.app.R;

/**
 * @author Fatema
 * since 8/25/21.
 */
public class VenueListFragment extends Fragment {

    public VenueListFragment() {
    }

    public static VenueListFragment newInstance() {
        return new VenueListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_venue_list, container, false);

        getActivity().setTitle(getString(R.string.venue_list));

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            recyclerView.setAdapter(new VenueAdapter(Collections.emptyList()));
        }

        return view;
    }
}