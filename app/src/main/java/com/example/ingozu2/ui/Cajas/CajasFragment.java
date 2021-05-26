package com.example.ingozu2.ui.Cajas;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ingozu2.R;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CajasFragment extends Fragment{

    private CajasViewModel galleryViewModel;
        private List<Cajas> lista = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        galleryViewModel =
                ViewModelProviders.of(this).get(CajasViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cajas, container, false);
        RecyclerView rv = (RecyclerView) root.findViewById(R.id.recyclerview_cajas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        final CajasAdapter adapter = new CajasAdapter(lista,getContext());
        rv.setAdapter(adapter);
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

                                    Cajas caja = dc.getDocument().toObject(Cajas.class);
                                    adapter.addCaja(caja);
                                    break;
                                case MODIFIED:
                                    Log.d("", "Modified city: " + dc.getDocument().getData());

                                    break;
                                case REMOVED:
                                    Log.d("", "Removed city: " + dc.getDocument().getData());

                                    break;
                            }
                        }

                    }
                });



        return root;
    }
}