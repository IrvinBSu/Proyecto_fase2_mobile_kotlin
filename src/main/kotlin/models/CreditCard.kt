package models

import java.time.LocalDate

class CreditCard(
    val cardNumber: String,
    val name:String,
    val expirationDate: LocalDate,
    var balance:Double,
    val limit:Double,
    isDefault:Boolean = false): PaymentMethod() {

    override fun charge(amount: Double): Boolean {
        if(amount < limit){
            if(amount <= balance){
                balance -= amount
                println("Pago completado con exito")
                return true
            } else {
                println("No cuentas con suficiente crédito en tu tarjeta")
                return false
            }
        } else {
            println("El pago es mayor al límite de tu tarheta")
            return false
        }
    }
}