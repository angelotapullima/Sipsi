package com.bufeotec.sipcsi.PuebloOpina.MisPublicaciones.Views;


import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bufeotec.sipcsi.PuebloOpina.MisPublicaciones.Models.ModelMyFeed;
import com.bufeotec.sipcsi.PuebloOpina.MisPublicaciones.Repository.MyFeedWebServiceRepository;
import com.bufeotec.sipcsi.PuebloOpina.MisPublicaciones.ViewModels.MyFeedListViewModel;
import com.bufeotec.sipcsi.R;
import com.bufeotec.sipcsi.Util.Preferences;

import java.util.List;

import static com.bufeotec.sipcsi.WebServices.DataConnection.IP;


public class MyFeedFragment extends Fragment  implements  SwipeRefreshLayout.OnRefreshListener{




    RecyclerView rcv_misPublicaciones;
    ProgressBar progressBar;
    CardView cdv_mensaje;
    SwipeRefreshLayout swipeRefreshLayout;
    Context context;
    Activity activity;
    Preferences pref;

    String url = "http://" + IP + "/index.php?c=Pueblo&a=guardar&key_mobile=123456asdfgh";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view = null;
    MyFeedListViewModel retroViewModel;
    ProgressDialog progressDialog;
    public MyFeedFragment() {
        // Required empty public constructor
    }



    public static MyFeedFragment newInstance(String param1, String param2) {
        MyFeedFragment fragment = new MyFeedFragment();
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
        retroViewModel = ViewModelProviders.of(getActivity()).get(MyFeedListViewModel.class);
    //    progressDialog = new ProgressDialog(getActivity());

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
        adapter = new AdapterMyFeed(getActivity());
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





    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
