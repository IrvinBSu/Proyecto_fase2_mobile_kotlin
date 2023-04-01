import models.Order

fun main() {

    FlowManager.start()
    val ord: Order = FlowManager.registerOrder()
    FlowManager.goToPayment(ord)

}
