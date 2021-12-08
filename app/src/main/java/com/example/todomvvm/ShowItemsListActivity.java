package com.example.todomvvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todomvvm.adapter.ItemListAdapter;
import com.example.todomvvm.databinding.ActivityShowItemsListBinding;
import com.example.todomvvm.db.entity.Items;
import com.example.todomvvm.viewmodel.ShowItemListActivityViewModel;

import java.util.Objects;

public class ShowItemsListActivity extends AppCompatActivity implements ItemListAdapter.HandleItemClick{

    private int category_id;
    private ItemListAdapter itemListAdapter;
    private ShowItemListActivityViewModel viewModel;
    private Items itemToUpdate = null;
    private ActivityShowItemsListBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowItemsListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        init();
    }

    public void init(){
        Bundle bundle = getIntent().getExtras();
        category_id = bundle.getInt("category_id",0);
        String categoryName = bundle.getString("category_name");

        Objects.requireNonNull(getSupportActionBar()).setTitle(categoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        binding.addNewItemInput.setOnClickListener(v -> {
            String itemName = binding.addNewItemInput.getText().toString();
            if(TextUtils.isEmpty(itemName)){
                Toast.makeText(ShowItemsListActivity.this,"Enter Item Name", Toast.LENGTH_LONG).show();
                return;
            }
            if(itemToUpdate == null){
                saveNewItems(itemName);
            }else {
                updateNewItem(itemName);
            }
        });
        initRecyclerView();
        initViewModel();

    }

    private void initViewModel(){
        viewModel = new ViewModelProvider(this).get(ShowItemListActivityViewModel.class);
        viewModel.getItemListObserver().observe(this, items -> {
            if(items == null){
                binding.recyclerView.setVisibility(View.GONE);
                binding.noResult.setVisibility(View.VISIBLE);
            }else{
                itemListAdapter.setItemsList(items);
                binding.recyclerView.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initRecyclerView(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemListAdapter = new ItemListAdapter(this, this);
        binding.recyclerView.setAdapter(itemListAdapter);
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