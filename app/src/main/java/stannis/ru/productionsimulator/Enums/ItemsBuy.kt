package stannis.ru.productionsimulator.Enums

import android.util.Log
import stannis.ru.productionsimulator.R

enum class ItemsBuy(val itemId: Int, val label: String, val image: Int, val price: Int) {

    NULL(0, " ", R.mipmap.angel_background, 0),
    WOOD(1, "Древесина", R.mipmap.wood_foreground, 3),
    PLANKS(2, "Пиломатериалы", R.mipmap.planks_foreground, 10),
    STONE(3, "Камень", R.mipmap.stone_foreground, 4),
    OIL(4, "Топливо", R.mipmap.oil_foreground, 5),
    SHOVEL(5, "Лопата", R.mipmap.shovel_foreground, 15),
    PICKAXE(6, "Кирка", R.mipmap.pickaxe_foreground, 20),
    STEEL(7, "Сталь", R.mipmap.metal_foreground, 7),
    IRON(8, "Железо", R.mipmap.metal_foreground, 6),
    TRACTOR(9, "Трактор", R.mipmap.tractor_foreground, 150),
    CAMP(10, "Лагерь", R.mipmap.camp_foreground, 120),
    GRANITE(11, "Лопата", R.mipmap.oil_foreground, 20);


    fun getName(): String = label

    fun getId(): Int = itemId

    fun getItemImage(): Int = image

    fun getItemPrice():Int = price

    companion object {
        fun findById(id: Int): ItemsBuy {
            when (id) {
                0 -> return NULL
                1 -> return WOOD
                2 -> return PLANKS
                3 -> return STONE
                4 -> return OIL
                5 -> return SHOVEL
                6 -> return PICKAXE
                7 -> return STEEL
                8 -> return IRON
                9 -> return TRACTOR
                10 -> return CAMP
                11->return GRANITE
                else -> Log.e("ITEMS", "Item with ID $id requested, but not exists")
            }
            return WOOD
        }
    }
}