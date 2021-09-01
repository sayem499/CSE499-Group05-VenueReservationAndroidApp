package com.reservation.app.ui.venue.list;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reservation.app.R;
import com.reservation.app.datasource.VenueDataManager;
import com.reservation.app.datasource.helper.RemoteResult;
import com.reservation.app.model.Venue;
import com.reservation.app.ui.util.DialogBuilder;
import com.reservation.app.ui.venue.list.adapter.VenueAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fatema
 * since 8/25/21.
 */
public class VenueListFragment extends Fragment {

    private VenueAdapter venueAdapter;
    private RecyclerView recyclerView;

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

        recyclerView = view.findViewById(R.id.list);

        venueAdapter = new VenueAdapter(new ArrayList<>());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(venueAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Dialog progressDialog = DialogBuilder.buildProgressDialog(getContext());
        progressDialog.show();

        VenueDataManager.requestVenueList(new RemoteResult<List<Venue>>() {

            @Override
            public void onSuccess(List<Venue> data) {
                venueAdapter.updateList(data);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Exception error) {
                DialogBuilder.buildOkDialog(getContext(), error.getMessage()).show();
                progressDialog.dismiss();
            }
        });
    }
}