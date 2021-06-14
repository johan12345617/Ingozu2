package com.example.ingozu2.ui.Cajas;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.ingozu2.R;

import java.util.List;

public class CajasAdapter extends RecyclerView.Adapter<CajasAdapter.CajasViewHolder> {
    private Context c;
    private List<Cajas> lista;
    final private OnCajaListener selectionListener;

    public CajasAdapter(List<Cajas> lista,Context c,OnCajaListener selectionListener){
        this.c=c;
        this.lista=lista;
        this.selectionListener=selectionListener;
    }

    public void addCaja(Cajas e){
        lista.add(e);
        notifyItemInserted(lista.size());
    }

    public Cajas eliminarCaja(int position){
        Log.d("eliminando adapter",""+lista.get(position).getNombre());
        Cajas eliminado = lista.remove(position);
        notifyItemRemoved(position);
        return eliminado;
    }

    @Override
    public CajasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_objeto,parent,false);
        return new CajasAdapter.CajasViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CajasViewHolder holder, int position) {
        holder.getTv_name().setText(lista.get(position).getNombre());
        Glide.with(c).load(lista.get(position).getUrlFoto()).into(holder.getIv_image_thumbnail());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public interface OnCajaListener {
        void onCajaSelected(int position);
        void onLongCajaSelected(int position,View v);
    }

    public class CajasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private TextView tv_name;
        private ImageView iv_image_thumbnail;

        public CajasViewHolder(@NonNull View itemView) {
            super(itemView);
            setTv_name((TextView) itemView.findViewById(R.id.name_id));
            setIv_image_thumbnail((ImageView) itemView.findViewById(R.id.image_id));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public TextView getTv_name() {
            return tv_name;
        }

        public void setTv_name(TextView tv_name) {
            this.tv_name = tv_name;
        }

        public ImageView getIv_image_thumbnail() {
            return iv_image_thumbnail;
        }

        public void setIv_image_thumbnail(ImageView iv_image_thumbnail) {
            this.iv_image_thumbnail = iv_image_thumbnail;
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            selectionListener.onCajaSelected(position);
        }

        @Override
        public boolean onLongClick(View v){
            int position=getAdapterPosition();
            selectionListener.onLongCajaSelected(position,v);
            return true;
        }
    }

}