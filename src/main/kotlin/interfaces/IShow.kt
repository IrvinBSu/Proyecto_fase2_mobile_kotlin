package interfaces

//interfaz para mostrar los elementos de un mapa
interface IShow<V> {
    val items:MutableMap<String, V>
    fun showItems()
}