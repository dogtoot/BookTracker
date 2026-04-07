package com.cj186.booktracker.library;

import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cj186.booktracker.model.Book;
import com.cj186.booktracker.R;

/**
 * Collin J. Johnson
 * 5/6/2025
 * 2376 Mobile Applications Development
 *
 * This view holder is the covers seen on the library screen.
 */

public class BookViewHolder extends RecyclerView.ViewHolder{
    // Cover and status for the library.
    private final ImageView cover;
    private final ImageView favorite;
    private final TextView status;

    public BookViewHolder(@NonNull View itemView) {
        // Call the super constructor and set the views.
        super(itemView);
        cover = itemView.findViewById(R.id.book_cover_img);
        favorite = itemView.findViewById(R.id.favorite);
        status = itemView.findViewById(R.id.book_status);
    }

    public void bind(Book book){
        // Get the imagebytes and assign it converted to a bitmap to cover.
        byte[] imageBytes = book.getImageBytes();
        cover.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
        favorite.setVisibility(book.isFavorite() ? View.VISIBLE : View.INVISIBLE);
        // Set the status text.
        status.setText(book.getStatus().getLabel());
    }
}
