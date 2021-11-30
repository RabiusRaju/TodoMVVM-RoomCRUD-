package com.example.todomvvm.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.todomvvm.db.dao.ShoppingListDao;
import com.example.todomvvm.db.entity.Category;
import com.example.todomvvm.db.entity.Items;

@Database(entities = {Category.class, Items.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ShoppingListDao shoppingListDao();
    public static AppDatabase INSTANCE;

    public static AppDatabase getDBinstance(Context context){
        if(INSTANCE == null){
            /*
            * Database Class Build this
            * */
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"AppDB")
                        .allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}
