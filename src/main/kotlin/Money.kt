class Money(isDefault: Boolean = false):PaymentMethod(isDefault) {
    override fun charge(amount: Double): Boolean {
        println("El total a pagar es de ${amount}")
        println("Puedes pasar a caja a pagarlo en efectivo")
        return true
    }
}