package models

//clase para representar dinero
//hereda de PaymentMethod
class Money(isDefault: Boolean = false): PaymentMethod() {
    //sobrecarga al método de pago
    //el método indica el total de pagar y avisa al usuario que puede pagarlo en caja
    override fun charge(amount: Double): Boolean {
        println("El total a pagar es de ${amount}")
        println("Puedes pasar a caja a pagarlo en efectivo")
        return true
    }
}