package com.bufeotec.sipcsi.Activitys;


import android.content.Intent;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.bufeotec.sipcsi.Principal.MainActivity;

import com.bufeotec.sipcsi.R;
import com.bufeotec.sipcsi.WebServices.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

import static com.bufeotec.sipcsi.WebServices.DataConnection.IP;

public class CambiarContrasena extends AppCompatActivity {


    //inicialiamos las variables para cada elemento de la vista
    EditText contraNueva,contraAntigua,contraNuevaRepetida;
    Button btnContra;
    String id ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);



        //enlazamos las variables con los elementos de la vista
        contraNueva=findViewById(R.id.contraNueva);
        contraAntigua=findViewById(R.id.contraAntigua);
        contraNuevaRepetida=findViewById(R.id.contraNuevaRepetida);
        btnContra=findViewById(R.id.btnCambiarContra);

        //enviamos titulo al toolbar y habilitamos la flecha
        showToolbar("Cambiar Contraseña",true);

        //obtenemos el id de la pantalla "EditarPerfil"
        id=getIntent().getExtras().getString("id");



        //al hacer click al boton validamos si coinciden las contraseñas y enviamos los datos al servidor
        btnContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (contraNueva.getText().toString().equals(contraNuevaRepetida.getText().toString())){
                    cambiar();
                }else{
                    Toast.makeText(CambiarContrasena.this, "Las Contraseñas no Coinciden", Toast.LENGTH_SHORT).show();
                }

                cambiar();

            }
        });
    }

    StringRequest stringRequest;


    //metodo para enviar datos de cambio de contraseña
    private void cambiar() {
        String url ="https://"+IP+"/index.php?c=Usuario&a=editar_contrasenha&key_mobile=123456asdfgh";
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                //validacion de contraseña se acuerdo a la respuesta del servidor

                Log.e("respuesta de contraseña", "onResponse: " + response.trim().toString() );
                if(response.trim().equalsIgnoreCase("1")){


                    Toast.makeText(CambiarContrasena.this, "Contraseña Modifcicada Satisfactoriamente", Toast.LENGTH_SHORT).show();
                    Intent i =  new Intent(CambiarContrasena.this, MainActivity.class);
                    startActivity(i);
                }else if(response.trim().equalsIgnoreCase("2")){
                    Toast.makeText(CambiarContrasena.this, "Hubo un Problema", Toast.LENGTH_SHORT).show();
                    contraAntigua.setText("");
                    contraNueva.setText("");
                    contraNuevaRepetida.setText("");
                }
                else {
                    //Toast.makeText(context,"la fruta ",Toast.LENGTH_SHORT).show();
                    Toast.makeText(CambiarContrasena.this, "La Contrasela Antigua no Coincide", Toast.LENGTH_SHORT).show();
                    contraAntigua.setText("");
                    contraNueva.setText("");
                    contraNuevaRepetida.setText("");


            }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context,"error ",Toast.LENGTH_SHORT).show();
                Log.i("RESPUESTA: ",""+error.toString());

            }
        } ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String,String> parametros=new HashMap<>();                              //creamos un hashmap con los parametros correspondientes
                parametros.put("id_usuario",id);
                parametros.put("contrasenha_nueva",contraNueva.getText().toString());
                parametros.put("contrasenha_antigua",contraAntigua.getText().toString());

                return parametros;                                                          //enviamos el hashmap

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(getApplicationContext()).addToRequestQueue(stringRequest);
    }



    //mostrar el toolbar
    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);      //asociamos el toolbar con el archivo xml
        toolbar.setTitleTextColor(Color.WHITE);                     //el titulo color blanco
        toolbar.setSubtitleTextColor(Color.WHITE);                  //el subtitulo color blanco
        setSupportActionBar(toolbar);                               //pasamos los parametros anteriores a la clase Actionbar que controla el toolbar

        getSupportActionBar().setTitle(tittle);                     //asiganmos el titulo que llega
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);  //y habilitamos la flacha hacia atras

    }


    //comportamiento de la flecha atras
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();                        //definimos que al dar click a la flecha, nos lleva a la pantalla anterior
        return false;
    }
}
