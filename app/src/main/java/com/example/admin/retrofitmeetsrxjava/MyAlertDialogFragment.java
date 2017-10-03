package com.example.admin.retrofitmeetsrxjava;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.retrofitmeetsrxjava.data.modelhomeworklocation.Place;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.value;


public class MyAlertDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Double mParam1;
    private Double mParam2;

  //  private OnFragmentInteractionListener mListener;



    public MyAlertDialogFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MyAlertDialogFragment newInstance(String title, Double currentLat, Double currentLon) {
        MyAlertDialogFragment fragment = new MyAlertDialogFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_PARAM1, currentLat);
        args.putDouble(ARG_PARAM2, currentLon);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        final Double showDoubleLati =getArguments().getDouble(ARG_PARAM1);
        final Double showDoubleLongi =getArguments().getDouble(ARG_PARAM2);
       // final String showLat = String.valueOf(getArguments().getDouble(ARG_PARAM1));
        //final String showLon = String.valueOf(getArguments().getDouble(ARG_PARAM2));
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(showDoubleLati + "\n" + showDoubleLongi);
        alertDialogBuilder.setPositiveButton("View on Map",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on success
                Place place = new Place(showDoubleLati,showDoubleLongi );
                List<Place> placeList = new ArrayList<Place>();
                placeList.add(place);


                Intent intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra("passList", (Serializable) placeList);
               // Bundle args = new Bundle();
                //args.putSerializable("passList", (Serializable) placeList);
                //intent.putExtras(args);
            //    intent.putExtra("lat", currentLocation.getLatitude());
              //  intent.putExtra("lon", currentLocation.getLongitude());
                //intent.putExtra("location", currentLocation);
                startActivity(intent);
            }
        });
        alertDialogBuilder.setNegativeButton("Add to list", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // String value1 = showLat;
               // String value2 = showLon;
                //  Log.d("Quantity: ", value);
                MainActivity callingActivity = (MainActivity) getActivity();
                callingActivity.onUserSelectValue(showDoubleLati, showDoubleLongi);

                             /*
                                Intent intent = new Intent(getContext(), MapsActivity.class);
                                intent.putExtra("lat", currentLocation.getLatitude());
                                intent.putExtra("lon", currentLocation.getLongitude());
                                //    intent.putExtra("location", currentLocation.getLatitude());
                                startActivity(intent);
*/
                              /*  if (dialog != null && dialog.isShowing()) {
                                        dialog.dismiss();
                                }
                                */
            }

        });
        return alertDialogBuilder.create();
    }




        /*
        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getDouble(ARG_PARAM1);
            mParam2 = getArguments().getDouble(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_alert_dialog, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    */
}
