package stannis.ru.productionsimulator.Enums

import stannis.ru.productionsimulator.R

enum class EnumFactory(val factory: String, val type: Int, val res_type: Items, val prod_type: Items, val image: Int, val price : Int, val res_cap : Int, val productivity_cap:Int) {

    SAWMILL("Лесопилка", 0, Items.WOOD, Items.PLANKS, R.mipmap.lesopilka1_background, 0, 10, 5),
    QUARRY("Камеломня", 1, Items.STONE, Items.GRANITE, R.mipmap.angel_foreground, 800, 15, 10);

    fun getResType(): Items = res_type

    fun getProdType(): Items = prod_type

    fun getName(): String = factory

    fun getFactoryType(): Int = type

    fun getImg(): Int = image


    companion object {
        fun getSize():Int = 2
        fun findById(id: Int): EnumFactory {
            when (id) {
                0 -> return SAWMILL
                1 -> return QUARRY
            }
            return SAWMILL
        }
    }
}