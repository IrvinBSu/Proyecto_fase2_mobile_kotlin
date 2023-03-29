data class Client (var name: String, var password: String){

    private var id:Int = (Math.random() * (1000 + 1)).toInt()

    init {
        println("Usuario creado")
    }

    fun updateProfile(name: String, password: String){
        this.name = name
        this.password = password
    }
}