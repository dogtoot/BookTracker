package com.cj186.booktracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LibraryAdapter extends RecyclerView.Adapter<BookViewHolder> {
    ArrayList<Book> bookList;
    RecyclerView recyclerView;

    public LibraryAdapter(ArrayList<Book> bookList, RecyclerView recyclerView){
        this.bookList = bookList;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_cover, parent, false);

        int width = recyclerView.getWidth();
        int height = view.getHeight();

        int itemHeight = (int) (width * 0.3);

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = itemHeight;
        view.setLayoutParams(layoutParams);

        /*parent.post(() -> {
            int parentHeight = parent.getHeight();

            int itemHeight = (int) (width * 0.25);

            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = itemHeight;
            view.setLayoutParams(layoutParams);
        });*/

        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return bookList == null ? 0 : bookList.size();
    }
}
