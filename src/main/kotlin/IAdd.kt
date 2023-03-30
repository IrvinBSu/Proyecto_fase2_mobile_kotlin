interface IAdd<V> {
    val items:MutableMap<String, V>

    fun addItem(item:String, price:Double, amount:Int)
}