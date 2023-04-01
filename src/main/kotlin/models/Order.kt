package models

import interfaces.IAdd
import interfaces.IShow
import java.util.Date

class Order(
    val customerId:Int,
    val total:Double,
    val orderDate:Date): IAdd<Int>, IShow<Int> {

    private var id:Int = (Math.random() * (1000 + 1)).toInt()

    override val items:MutableMap<String, Int> = mutableMapOf()
    override fun addItem(item:String, price:Double, amount:Int){
        items[item] = amount
    }

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