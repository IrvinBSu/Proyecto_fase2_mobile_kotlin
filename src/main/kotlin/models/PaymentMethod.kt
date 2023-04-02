package models

//clase abstracta método de pago
abstract class PaymentMethod {
    //identificador generado aleatoriamente
    private var id:Int = ((Math.random() * 1000) + 1).toInt()

    //realizar el pago con este medio
    //regresa un booleano que indica si la acción se realizó correctamente
    abstract fun charge(amount:Double):Boolean
}