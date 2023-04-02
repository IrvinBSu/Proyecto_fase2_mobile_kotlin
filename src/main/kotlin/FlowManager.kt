import controllers.Cart
import controllers.Menu
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

        //objeto del cliente actual
        @JvmField
        var currentClient : Client? = null

        //pedir al usuario loguearse o crear una cuenta
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
        private fun logIn():Boolean{
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
        private fun createAccount():Boolean{
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
            println("Usuario creado")
            return true
        }

        //método de inicio de la aplicación
        fun start(selection: Int){
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
        }

        //imprimir un mensaje con el nombre del usuario y la fecha
        fun greet() = runBlocking{
            println("Bienvenid@ ${currentClient?.name}")
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            val date = Date()
            println("Hoy es " + formatter.format(date))
            //mostrar el menú
            println("Este es nuestro nuevo sistema para ordenar.")
            GlobalScope.launch{
                Menu.getMenu()
            }
        }


        //seleccionar la cantidad del alimento
        private fun chooseAmountItems():Int{
            //leer la cantidad que se ordena
            println("¿Cuántos deseas ordenar de este alimento/bebida?")
            val amount = readln().toInt()
            if(amount in 1 .. 99){
                //si la cantidad a ordenar es válida, agregar al carrito
                return amount
            } else {
                //la cantidad no es válida, lanzar una excepción
                throw Exception("Solo puedes agregar entre 1 y 99 unidades")
            }
        }


        //método para ordenar un elemento del menú
        private fun orderItem():Boolean{
            try {
                //leer un input del usuario
                val input = readln().trim()
                val selection = input.toInt()
                if (selection in 1..Menu.getTotalItems()){
                    //si está en un rango válido obtener el elemento
                    val item = Menu.getItem(selection-1)
                    val amount = chooseAmountItems()
                    cart.addItem(item.name, Pair(item.price, amount))
                    return true
                } else {
                    //el número no existe en el menú, lanzar una excepción
                    throw Exception("Debes ingresar un número entre 1 y ${Menu.getTotalItems()}")
                }
            } catch (e: ClassCastException) { //manejar errores de conversión
                println("Debes ingresar un número")
                return  false
            } catch (e: Exception) { //mostrar el resto de errores
                println(e.message)
                println("Vuelve a intentarlo")
                return false
            }
        }

        //tomar la orden del usuario
        fun takeOrder() {
            //se realiza al menos una vez
            do {
                //registrar la orden del usuario
                println("¿Qué deseas ordenar? Ingresa el número del alimento en el menú")
                var itemSelection:Boolean = orderItem()
                while(!itemSelection){
                    itemSelection = orderItem()
                }
                //Preguntar si desea algo más
                println("¿Deseas ordenar algo más? y/n")
                var input:String = readln()
                while(input != "y" && input != "n"){
                    println("Selecciona 'y' o 'n'")
                    input = readln()
                }
            } while(input != "n") //parar al recibir n
        }

        //registrar la orden del usuario y crear el objeto Order
        fun registerOrder():Order{
            //mostrar la orden del usuario
            println("¡Perfecto! A continuación se muestra tu orden:")
            cart.showItems()
            //crear y regresar un objeto de la orden
            val ord:Order = Order(currentClient?.id ?: 0, cart.getTotal(), Date())
            cart.items.forEach{
                ord.addItem(it.key, it.value.second)
            }
            return ord
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
                    val input = readln()
                    val selection = input.toInt()
                    //de acuerdo a la selección realizar el pago con el método correspondiente al tipo de pago
                    when(selection) {
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

        //método para manejar el pago con una tarjeta de crédito
        private fun handleCreditCard(amount:Double):Boolean {
            if(currentClient?.creditCards?.size!! > 0){
                //si el usuario tiene registradas tarjetas de crédito pedir que selecciona una
                currentClient?.showCreditCards()
                val card: CreditCard = currentClient?.selectCreditCard()!!
                return card.charge(amount)
            } else {
                //si no crear una nueva tarjeta
                var card: CreditCard? = CreditCard.createCreditCard()
                //reintentar hasta que funcone el proceso
                while(card == null){
                    card = CreditCard.createCreditCard()
                }
                currentClient?.addPaymentMethod(card)
                return card.charge(amount)
            }
        }

        //método para manejar el pago con una tarjeta de regalo
        private fun handleGiftCard(amount: Double):Boolean{
            if(currentClient?.giftCards?.size!! > 0){
                //si se tienen tarjetas registradas pedir que se seleccione una
                currentClient?.showGiftCards()
                val card: GiftCard = currentClient?.selectGiftCard()!!
                //cargar a la tarjeta el pago
                return card.charge(amount)
            } else {
                //si no crear una nueva
                var card: GiftCard? = GiftCard.createGiftCard()
                //se repite el proceso hasta que sea exitoso
                while(card == null){
                    card = GiftCard.createGiftCard()
                }
                currentClient?.addPaymentMethod(card)
                //cargar a la tarjeta el pago
                return card.charge(amount)
            }
        }

        //método para manejar el pago con dinero
        private fun handleMoney(amount: Double):Boolean{
            return Money().charge(amount)
        }

        //realizar el pago
        //es una función de orden mayor
        //recibe una orden, un handler para el pago y métodos en caso de éxito o de error
        private fun makePayment(
            ord: Order,
            paymentHandler: (amount:Double)->Boolean,
            onSuccess: (Order) ->Unit,
            onError: (Order) -> Unit) = runBlocking {

            println("Procesando pago...")
            //lanzar validación del pago
            val paymentProcess= launch{
                delay(1000L)
                if(paymentHandler(ord.total)){
                    //si el pago es exitoso ir al método de éxito
                    ord.deliverOrder()
                    onSuccess(ord)
                } else {
                    //si el pago falla cancelar proceso de la orden ir al método de error
                    onError(ord)
                }

            }
            paymentProcess.join()
        }

        //método para terminar el proceso
        private fun end(ord:Order){
            //se muestra que la orden fue completa
            currentClient?.addOrder(ord);
            println("Tu orden ha sido completada y en un momento será entregada")
            println("¡Qué la difrutes!")
        }

        //método para manejar errores de pago
        private fun handlePaymentError(ord:Order){
            println("Ocurrió un error con el pago")
            println("Intenta de nuevo")
            //regresar a las opciones de pago
            goToPayment(ord)
        }
    }
}
