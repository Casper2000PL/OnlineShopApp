package com.casper.onlineshopapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.casper.onlineshopapp.Domain.CategoryModel
import com.casper.onlineshopapp.Domain.ItemModel
import com.casper.onlineshopapp.Domain.SliderModel
import com.casper.onlineshopapp.Repository.MainRepository

class MainViewModel(): ViewModel() {
    private val repository = MainRepository()

    fun loadBanner():LiveData<MutableList<SliderModel>> {
        return repository.loadBanner()
    }

    fun loadCategory():LiveData<MutableList<CategoryModel>> {
        return repository.loadCategory()
    }

    fun loadBestSeller():LiveData<MutableList<ItemModel>> {
        return repository.loadBestSeller()
    }

}