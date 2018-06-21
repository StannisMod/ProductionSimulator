package stannis.ru.productionsimulator.Enums

import stannis.ru.productionsimulator.R

enum class EnumFactory(val factory: String, val type: Int, val res_type: Items, val prod_type: Items, val image: Int, val price: Int, val res_cap: Int, val productivity_cap: Int, val coef: Int, val workerkLimit:Int) {

    SAWMILL("Лесопилка", 0, Items.WOOD, Items.PLANKS, R.mipmap.lesopilka1_background, 0, 10, 6, -800, 8),
    QUARRY("Каменоломня", 1, Items.STONE, Items.GRANITE, R.mipmap.brickwork_foreground, 2000, 15, 12, -1600, 15),
    FOUNDRY("Литейный завод", 2, Items.ORE, Items.IRON, R.mipmap.cardboardwork_foreground, 10000, 30, 30, -5000, 30);

    fun getResType(): Items = res_type

    fun getProdType(): Items = prod_type

    fun getName(): String = factory

    fun getFactoryType(): Int = type

    fun getImg(): Int = image


    companion object {
        fun getSize(): Int = 3
        fun findById(id: Int): EnumFactory {
            when (id) {
                0 -> return SAWMILL
                1 -> return QUARRY
                2 -> return FOUNDRY
            }
            return SAWMILL
        }
    }
}