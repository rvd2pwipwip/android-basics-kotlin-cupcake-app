package com.example.cupcake.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "OrderViewModel"

class OrderViewModel: ViewModel() {

    private val _quantity = MutableLiveData<Int>(0)
    val quantity: LiveData<Int> = _quantity

    private val _flavor = MutableLiveData<String>("")
    val flavor: LiveData<String> = _flavor

    private val _date = MutableLiveData<String>("")
    val date: LiveData<String> = _date

    private val _price = MutableLiveData<Double>(0.0)
    val price: LiveData<Double> = _price

    private val _options = MutableLiveData<MutableList<String>>()
    val options: LiveData<MutableList<String>> = _options

    val dateOptions = getPickupOptions()

    fun setQuantity(numberCupcakes: Int) {
        Log.d(TAG, "called setQuantity")
        _quantity.value = numberCupcakes
    }

    fun setFlavor(desiredFlavor: String) {
        Log.d(TAG, "called setFlavor")
        _flavor.value = desiredFlavor
    }

    fun setDate(pickupDate: String) {
        _date.value = pickupDate
    }


    fun hasNoFlavorSet(): Boolean {
        return _flavor.value.isNullOrEmpty()
    }

    fun getPickupOptions(): MutableList<String> {
        Log.d(TAG, "called getPickupOptions")
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()

        repeat(4) {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }

        _options.value = options

        Log.d(TAG, "${_options.value.toString()}")
        return options
    }

}