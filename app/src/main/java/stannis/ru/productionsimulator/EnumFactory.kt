package stannis.ru.productionsimulator

enum class EnumFactory(val factory : String, val type : Int, val res_type : Int) {

    SAWMILL("Лесопилка", 0, 0);

    fun getResType() : Int = res_type

    fun getName() : String = factory

    fun getFactoryType() : Int = type

    companion object {
        fun findById(id : Int) : EnumFactory {
            if (id == 0)
                return SAWMILL

            return SAWMILL
        }
    }
}