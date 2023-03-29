import java.util.Date

class Order(
    val customerId:Int,
    val total:Double,
    val orderDate:Date): IAdd<Int>, IShow<Int> {

    private var id:Int = (Math.random() * (1000 + 1)).toInt()

    override val items:MutableMap<String, Int> = mutableMapOf()

    override fun addItem(item: String, price: Double) {
        TODO("Not yet implemented")
    }

    override fun showItems() {
        TODO("Not yet implemented")
    }


}