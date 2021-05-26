package com.example.ingozu2.ui.Cajas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ingozu2.R;


import java.util.HashMap;
import java.util.List;

public class CajasAdapter extends RecyclerView.Adapter<CajasAdapter.CajasViewHolder> {
    private Context c;
    private List<Cajas> lista;

    public CajasAdapter(List<Cajas> lista,Context c){
        this.c=c;
        this.lista=lista;
    }

    public void addCaja(Cajas e){
        lista.add(e);
        notifyItemInserted(lista.size());
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

    public interface OnObjectListener {
        void onObjectClick(int position);
    }

    public static class CajasViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_name;
        private ImageView iv_image_thumbnail;

        public CajasViewHolder(@NonNull View itemView) {
            super(itemView);
            setTv_name((TextView) itemView.findViewById(R.id.name_id));
            setIv_image_thumbnail((ImageView) itemView.findViewById(R.id.image_id));
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
    }

}