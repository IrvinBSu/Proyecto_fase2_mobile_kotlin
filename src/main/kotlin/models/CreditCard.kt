package models

import java.time.LocalDate

//clase para representar tarjetas de crédito
//hereda de PaymentMethod
class CreditCard(
    val cardNumber: String,
    val name:String,
    val expirationDate: LocalDate,
    var balance:Double,
    val limit:Double,
    isDefault:Boolean = false): PaymentMethod() {

    //sobrecarga al método de pago
    //verifica que la cantidad a pagar sea menor al límite de la tarjeta y que tenga crédito suficiente
    override fun charge(amount: Double): Boolean {
        if(amount < limit){
            if(amount <= balance){
                //si el pago se realiza exitosamente restar la cantidad al balance
                balance -= amount
                println("Pago completado con exito")
                return true
            } else {
                println("No cuentas con suficiente crédito en tu tarjeta")
                return false
            }
        } else {
            println("El pago es mayor al límite de tu tarheta")
            return false
        }
    }

    companion object {
        //leer el número de tarjeta de crédito
        private fun readCardNumber():String{
            println("Ingresa tu número de tarjeta (16 digitos)")
            val cardNumber:String = readln()
            //lanzar un error si no cumple con la longitud
            if(cardNumber.length != 16){
                throw Exception("El número de tarjeta no tiene la longitud requerida")
            }
            return cardNumber
        }

        //leer nombre
        private fun readName():String{
            println("Ingresa el nombre registrado en la tarjeta")
            val name:String = readln()
            return name
        }

        //leer fecha de vencimiento y covertirla a LocalDate
        private fun readDate():LocalDate{
            println("Ingresa la fecha de vencimiento (yyyy-mm)")
            var readDate:String = readln().trim()
            readDate = readDate.plus("-01")
            val date:LocalDate = LocalDate.parse(readDate)
            val today = LocalDate.now()
            if(today.isAfter(date)){
                throw Exception("Esta tarjeta ya expiró")
            }
            return date
        }

        //leer el balance en la tarjeta
        private fun readBalance():Double{
            println("Ingresa el balance en la tarjeta (un número)")
            val balance = readln().toDouble()
            return balance
        }

        //leer límite de la tarjeta
        private fun readLimit():Double{
            println("Ingresa el límite la tarjeta (un número)")
            val limit = readln().toDouble()
            return limit
        }

        //método para crear una tarjeta de crédito
        fun createCreditCard(): CreditCard?{
            try {
                val cardNumber:String = readCardNumber()
                val name:String = readName()
                val date:LocalDate = readDate()
                val balance:Double = readBalance()
                val limit:Double = readLimit()
                //crear la tarjeta y agregarla al cliente
                val card: CreditCard = CreditCard(cardNumber, name, date, balance, limit)
                return card
            } catch(e: ClassCastException){ //manejar excepciones
                println("Recuerda ingresar los datos en el formato requerido")
                println("Inténtalo de nuevo")
                return null
            } catch(e: Exception){
                println(e.message)
                return null
            }
        }
    }
}