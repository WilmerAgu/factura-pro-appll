package com.example.facturapro.data.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facturapro.R;
import com.example.facturapro.data.model.EgresosModel;
import com.example.facturapro.data.model.FacturaModel;

import java.util.List;

public class EgresosAdapter  extends RecyclerView.Adapter<EgresosAdapter.ViewHolder>{


    private List<EgresosModel> egreso;

    public EgresosAdapter(List<EgresosModel> egreso) {
        this.egreso = egreso;
    }

    private EgresosAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(EgresosModel egreso);
    }

    public void setOnItemClickListener(EgresosAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public EgresosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_egresos, parent, false);
        return new EgresosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EgresosAdapter.ViewHolder holder, int position) {
        EgresosModel egreso = this.egreso.get(position);

        holder.numeroFacturaEgreso.setText(egreso.getNumeroFacturaEgreso());
        holder.categoriaEgreso.setText(egreso.getCategoriaEgreso());
        holder.fechaEgreso.setText(egreso.getFechaEgreso());



        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(egreso);
            }
        });
    }

    @Override
    public int getItemCount() {
        return egreso.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView numeroFacturaEgreso;
        public TextView categoriaEgreso;
        public TextView fechaEgreso;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numeroFacturaEgreso = itemView.findViewById(R.id.tvListaNumeroFacturaEgreso);
            categoriaEgreso = itemView.findViewById(R.id.tvListaCategoriaEgreso);
            fechaEgreso = itemView.findViewById(R.id.tvListaFechaEgreso);

        }
    }

}
