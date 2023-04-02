package interfaces

//interfaz para añadir elementos a un mapa
interface IAdd<V>
{
    val items:MutableMap<String, V>

    fun addItem(item:String, value:V)
}