package models

import enums.FoodCategory

//Data class para representar los alimentos y bebidas
data class FoodItem(
    val foodCategory: FoodCategory,
    val name: String,
    val description: String,
    val price:Double
){

}