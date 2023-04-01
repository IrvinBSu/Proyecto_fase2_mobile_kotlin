package controllers

import enums.FoodCategory
import models.FoodItem

class Menu {

    private val menuAvailable: List<FoodItem> = listOf(
        FoodItem (FoodCategory.ARROZ_ORIENTAL,"Gohan Samurai", "Preparado con tiras de cangrejo, aguacate",85.0),
        FoodItem (FoodCategory.ARROZ_ORIENTAL,"Gohan Furikake","Arroz blanco condimentado con salmón o camarón",75.0),
        FoodItem (FoodCategory.ENTRADAS,"Camarones Roca","Camarón al tempura con salsa agridulce",145.0),
        FoodItem (FoodCategory.ENTRADAS,"Edamames","Frijol de soja al vapor sazonado con sal",70.0)
    )

    fun getMenu(){
        println("Cargando menu...")
        var cont=1
        val categories = FoodCategory.values()
        categories.forEach {
            println("-------${it.category}------")
            val category = it
            menuAvailable.forEach{
                if(it.foodCategory == category){
                    println("   ${cont} - ${it.name} : $${it.price}")
                    println("          ${it.description}")
                    cont++
                }
            }
        }
    }

    fun getItem(index:Int): FoodItem {
        return menuAvailable[index]
    }

    fun getTotalItems():Int{
        return menuAvailable.size
    }
}