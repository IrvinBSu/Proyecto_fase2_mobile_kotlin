package models

//clase para representar una tarjeta de regalo
//hereda de PaymentMethod
class GiftCard(
    val code: String,
    var balance:Double,
    isDefault:Boolean = false): PaymentMethod() {

    //sobrecarga al método de pago
    //verifica que la cantidad a pagar sea menor al balance de la tarjeta
    override fun charge(amount: Double): Boolean {
        if(amount <= balance){
            //si el pago se realiza exitosamente restar al balance
            balance -= amount
            println("Pago completado con exito")
            return true
        } else {
            println("No cuentas con el monto suficiente en tu tarjeta de regalo")
            return false
        }
    }

    companion object {
        //método para crear una tarjeta de regalo con input del usuario

        //leer el código de la tarjeta
        private fun readCode():String {
            //leer el código de la tarjeta
            println("Ingresa el código de tu tarjeta de regalo (10 digitos)")
            val code: String = readln()
            //lanzar una excepción si no tiene el tamaño requerido
            if (code.length != 10) {
                throw Exception("El número de tarjeta no tiene la longitud requerida")
            }
            return code;
        }

        //leer el balance de la tarjeta
        private fun readBalance():Double{
            println("Ingresa el balance en la tarjeta (un número)")
            val balance = readln().toDouble()
            return balance;
        }

        //crear una tarjeta de regalo
        fun createGiftCard(): GiftCard? {
            try {
                val code = readCode()
                val balance = readBalance()
                //crear la tarjeta
                val card = GiftCard(code, balance)
                return card
            } catch (e: ClassCastException) { //manejo de excepciones
                println("Recuerda ingresar los datos en el formato requerido")
                println("Inténtalo de nuevo")
                return null
            } catch (e: Exception) {
                println(e.message)
                return null
            }
        }
    }
}