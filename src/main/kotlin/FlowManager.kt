import controllers.Cart
import controllers.Menu
import kotlinx.coroutines.runBlocking
import models.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

//Clase para manejar el flujo de la aplicación
class FlowManager {

    //se implementa un companion object
    companion object  {

        //lista con clientes para realizar pruebas
        private val clients: MutableList<Client> = mutableListOf(
            Client("juan23",
                "juan123",
                mutableListOf(
                    CreditCard("4453045671", "Juan Pérez", LocalDate.parse("2023-05-01"), 5000.0, 3000.0),
                    GiftCard("1234567890",2000.0)
                )
            ),
            Client("elena28",
                "gato",
                mutableListOf(CreditCard("4253022698", "Elena Martínez", LocalDate.parse("2024-05-01"), 7000.0, 2000.0)),
            ),
            Client("julio25", "abc123")
        )

        //mapa con las contraseñas de los usuarios
        private val passwords: MutableMap<String, String> = mutableMapOf(
            "juan23" to "juan123",
            "elena28" to "gato",
            "julio25" to "abc123"
        )

        //objetos para el carrito de compras y menú
        private val cart = Cart()
        private val menu = Menu()

        //objeto del cliente actual
        @JvmField
        var currentClient : Client? = null

        //pedir al usuario logearse o crear una cuenta
        fun welcome():Int {

            println("¡Hola, Bienvenido al restaurante XXX!")
            println("¿Qué deseas hacer?")
            println("1. Iniciar sesión")
            println("2. Crear una nueva cuenta")

            //leer la entrada del usuario
            while(true){
                try {
                    val selection = readln().toInt()
                    //regresar la selección si es válida
                    if (selection in (1..2)) {
                        return selection
                    } else {
                        throw Exception("Fuera de rango")
                    }
                } catch (e: ClassCastException) { //manejo de errores
                    println("Debes ingresar un número")
                } catch (e: Exception) {
                    println("Debes ingresar un número entre 1 y 2")
                }
            }
        }

        //login para el usuario
        //regresa un booleano que indica si el login fue exitoso
        fun logIn():Boolean{
            //leer los datos del usuario
            println("Ingresa tus datos:")
            println("Nombre:")
            val name = readln()
            println("Contraseña:")
            val password = readln()
            //verificar que la contraseña sea correcta
            val loginSuccess = passwords[name] == password
            if (loginSuccess) {
                //asignar el objeto de cliente si el login fue exitoso
                currentClient =  clients.find {
                    it.name == name
                }
            }
            return loginSuccess
        }

        //crear cuenta para el usuario
        //regresa un booleano que indica si fue exitoso
        fun createAccount():Boolean{
            //leer el nombre de usuario y verificar que no exista
            println("Ingresa un nombre de usuario:")
            val name = readln()
            if(passwords[name] != null){
                return false
            }
            //leer la contraseña
            println("Ingresa una contraseña:")
            val password = readln()
            //crear el objeto cliente
            currentClient = Client(name, password)
            //añadirlo a la lista de clientes y al mapa de contraseñas
            clients.add(currentClient!!)
            passwords[name] = password
            return true
        }

        //método de inicio de la aplicación
        fun start(){
            //obtener la selección inicial
            val selection = welcome()
            if(selection == 1){
                //si la selección es 1 logearse
                var loginSucess = logIn()
                //reintentar mientras el login no sea exitoso
                while(!loginSucess){
                    println("La contraseña no corresponde al usuario")
                    println("Inténtalo de nuevo")
                    loginSucess = logIn()
                }
            } else {
                //crear cuenta
                var creationSuccess = createAccount()
                //reintentar mientras la creación e cuenta no sea exitosa
                while(!creationSuccess){
                    println("Este nombre de usuario ya existe")
                    println("Inténtalo de nuevo")
                    creationSuccess = createAccount()
                }
            }
            //imprimir un mensaje con el nombre del usuario y la fecha
            println("Bienvenid@ ${currentClient?.name}")
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            val date = Date()
            println("Hoy es " + formatter.format(date))
        }

        //método para ordenar un elemento del menú
        fun orderItem():String{
            var input: String? = null
            try {
                //leer un input del usuario y convertirlo a entero
                input = readln()
                val selection = input.toInt()
                if (selection in 1..menu.getTotalItems()){
                    //si está en un rango válido obtener el elemento
                    val item = menu.getItem(selection-1)
                    //leer la cantidad que se ordena
                    println("¿Cuántos deseas ordenar de este alimento/bebida?")
                    val amount = readln().toInt()
                    if(amount in 1 .. 99){
                        //si la cantidad a ordenar es válida, agregar al carrito
                        cart.addItem(item.name, Pair(item.price, amount))
                    } else {
                        //la cantidad no es válida, lanzar una excepción
                        throw Exception("Solo puedes agregar entre 1 y 99 unidades")
                    }
                } else {
                    //el número no existe en el menú, lanzar una excepción
                    throw Exception("Debes ingresar un número entre 1 y ${menu.getTotalItems()}")
                }
            } catch (e: ClassCastException) { //manejar errores de conversión
                println("Debes ingresar un número")
            } catch (e: Exception) { //mostrar el resto de errores
                println(e.message)
                println("Vuelve a intentarlo")
            } finally {
                return input!! //en cualquier caso regresar la lectura
            }
        }

        //registrar la orden del usuario
        fun registerOrder(): Order {
            //mostrar el menú
            println("Este es nuestro nuevo sistema para ordenar. ¿Qué deseas ordenar?")
            Menu().getMenu()
            var itemSelection : String? = null
            //se realiza al menos una vez
            do {
                //registrar la orden del usuario
                itemSelection =  orderItem()
                //si no se ha terminado la orden preguntar si desea algo más
                if(itemSelection != "LISTO") {
                    println("¿Deseas ordenar algo más? Ingresa LISTO para terminar")
                }
            } while(itemSelection != "LISTO") //parar al recibir LISTO

            //mostrar la orden del usuario
            println("¡Perfecto! A continuación se muestra tu orden:")
            cart.showItems()
            //crear y regresar un objeto de la orden
            return Order(currentClient?.id ?: 0, cart.getTotal(), Date())
        }

        //ir al pago
        fun goToPayment(ord: Order){
            //mostrar las opciones de pago
            println("Procedamos con el pago")
            println("¿Qué medio deseas utilizar?")
            println("1. Dinero en efectivo")
            println("2. Tarjeta de crédito")
            println("3. Tarjeta de regalo")

            while(true){
                try {
                    //leer la selección del usuario
                    val selection = readln().toInt()
                    //de acuerdo a la selección realizar el pago con el método correspondiente al tipo de pago
                    when(selection){
                        1 -> makePayment(ord, ::handleMoney, ::end, ::handlePaymentError)
                        2 -> makePayment(ord, ::handleCreditCard, ::end, ::handlePaymentError)
                        3 -> makePayment(ord, ::handleGiftCard, ::end, ::handlePaymentError)
                        else -> throw Exception("Fuera de rango") //si la selección no está en el rango lanzar una excepción
                    }
                    return
                } catch (e: ClassCastException) { //manejo de errores
                    println("Debes ingresar un número")
                } catch (e: Exception) {
                    println("Debes ingresar un número entre 1 y 3")
                }
            }

        }

        //método para crear una tarjeta de crédito
        fun createCreditCard(): CreditCard?{
            try {
                //leer el número de tarjeta de crédito
                println("Ingresa tu número de tarjeta (16 digitos)")
                val cardNumber:String = readln()
                //lanzar un error si no cumple con la longitud
                if(cardNumber.length != 16){
                    throw Exception("El número de tarjeta no tiene la longitud requerida")
                }
                //leer nombre
                println("Ingresa el nombre registrado en la tarjeta")
                val name:String = readln()
                //leer fecha de vencimiento y covertirla a LocalDate
                println("Ingresa la fecha de vencimiento (yyyy-mm)")
                val readDate:String = readln().trim()
                readDate.plus("-01")
                val date:LocalDate = LocalDate.parse(readDate)
                //leer el balance en la tarjeta
                println("Ingresa el balance en la tarjeta (un número)")
                val balance = readln().toDouble()
                //leer límite de la tarjeta
                println("Ingresa el límite la tarjeta (un número)")
                val limit = readln().toDouble()
                //crear la tarjeta y agregarla al cliente
                val card: CreditCard = CreditCard(cardNumber, name, date, balance, limit)
                currentClient?.addPaymentMethod(card)
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

        //método para seleccionar una tarjeta de crédito
        fun selectCreditCard(): CreditCard {
            //mostrar las tarjetas del usuario
            println("Estas son tus tarjetas registradas:")
            var count = 1
            currentClient?.creditCards?.forEach {
                println("$count -- ${it.cardNumber} - ${it.name}")
                count++
            }
            //pedirle que seleccione una
            println("Selecciona una tarjeta")

            while(true){
                try {
                    //leer la selección del usuario
                    val selection = readln().toInt()
                    if (selection in 1..currentClient?.creditCards?.size!!) {
                        //si la selección es válida regresar la tarjeta de crédito correspondiente
                        return currentClient?.creditCards!![selection-1]
                    } else {
                        throw Exception("Fuera de rango")
                    }
                } catch (e: ClassCastException) { //manejo de excepciones
                    println("Debes ingresar un número")
                } catch (e: Exception) {
                    println("Debes ingresar un número entre 1 y ${currentClient?.creditCards?.size}")
                }
            }
        }

        //método para manejar el pago con una tarjeta de crédito
        fun handleCreditCard(amount:Double):Boolean {
            if(currentClient?.creditCards?.size!! > 0){
                //si el usuario tiene registradas tarjetas de crédito pedir que selecciona una
                val card: CreditCard = selectCreditCard()
                return card.charge(amount)
            } else {
                //si no crear una nueva tarjeta
                var card: CreditCard? = createCreditCard()
                //reintentar hasta que funcone el proceso
                while(card == null){
                    card = createCreditCard()
                }
                return card.charge(amount)
            }
        }

        //método para manejar el pago con una tarjeta de regalo
        fun createGiftCard(): GiftCard?{
            try {
                //leer el código de la tarjeta
                println("Ingresa el código de tu tarjeta de regalo (10 digitos)")
                val code:String = readln()
                //lanzar una excepción si no tiene el tamaño requerido
                if(code.length != 10){
                    throw Exception("El número de tarjeta no tiene la longitud requerida")
                }
                //leer el balance de la tarjeta
                println("Ingresa el balance en la tarjeta (un número)")
                val balance = readln().toDouble()
                //crear la tarjeta y agregarla para el cliente
                val card = GiftCard(code, balance)
                currentClient?.addPaymentMethod(card)
                return card
            } catch(e: ClassCastException){ //manejo de excepciones
                println("Recuerda ingresar los datos en el formato requerido")
                println("Inténtalo de nuevo")
                return null
            } catch(e: Exception){
                println(e.message)
                return null
            }
        }

        //método para seleccionar una tarjeta de regalo
        fun selectGiftCard(): GiftCard {
            //mostrar las tarjetas de crédito disponibles para el usuario
            println("Estas son tus tarjetas de regalo registradas:")
            var count = 1
            currentClient?.giftCards?.forEach {
                println("$count -- ${it.code}")
                count++
            }
            println("Selecciona una tarjeta")

            while(true){
                try {
                    //leer la selección de una tarjeta
                    val selection = readln().toInt()
                    if (selection in 1..currentClient?.giftCards?.size!!) {
                        //si la selección es válida regresar la tarjeta
                        return currentClient?.giftCards!![selection-1]
                    } else {
                        throw Exception("Fuera de rango")
                    }
                } catch (e: ClassCastException) { //manejo de excepciones
                    println("Debes ingresar un número")
                } catch (e: Exception) {
                    println("Debes ingresar un número entre 1 y ${currentClient?.giftCards?.size}")
                }
            }
        }

        //método para manejar el pago con una tarjeta de regalo
        fun handleGiftCard(amount: Double):Boolean{
            if(currentClient?.giftCards?.size!! > 0){
                //si se tienen tarjetas registradas pedir que se seleccione una
                val card: GiftCard = selectGiftCard()
                //cargar a la tarjeta el pago
                return card.charge(amount)
            } else {
                //si no crear una nueva
                var card: GiftCard? = createGiftCard()
                //se repite el proceso hasta que sea exitoso
                while(card == null){
                    card = createGiftCard()
                }
                //cargar a la tarjeta el pago
                return card.charge(amount)
            }
        }

        //método para manejar el pago con dinero
        fun handleMoney(amount: Double):Boolean{
            return Money().charge(amount)
        }

        //realizar el pago
        //es una función de orden mayor
        //recibe una orden, un handler para el pago y métodos en caso de éxito o de error
        fun makePayment(
            ord: Order,
            paymentHandler: (amount:Double)->Boolean,
            onSuccess: ()->Unit,
            onError:()->Unit) = runBlocking {
            val orderProcess = launch{
                cart.deliverOrder()
            }
            var paymentProcess= launch{
                if(paymentHandler(ord.total)){
                    delay(500L)
                    onSuccess()
                } else {
                    println("Procesando pago...")
                    orderProcess.cancel()
                    onError()
                    goToPayment(ord)
                }

            }
            paymentProcess.join()
        }

        //método para terminar el proceso
        fun end(){
            //se muestra que la orden fue completa
            println("Tu orden ha sido completada y en un momento será entregada")
            println("¡Qué la difrutes!")
        }

        //método para manejar errores de pago
        fun handlePaymentError(ord: Order){
            println("Ocurrió un error con el pago")
            println("Intenta de nuevo")
            //regresar a la selección de métodos de pago
            goToPayment(ord)
        }
    }
}
