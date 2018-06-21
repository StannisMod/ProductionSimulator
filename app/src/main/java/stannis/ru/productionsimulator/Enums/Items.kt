package stannis.ru.productionsimulator.Enums

import android.content.Context
import android.util.Log
import stannis.ru.productionsimulator.Models.Player
import stannis.ru.productionsimulator.R

enum class Items(val itemId: Int, val label: String, val image: Int, val price: Int) {

    NULL(0, " ", R.mipmap.angel_background, 0),
    WOOD(1, "Древесина", R.mipmap.wood_foreground, 1),
    STONE(2, "Камень", R.mipmap.stone_foreground, 6),
    ORE(3, "Руда", R.mipmap.ore_foreground, 12),
    PLANKS(4, "Пиломатериалы", R.mipmap.planks_foreground, 10),
    GRANITE(5, "Гранит", R.mipmap.granite_foreground, 20),
    IRON(6, "Железо", R.mipmap.metal_foreground, 40),
    SHOVEL(7, "Лопата", R.mipmap.shovel_foreground, 50),
    PICK_AXE(8, "Кирка", R.mipmap.pickaxe_foreground, 60),
    AXE(9, "Топор", R.mipmap.axe_foreground, 70),
    TRACTOR(10, "Трактор", R.mipmap.tractor_foreground, 150),
    TOOL_KIT(11, "Набор инструментов", R.mipmap.tool_kit_foreground, 100);

    fun getName(): String = label

    fun getId(): Int = itemId

    fun getItemImage(): Int = image

    companion object {
        fun getNumOfResCap(): Pair<Int, Int> = Pair(10, 10)
        fun getNumOfProdCap(): Pair<Int, Int> = Pair(7, 9)
        fun getNumOfRepair(): Int = 11
        fun generateSellPrice(id: Int, ctx: Context): Int {
            val add = (Player.getInstance(ctx).reputation - 42) / 10
            return findById(id).price + add
        }

        fun findById(id: Int): Items {
            when (id) {
                0 -> return NULL
                1 -> return WOOD
                2 -> return STONE
                3 -> return ORE
                4 -> return PLANKS
                5 -> return GRANITE
                6 -> return IRON
                7 -> return SHOVEL
                8 -> return PICK_AXE
                9 -> return AXE
                10 -> return TRACTOR
                11 -> return TOOL_KIT
                else -> Log.e("ITEMS", "Item with ID $id requested, but not exists")
            }
            return WOOD
        }

        fun getSize(): Int = 12
    }
}