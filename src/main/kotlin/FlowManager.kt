import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class FlowManager {
    companion object  {

        private val passwords: MutableMap<String, String> = mutableMapOf(
            "juan23" to "juan123",
            "elena28" to "gato",
            "julio25" to "abc123"
        )
        private val clients: MutableList<Client> = mutableListOf(
            Client("juan23",
                "juan123",
                mutableListOf(CreditCard("4453045671", "Juan Pérez", LocalDate.parse("2023-05-01"), 5000.0, 3000.0)),
                mutableListOf(GiftCard("1234567890",2000.0))
            ),
            Client("elena28",
                "gato",
                mutableListOf(CreditCard("4253022698", "Elena Martínez", LocalDate.parse("2024-05-01"), 7000.0, 2000.0)),
            ),
            Client("julio25", "abc123")
        )

        private val cart = Cart()
        private val menu = Menu()

        @JvmField
        var currentClient : Client? = null

        fun welcome():Int {

            println("¡Hola, Bienvenido al restaurante XXX!")
            println("¿Qué deseas hacer?")
            println("1. Iniciar sesión")
            println("2. Crear una nueva cuenta")

            while(true){
                try {
                    val selection = readln().toInt()
                    if (selection in (1..2)) {
                        return selection
                    } else {
                        throw Exception("Fuera de rango")
                    }
                } catch (e: ClassCastException) {
                    println("Debes ingresar un número")
                } catch (e: Exception) {
                    println("Debes ingresar un número entre 1 y 2")
                }
            }
        }

        fun logIn():Boolean{
            println("Ingresa tus datos:")
            println("Nombre:")
            val name = readln()
            println("Contraseña:")
            val password = readln()
            val loginSuccess = passwords[name] == password
            if (loginSuccess) {
                currentClient =  clients.find {
                    it.name == name
                }
            }
            return loginSuccess
        }

        fun createAccount():Boolean{
            println("Ingresa un nombre de usuario:")
            val name = readln()
            if(passwords[name] != null){
                return false
            }
            println("Ingresa una contraseña:")
            val password = readln()
            currentClient = Client(name, password)
            clients.add(currentClient!!)
            passwords[name] = password
            return true
        }

        fun start(){
            val selection = welcome()
            if(selection == 1){
                var loginSucess = logIn()
                while(!loginSucess){
                    println("La contraseña no corresponde al usuario")
                    println("Inténtalo de nuevo")
                    loginSucess = FlowManager.logIn()
                }
            } else {
                var creationSuccess = createAccount()
                while(!creationSuccess){
                    println("Este nombre de usuario ya exite")
                    println("Inténtalo de nuevo")
                    creationSuccess = createAccount()
                }
            }
            println("Bienvenid@ ${currentClient?.name}")
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            val date = Date()
            println("Hoy es " + formatter.format(date))
        }

        fun orderItem():String{
            var input: String? = null
            try {
                input = readln()
                val selection = input.toInt()
                if (selection in 1..menu.getTotalItems()){
                    val item = menu.getItem(selection-1)
                    println("¿Cuántos deseas ordenar de este alimento/bebida?")
                    val amount = readln().toInt()
                    if(amount in 1 .. 99){
                        cart.addItem(item.name, item.price, amount)
                    } else {
                        throw Exception("Solo pueedes agregar entre 1 y 99 unidades")
                    }
                } else {
                    throw Exception("Debes ingresar un número entre 1 y ${menu.getTotalItems()}")
                }
            } catch (e: ClassCastException) {
                println("Debes ingresar un número")
            } catch (e: Exception) {
                println(e.message)
                println("Vuelve a intentarlo")
            } finally {
                return input!!
            }
        }

        fun registerOrder():Order{
            println("Este es nuestro nuevo sistema para ordenar. ¿Qué deseas ordenar?")
            Menu().getMenu()
            var itemSelection : String? = null

            while(itemSelection != "LISTO") {
                itemSelection =  orderItem()
                if(itemSelection != "LISTO") {
                    println("¿Deseas ordenar algo más?")
                }
            }

            println("¡Perfecto! A continuación se muestra tu orden:")
            cart.showItems()
            return Order(currentClient?.getId() ?: 0, cart.getTotal(), Date())
        }

        fun goToPayment(ord:Order){
            println("Procedamos con el pago")
            println("¿Qué medio deseas utilizar?")
            println("1. Dinero en efectivo")
            println("2. Tarjeta de crédito")
            println("3. Tarjeta de regalo")

            while(true){
                try {
                    val selection = readln().toInt()
                    when(selection){
                        1 -> makePayment(ord, ::handleMoney, ::end, ::handlePaymentError)
                        2 -> makePayment(ord, ::handleCreditCard, ::end, ::handlePaymentError)
                        3 -> makePayment(ord, ::handleGiftCard, ::end, ::handlePaymentError)
                        else -> throw Exception("Fuera de rango")
                    }
                    return
                } catch (e: ClassCastException) {
                    println("Debes ingresar un número")
                } catch (e: Exception) {
                    println("Debes ingresar un número entre 1 y 3")
                }
            }

        }

        fun createCreditCard():CreditCard?{
            try {
                println("Ingresa tu número de tarjeta (16 digitos)")
                val cardNumber:String = readln()
                if(cardNumber.length != 16){
                    throw Exception("El número de tarjeta no tiene la longitud requerida")
                }
                println("Ingresa el nombre registrado en la tarjeta")
                val name:String = readln()
                println("Ingresa la fecha de vencimiento (yyyy-mm)")
                val readDate:String = readln().trim()
                readDate.plus("-01")
                val date:LocalDate = LocalDate.parse(readDate)
                println("Ingresa el balance en la tarjeta (un número)")
                val balance = readln().toDouble()
                println("Ingresa el límite la tarjeta (un número)")
                val limit = readln().toDouble()
                val card: CreditCard = CreditCard(cardNumber, name, date, balance, limit)
                currentClient?.addCreditCard(card)
                return card
            } catch(e: ClassCastException){
                println("Recuerda ingresar los datos en el formato requerido")
                println("Inténtalo de nuevo")
                return null
            } catch(e: Exception){
                println(e.message)
                return null
            }
        }

        fun selectCreditCard():CreditCard{
            println("Estas son tus tarjetas registradas:")
            var count = 1
            currentClient?.creditCards?.forEach {
                println("$count -- ${it.cardNumber} - ${it.name}")
                count++
            }
            println("Selecciona una tarjeta")

            while(true){
                try {
                    val selection = readln().toInt()
                    if (selection in 1..currentClient?.creditCards?.size!!) {
                        return currentClient?.creditCards!![selection-1]
                    } else {
                        throw Exception("Fuera de rango")
                    }
                } catch (e: ClassCastException) {
                    println("Debes ingresar un número")
                } catch (e: Exception) {
                    println("Debes ingresar un número entre 1 y ${currentClient?.creditCards?.size}")
                }
            }
        }

        fun handleCreditCard(amount:Double):Boolean {
            if(currentClient?.creditCards?.size!! > 0){
                val card:CreditCard = selectCreditCard()
                return card.charge(amount)
            } else {
                var card:CreditCard? = createCreditCard()
                while(card == null){
                    card = createCreditCard()
                }
                return card.charge(amount)
            }
        }

        fun createGiftCard():GiftCard?{
            try {
                println("Ingresa el código de tu tarjeta de regalo (10 digitos)")
                val code:String = readln()
                if(code.length != 10){
                    throw Exception("El número de tarjeta no tiene la longitud requerida")
                }
                println("Ingresa el balance en la tarjeta (un número)")
                val balance = readln().toDouble()
                val card: GiftCard = GiftCard(code, balance)
                currentClient?.addGiftCard(card)
                return card
            } catch(e: ClassCastException){
                println("Recuerda ingresar los datos en el formato requerido")
                println("Inténtalo de nuevo")
                return null
            } catch(e: Exception){
                println(e.message)
                return null
            }
        }

        fun selectGiftCard():GiftCard{
            println("Estas son tus tarjetas de regalo registradas:")
            var count = 1
            currentClient?.giftCards?.forEach {
                println("$count -- ${it.code}")
                count++
            }
            println("Selecciona una tarjeta")

            while(true){
                try {
                    val selection = readln().toInt()
                    if (selection in 1..currentClient?.giftCards?.size!!) {
                        return currentClient?.giftCards!![selection-1]
                    } else {
                        throw Exception("Fuera de rango")
                    }
                } catch (e: ClassCastException) {
                    println("Debes ingresar un número")
                } catch (e: Exception) {
                    println("Debes ingresar un número entre 1 y ${currentClient?.giftCards?.size}")
                }
            }
        }

        fun handleGiftCard(amount: Double):Boolean{
            if(currentClient?.giftCards?.size!! > 0){
                val card:GiftCard = selectGiftCard()
                return card.charge(amount)
            } else {
                var card:GiftCard? = createGiftCard()
                while(card == null){
                    card = createGiftCard()
                }
                return card.charge(amount)
            }
        }

        fun handleMoney(amount: Double):Boolean{
            return Money().charge(amount)
        }

        fun makePayment(
            ord: Order,
            paymentHandler: (amount:Double)->Boolean,
            onSuccess: ()->Unit,
            onError:()->Unit) = runBlocking {
            if(paymentHandler(ord.total)){
                onSuccess()
            } else {
                onError()
                goToPayment(ord)
            }
        }

        fun end(){
            println("Tu orden ha sido completada y en un momento será entregada")
            println("¡Qué la difrutes!")
        }

        fun handlePaymentError(){
            println("Ocurrió un error con el pago")
            println("Intenta de nuevo")
        }
    }
}