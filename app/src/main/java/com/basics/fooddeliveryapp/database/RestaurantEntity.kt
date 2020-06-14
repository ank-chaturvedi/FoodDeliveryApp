package com.basics.fooddeliveryapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "restaurant")
data class RestaurantEntity(
    @PrimaryKey val id:String,
    @ColumnInfo val name:String,
    @ColumnInfo val cost:String,
    @ColumnInfo val rating:String,
    @ColumnInfo val image:String
)