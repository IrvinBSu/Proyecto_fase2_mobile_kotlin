class GiftCard(
    val balance:Double,
    val limit:Double,
    isDefault:Boolean = false):PaymentMethod(isDefault) {
    override fun charge(amount: Double): Boolean {
        TODO("Not yet implemented")
    }
}