package com.blogspot.programer27android.playsound;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterPlay extends RecyclerView.Adapter<AdapterPlay.PlayHolder> {
    ArrayList<PlayInfo> arrayPlayinfo;
    Context c;

    public AdapterPlay(Context c,ArrayList<PlayInfo> arrayPlayinfo) {
        this.c=c;
        this.arrayPlayinfo=arrayPlayinfo;
    }
//buat sendri
    OnitemClickListener clickPlay;
    public void setClickItem(OnitemClickListener clickPlay){
        this.clickPlay=clickPlay;
    }
    public interface OnitemClickListener{
        void onItemClick(Button b,View v, PlayInfo obj,int posisi);
    }
    //creates


    @Override
    public PlayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.for_recycle,parent,false);
        return new PlayHolder(v);
    }

    @Override
    public void onBindViewHolder(final PlayHolder holder, final int position) {
        final PlayInfo playinfo=arrayPlayinfo.get(position);
        holder.surat.setText(playinfo.surat);
        holder.qori.setText(playinfo.qori);
        holder.ply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickPlay !=null){
                    clickPlay.onItemClick(holder.ply,v,playinfo,position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayPlayinfo.size();
    }


    public class PlayHolder extends RecyclerView.ViewHolder {
        TextView surat,qori;
        Button ply;
        public PlayHolder(View itemView) {
            super(itemView);
            surat= itemView.findViewById(R.id.surat);
            qori=itemView.findViewById(R.id.qori);
            ply=itemView.findViewById(R.id.playbtn);
        }
    }
}
