package com.bufeotec.sipcsi.PuebloOpina.Publicaciones.Views;


import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bufeotec.sipcsi.Models.Areas;
import com.bufeotec.sipcsi.Principal.MainActivity;
import com.bufeotec.sipcsi.PuebloOpina.Publicaciones.Models.ModelFeed;
import com.bufeotec.sipcsi.PuebloOpina.Publicaciones.Repository.FeedWebServiceRepository;
import com.bufeotec.sipcsi.PuebloOpina.Publicaciones.ViewModels.FeedListViewModel;
import com.bufeotec.sipcsi.R;
import com.bufeotec.sipcsi.Util.Preferences;
import com.bufeotec.sipcsi.WebServices.DataConnection;
import com.bufeotec.sipcsi.WebServices.VolleySingleton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.theartofdev.edmodo.cropper.CropImage;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationAction;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;
import static com.bufeotec.sipcsi.WebServices.DataConnection.IP;
import static net.gotev.uploadservice.Placeholders.ELAPSED_TIME;
import static net.gotev.uploadservice.Placeholders.PROGRESS;
import static net.gotev.uploadservice.Placeholders.TOTAL_FILES;
import static net.gotev.uploadservice.Placeholders.UPLOADED_FILES;
import static net.gotev.uploadservice.Placeholders.UPLOAD_RATE;




public class FeedFragment extends Fragment implements View.OnClickListener,  SwipeRefreshLayout.OnRefreshListener{



    DataConnection  dc2;
    SwipeRefreshLayout swipeRefreshLayout;
    Context context;
    Activity activity;
    ProgressBar progressBar;
    CardView cdv_mensaje;
    TextView btn_publicar, dialog_publicar_close, nombre_publicacion;
    public static ArrayList<Areas> arrayArea;
    ArrayList<String> arrayarea;
    Spinner spn_areas;
    AppCompatButton bt_publicar;
    ImageView imagen_publicacion, fotocaptada;
    EditText et_post;
    ImageButton bt_photo;
    Preferences pref;
    boolean valorFoto;

    String IdPueblo;
    View bottomEli;
    LinearLayout tap_de_accion,LayoutEliminar;
    private BottomSheetBehavior mBottomSheetBehavior;


    private int REQUEST_CAMERA = 0, SELET_GALERRY = 9;
    public Uri output, resultUriRecortada;
    String userChoosenTask;

    String url = "https://" + IP + "/index.php?c=Pueblo&a=guardar&key_mobile=123456asdfgh";



    View view = null;
    FeedListViewModel retroViewModel;
    ProgressDialog progressDialog;
    public FeedFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    //    progressDialog = new ProgressDialog(getActivity());
        retroViewModel = ViewModelProviders.of(getActivity()).get(FeedListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_feed, container, false);

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
        btn_publicar.setOnClickListener(this);

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

