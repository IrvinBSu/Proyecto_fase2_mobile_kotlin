abstract class PaymentMethod(val isDefault:Boolean = false) {
    private var id:Int = (Math.random() * (1000 + 1)).toInt()
    public abstract fun charge(amount:Double):Boolean
}