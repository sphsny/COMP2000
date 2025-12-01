package com.example.comp2000.ui.menu;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
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

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {
    // fields
    private MenuAdapter adapter; // uses adapter to convert each item XML into view
    private final List<MenuItem> menuList = new ArrayList<>(); // list that holds menu items
    private Uri selectedImageUri = null; // image data source via stored url
    private ImageView activeImageView = null; // stores placeholder/target where the uri image goes

    // upload image via photo picker, safe image storing method by google without needing phone permissions https://developer.android.com/training/data-storage/shared/photo-picker#java
    private final ActivityResultLauncher<PickVisualMediaRequest> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // if user selects an image
                if (uri != null && activeImageView != null) {
                    selectedImageUri = uri; // save uri from image to reuse
                    activeImageView.setImageURI(uri); // display image in dialog preview
                }
            });

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

        loadMenuItems(); // custom method to load menu items into recyclerview

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
        addButton.setOnClickListener(v -> showMenuItemDialog(null, -1));

        // edit existing item (pop up window with existing item values)
        adapter.setOnEditClickListener(
                position -> showMenuItemDialog(menuList.get(position), position)
        );

        // delete item
        adapter.setOnDeleteClickListener(position -> {
            menuList.remove(position);
            adapter.notifyItemRemoved(position);
        });
    }

    // use custom dialog/pop up window for adding/editing an item https://developer.android.com/develop/ui/views/components/dialogs
    private void showMenuItemDialog(@Nullable MenuItem item, int position) {

        // inflate custom layout from xml
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_item_menu, null);

        ImageView imageInput = dialogView.findViewById(R.id.input_image);
        Button uploadButton = dialogView.findViewById(R.id.upload_item_image_btn);
        Button saveButton = dialogView.findViewById(R.id.save_item_btn);
        Button cancelButton = dialogView.findViewById(R.id.cancel_item_btn);

        EditText nameInput = dialogView.findViewById(R.id.input_item_name);
        EditText detailsInput = dialogView.findViewById(R.id.input_item_details);
        EditText priceInput = dialogView.findViewById(R.id.input_item_price);

        // if item is empty (add new item)
        if (item == null) {
            selectedImageUri = null;
            imageInput.setImageResource(R.drawable.ic_menu); // placeholder image

        // item already exists (edit item)
        } else {
            // change text in forms
            nameInput.setText(item.name);
            detailsInput.setText(item.details);
            priceInput.setText(item.price);

            if (item.imageUri != null) {
                selectedImageUri = Uri.parse(item.imageUri); // parse image as uri
                imageInput.setImageURI(selectedImageUri); // set image uri
            } else {
                // no image uploaded, leave placeholder
                selectedImageUri = null;
                imageInput.setImageResource(R.drawable.ic_menu);
            }
        }

        // instantiate alertdialog https://developer.android.com/develop/ui/views/components/dialogs#AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        // set dialog view
        builder.setView(dialogView);

        // get alertdialog
        AlertDialog dialog = builder.create();

        // listener for image upload button
        uploadButton.setOnClickListener(v -> {
            activeImageView = imageInput; // change active image to image input
            pickImage(); // pick image function
        });

        // listener for save item button
        saveButton.setOnClickListener(v -> {
            // update values
            String name = nameInput.getText().toString();
            String details = detailsInput.getText().toString();
            String price = priceInput.getText().toString();
            String uri = selectedImageUri == null ? null : selectedImageUri.toString();

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
                menuList.add(new MenuItem(name, details, price, uri));
                adapter.notifyItemInserted(menuList.size() - 1); // update the recyclerview with the new item position
            } else {
                // update existing values
                item.name = name;
                item.details = details;
                item.price = price;
                item.imageUri = uri;

                adapter.notifyItemChanged(position); // update recyclerview of existing menu item
            }

            dialog.dismiss(); // close dialog, nothing happens
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss()); // click cancel button to close dialog

        dialog.show(); // display dialog layout (pop up screen)
    }

    // helper to let user select image, template from https://developer.android.com/training/data-storage/shared/photo-picker#java
    private void pickImage() {
        pickImageLauncher.launch(
                new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE) // allow only image uploads
                        .build()
        );
    }


    // sample menu items, get from DB later
    private void loadMenuItems() {
        menuList.add(new MenuItem("Pizza", "Wheat base, tomato sauce, mozzarella, oregano", "£10", null));
        menuList.add(new MenuItem("Pasta", "Wheat, tomato sauce, parmesan", "£8", null));
    }
}

// on text fields // https://developer.android.com/reference/com/google/android/material/textfield/TextInputLayout#setError(java.lang.CharSequence)
// on adapters https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView.Adapter#summary