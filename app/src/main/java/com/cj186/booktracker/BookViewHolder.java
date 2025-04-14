package com.cj186.booktracker;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookViewHolder extends RecyclerView.ViewHolder{
    ImageView cover;
    TextView status;

    public BookViewHolder(@NonNull View itemView) {
        super(itemView);
        cover = itemView.findViewById(R.id.book_cover_img);
        status = itemView.findViewById(R.id.book_status);
    }

    public void bind(Book book){
        cover.setImageBitmap(book.getImage());
        status.setText(book.getStatus().getLabel());
    }
}
