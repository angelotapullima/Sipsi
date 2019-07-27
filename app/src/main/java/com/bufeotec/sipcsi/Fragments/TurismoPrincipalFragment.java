package com.bufeotec.sipcsi.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bufeotec.sipcsi.R;


public class TurismoPrincipalFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {



    BottomNavigationView bnv_menu;
    public Fragment fragmentactual;
    FragmentTransaction fragmentTransaction;


    private OnFragmentInteractionListener mListener;

    public TurismoPrincipalFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_turismo_principal, container, false);

        bnv_menu = view.findViewById(R.id.bnv_menu);
        bnv_menu.setOnNavigationItemSelectedListener(this);

        setInitialFragment();
        return  view;
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment;
        fragmentTransaction = getFragmentManager().beginTransaction();


        switch (menuItem.getItemId()) {
            case R.id.lista_pt:
                fragment =  getFragmentManager().findFragmentByTag("ListaPuntos");

                if (fragment!=null) {
                    //Toast.makeText(getApplicationContext(),"Existe",Toast.LENGTH_SHORT).show();
                    fragmentTransaction.hide(fragmentactual).show(fragment);
                }else {
                    fragment = new ListaPuntosTuristicos();
                    //Toast.makeText(getApplicationContext(),"Anadido",Toast.LENGTH_SHORT).show();
                    fragmentTransaction.hide(fragmentactual).add(R.id.content_frame, fragment, "ListaPuntos");
                }
                fragmentTransaction.commit();
                fragmentactual = fragment;

                break;
            case R.id.mapa_pt:
                fragment =  getFragmentManager().findFragmentByTag("MapaPuntos");


                if (fragment!=null) {
                    //Toast.makeText(getApplicationContext(),"Existe",Toast.LENGTH_SHORT).show();
                    fragmentTransaction.hide(fragmentactual).show(fragment);
                }else {
                    fragment = new PuntosTuristicosFragment ();
                    //Toast.makeText(getApplicationContext(),"Anadido",Toast.LENGTH_SHORT).show();
                    fragmentTransaction.hide(fragmentactual).add(R.id.content_frame, fragment, "MapaPuntos");
                }
                fragmentTransaction.commit();
                fragmentactual = fragment;

                break;

        }
        return true;
    }



    private void setInitialFragment() {
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentactual = new ListaPuntosTuristicos();
        fragmentTransaction.add(R.id.content_frame, fragmentactual,"ListaPuntos");
        fragmentTransaction.commit();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
