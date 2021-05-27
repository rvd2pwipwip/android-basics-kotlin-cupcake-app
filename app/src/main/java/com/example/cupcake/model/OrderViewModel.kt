package com.example.cupcake.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val PRICE_PER_CUPCAKE = 2.00
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00
private const val NO_SAME_DAY = "Special Flavor"

private const val TAG = "OrderViewModel"

class OrderViewModel: ViewModel() {

    private val _orderName = MutableLiveData<String>()
    val orderName: LiveData<String> = _orderName

    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    //ui displays flavors as radio buttons or check boxes
    private val _isSingleOrder = MutableLiveData<Boolean>()
    val isSingleOrder: LiveData<Boolean> = _isSingleOrder

    private val _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> = _flavor

    private val _flavors = MutableLiveData<MutableMap<String, Int>>()
    val flavors: LiveData<MutableMap<String, Int>> = _flavors

    private val _hasChocolate = MutableLiveData<Boolean>()
    val hasChocolate: LiveData<Boolean> = _hasChocolate

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private val _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price) {
        NumberFormat.getCurrencyInstance().format(it)
    }

    val dateOptions = getPickupOptions()

    init {
        resetOrder()
    }

    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes
        _isSingleOrder.value = _quantity.value == 1
        _flavors.value = setFlavors()
        Log.d(TAG, "${_flavors.value}")
        Log.d(TAG, "${flavors.value?.get("Vanilla")}")
        updatePrice()
    }

    fun setFlavor(desiredFlavor: String) {
        _flavor.value = desiredFlavor
        if (desiredFlavor == NO_SAME_DAY && date.value == dateOptions[0]) {
            setDate(dateOptions[1])
            updatePrice()
        }
    }

    fun setDate(pickupDate: String) {
        _date.value = pickupDate
        updatePrice()
    }

    fun setOrderName(name: String) {
        _orderName.value = name
    }

    fun hasNoFlavorSet(): Boolean {
        return _flavor.value.isNullOrEmpty()
    }

    fun getPickupOptions(): MutableList<String> {
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()

        repeat(4) {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }

        return options
    }

    private fun updatePrice() {
        var calculatedPrice = (quantity.value ?: 0) * PRICE_PER_CUPCAKE
        if (_date.value == dateOptions[0]) {
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        _price.value = calculatedPrice
    }

    fun setFlavors(): MutableMap<String, Int> {
        return mutableMapOf(
            "Vanilla" to _quantity.value as Int,
            "Chocolate" to 0,
            "Red Velvet" to 0,
            "Salted Caramel" to 0,
            "Coffee" to 0,
            "Special Flavor" to 0
        )
    }

    fun addFlavor(flavor: String) {
//        _flavors.value?.set(flavor, 1)
        _flavors.value!![flavor] = 1
        _hasChocolate.value = !_hasChocolate.value!!
        Log.d(TAG, "Added $flavor ${flavors.value?.get(flavor)}")
        Log.d(TAG, "${flavors.value}")
    }


    fun resetOrder() {
        _orderName.value = ""
        _quantity.value = 0
        _isSingleOrder.value = false
        _flavor.value = ""
        _flavors.value = setFlavors()
        _hasChocolate.value = false
        _date.value = dateOptions[0]
        _price.value = 0.0
    }

}