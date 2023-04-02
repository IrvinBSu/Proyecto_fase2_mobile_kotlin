package controllers

import enums.FoodCategory
import models.FoodItem

class Menu {

    private val menuAvailable: List<FoodItem> = listOf(
        FoodItem (FoodCategory.ARROZ_ORIENTAL,"Gohan Samurai", "Preparado con tiras de cangrejo, aguacate",85.0),
        FoodItem (FoodCategory.ARROZ_ORIENTAL,"Gohan Furikake","Arroz blanco condimentado con salmón o camarón",75.0),
        FoodItem (FoodCategory.ENTRADAS,"Camarones Roca","Camarón al tempura con salsa agridulce",145.0),
        FoodItem (FoodCategory.ENTRADAS,"Edamames","Frijol de soja al vapor sazonado con sal",70.0)
        FoodItem (FoodCategory.ENTRADAS,"Tropical","Arandano, nuez mixta, pera, manzana, en combinacion de lechugas con mango",125.0),
        FoodItem (FoodCategory.RAMEN,"Ribeye Ramen","Ribeye importado en fondo trdicional",185.0),
        FoodItem (FoodCategory.RAMEN,"Pork Belly Ramen","Ramen tradicional preparada con consome de cerdo y pollo",200.0),
        FoodItem (FoodCategory.RAMEN,"Pork Miso Ramen","Pasta japonesa preparada en delicioso fondo de miso, shiitake",160.0),
        FoodItem (FoodCategory.BEBIDAS,"Squirt","600 ml",37.0),
        FoodItem (FoodCategory.BEBIDAS,"Pepsi","600 ml",37.0),
        FoodItem (FoodCategory.BEBIDAS,"Manzanita","600 ml",37.0),
        FoodItem (FoodCategory.BEBIDAS,"Ramune Soda","500 ml", 80.0),
        FoodItem (FoodCategory.YAKIMESHI,"Soria Especial","Con mantequilla al ajo, edamame, calamar",110.0),
        FoodItem (FoodCategory.YAKIMESHI,"Mixto","Camaron, carne, huevo, verduras",85.0),
        FoodItem (FoodCategory.ESPECIALES,"Dragon Roll","Mango y aguacate, camaron empanizado, queso crema",130.0),
        FoodItem (FoodCategory.ESPECIALES,"Avocado Crab","Aguacate, camaron empanizdo y aguacate",120.0),
        FoodItem (FoodCategory.ESPECIALES,"Super Tampico","Salsa tampipco, cangrej, queso, chile toreado y kaklague",125.0),
        FoodItem (FoodCategory.ESPECIALES,"Green Roll","Espinaza al tempura, salmon, queso, aguacate",120.0),
        FoodItem (FoodCategory.ESPECIALES,"Sake Roll","Salmon fresco, aguacate, queso crema y pepino",125.0)
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
