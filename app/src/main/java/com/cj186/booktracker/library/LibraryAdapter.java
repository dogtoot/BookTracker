package com.cj186.booktracker.library;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cj186.booktracker.model.Book;
import com.cj186.booktracker.bookdescription.BookDescriptionActivity;
import com.cj186.booktracker.R;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class LibraryAdapter extends RecyclerView.Adapter<BookViewHolder>{
    private final ArrayList<Book> bookList;
    private ArrayList<Book> displayedList;
    private final Context ctx;

    public LibraryAdapter(Context ctx, ArrayList<Book> bookList){
        // Filter bookList depending on if we're using only favorites.
        this.bookList = new ArrayList<>(bookList);
        this.displayedList = new ArrayList<>(bookList);

        // Set our context.
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Return our book view holder after inflating it.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_cover, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        // Get the book and add an onclick listener.
        Book book = displayedList.get(position);
        holder.itemView.setOnClickListener(v -> {
            // Onclick pass the Book object to BookDescriptionActivity through intent.
            Intent intent = new Intent(ctx, BookDescriptionActivity.class);
            intent.putExtra("book_obj", book.getId());
            ctx.startActivity(intent);
        });
        // Bind the book.
        holder.bind(book);
    }

    public void setFilter(boolean useFavorites) {
        if (useFavorites) {
            displayedList = bookList.stream()
                    .filter(Book::isFavorite)
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {
            displayedList = new ArrayList<>(bookList);
        }

        notifyDataSetChanged();
    }

    public void updateBooks(ArrayList<Book> newBooks){
        bookList.clear();
        bookList.addAll(newBooks);
        displayedList = new ArrayList<>(bookList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return displayedList.size();
    }
}
