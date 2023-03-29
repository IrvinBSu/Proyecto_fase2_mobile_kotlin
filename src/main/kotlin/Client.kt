data class Client (var name: String, var password: String){

    public var id:Int = (Math.random() * (1000 + 1)).toInt()
    get():Int = id

    private var cards:MutableList<PaymentMethod> = mutableListOf()

    init {
        println("Usuario creado")
    }

    fun updateProfile(name: String, password: String){
        this.name = name
        this.password = password
    }

    fun addCreditCard(card:CreditCard){
        cards.add(card)
    }

    fun addGiftCard(card:GiftCard){
        cards.add(card)
    }
}