package com.example.comp2000.ui.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import com.example.comp2000.data.model.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.comp2000.R;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {

    // parameters
    private RecyclerView recyclerView; // display list items in recyclerview
    private MenuAdapter adapter; // uses adapter to convert each item XML into view
    private List<MenuItem> menuList = new ArrayList<>(); // list that holds menu items

    public MenuFragment() {
        // required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate view (xml -> view)
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        // find recyclerview by ID
        recyclerView = view.findViewById(R.id.menuRecyclerView);
        // set layout for recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // custom method to load menu items into recyclerview
        loadMenuItems();

        // check if isStaff is true

        // get menuadapter
        adapter = new MenuAdapter(requireContext(), menuList);
        // connect adapter to recyclerview
        recyclerView.setAdapter(adapter);

        // pass clicks to adapter
        adapter.setOnItemClickListener(position -> {
            MenuItem item = menuList.get(position); // get current position to render items
            // listen to edit and delete button clicks
            // open edit window
            // delete item from list
        });

        // return the inflated view
        return view;
    }

    // sample menu items, get from DB later?
    private void loadMenuItems() {
        menuList.add(new MenuItem("Pizza", "£10", R.drawable.ic_menu));
        menuList.add(new MenuItem("Pasta", "£8", R.drawable.ic_menu));
        menuList.add(new MenuItem("Tiramisu", "£4", R.drawable.ic_menu));
        menuList.add(new MenuItem("Pizza", "£10", R.drawable.ic_menu));
        menuList.add(new MenuItem("Pasta", "£8", R.drawable.ic_menu));
        menuList.add(new MenuItem("Tiramisu", "£4", R.drawable.ic_menu));
    }
}