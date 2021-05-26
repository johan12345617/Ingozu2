package com.example.ingozu2;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.bumptech.glide.Glide;
import com.example.ingozu2.ui.Cajas.Cajas;
import com.example.ingozu2.ui.Herramientas.Herramientas;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;
import java.util.Arrays;


public class Agregar extends AppCompatActivity {

    private FirebaseFirestore  db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private static final int PHOTO_HERRAMIENTA = 1;
    private static final int PHOTO_CAJA = 2;
    private FirebaseAuth mAuth;

    private Uri herramienta;
    private String urlfoto;
    private Uri caja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        int codigo = (int) bundle.getInt("codigo");

        final FirebaseStorage storage = FirebaseStorage.getInstance();

        switch(codigo){
            case 1:
                //agregar herramienta

                setContentView(R.layout.agregar_herramienta);
                setTitle(R.string.tittle_agregar_herramienta);
                /*Spinner opciones = findViewById(R.id.spinner);
                final String[] listaopciones ={"1","2","3","4","5","6","7","8","9","10","11",
                        "1","2","3","4","5","6","7","8","9","10","11"};
                ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(listaopciones));
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,arrayList);
                opciones.setAdapter(arrayAdapter);*/

                final EditText nombre = (EditText) findViewById(R.id.editTextHerramienta);
                Button agregar = (Button) findViewById(R.id.buttonAgregarh);
                Button cancelar = (Button) findViewById(R.id.buttonCancelarh);
                final EditText descripcion = (EditText) findViewById(R.id.herramientaDescripcion);
                final EditText cantidad = (EditText) findViewById(R.id.HerramientaCantidad);
                Button escoger = (Button) findViewById(R.id.btEscogerHerramienta);

                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                escoger.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                        i.setType("image/jpeg");
                        i.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                        startActivityForResult(Intent.createChooser(i,"Selecciona una foto"),PHOTO_HERRAMIENTA);
                    }
                });
                agregar.setOnClickListener(new View.OnClickListener() {//agregar heramienta
                    public void onClick(View v) {
                        //agregando la imagen

                        storageReference = storage.getReference("Herramientas");
                        final StorageReference ref = storageReference.child(herramienta.getLastPathSegment());
                        UploadTask uploadTask = ref.putFile(herramienta);

                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return ref.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    urlfoto = downloadUri.toString();
                                    Log.d("link",""+urlfoto);
                                    //agregando la herramienta
                                    db.collection("herramientas").document().set(new Herramientas(
                                            nombre.getText().toString(),Integer.parseInt(cantidad.getText().toString()),descripcion.getText().toString(),"testing",urlfoto));
                                } else {
                                    // Handle failures
                                    // ...
                                    Log.d("internet","no hay conexion a internet");
                                }
                            }
                        });
                        finish();
                    }
                });

                break;

            case 2:
                //agregar caja
                setContentView(R.layout.agregar_caja);
                setTitle(R.string.tittle_agregar_caja);

                final ArrayList<String> listaopciones1 = new ArrayList<>();
                final ArrayList<String> claves = new ArrayList<>();
                final ArrayList<String> claves1 = new ArrayList<>();

                final EditText nombre1 = (EditText) findViewById(R.id.nombreCaja);
                final EditText descripcion1 = (EditText) findViewById(R.id.CajaDescripcion);
                final EditText nivel = (EditText) findViewById(R.id.nivelCaja);

                final int[] i = {0};

                db.collection("estantes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                /*Map<String, Object> map = new HashMap<>();
                                map = document.getData();
                                for (Map.Entry<String, Object> entry : map.entrySet()) {
                                    listaopciones1.add(entry.getKey());
                                    //entry.getValue();
                                    i[0]++;
                                }*/
                                claves.add(document.getId());
                                Log.d("consulta", claves.get(i[0]));
                                i[0]++;
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

                Log.d("test1 ","1");

                for (int j=0;j<claves1.size();j++){
                    claves1.add(claves.get(j));
                    Log.d("claves ",claves.get(j));
                    Log.d("test1-2 ","2");
                }

                Log.d("test2 ","2");

                Button agregar1 = (Button) findViewById(R.id.buttonAgregarc);
                Button cancelar1 = (Button) findViewById(R.id.buttonCancelarc);
                Button fotoc = (Button) findViewById(R.id.buttonFotoCaja);
                Button escoger1 = (Button) findViewById(R.id.btEscogerFotoaCaja);

                //cancelar
                cancelar1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

                //agregar
                agregar1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        storageReference = storage.getReference("cajas");
                        final StorageReference ref = storageReference.child(caja.getLastPathSegment());
                        UploadTask uploadTask = ref.putFile(caja);

                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return ref.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    urlfoto = downloadUri.toString();
                                    //agregando la herramienta

                                    db.collection("cajas").document().set(new Cajas(Integer.parseInt(nivel.getText().toString()),nombre1.getText().toString(),urlfoto,descripcion1.getText().toString()));
                                } else {
                                    // Handle failures
                                    // ...
                                    Log.d("internet","no hay conexion a internet");
                                }
                            }
                        });
                        finish();
                    }
                });

                //tomar foto
                fotoc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                //escoger foto
                escoger1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                        i.setType("image/jpeg");
                        i.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                        startActivityForResult(Intent.createChooser(i,"Selecciona una foto"),PHOTO_CAJA);
                    }
                });

                break;

        }

    }


    //seleccionar foto de galeria
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_HERRAMIENTA && resultCode == Activity.RESULT_OK) {
            ImageView fotoHerramienta = (ImageView) findViewById(R.id.fotoHerramienta);
            Uri u = data.getData();
            Glide.with(getApplicationContext()).load(u).into(fotoHerramienta);
            fotoHerramienta.setVisibility(View.VISIBLE);
            herramienta = u;

        } else if (requestCode == PHOTO_CAJA && resultCode == Activity.RESULT_OK) {
            ImageView fotocaja = (ImageView) findViewById(R.id.fotoCaja);
            Uri u = data.getData();
            Log.d("TAG",""+u.toString());
            Glide.with(getApplicationContext()).load(u).into(fotocaja);
            fotocaja.setVisibility(View.VISIBLE);
            caja = u;

        }
    }

    public static boolean verifyStoragePermissions(Activity activity) {
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int REQUEST_EXTERNAL_STORAGE = 1;
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return false;
        }else{
            return true;
        }
    }

}