class GiftCard(
    val code: String,
    var balance:Double,
    isDefault:Boolean = false):PaymentMethod(isDefault) {

    override fun charge(amount: Double): Boolean {
        if(amount <= balance){
            balance -= amount
            println("Pago completado con exito")
            return true
        } else {
            println("No cuentas con el monto suficiente en tu tarjeta de regalo")
            return false
        }
    }
}