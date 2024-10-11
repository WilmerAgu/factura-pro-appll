package com.example.facturapro.data.dao;

import android.util.Log;

import com.example.facturapro.data.model.Factura;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacturaDao {
    private static final String TAG = "FacturaDao";
    private static final String COLLECTION_NAME = "facturas";

    private final FirebaseFirestore db;


    public FacturaDao(FirebaseFirestore db)
    {
        this.db = db;
    }


    public void insert(Factura factura, OnSuccessListener<String> listener) {
        Map<String, Object> facturaData = new HashMap<>();
        facturaData.put("numeroFactura", factura.getNumeroFactura());
        facturaData.put("monto", factura.getMonto());
        facturaData.put("categoria", factura.getCategoria());
        facturaData.put("vendedor", factura.getVendedor());
        facturaData.put("ciudad", factura.getCiudad());
        facturaData.put("fecha", factura.getFecha());

        db.collection(COLLECTION_NAME)
                .add(facturaData)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "onSuccess: " + documentReference.getId());
                    listener.onSuccess(documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "onFailure: ", e);
                    listener.onSuccess(null);
                });
    }


    public void update(String id, Factura factura, OnSuccessListener<Boolean> listener) {
        Map<String, Object> facturaData = new HashMap<>();
        facturaData.put("numeroFactura", factura.getNumeroFactura());
        facturaData.put("monto", factura.getMonto());
        facturaData.put("categoria", factura.getCategoria());
        facturaData.put("vendedor", factura.getVendedor());
        facturaData.put("ciudad", factura.getCiudad());
        facturaData.put("fecha", factura.getFecha());

        db.collection(COLLECTION_NAME)
                .document(id)
                .update(facturaData)
                .addOnSuccessListener(unused -> listener.onSuccess(true))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "onFailure: ", e);
                    listener.onSuccess(false);
                });
    }
    public void getById(String id, OnSuccessListener<Factura> listener) {
        db.collection(COLLECTION_NAME)
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Factura factura = document.toObject(Factura.class);
                            listener.onSuccess(factura);
                        } else {
                            listener.onSuccess(null);
                        }
                    } else {
                        Log.e(TAG, "onComplete: ", task.getException());
                        listener.onSuccess(null);
                    }
                });
    }
    public void getAllFacturas(OnSuccessListener<List<Factura>> listener) {
        db.collection(COLLECTION_NAME)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Factura> facturaList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Factura factura = documentSnapshot.toObject(Factura.class);
                        facturaList.add(factura);
                    }
                    Log.d(TAG, "Facturas cargadas: " + facturaList.size());
                    listener.onSuccess(facturaList);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al cargar facturas", e);
                    listener.onSuccess(null);
                });
    }

    public void delete(String id, OnSuccessListener<Boolean> listener) {
        db.collection(COLLECTION_NAME)
                .document(id)
                .delete()
                .addOnSuccessListener(unused -> listener.onSuccess(true))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "onFailure: ", e);
                    listener.onSuccess(false);
                });
    }

}
