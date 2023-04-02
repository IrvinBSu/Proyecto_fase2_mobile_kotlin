package models

import java.time.LocalDate

//clase para representar tarjetas de crédito
//hereda de PaymentMethod
class CreditCard(
    val cardNumber: String,
    val name:String,
    val expirationDate: LocalDate,
    var balance:Double,
    val limit:Double,
    isDefault:Boolean = false): PaymentMethod() {

    //sobrecarga al método de pago
    //verifica que la cantidad a pagar sea menor al límite de la tarjeta y que tenga crédito suficiente
    override fun charge(amount: Double): Boolean {
        if(amount < limit){
            if(amount <= balance){
                //si el pago se realiza exitosamente restar la cantidad al balance
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