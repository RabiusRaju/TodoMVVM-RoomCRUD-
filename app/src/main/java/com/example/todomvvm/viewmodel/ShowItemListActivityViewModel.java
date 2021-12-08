package com.example.todomvvm.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.todomvvm.db.AppDatabase;
import com.example.todomvvm.db.entity.Items;

import java.util.List;

public class ShowItemListActivityViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Items>> listOfItems;
    private final AppDatabase appDatabase;

    public ShowItemListActivityViewModel(Application application){
        super(application);
        listOfItems = new MutableLiveData<>();
        appDatabase = AppDatabase.getDBinstance(getApplication().getApplicationContext());
    }

    public MutableLiveData<List<Items>> getItemListObserver(){
        return listOfItems;
    }

    public void getAllItemList(int categoryId){
        List<Items> itemsList = appDatabase.shoppingListDao().getAllItemsList(categoryId);
        if(itemsList.size() > 0){
            listOfItems.postValue(itemsList);
        }else {
            listOfItems.postValue(null);
        }
    }

    public void insertItem(Items item){
        appDatabase.shoppingListDao().insertItems(item);
        getAllItemList(item.categoryId);
    }

    public void updateItem(Items item){
        appDatabase.shoppingListDao().updateItems(item);
        getAllItemList(item.categoryId);
    }

    public void deleteItem(Items item){
        appDatabase.shoppingListDao().deleteItems(item);
        getAllItemList(item.categoryId);
    }
}
