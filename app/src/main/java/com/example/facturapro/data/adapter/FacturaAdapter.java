package com.example.facturapro.data.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facturapro.R;
import com.example.facturapro.data.model.Factura;

import java.util.List;

public class FacturaAdapter extends RecyclerView.Adapter<FacturaAdapter.FacturaViewHolder> {

    private List<Factura> facturaList;

    public FacturaAdapter(List<Factura> facturaList) {
        this.facturaList = facturaList;
    }

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Factura factura);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FacturaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_facturas, parent, false);
        return new FacturaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacturaViewHolder holder, int position) {
        Factura factura = facturaList.get(position);
        holder.numeroFactura.setText(factura.getNumeroFactura());  // 'numero' es un campo de la clase Factura
        holder.categoria.setText(factura.getCategoria());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(factura);
            }
        });
    }

    @Override
    public int getItemCount() {
        return facturaList.size();
    }

    public class FacturaViewHolder extends RecyclerView.ViewHolder {

        public TextView numeroFactura;
        public TextView categoria;

        public FacturaViewHolder(@NonNull View itemView) {
            super(itemView);
            numeroFactura = itemView.findViewById(R.id.tvListaNumeroFactura);
            categoria = itemView.findViewById(R.id.tvListaCategoria);

        }
    }
}
