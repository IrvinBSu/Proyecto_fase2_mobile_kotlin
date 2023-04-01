package models
abstract class PaymentMethod {
    private var id:Int = ((Math.random() * 1000) + 1).toInt()
    abstract fun charge(amount:Double):Boolean
}