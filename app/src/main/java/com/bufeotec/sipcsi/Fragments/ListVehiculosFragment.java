package com.bufeotec.sipcsi.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bufeotec.sipcsi.Activitys.SeguimientoPorUnidad;
import com.bufeotec.sipcsi.Adapter.ListVehiculosAdapter;
import com.bufeotec.sipcsi.Models.Vehiculos;
import com.bufeotec.sipcsi.R;
import com.bufeotec.sipcsi.Util.Preferences;
import com.bufeotec.sipcsi.WebServices.DataConnection;

import java.util.ArrayList;


public class ListVehiculosFragment extends Fragment {


    Activity activity;
    Context context;
    DataConnection dc;
    ListVehiculosAdapter listVehiculosAdapter;
    CardView cdv_mensaje;
    ProgressBar progressBar;
    RecyclerView rcv_vehiculos;
    Preferences pref;

    public ArrayList<Vehiculos> arrayvehiculos;


    private OnFragmentInteractionListener mListener;

    public ListVehiculosFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_list_vehiculos, container, false);


        activity = getActivity();
        context = getContext();
        pref=new Preferences(context);
        getActivity().setTitle("Lista De Vehiculos");
        progressBar = view.findViewById(R.id.progressbar);
        rcv_vehiculos = view.findViewById(R.id.rcv_vehiculos);
        cdv_mensaje = view.findViewById(R.id.cdv_mensaje);
        cdv_mensaje.setVisibility(View.INVISIBLE);


        dc = new DataConnection(getActivity(),"listarVehiculos",pref.getIdUsuarioPref(),false);
        new GetVehiculos().execute();

        return view;
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class GetVehiculos extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            arrayvehiculos = dc.getListaVehiculos();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            recycler();


        }
    }

    private void recycler() {



        progressBar.setVisibility(ProgressBar.INVISIBLE);
        rcv_vehiculos.setHasFixedSize(true);


        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rcv_vehiculos.setLayoutManager(layoutManager);

        listVehiculosAdapter = new ListVehiculosAdapter(activity, arrayvehiculos, R.layout.rcv_item_vehiculos, new ListVehiculosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Vehiculos vehiculos, int position) {

                String id = vehiculos.getId_vehiculo();
                String lat = vehiculos.getLatitud();
                String lon = vehiculos.getLongitud();
                String to = vehiculos.getTok();
                String placa = vehiculos.getPlaca();

                Intent i =  new Intent(activity, SeguimientoPorUnidad.class);

                i.putExtra("lat",lat);
                i.putExtra("long",lon);
                i.putExtra("id_vehiculo",id);
                i.putExtra("tok",to);
                i.putExtra("placa",placa);
                startActivity(i);



            }
        });

        rcv_vehiculos.setAdapter(listVehiculosAdapter);
        if( arrayvehiculos.size()>0){
            cdv_mensaje.setVisibility(View.INVISIBLE);
        }else{
            cdv_mensaje.setVisibility(View.VISIBLE);
        }
    }
}
