package stannis.ru.productionsimulator.Enums

import stannis.ru.productionsimulator.R

enum class EnumFactory(val factory: String, val type: Int, val res_type: Items, val prod_type: Items, val image: Int) {

    SAWMILL("Лесопилка", 0, Items.WOOD, Items.PLANKS, R.mipmap.lesopilka1_background),
    QUARRY("Камеломня", 1, Items.STONE, Items.GRANITE, R.mipmap.angel_foreground);

    fun getResType(): Items = res_type

    fun getProdType(): Items = prod_type

    fun getName(): String = factory

    fun getFactoryType(): Int = type

    fun getImg(): Int = image

    companion object {
        fun findById(id: Int): EnumFactory {
            when (id) {
                0 -> return SAWMILL
                1 -> return QUARRY
            }
            return SAWMILL
        }
    }
}