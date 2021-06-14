package com.example.ingozu2;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.ingozu2.ui.Cajas.Cajas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ingozu2.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Detalles extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Cajas caja;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        Toolbar toolbar = findViewById(R.id.toolbar);

        //seteando datos en los layout
        final TextView box = findViewById(R.id.nombreCaja);
        final ImageView fotoCaja = findViewById(R.id.detalleCaja);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());
        String key = "";
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            key = extras.getString("codigo");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DocumentReference docRef = db.collection("cajas").document(key);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        caja= document.toObject(Cajas.class);
                        box.setText(caja.getNombre());
                        Glide.with(getApplicationContext()).load(caja.getUrlFoto()).into(fotoCaja);

                    } else {
                        Toast.makeText(getApplicationContext(),"fALLA AL CARGAR",Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(),"fALLA AL CARGAR",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //box.setText(caja.getNombre());
    }

    public void imagenCompleta(View v){
        Intent i = new Intent(this,ImagenCompleta.class);
        i.putExtra("imagenPath",""+caja.getUrlFoto());
        startActivity(i);
    }
}