package com.reservation.app.ui;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.reservation.app.R;
import com.reservation.app.datasource.SharedPrefManager;
import com.reservation.app.datasource.UserModel;
import com.reservation.app.viewmodel.AppViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    private AppViewModel appViewModel;
    private SharedPrefManager pref;
    private EditText firstName,lastName,email,address;
    private Button updateButton;
    private List<UserModel> userList;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        appViewModel =  new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.requireActivity().getApplication())).get(AppViewModel.class);
        pref = new SharedPrefManager(getContext());


        userList = new ArrayList<>();
        appViewModel.initUserData();
        userList = appViewModel.fetchUserData();




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
         firstName = view.findViewById(R.id.firstNameEditText);
         lastName = view.findViewById(R.id.lastNameEditText);
         email = view.findViewById(R.id.emailAddressEditText);
         address = view.findViewById(R.id.addressEditText);


         setUserData();

         updateButton = view.findViewById(R.id.button_update);
         updateButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 updateUserData();
             }
         });



         return view;
    }

    private void setUserData(){

        if(userList != null)
        for(UserModel userModel : userList)
            if(userModel.getUserPhoneNumber().equals(pref.getPhoneNumber())){
                firstName.setText(userModel.getUserFirstName());
                lastName.setText(userModel.getUserLastName());
                email.setText(userModel.getUserEmail());
                address.setText(userModel.getUserAddress());
                break;
            }

    }

    private void updateUserData() {
        UserModel userModel = new UserModel();
        userModel.setUserFirstName(firstName.getText().toString());
        userModel.setUserLastName(lastName.getText().toString());
        userModel.setUserEmail(email.getText().toString());
        userModel.setUserAddress(address.getText().toString());
        userModel.setUserPhoneNumber(pref.getPhoneNumber());

        appViewModel.insertUserData(userModel,getContext());
        Toast.makeText(getContext(),"Profile Updated.", Toast.LENGTH_SHORT).show();
    }

}