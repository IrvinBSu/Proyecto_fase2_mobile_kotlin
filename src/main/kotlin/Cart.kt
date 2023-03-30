class Cart:IAdd<Pair<Double, Int>>, IShow<Pair<Double, Int>> {

    private var id:Int = (Math.random() * (1000 + 1)).toInt()

    override val items: MutableMap<String, Pair<Double, Int>> = mutableMapOf()

    override fun addItem(item:String, price:Double, amount:Int){
        val numItems = (items[item]?.second?.plus(amount)) ?: amount
        items[item] = Pair(price, numItems)
    }

    fun removeItem(item:String){
        if(items[item]?.second!! > 0) {
            items[item] = items[item]?.let { Pair(it.first, it.second - 1) }!!
        } else {
            items.remove(item);
        }
    }

    fun getTotal():Double{
        var total:Double = 0.0
        items.values.forEach{total += it.first * it.second}
        return total
    }

    override fun showItems(){
        val itemStrings = items.map{
            entry -> "${entry.key} --  Price: $${entry.value.first}  Total: ${entry.value.second}"
        }

        itemStrings.forEach{
            it -> println(it)
        }

        println("Total: ${getTotal()}")

    }

}