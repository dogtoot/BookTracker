package com.cj186.booktracker.library;

import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cj186.booktracker.model.Book;
import com.cj186.booktracker.R;

public class BookViewHolder extends RecyclerView.ViewHolder{
    // Cover and status for the library.
    private ImageView cover;
    private TextView status;

    public BookViewHolder(@NonNull View itemView) {
        // Call the super constructor and set the views.
        super(itemView);
        cover = itemView.findViewById(R.id.book_cover_img);
        status = itemView.findViewById(R.id.book_status);
    }

    public void bind(Book book){
        // Get the imagebytes and assign it converted to a bitmap to cover.
        byte[] imageBytes = book.getImageBytes();
        cover.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));

        // Set the status text.
        status.setText(book.getStatus().getLabel());
    }
}
