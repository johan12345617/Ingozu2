package com.example.ingozu2.ui.Cajas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ingozu2.Detalles;
import com.example.ingozu2.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CajasFragment extends Fragment implements CajasAdapter.OnCajaListener ,PopupMenu.OnMenuItemClickListener{

    private CajasViewModel galleryViewModel;
    private List<String> claves = new ArrayList<>();
    private List<Cajas> lista = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int position;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        galleryViewModel =
                ViewModelProviders.of(this).get(CajasViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cajas, container, false);
        RecyclerView rv = root.findViewById(R.id.recyclerview_cajas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),

                DividerItemDecoration.VERTICAL);

        rv.addItemDecoration(dividerItemDecoration);
        final CajasAdapter adapter = new CajasAdapter(lista,getContext(),this);
        rv.setAdapter(adapter);
        //cargando informacion
        /*db.collection("cajas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        adapter.addCaja(document.toObject(Cajas.class));
                        claves.add(document.getId());
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Error");
                    builder.setMessage("Hubo un error al cargar los datos");
                    builder.setPositiveButton("Aceptar", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    getActivity().finish();
                }
            }
        });*/

        //escuchador de cambios
        db.collection("cajas")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("", "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    adapter.addCaja(dc.getDocument().toObject(Cajas.class));
                                    claves.add(dc.getDocument().getId());
                                    break;
                                case MODIFIED:
                                    Log.d("", "Modified city: " + dc.getDocument().getData());

                                    break;
                                case REMOVED:
                                    claves.remove(dc.getDocument().getId());
                                    Log.d("codigo a eliminar",""+position);
                                    adapter.eliminarCaja(position);
                                    break;
                            }
                        }

                    }
                });

        return root;
    }

    @Override
    public void onCajaSelected(int position) {
        Intent i = new Intent(getActivity(), Detalles.class);
        i.putExtra("codigo",claves.get(position));
        startActivity(i);
    }

    @Override
    public void onLongCajaSelected(int position,View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        inflater.inflate(R.menu.menu_caja_opciones, popup.getMenu());
        this.position=position;
        Log.d("codigoaEliminadoCajasFr",""+position);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.eliminarCaja:
                Log.d("Eliminando de Firebase",""+claves.get(position));
                db.collection("cajas").document(claves.get(position)).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Caja Eliminada", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error al Eliminar", Toast.LENGTH_SHORT).show();
                    }
                });

                //eliminar la foto agregada(falta implementar)

                /*final FirebaseStorage storage = FirebaseStorage.getInstance();
                // Create a storage reference from our app
                StorageReference storageRef = storage.getReference();

                // Create a reference to the file to delete
                StorageReference desertRef = storageRef.child("images/desert.jpg");

                // Delete the file
                desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!
                    }
                });*/

                return true;
            case R.id.editarCaja:
                return true;
            default:
                return false;
        }
    }
}