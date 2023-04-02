package models

//clase Cliente
//se guarda nombre, contraseña, métodos de pago y órdenes previas
class Client (
    var name: String,
    var password: String,
    var paymentMethods: MutableList<PaymentMethod> = mutableListOf(),
    val orders:MutableList<Order> = mutableListOf()
){
    val creditCards : List<CreditCard>
        //obtener solo las tarjetas de crédito de la lista de métodos de pago
        get() = paymentMethods.filterIsInstance<CreditCard>().toMutableList()

    val giftCards : List<GiftCard>
        //obtener solo las tarjetas de regalo de la lista de métodos de pago
        get() = paymentMethods.filterIsInstance<GiftCard>().toMutableList()

    //identificador generado aleatoriamente
    var id:Int = (Math.random() * (1000 + 1)).toInt()
        get() = id

    init {
        println("Usuario creado")
    }

    //añadir un método de pago a la lista
    fun addPaymentMethod(paymentMethod: PaymentMethod){
        paymentMethods.add(paymentMethod)
    }
}