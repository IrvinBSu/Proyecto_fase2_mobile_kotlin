package models

data class Client (
    var name: String,
    var password: String,
    var creditCards:MutableList<CreditCard> = mutableListOf(),
    var giftCards: MutableList<GiftCard> = mutableListOf(),
    val orders:MutableList<Order> = mutableListOf()
){

    private var id:Int = (Math.random() * (1000 + 1)).toInt()

    init {
        println("Usuario creado")
    }

    fun updateProfile(name: String, password: String){
        this.name = name
        this.password = password
    }

    public fun getId():Int{
        return id
    }

    fun addPaymentMethod(paymentMethod: PaymentMethod){
        paymentMethods.add(paymentMethod)
    }
}