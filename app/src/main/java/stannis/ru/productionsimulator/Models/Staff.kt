package stannis.ru.productionsimulator.Models

class Staff(var name: String, var age:Int, var prof: String, var quality:Int, var nation:String, var salary:Int ){
    override fun toString(): String {
        return "${name}, ${prof}, ${age}, "
    }


}
