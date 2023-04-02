package controllers

import interfaces.IAdd
import interfaces.IShow

//clase del carrito de compras
//implementa IAdd e ISgiw
class Cart: IAdd<Pair<Double, Int>>, IShow<Pair<Double, Int>> {

    //elementos del carrito con su cantidad y precio unitario
    override val items: MutableMap<String, Pair<Double, Int>> = mutableMapOf()
    
    private var orderStatus = listOf("Orden Confirmada...","Preparando...","Alimentos Listos...","Alimentos Entregados...")

    //agregar un elemento, recibe el nombre del item y un par(precio, cantidad)
    override fun addItem(item:String, value:Pair<Double, Int>){
        //si existe la llave sumar a la cantidad existente, si no crear la llave en el mapa con este valor
        val numItems = (items[item]?.second?.plus(value.second)) ?: value.second
        items[item] = Pair(value.first, numItems)
    }

    //remover un elemento
    fun removeItem(item:String){
        //si la cantidad es mayor a 0, restar uno de la cantidad
        if(items[item]?.second!! > 0) {
            items[item] = items[item]?.let { Pair(it.first, it.second - 1) }!!
        } else {
            //remover el elemento del mapa
            items.remove(item);
        }
    }

    //obtener el costo total de los elementos en el carrito
    fun getTotal() : Double{
        var total = 0.0
        items.values.forEach{total += it.first * it.second}
        return total
    }

    //mostrar los elementos del carrito en un formato y el costo total
    override fun showItems(){
        val itemStrings = items.map{
            entry -> "${entry.key} --  Price: $${entry.value.first}  Total: ${entry.value.second}"
        }

        itemStrings.forEach{
            println(it)
        }

        println("Total: ${getTotal()}")

    }
    
    suspend fun deliverOrder(){
        orderStatus.forEach {
            delay(500L)
            println("Estatus: ${it}")
        }
    }

}
