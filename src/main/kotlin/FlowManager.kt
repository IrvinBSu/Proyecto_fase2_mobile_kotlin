import java.text.SimpleDateFormat
import java.util.*

class FlowManager {
    companion object  {

        private val passwords: MutableMap<String, String> = mutableMapOf(
            "juan23" to "juan123",
            "elena28" to "gato",
            "julio25" to "abc123"
        )
        private val clients: MutableList<Client> = mutableListOf(
            Client("juan23", "juan123"),
            Client("elena28", "gato"),
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
                        cart.addItem(item.name, item.price)
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

        fun registerOrder(){
            println("Este es nuestro nuevo sistema para ordenar. ¿Qué deseas ordenar?")
            Menu().getMenu()
            var itemSelection : String? = null
            while(itemSelection != "LISTO") {
                itemSelection =  orderItem()
                if(itemSelection != "LISTO") {
                    println("¿Deseas ordenar algo más?")
                }
            }
        }
    }
}