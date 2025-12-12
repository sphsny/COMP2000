package com.example.comp2000.ui.menu;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;

import com.example.comp2000.Roles;
import com.example.comp2000.data.model.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.comp2000.R;
import com.example.comp2000.data.model.RestaurantDB;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {
    // fields
    private RestaurantDB db; // connect DB
    private MenuAdapter adapter; // uses adapter to convert each item XML into view
    private final List<MenuItem> menuList = new ArrayList<>(); // list that holds menu items
    private String selectedImage = "default"; // item image, default ic_menu

    public MenuFragment() {
        // required empty public constructor
    }

    // create UI layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false); // inflate view (xml -> view)

        RecyclerView recyclerView = view.findViewById(R.id.menuRecyclerView); // display list items in recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // set layout for recyclerview

        db = new RestaurantDB(requireContext());
        menuList.clear();
        menuList.addAll(db.getAllMenuItems());

        adapter = new MenuAdapter(requireContext(), menuList); // get menu adapter
        recyclerView.setAdapter(adapter); // connect adapter to recyclerview

        setStaffFeatures(view); // get staff features

        return view; // return the inflated view
    }

    // set up staff features if staff is logged in
    private void setStaffFeatures(View view) {

        // find add item button
        Button addButton = view.findViewById(R.id.add_menu_item);

        // make button visible only for staff
        if (Roles.isStaff(requireContext())) {
            addButton.setVisibility(View.VISIBLE);
        } else {
            addButton.setVisibility(View.GONE);
        }

        // add new item (pop up window)
        addButton.setOnClickListener(v -> showMenuItemDialog(null));

        // edit existing item (pop up window with existing item values)
        adapter.setOnEditClickListener(
                position -> showMenuItemDialog(menuList.get(position))
        );

        // delete item
        adapter.setOnDeleteClickListener(position -> {
            int itemId = menuList.get(position).id; // get item id
            db.deleteMenuItem(itemId); // delete menu item
            loadMenuList(); // reload list
        });
    }

    // use custom dialog/pop up window for adding/editing an item https://developer.android.com/develop/ui/views/components/dialogs
    private void showMenuItemDialog(@Nullable MenuItem item) {

        // inflate custom layout from xml
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_item_menu, null);

        ImageView imageInput = dialogView.findViewById(R.id.input_image);
        Button imageButton = dialogView.findViewById(R.id.upload_item_image_btn);
        Button saveButton = dialogView.findViewById(R.id.save_item_btn);
        Button cancelButton = dialogView.findViewById(R.id.cancel_item_btn);

        EditText nameInput = dialogView.findViewById(R.id.input_item_name);
        EditText detailsInput = dialogView.findViewById(R.id.input_item_details);
        EditText priceInput = dialogView.findViewById(R.id.input_item_price);

        // if item is empty (add new item)
        if (item == null) {
            selectedImage = "default"; // if selectedImage equals "default"
            imageInput.setImageResource(R.drawable.ic_menu); // set default image
            // item already exists (edit item)
        } else {
            int img = updateImage(item.imageName); //
            imageInput.setImageResource(img); //
            // change text in forms
            nameInput.setText(item.name);
            detailsInput.setText(item.details);
            priceInput.setText(item.price);
        }

        // instantiate alertdialog https://developer.android.com/develop/ui/views/components/dialogs#AlertDialog
        AlertDialog dialog = new AlertDialog.Builder(requireContext()).setView(dialogView).create();

        imageButton.setOnClickListener(v -> selectImage(imageInput)); // select image button

        // listener for save item button
        saveButton.setOnClickListener(v -> {
            // update values
            String name = nameInput.getText().toString();
            String details = detailsInput.getText().toString();
            String price = priceInput.getText().toString();

            // name input validation
            if (name.isEmpty()) {
                nameInput.setError("Name required!");
                return;
            }

            // price input validation
            if (price.isEmpty()) {
                priceInput.setError("Price required!");
                return;
            }

            // update the parameters
            if (item == null) {
                // create new item
                MenuItem newItem = new MenuItem(name, details, price, selectedImage);
                db.addMenuItem(newItem);
            } else {
                // update existing values
                item.name = name;
                item.details = details;
                item.price = price;
                item.imageName = selectedImage;
                db.updateMenuItem(item.id, item);
            }

            loadMenuList();
            dialog.dismiss(); // close dialog, nothing happens
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss()); // click cancel button to close dialog

        dialog.show(); // display dialog layout (pop up screen)
    }

    private void selectImage(ImageView preview) {
        String[] images = {"pizza1","pizza2","pizza3","pizza4","pizza5","pizza6"}; // get pre loaded images as string to select from drawable folder

        new AlertDialog.Builder(requireContext())
                .setTitle("Select an image")
                .setItems(images, (dialog, i) -> {
                    selectedImage = images[i];
                    preview.setImageResource(updateImage(selectedImage));
                })
                .show();
    }

    private int updateImage(String imageName) {
        return getResources().getIdentifier(imageName, "drawable", requireContext().getPackageName()); // get identifier to convert string into R drawable
    }

    private void loadMenuList() {
        menuList.clear();
        menuList.addAll(db.getAllMenuItems());
        adapter.notifyDataSetChanged();
    }
}

// on text fields https://developer.android.com/reference/com/google/android/material/textfield/TextInputLayout#setError(java.lang.CharSequence)
// on adapters https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView.Adapter#summary
// on updating menu list https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView.Adapter#notifyDataSetChanged()