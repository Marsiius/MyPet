package it.pdm.app

class MySingleton {
    companion object {
        private var instance: MySingleton? = null
        fun getInstance(): MySingleton {
            if (instance == null) {
                instance = MySingleton()
            }
            return instance!!
        }
    }
    var myValue: String = "0"
}
