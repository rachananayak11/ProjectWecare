package com.example.projectwecare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class attendnurseviewadapter extends RecyclerView.Adapter<attendnurseviewadapter.myviewholder> {

    Context context;
    ArrayList<Allnurseview> list;

    public attendnurseviewadapter(Context context, ArrayList<Allnurseview> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.attendview, parent, false);
        return new myviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        Allnurseview a = list.get(position);
        holder.id.setText(a.getNurseID());
        holder.name.setText(a.getFname());
        holder.type.setText(a.getDutyshift());

        byte[] bytes = Base64.decode(a.getencodedImage(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        holder.propic.setImageBitmap(bitmap);

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = a.getContactnum();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class myviewholder extends RecyclerView.ViewHolder {

        TextView name, id, type;
        RoundedImageView propic;
        AppCompatImageButton call;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.attendid);
            name = itemView.findViewById(R.id.attendname);
            type = itemView.findViewById(R.id.attendtype);
            propic = itemView.findViewById(R.id.imageProfileattend);
            call = itemView.findViewById(R.id.call1);
        }

    }
}
