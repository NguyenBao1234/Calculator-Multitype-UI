package com.app.emptyapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DemoVM: ViewModel()
{
    val count = MutableLiveData(0)

    fun IncreaseCount (){
        count.value = (count.value?:0) +1
    }
}