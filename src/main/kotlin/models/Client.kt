package models

data class Client (
    var name: String,
    var password: String,
    var paymentMethods: MutableList<PaymentMethod> = mutableListOf(),
    val orders:MutableList<Order> = mutableListOf()
){
    val creditCards : List<CreditCard>
        get() = paymentMethods.filterIsInstance<CreditCard>().toList()

    val giftCards : List<GiftCard>
        get() = paymentMethods.filterIsInstance<GiftCard>().toList()

    private var id:Int = (Math.random() * (1000 + 1)).toInt()

    init {
        println("Usuario creado")
    }

    fun updateProfile(name: String, password: String){
        this.name = name
        this.password = password
    }

    fun getId():Int{
        return id
    }

    fun addPaymentMethod(paymentMethod: PaymentMethod){
        paymentMethods.add(paymentMethod)
    }
}