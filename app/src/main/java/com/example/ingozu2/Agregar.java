package com.example.ingozu2;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.bumptech.glide.Glide;
import com.example.ingozu2.ui.Cajas.Cajas;
import com.example.ingozu2.ui.Herramientas.Herramientas;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;


public class Agregar extends AppCompatActivity{

    private FirebaseFirestore  db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private static final int PHOTO_HERRAMIENTA = 1;
    private static final int PHOTO_CAJA = 2;
    private FirebaseAuth mAuth;
    static final int REQUEST_IMAGE_CAPTURE = 3;
    private Uri herramienta;
    private String urlfoto;
    private Uri caja;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final ProgressDialog dialog = new ProgressDialog(Agregar.this);;
        final Cajas[] cajaRapida = new Cajas[1];
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
                                            nombre.getText().toString(),Integer.parseInt(cantidad.getText().toString()),descripcion.getText().toString(),"testing",urlfoto)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getBaseContext(),"¡Herramienta Agregada!",Toast.LENGTH_LONG);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getBaseContext(),"Error al subir",Toast.LENGTH_LONG);
                                        }
                                    });
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
                        //agregando caja rapido
                        cajaRapida[0] =new Cajas(Integer.parseInt(nivel.getText().toString()),
                                nombre1.getText().toString(),
                                urlfoto,
                                descripcion1.getText().toString());

                        //agregando imagen
                        storageReference = storage.getReference("cajas");
                        if(caja==null){
                            Resources resources = getApplicationContext().getResources();
                            caja=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                                    resources.getResourcePackageName(R.drawable.not_available) + '/'
                                    + resources.getResourceTypeName(R.drawable.not_available) + '/'
                                    + resources.getResourceEntryName(R.drawable.not_available) );
                        }

                        final StorageReference ref = storageReference.child(caja.getLastPathSegment());
                        UploadTask uploadTask = ref.putFile(caja);
                        Task<Uri> urlTask = uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                dialog.show();
                                dialog.setContentView(R.layout.progress_view);
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            }
                        }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                                    //agregando caja
                                    db.collection("cajas").document().set(
                                            new Cajas(Integer.parseInt(nivel.getText().toString()),
                                                    nombre1.getText().toString(),
                                                    urlfoto,
                                                    descripcion1.getText().toString()))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Intent i = new Intent(getApplicationContext(),ListaPrincipal.class);
                                                    i.putExtra("exito",1);
                                                    dialog.dismiss();
                                                    startActivity(i);
                                                    finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Intent i = new Intent(getApplicationContext(),ListaPrincipal.class);
                                            i.putExtra("exito",0);
                                            dialog.dismiss();
                                            startActivity(i);
                                            finish();
                                        }
                                    });
                                }else{
                                    Intent i = new Intent(getApplicationContext(),ListaPrincipal.class);
                                    i.putExtra("exito",0);
                                    dialog.dismiss();
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });
                    }
                });

                //tomar foto
                fotoc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("foto","probando tomar foto");
                        tomarFoto(view);
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
            ImageView fotoHerramienta = findViewById(R.id.fotoHerramienta);
            Uri u = data.getData();
            Glide.with(getApplicationContext()).load(u).into(fotoHerramienta);
            fotoHerramienta.setVisibility(View.VISIBLE);
            herramienta = u;

        } else if (requestCode == PHOTO_CAJA && resultCode == Activity.RESULT_OK) {
            ImageView fotocaja = findViewById(R.id.fotoCaja);
            Uri u = data.getData();
            Log.d("TAG",""+u.toString());
            Glide.with(getApplicationContext()).load(u).into(fotocaja);
            fotocaja.setVisibility(View.VISIBLE);
            caja = u;
        }
        else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            ImageView fotocaja = findViewById(R.id.fotoCaja);
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            fotocaja.setImageBitmap(photo);
            caja=data.getData();

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            caja = getImageUri(getApplicationContext(), photo);
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

    private void tomarFoto(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000,true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }
}