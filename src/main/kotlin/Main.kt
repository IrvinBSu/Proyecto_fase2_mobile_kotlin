import models.Order

fun main() {
    //Programa principal
    //Menú de inicio
    val selection = FlowManager.welcome()
    FlowManager.start(selection)
    FlowManager.greet()
    //Registrar la orden del usuario
    FlowManager.takeOrder()
    val ord: Order = FlowManager.registerOrder()
    //Pasar al pago
    FlowManager.goToPayment(ord)
}
