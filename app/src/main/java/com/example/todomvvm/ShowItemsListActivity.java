package com.example.todomvvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todomvvm.db.Items;
import com.example.todomvvm.viewmodel.ShowItemListActivityViewModel;

import java.util.List;

public class ShowItemsListActivity extends AppCompatActivity implements ItemListAdapter.HandleItemClick{

    private int category_id;
    private ItemListAdapter itemListAdapter;
    private ShowItemListActivityViewModel viewModel;
    private Items itemToUpdate = null;

    private TextView noResulttextView;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items_list);
        init();
    }

    public void init(){
        category_id = getIntent().getIntExtra("category_id",0);
        String categoryName = getIntent().getStringExtra("category_name");

        getSupportActionBar().setTitle(categoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noResulttextView = findViewById(R.id.noResult);

        final EditText addNewItemInput = findViewById(R.id.addNewItemInput);
        ImageView saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = addNewItemInput.getText().toString();
                if(TextUtils.isEmpty(itemName)){
                    Toast.makeText(ShowItemsListActivity.this,"Enter Item Name", Toast.LENGTH_LONG).show();
                    return;
                }
                if(itemToUpdate == null){
                    saveNewItems(itemName);
                }else {
                    updateNewItem(itemName);
                }

            }
        });
        initRecyclerView();
        initViewModel();

    }

    private void initViewModel(){
        viewModel = new ViewModelProvider(this).get(ShowItemListActivityViewModel.class);
        viewModel.getItemListObserver().observe(this, new Observer<List<Items>>() {
            @Override
            public void onChanged(List<Items> items) {
                if(items == null){
                    recyclerView.setVisibility(View.GONE);
                    noResulttextView.setVisibility(View.VISIBLE);
                }else{
                    itemListAdapter.setItemsList(items);
                    noResulttextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initRecyclerView(){
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemListAdapter = new ItemListAdapter(this, this);
        recyclerView.setAdapter(itemListAdapter);
    }

    public void saveNewItems(String itemName){
        Items item = new Items();
        item.itemName = itemName;
        item.categoryId = category_id;
        viewModel.insertItem(item);
        ((EditText)findViewById(R.id.addNewItemInput)).setText("");
    }

    public void updateNewItem(String itemName){
        itemToUpdate.itemName = itemName;
        viewModel.updateItem(itemToUpdate);

        ((EditText)findViewById(R.id.addNewItemInput)).setText("");
        itemToUpdate = null;
    }

    @Override
    public void itemClick(Items item) {
        if(item.completed){
            item.completed = false;
        }else{
            item.completed = true;
        }
        viewModel.updateItem(item);
    }

    @Override
    public void removeItem(Items item) {
        viewModel.deleteItem(item);
    }

    @Override
    public void editItem(Items item) {
        this.itemToUpdate = item;
        ((EditText)findViewById(R.id.addNewItemInput)).setText(item.itemName);
    }
}