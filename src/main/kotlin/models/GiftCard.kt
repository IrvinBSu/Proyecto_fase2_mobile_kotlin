package models

//clase para representar una tarjeta de regalo
//hereda de PaymentMethod
class GiftCard(
    val code: String,
    var balance:Double,
    isDefault:Boolean = false): PaymentMethod() {

    //sobrecarga al método de pago
    //verifica que la cantidad a pagar sea menor al balance de la tarjeta
    override fun charge(amount: Double): Boolean {
        if(amount <= balance){
            //si el pago se realiza exitosamente restar al balance
            balance -= amount
            println("Pago completado con exito")
            return true
        } else {
            println("No cuentas con el monto suficiente en tu tarjeta de regalo")
            return false
        }
    }
}