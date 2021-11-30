package com.example.todomvvm.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todomvvm.db.entity.Category;
import com.example.todomvvm.db.entity.Items;

import java.util.List;

@Dao
public interface ShoppingListDao {

    @Query("Select * from Category")
    List<Category> getAllCategoriesList();

    @Insert
    void insertCategory(Category...categories);

    @Update
    void updateCategory(Category category);

    @Delete
    void deleteCategory(Category category);

    @Query("Select * from Items where categoryId = :catId")
    List<Items> getAllItemsList(int catId);

    @Insert
    void insertItems(Items...items);

    @Update
    void updateItems(Items items);

    @Delete
    void deleteItems(Items items);

}
