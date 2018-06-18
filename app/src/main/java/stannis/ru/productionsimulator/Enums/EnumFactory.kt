package stannis.ru.productionsimulator.Enums

enum class EnumFactory(val factory : String, val type : Int, val res_type : Items, val prod_type : Items) {

    SAWMILL("Лесопилка", 0, Items.WOOD, Items.PLANKS);

    fun getResType() : Items = res_type

    fun getProdType() : Items = prod_type

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