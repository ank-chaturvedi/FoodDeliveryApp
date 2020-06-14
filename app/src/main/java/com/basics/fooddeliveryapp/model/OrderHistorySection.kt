package com.basics.fooddeliveryapp.model

data class OrderHistorySection(
    val restName: String,
    val orderPlacedAt: String,
    val list: List<FoodItems>
)