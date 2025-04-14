package com.cj186.booktracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class LibraryAdapter extends RecyclerView.Adapter<BookViewHolder>{
    private final ArrayList<Book> bookList;
    private final Context ctx;

    public LibraryAdapter(Context ctx, ArrayList<Book> bookList, boolean useFavorites){
        if(useFavorites)
            this.bookList = bookList.stream()
                    .filter(Book::isFavorite)
                    .collect(Collectors.toCollection(ArrayList::new));
        else
            this.bookList = bookList;

        this.ctx = ctx;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_cover, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, BookDescriptionActivity.class);
                intent.putExtra("book_obj", book);
                ctx.startActivity(intent);
            }
        });
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return bookList == null ? 0 : bookList.size();
    }
}
