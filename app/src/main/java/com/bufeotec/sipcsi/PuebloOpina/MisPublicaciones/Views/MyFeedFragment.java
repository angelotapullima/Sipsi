package com.bufeotec.sipcsi.PuebloOpina.MisPublicaciones.Views;


import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bufeotec.sipcsi.PuebloOpina.MisPublicaciones.Models.ModelMyFeed;
import com.bufeotec.sipcsi.PuebloOpina.MisPublicaciones.Repository.MyFeedWebServiceRepository;
import com.bufeotec.sipcsi.PuebloOpina.MisPublicaciones.ViewModels.MyFeedListViewModel;
import com.bufeotec.sipcsi.R;
import com.bufeotec.sipcsi.Util.Preferences;
import com.bufeotec.sipcsi.WebServices.VolleySingleton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bufeotec.sipcsi.WebServices.DataConnection.IP;


public class MyFeedFragment extends Fragment  implements  SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {




    RecyclerView rcv_misPublicaciones;
    ProgressBar progressBar;
    CardView cdv_mensaje;
    SwipeRefreshLayout swipeRefreshLayout;
    Context context;
    Activity activity;
    Preferences pref;
    LinearLayout tap_de_accion,LayoutEliminar;
    private BottomSheetBehavior mBottomSheetBehavior;
    String IdPueblo;
    View bottomEli;

    String url = "https://" + IP + "/index.php?c=Pueblo&a=guardar&key_mobile=123456asdfgh";


    View view = null;
    MyFeedListViewModel retroViewModel;
    ProgressDialog progressDialog;
    public MyFeedFragment() {
        // Required empty public constructor
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        retroViewModel = ViewModelProviders.of(getActivity()).get(MyFeedListViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_publicaciones, container, false);

        context = getContext();
        activity = getActivity();


        initViews(view);
        setAdapter();
       //progressDialog= ProgressDialog.show(getActivity(), "Loading...", "Please wait...", true);
       cargarvista();


        bottomEli = view.findViewById(R.id.bottomEli);
        tap_de_accion = view.findViewById(R.id.tap_de_accion);
        LayoutEliminar = view.findViewById(R.id.bottomEliminar);

        LayoutEliminar.setOnClickListener(this);

        mBottomSheetBehavior=BottomSheetBehavior.from(bottomEli);
        mBottomSheetBehavior.setPeekHeight(0);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    tap_de_accion.setVisibility(View.GONE);
                }
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    tap_de_accion.setVisibility(View.VISIBLE);
                }
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    tap_de_accion.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        return  view;
    }



    private void initViews(View view){
        rcv_misPublicaciones = (RecyclerView)view.findViewById(R.id.rcv_misPublicaciones);
        progressBar = view.findViewById(R.id.progressbar);
        cdv_mensaje = view.findViewById(R.id.cdv_mensaje);
        pref = new Preferences(context);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);

    }

    Application application;
    public void cargarvista(){
        retroViewModel.getAllPosts().observe(this, new Observer<List<ModelMyFeed>>() {
            @Override
            public void onChanged(@Nullable List<ModelMyFeed> modelMyFeeds) {
                adapter.setWords(modelMyFeeds);
                if(progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                cdv_mensaje.setVisibility(View.INVISIBLE);
            }
        });
    }

    AdapterMyFeed adapter = null;

    private void setAdapter(){
        adapter = new AdapterMyFeed(getActivity(), new AdapterMyFeed.OnItemClickListener() {
            @Override
            public void onItemClick(ModelMyFeed modelFeed, int position) {

                Log.e("mis publicaciones", "onItemClick: true " );

                IdPueblo=modelFeed.getId();
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                }else{
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
        });
        rcv_misPublicaciones.setAdapter(adapter);
        rcv_misPublicaciones.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onRefresh() {


         MyFeedWebServiceRepository myFeedWebServiceRepository = new MyFeedWebServiceRepository(application);
         myFeedWebServiceRepository.providesWebService();
        setAdapter();
        cargarvista();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {

        if (v.equals(LayoutEliminar)){

            EliminarPublicacionWeb(IdPueblo);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        }
    }


    StringRequest stringRequest;
    private void EliminarPublicacionWeb(final String idpueblo) {
        String url ="https://"+IP+"/index.php?c=Pueblo&a=eliminar&key_mobile=123456asdfgh";
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("eliminar Publicación: ",""+response);

                if (response.toString().equals("1")){
                    Toast.makeText(context, "Publicacion eliminada", Toast.LENGTH_SHORT).show();
                    retroViewModel.deleteOneFeed(idpueblo);
                    //onRefresh();
                }
            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context,"error ",Toast.LENGTH_SHORT).show();
                Log.i("RESPUESTA: ",""+error.toString());

            }
        })  {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //String imagen=convertirImgString(bitmap);


                Map<String,String> parametros=new HashMap<>();
                parametros.put("id",idpueblo);

                return parametros;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(stringRequest);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