    RecyclerView recyclerView;
    private void initViews(View view){
        recyclerView = (RecyclerView)view.findViewById(R.id.post_list);
        progressBar = view.findViewById(R.id.progressbar);
        cdv_mensaje = view.findViewById(R.id.cdv_mensaje);
        btn_publicar = view.findViewById(R.id.btn_publicar);
        pref = new Preferences(context);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);

    }

    Application application;
    public void cargarvista(){
        retroViewModel.getAllPosts().observe(this, new Observer<List<ModelFeed>>() {
            @Override
            public void onChanged(@Nullable List<ModelFeed> modelFeeds) {
                adapter.setWords(modelFeeds);
                if(progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                cdv_mensaje.setVisibility(View.INVISIBLE);
            }
        });
    }

    AdapterFeed adapter = null;

    private void setAdapter(){
        adapter = new AdapterFeed(getActivity(), new AdapterFeed.OnItemClickListener() {
            @Override
            public void onItemClick(ModelFeed modelFeed1, int position) {




                IdPueblo=modelFeed1.getId();
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else{
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onRefresh() {


         FeedWebServiceRepository feedWebServiceRepository = new FeedWebServiceRepository(application);
         feedWebServiceRepository.providesWebService();
        setAdapter();
        cargarvista();
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        if (v.equals(btn_publicar)) {
            //Intent i  = new Intent(context, Publicar.class);
            //startActivity(i);
            dialogpublicar();
        }if (v.equals(LayoutEliminar)){

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

    private void dialogpublicar() {


        final Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED);
        dialog.setContentView(R.layout.dialog_add_post);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        dialog_publicar_close = dialog.findViewById(R.id.dialog_publicar_close);
        imagen_publicacion = dialog.findViewById(R.id.imagen_publicacion);
        nombre_publicacion = dialog.findViewById(R.id.nombre_publicacion);
        et_post = dialog.findViewById(R.id.et_post);
        fotocaptada = dialog.findViewById(R.id.fotocaptada);
        bt_photo = dialog.findViewById(R.id.bt_photo);


        spn_areas = dialog.findViewById(R.id.spn_areas);
        nombre_publicacion.setText(pref.getNombrePref());
        dc2 = new DataConnection(activity, "listarAreas", false);
        new GetAreas().execute();


        bt_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        dialog_publicar_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        bt_publicar = (AppCompatButton) dialog.findViewById(R.id.bt_publicar);
        et_post.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bt_publicar.setEnabled(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bt_publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!(spn_areas.getSelectedItem().toString().equals("Seleccione"))) {
                    if (fotocaptada.getDrawable() == null) {
                        valorFoto = false;
                        publicar();
                    } else {
                        valorFoto = true;
                        uploadMultipart();
                    }
                    //uploadMultipart();
                    //publicar(valorFoto);
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Por favor, seleccione el área", Toast.LENGTH_LONG).show();
                }

            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public class GetAreas extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            arrayarea = new ArrayList<String>();
            arrayArea = dc2.getListaAreas();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //Toast.makeText(context,"size"+arrayArea.size(),Toast.LENGTH_LONG).show();

            for (Areas obj : arrayArea) {
                arrayarea.add(obj.getArea_nombre());
            }
            //progressBar.setVisibility(ProgressBar.INVISIBLE);
            arrayarea.add(0, "Seleccione");
            ArrayAdapter<String> adapEquipos = new ArrayAdapter<String>(context, R.layout.spiner_item, arrayarea);
            adapEquipos.setDropDownViewResource(R.layout.spiner_dropdown_item);
            spn_areas.setAdapter(adapEquipos);

        }
    }
    public void uploadMultipart() {
        //getting name for the image



        String path = resultUriRecortada.getPath();

        //getting the actual path of the image


        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            PendingIntent clickIntent = PendingIntent.getActivity(
                    context, 1, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            //Creating a multi part request
            new MultipartUploadRequest(context, uploadId, url)
                    .addFileToUpload(path, "imagen") //Adding file
                    .addParameter("usuario_id", pref.getIdUsuarioPref()) //Adding text parameter to the request
                    .addParameter("distrito_id", pref.getDistritoIdPref()) //Adding text parameter to the request
                    .addParameter("destino", arrayArea.get(spn_areas.getSelectedItemPosition() - 1).getArea_nombre()) //Adding text parameter to the request
                    .addParameter("queja", et_post.getText().toString()) //Adding text parameter to the request
                    .setNotificationConfig(getNotificationConfig(uploadId,R.string.cargando))
                    .setMaxRetries(2)

                    .startUpload(); //Starting the upload
                            /*getNotificationConfig().setTitleForAllStatuses("Cargando Imagen")
                            .setRingToneEnabled(false)
                            .setClickIntentForAllStatuses(clickIntent)
                            .setClearOnActionForAllStatuses(true))*/



        } catch (Exception exc) {
            Toast.makeText(context, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected UploadNotificationConfig getNotificationConfig(final String uploadId, @StringRes int title) {
        UploadNotificationConfig config = new UploadNotificationConfig();




        PendingIntent clickIntent = PendingIntent.getActivity(
                context, 1, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);


        config.setTitleForAllStatuses(getString(title))
                .setRingToneEnabled(false)
                .setClickIntentForAllStatuses(clickIntent);



        config.getProgress().message = "Subiendo " + UPLOADED_FILES + " de " + TOTAL_FILES
                + " a " + UPLOAD_RATE + " - " + PROGRESS;
        config.getProgress().iconResourceID = R.drawable.posible;
        config.getProgress().iconColorResourceID = Color.BLUE;
        config.getProgress().actions.add(new UploadNotificationAction(R.drawable.posible,"Ver progreso",clickIntent));



        config.getCompleted().message = "Subida completada exitosamente en " + ELAPSED_TIME;
        config.getCompleted().iconResourceID = R.drawable.posible;
        config.getCompleted().iconColorResourceID = Color.GREEN;
        config.getCompleted().actions.add(new UploadNotificationAction(R.drawable.posible,"Imagen Cargada Exitosamente",clickIntent));

        config.getError().message = "Error al Cargar Imagen";
        config.getError().iconResourceID = R.drawable.posible;
        config.getError().iconColorResourceID = Color.RED;

        config.getCancelled().message = "\n" +
                "La carga ha sido cancelada";
        config.getCancelled().iconResourceID = R.drawable.posible;
        config.getCancelled().iconColorResourceID = Color.YELLOW;

        return config;
    }
    public void publicar() {

        RequestParams params1 = new RequestParams();


        params1.put("usuario_id", pref.getIdUsuarioPref());
        params1.put("distrito_id", pref.getDistritoIdPref());
        params1.put("destino", arrayArea.get(spn_areas.getSelectedItemPosition() - 1).getArea_nombre());
        params1.put("queja", et_post.getText().toString());


        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(70000);
        client.post(url, params1, new AsyncHttpResponseHandler() {

            String respuesta = null;
            ProgressDialog loading;

            @Override
            public void onStart() {
                super.onStart();
                loading = new ProgressDialog(context);
                loading.setTitle("SIPSI");
                loading.setMessage("Por favor espere...");
                loading.setIndeterminate(false);
                loading.setCancelable(false);
                //loading.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {

                    try {
                        respuesta = new String(responseBody, "UTF-8");

                        if (respuesta.equals("1")) {
                            // FragmentEquiposHijo.ActualizarEquipo();
                            //mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            //onRefresh();
                            Toast.makeText(context, "Guardado correctamente", Toast.LENGTH_SHORT).show();
                            //adaptadorListadoQuejas.notifyDataSetChanged();



                        } else {
                            Toast.makeText(context, "Vuelva a intentarlo", Toast.LENGTH_SHORT).show();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context, "Error al registrar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                //super.onFinish();
                //mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);

                loading.dismiss();
                // ActualizarQueja()

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);

        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {

                CropImage.activity(output).start(getContext(), this);
                //  CropImage.activity(output).setMaxCropResultSize(1080,566 ).setMinCropResultSize(800,400).start(this);
            } else if (requestCode == SELET_GALERRY) {

                Uri uri = result.getData();


                File f1, f2;
                f1 = new File(getRealPathFromUri(context, uri));
                String fname = f1.getName();


                f2 = new File(Environment.getExternalStorageDirectory() + "/Sipsi/", "Queja");
                f2.mkdirs();
                try {
                    FileUtils.copyFileToDirectory(f1, f2);
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
                    values.put(MediaStore.MediaColumns.DATA, f2.toString() + "/" + fname);
                    context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    //Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_LONG).show();
                }

                CropImage.activity(uri).start(context, this);

            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult resultado = CropImage.getActivityResult(result);
            if (resultCode == RESULT_OK) {

                resultUriRecortada = resultado.getUri();

                fotocaptada.setImageBitmap(BitmapFactory.decodeFile(resultUriRecortada.getPath()));

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = resultado.getError();
                //Toast.makeText(getApplicationContext(),"Error"+error, Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, "Error: Intente de nuevo", Toast.LENGTH_SHORT).show();

            }
        }

    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Camara", "Galería", "Cancelar"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Seleccione :");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Camara")) {
                    userChoosenTask = "Camara";

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File carpetas = new File(Environment.getExternalStorageDirectory() + "/Sipsi/", "Queja");
                    carpetas.mkdirs();

                    String aleatorio = pref.getIdUsuarioPref() + "_" + et_post.getText().toString();
                    String nombre = aleatorio + ".jpg";
                    File imagen = new File(carpetas, nombre);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                        String authorities = context.getPackageName() + ".provider";
                        Uri imageUri = FileProvider.getUriForFile(context, authorities, imagen);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    } else {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));

                    }
                    output = Uri.fromFile(imagen);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[item].equals("Galería")) {
                    userChoosenTask = "Galería";


                    Intent intentgaleria = new Intent(Intent.ACTION_PICK);
                    intentgaleria.setType("image/*");
                    if (intentgaleria.resolveActivity(getActivity().getPackageManager()) != null) {


                        startActivityForResult(intentgaleria, SELET_GALERRY);
                    }
                } else if (items[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
