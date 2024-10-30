package com.example.facturapro.data.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facturapro.R;
import com.example.facturapro.data.model.FacturaModel;

import java.util.List;

public class FacturaAdapter extends RecyclerView.Adapter<FacturaAdapter.ViewHolder> {

    private List<FacturaModel> factura;

    public FacturaAdapter(List<FacturaModel> factura) {
        this.factura = factura;
    }

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(FacturaModel factura);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FacturaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_facturas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacturaAdapter.ViewHolder holder, int position) {
        FacturaModel factura = this.factura.get(position);

        holder.numeroFactura.setText(factura.getNumeroFactura());
        holder.categoria.setText(factura.getCategoria());
        holder.fecha.setText(factura.getFecha());



        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(factura);
            }
        });
    }

    @Override
    public int getItemCount() {
        return factura.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView numeroFactura;
        public TextView categoria;
        public TextView fecha;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numeroFactura = itemView.findViewById(R.id.tvListaNumeroFactura);
            categoria = itemView.findViewById(R.id.tvListaCategoria);
            fecha = itemView.findViewById(R.id.tvListaFecha);

        }
    }
}
