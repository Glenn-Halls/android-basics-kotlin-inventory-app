package com.example.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {
    val allItems: LiveData<List<Item>> = itemDao.getItems().asLiveData()
    private fun insertItem(item: Item) {
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

    private fun getNewItemEntry(
        itemName: String,
        itemPrice: String,
        itemCount: String,
    ) : Item {
        return Item(
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt(),
        )
    }

    fun addNewItem(
        itemName: String,
        itemPrice: String,
        itemCount: String,
    ) {
        val newItem = getNewItemEntry(itemName, itemPrice, itemCount)
        insertItem(newItem)
    }

    fun isEntryValid(
        itemName: String,
        itemPrice: String,
        itemCount: String,
    ) : Boolean {
        return !(itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank())
    }
}

class InventoryViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
