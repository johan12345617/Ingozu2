package com.example.ingozu2.ui.Herramientas;

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

import java.util.List;

public class HerramientasAdaptador extends RecyclerView.Adapter<HerramientasAdaptador.HerramientaViewHolder> {
    private Context c;
    private List<Herramientas> lista;

    public HerramientasAdaptador(List<Herramientas> lista,Context c){
            this.lista=lista;
            this.c=c;
    }

    @Override
    public HerramientaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_objeto,parent,false);
        return new HerramientaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HerramientaViewHolder holder, int position) {
            holder.getTv_name().setText(lista.get(position).getNombre());
            Glide.with(c).load(lista.get(position).getUrlFoto()).into(holder.getIv_image_thumbnail());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class HerramientaViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_name;
        private ImageView iv_image_thumbnail;

        public HerramientaViewHolder(@NonNull View itemView) {
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