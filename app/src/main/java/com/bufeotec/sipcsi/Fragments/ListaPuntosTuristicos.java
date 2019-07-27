package com.bufeotec.sipcsi.Fragments;
import android.app.Activity;
import android.content.Context;
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

import com.bufeotec.sipcsi.Adapter.ListTurismoAdapter;
import com.bufeotec.sipcsi.Models.Puntos;
import com.bufeotec.sipcsi.R;
import com.bufeotec.sipcsi.WebServices.DataConnection;

import java.util.ArrayList;


public class ListaPuntosTuristicos extends Fragment {

    Activity activity;
    Context context;
    DataConnection dc;
    //CardView cdv_mensaje;
    RecyclerView rcv_list_puntos;
    ListTurismoAdapter listTurismoAdapter;
    public ArrayList<Puntos> listaTuristicos;


    public ListaPuntosTuristicos() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_lista_puntos_turisticos, container, false);
        context=getContext();
        activity=getActivity();
        rcv_list_puntos=view.findViewById(R.id.rcv_lista_puntos);
        dc = new DataConnection(getActivity(),"listarTuristicos",false);
        new ListaPuntosTuristicos.GetTuristicos().execute();
        return  view;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public  class GetTuristicos extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            listaTuristicos = dc.getListturisticos();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recycler();
        }
    }


    private void recycler() {



        //progressBar.setVisibility(ProgressBar.INVISIBLE);


        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rcv_list_puntos.setLayoutManager(layoutManager);

        listTurismoAdapter =  new ListTurismoAdapter(context, listaTuristicos, R.layout.rcv_list_turismo, new ListTurismoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Puntos puntos, int position) {

            }
        });






        rcv_list_puntos.setAdapter(listTurismoAdapter);

        if( listaTuristicos.size()>0){
            //cdv_mensaje.setVisibility(View.INVISIBLE);
        }else{
            //cdv_mensaje.setVisibility(View.VISIBLE);
        }


    }
}
