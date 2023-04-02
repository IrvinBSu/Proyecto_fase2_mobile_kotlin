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

    //añadir un método de pago a la lista
    fun addPaymentMethod(paymentMethod: PaymentMethod){
        paymentMethods.add(paymentMethod)
    }

    //añadir una orden al usuario
    fun addOrder(ord: Order){
        orders.add(ord)
    }

    //mostrar las tarjetas del usuario
    fun showCreditCards(){
        println("Estas son tus tarjetas registradas:")
        var count = 1
        creditCards.forEach {
            println("$count -- ${it.cardNumber} - ${it.name}")
            count++
        }
    }

    //método para seleccionar una tarjeta de crédito
    fun selectCreditCard(): CreditCard {
        println("Selecciona una tarjeta")

        while(true){
            try {
                //leer la selección del usuario
                val selection = readln().toInt()
                if (selection in 1..creditCards.size) {
                    //si la selección es válida regresar la tarjeta de crédito correspondiente
                    return creditCards[selection-1]
                } else {
                    throw Exception("Fuera de rango")
                }
            } catch (e: ClassCastException) { //manejo de excepciones
                println("Debes ingresar un número")
            } catch (e: Exception) {
                println("Debes ingresar un número entre 1 y ${creditCards.size}")
            }
        }
    }

    //mostrar las tarjetas de crédito disponibles para el usuario
    fun showGiftCards(){
        println("Estas son tus tarjetas de regalo registradas:")
        var count = 1
        giftCards.forEach {
            println("$count -- ${it.code}")
            count++
        }
    }

    //método para seleccionar una tarjeta de regalo
    fun selectGiftCard(): GiftCard {
        println("Selecciona una tarjeta")

        while(true){
            try {
                //leer la selección de una tarjeta
                val selection = readln().toInt()
                if (selection in 1..giftCards.size) {
                    //si la selección es válida regresar la tarjeta
                    return giftCards[selection-1]
                } else {
                    throw Exception("Fuera de rango")
                }
            } catch (e: ClassCastException) { //manejo de excepciones
                println("Debes ingresar un número")
            } catch (e: Exception) {
                println("Debes ingresar un número entre 1 y ${giftCards.size}")
            }
        }
    }


}