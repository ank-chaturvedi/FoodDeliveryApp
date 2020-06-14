package com.basics.fooddeliveryapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface RestaurantDao {
    @Insert
    fun insert(restaurantEntity: RestaurantEntity)

    @Delete
    fun delete(restaurantEntity: RestaurantEntity)

    @Query(value = "Select * From restaurant where id = :id")
    fun getRestById(id:String):RestaurantEntity

    @Query(value = "Select * From restaurant")
    fun getAll():List<RestaurantEntity>



}