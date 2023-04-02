import models.Order

fun main() {
    //Programa principal
    //Menú de inicio
    FlowManager.start()
    //Registrar la orden del usuario
    val ord: Order = FlowManager.registerOrder()
    //Pasar al pago
    FlowManager.goToPayment(ord)
}
