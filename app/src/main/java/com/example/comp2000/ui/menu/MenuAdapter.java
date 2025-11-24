package com.example.comp2000.ui.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp2000.R;
import com.example.comp2000.data.model.MenuItem;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private Context context; // current app state
    private List<MenuItem> menuItems; // list that will hold menu items

    // isStaff bool for admin features

    // on item click listener (what happens when user clicks on specific item in the list)
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener listener;
    // add admin buttons onEditClick, onDeleteClick

    // setter for click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // constructor for adapter holding current app state and menu list
    public MenuAdapter(Context context, List<MenuItem> menuItems) {
        this.context = context;
        this.menuItems = menuItems;
    }

    // ViewHolder RecyclerView
    static class ViewHolder extends RecyclerView.ViewHolder {

        // define elements in menu item
        ImageView menuImage;
        TextView foodName;
        TextView foodPrice;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            // get elements from XML file by IDs
            menuImage = itemView.findViewById(R.id.menu_image);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);

            // check if isStaff is true
            // find edit button
            // find delete button

            // handle clicks on items
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout (turn XML into View), no items yet
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // display list items on screen at the current position
        MenuItem item = menuItems.get(position);

        // display admin buttons if isStaff true
        // listen for edit/delete button clicks

        // display corresponding items while scrolling down
        holder.foodName.setText(item.name);
        holder.foodPrice.setText(item.price);
        holder.menuImage.setImageResource(item.image);
    }

    @Override
    public int getItemCount() {
        // get size of list items to render list
        return menuItems.size();
    }
}
