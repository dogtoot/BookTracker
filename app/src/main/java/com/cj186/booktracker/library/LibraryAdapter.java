package com.cj186.booktracker.library;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cj186.booktracker.model.Book;
import com.cj186.booktracker.bookdescription.BookDescriptionActivity;
import com.cj186.booktracker.R;
import com.cj186.booktracker.model.Filter;
import com.cj186.booktracker.model.Status;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Collin J. Johnson
 * 5/6/2025
 * 2376 Mobile Applications Development
 *
 * This adapter dictates what books are shown in the library.
 */

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

    public void setFilter(Filter filter, boolean favorites) {
        Stream<Book> stream = bookList.stream();

        switch (filter){
            case PLANNING_TO_READ:
                stream = stream.filter(b -> b.getStatus().equals(Status.PLANNING_TO_READ));
                break;
            case CURRENTLY_READING:
                stream = stream.filter(b -> b.getStatus().equals(Status.CURRENTLY_READING));
                break;
            case COMPLETED:
                stream = stream.filter(b -> b.getStatus().equals(Status.COMPLETED));
                break;
            case REREADING:
                stream = stream.filter(b -> b.getStatus().equals(Status.REREADING));
                break;
            case ALL:
                break;
        }

        if(favorites)
            stream = stream.filter(Book::isFavorite);

        ArrayList<Book> newList = stream.collect(Collectors.toCollection(ArrayList::new));
        DiffUtil.DiffResult diff = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return displayedList.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return displayedList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return displayedList.get(oldItemPosition).equals(newList.get(newItemPosition));
            }
        });

        displayedList = newList;
        diff.dispatchUpdatesTo(this);
    }

    public void updateBooks(ArrayList<Book> newBooks){
        // Update the list.
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
