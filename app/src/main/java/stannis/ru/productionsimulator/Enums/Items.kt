package stannis.ru.productionsimulator.Enums

import android.util.Log
import stannis.ru.productionsimulator.R

enum class Items(val itemId : Int, val label : String, val image : Int) {

    NULL(0, " ", R.mipmap.angel_background),
    WOOD(1, "Древесина", R.mipmap.wood_foreground),
    PLANKS(2, "Пиломатериалы", R.mipmap.planks_foreground),
    STONE(3, "Камень", R.mipmap.stone_foreground),
    OIL(4, "Топливо", R.mipmap.oil_foreground),
    SHOVEL(5, "Лопата", R.mipmap.shovel_foreground),
    PICKAXE(6, "Кирка", R.mipmap.pickaxe_foreground),
    STEEL(7, "Сталь", R.mipmap.metal_foreground),
    IRON(8, "Железо", R.mipmap.metal_foreground),
    TRACTOR(9, "Трактор", R.mipmap.tractor_foreground),
    CAMP(10, "Лагерь", R.mipmap.camp_foreground),
    OIL1(11, "Лопата", R.mipmap.oil_foreground);

    fun getName() : String = label

    fun getId() : Int = itemId

    fun getItemImage() : Int = image

    companion object {
        fun findById(id : Int) : Items {
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
                else -> Log.e("ITEMS", "Item with ID $id requested, but not exists")
            }
            return WOOD
        }
    }
}