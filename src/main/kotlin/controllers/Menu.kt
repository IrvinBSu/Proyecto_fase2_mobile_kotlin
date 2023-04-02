package controllers

import enums.FoodCategory
import models.FoodItem

//clase para representar el menú
class Menu {

    //lista con los alimentos disponibles en el menú
    private val menuAvailable: List<FoodItem> = listOf(
        FoodItem (FoodCategory.ARROZ_ORIENTAL,"Gohan Samurai", "Preparado con tiras de cangrejo, aguacate",85.0),
        FoodItem (FoodCategory.ARROZ_ORIENTAL,"Gohan Furikake","Arroz blanco condimentado con salmón o camarón",75.0),
        FoodItem (FoodCategory.ENTRADAS,"Camarones Roca","Camarón al tempura con salsa agridulce",145.0),
        FoodItem (FoodCategory.ENTRADAS,"Edamames","Frijol de soja al vapor sazonado con sal",70.0)
    )

    //mostrar el menú
    fun getMenu(){
        println("Cargando menu...")
        //índice para numerar los elementos del menú
        var cont=1
        //se muestra cada categoría
        val categories = FoodCategory.values()
        categories.forEach {
            println("-------${it.category}------")
            val category = it
            //dentro de la categoría se muestra cada alimento con su nombre, precio y descripción
            menuAvailable.forEach{
                if(it.foodCategory == category){
                    println("   ${cont} - ${it.name} : $${it.price}")
                    println("          ${it.description}")
                    cont++
                }
            }
        }
    }

    //obtener el elemento del menú en un cierto índice
    fun getItem(index:Int): FoodItem {
        return menuAvailable[index]
    }

    //obtener la cantidad de elementos
    fun getTotalItems():Int{
        return menuAvailable.size
    }
}