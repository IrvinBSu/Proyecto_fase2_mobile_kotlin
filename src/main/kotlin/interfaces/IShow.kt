package interfaces

interface IShow<V> {
    val items:MutableMap<String, V>
    fun showItems()
}