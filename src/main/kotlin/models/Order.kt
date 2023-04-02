package models

import interfaces.IAdd
import interfaces.IShow
import java.util.Date

//clase para representar una orden
//guarda el id del cliente, el total, fecha y productos adquiridos
//implementa IAdd e IShow
class Order(
    val customerId:Int,
    val total:Double,
    val orderDate:Date): IAdd<Int>, IShow<Int> {

    //identificador aleatorio
    private var id:Int = (Math.random() * (1000 + 1)).toInt()

    //mapa de elementos comprados con sus cantidades
    override val items:MutableMap<String, Int> = mutableMapOf()

    //añadir un elemento a la orden
    override fun addItem(item:String, value:Int){
        items[item] = value
    }

    //mostrar los elementos adquiridos y el total
    override fun showItems() {
        val itemStrings = items.map{
                entry -> "${entry.key} --  Ordered: ${entry.value}"
        }

        itemStrings.forEach{
                it -> println(it)
        }

        println("Total: $total")
    }


}