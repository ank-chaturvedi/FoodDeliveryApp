package com.basics.fooddeliveryapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RestaurantEntity::class],version = 1)
abstract class DatabseEntity:RoomDatabase() {

    abstract fun restaurantDao():RestaurantDao
}