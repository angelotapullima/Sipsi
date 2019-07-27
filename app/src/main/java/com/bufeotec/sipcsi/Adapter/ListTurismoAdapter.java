package com.bufeotec.sipcsi.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bufeotec.sipcsi.Models.Puntos;
import com.bufeotec.sipcsi.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.bufeotec.sipcsi.WebServices.DataConnection.IP;

public class ListTurismoAdapter  extends RecyclerView.Adapter<ListTurismoAdapter.ListaTViewHolder> {
    private ArrayList<Puntos> array;
    private int layoutpadre;
    Context context;
    Puntos obj;
    private  OnItemClickListener listener;


    public ListTurismoAdapter(Context context, ArrayList<Puntos> array, int layoutpadre, OnItemClickListener listener) {
        this.array = array;
        this.layoutpadre = layoutpadre;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListTurismoAdapter.ListaTViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(layoutpadre,parent,false);
        return new ListaTViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ListTurismoAdapter.ListaTViewHolder holder, int position) {


        //setAnimation(holder.itemView, position);
        obj = array.get(position);
        /*if (obj.getTipo().equals("Moto")){

            holder.txt_tipo.setText(obj.getTipo());
            holder.txt_tipo.setBackgroundColor(Color.rgb(63,81,181));
            holder.view4.setBackgroundColor(Color.rgb(63,81,181));

        }if (obj.getTipo().equals("Carro")){

            holder.txt_tipo.setText(obj.getTipo());
            holder.txt_tipo.setBackgroundColor(Color.rgb(232,52,51));
            holder.view4.setBackgroundColor(Color.rgb(232,52,51));
        }*/


        Glide.with(context).load("https://"+IP+"/"+ obj.getImagen()).into(holder.image);
        holder.des_pt.setText(obj.getDescripcion());
        holder.nombre_pt.setText(obj.getNombre());
        holder.calle_pt.setText(obj.getDireccion());
        holder.bid(obj,listener);

    }

    @Override
    public int getItemCount() {
        return array.size();
    }
    public class ListaTViewHolder extends RecyclerView.ViewHolder {

        TextView nombre_pt,des_pt,calle_pt;
        ImageView image;



        public ListaTViewHolder(View itemView) {
            super(itemView);
            nombre_pt=itemView.findViewById(R.id.nombre_pt);
            des_pt=itemView.findViewById(R.id.des_pt);
            calle_pt=itemView.findViewById(R.id.calle_pt);
            image=itemView.findViewById(R.id.imagen_turismo);


        }
        public void bid(final Puntos puntos,final OnItemClickListener listener){

            /*btnSolicitar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(turismo,getAdapterPosition());
                }
            });*/
        }
    }
    public interface  OnItemClickListener{
        void onItemClick(Puntos puntos, int position);
    }
}
