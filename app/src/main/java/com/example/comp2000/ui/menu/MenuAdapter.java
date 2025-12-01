package com.example.comp2000.ui.menu;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp2000.R;
import com.example.comp2000.Roles;
import com.example.comp2000.data.model.MenuItem;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    // variables
    private final Context context; // current app state
    private final List<MenuItem> menuItems; // list that will hold menu items

    // constructor for adapter holding current app state and menu list
    public MenuAdapter(Context context, List<MenuItem> menuItems) {
        this.context = context;
        this.menuItems = menuItems;
    }

    // listeners
    // listen to clicks on unique item
    public interface OnEditClickListener { void onEdit(int position); }
    public interface OnDeleteClickListener { void onDelete(int position); }

    // log the click
    private OnEditClickListener editClickListener;
    private OnDeleteClickListener deleteClickListener;

    // pass click to adapter
    public void setOnEditClickListener(OnEditClickListener listener) { this.editClickListener = listener; }
    public void setOnDeleteClickListener(OnDeleteClickListener listener) { this.deleteClickListener = listener; }


    // define view holder (everything inside an unique card + its listeners)
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // define elements in menu item card
        ImageView menuImage; // image
        TextView foodName, foodDetails, foodPrice; // text views
        ImageButton btnEdit, btnDelete; // buttons

        public ViewHolder(@NonNull View itemView,
                          OnEditClickListener editListener,
                          OnDeleteClickListener deleteListener) {

            super(itemView);

            // get elements from XML file by IDs
            menuImage = itemView.findViewById(R.id.menu_image);
            foodName = itemView.findViewById(R.id.food_name);
            foodDetails = itemView.findViewById(R.id.food_details);
            foodPrice = itemView.findViewById(R.id.food_price);

            btnEdit = itemView.findViewById(R.id.btn_edit_item);
            btnDelete = itemView.findViewById(R.id.btn_delete_item);

            // handle clicks on edit button
            btnEdit.setOnClickListener(v -> {
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && editListener != null) {
                    editListener.onEdit(pos); // safe delete
                }
            });

            // handle clicks on delete button
            btnDelete.setOnClickListener(v -> {
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && deleteListener != null) {
                    deleteListener.onDelete(pos); // safe delete
                }
            });

            // getBindingAdapter from https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView.ViewHolder#getBindingAdapter()
        }
    }

    // create layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout (turn XML into View), no items yet
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new ViewHolder(view, editClickListener, deleteClickListener);
    }

    // display data while rendering recyclerview
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // display list items on screen at the current position
        MenuItem item = menuItems.get(position);

        // check staff role from Roles.java via AndroidContext
        boolean isStaff = Roles.isStaff(context);

        // view the edit/delete buttons for each item when current user is staff
        holder.btnEdit.setVisibility(isStaff ? View.VISIBLE : View.GONE);
        holder.btnDelete.setVisibility(isStaff ? View.VISIBLE : View.GONE);

        // display corresponding items while scrolling down
        holder.foodName.setText(item.name);
        holder.foodDetails.setText(item.details);
        holder.foodPrice.setText(item.price);

        // display image via url or if no url available, via fallback icon
        if (item.imageUri != null) {
            holder.menuImage.setImageURI(Uri.parse(item.imageUri));
        } else {
            holder.menuImage.setImageResource(R.drawable.ic_menu);
        }
    }

    // helpers
    @Override
    public int getItemCount() {
        // get size of list items to render list
        return menuItems.size();
    }
}
