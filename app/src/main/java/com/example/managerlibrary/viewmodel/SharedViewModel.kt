package com.example.managerlibrary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val searchText: MutableLiveData<String> = MutableLiveData()
}