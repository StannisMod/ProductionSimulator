package stannis.ru.productionsimulator.Models

import android.content.Context
import android.util.Log
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import java.util.*
import stannis.ru.productionsimulator.Enums.EnumFactory
import stannis.ru.productionsimulator.Functions.countProd_Cap
import stannis.ru.productionsimulator.Functions.countProductivity
import stannis.ru.productionsimulator.Functions.countRes_Cap
import stannis.ru.productionsimulator.Functions.round
import kotlin.collections.ArrayList


class Factory {

    val id: Int
    var isBought: Boolean
    var price: Int
    val type: EnumFactory
    var res: Inventory

    var productivity: Int
    var production: Inventory
    var machine_state: Double

    constructor(addToList: Boolean = true, id: Int, isBought: Boolean = false, price: Int = 0, type: EnumFactory, res: Int = 0, res_cap: Int = type.res_cap, productivity: Int = 0, production: Int = 0, production_cap: Int = type.production_cap, machine_state: Double = 10.0) {
        this.id = id
        this.isBought = isBought
        this.price = price
        this.type = type
        this.res = Inventory("${id}_Res", 1, res_cap)
        this.res.setInventorySlotContents(0, ItemStack(this.type.getResType().getId(), res, res_cap))

        this.productivity = productivity
        this.production = Inventory("${id}_Prod", 1, production_cap)
        this.production.setInventorySlotContents(0, ItemStack(this.type.getProdType().getId(), production, production_cap))
        this.machine_state = machine_state
        if (addToList)
            factories.add(this)
    }

    constructor(id: Int) {
        val type = EnumFactory.findById(id)
        this.id = id
        this.isBought = false
        this.price = type.price
        this.type = type
        this.res = Inventory("${id}_Res", 1, type.res_cap)
        this.res.setInventorySlotContents(0, ItemStack(this.type.getResType().getId(), 0, type.res_cap))
        this.productivity = 0
        this.production = Inventory("${id}_Prod", 1, type.production_cap)
        this.production.setInventorySlotContents(0, ItemStack(this.type.getProdType().getId(), 0, type.production_cap))
        this.machine_state = 10.0
        factories.add(this)

    }

    fun toDetailedString(): String {
        return "Factory[ID: $id, RES: $res, isBought = $isBought RES_CAPACITY: ${res.getInventoryStackLimit()}, PROD: $productivity, PRODUCTION: $production, PROD_CAPACITY: ${production.getInventoryStackLimit()}, MASHINE_STAT: $machine_state]"
    }

    companion object {
        var factories = ArrayList<Factory>()
        fun clear() {
            factories = ArrayList()
        }

        fun getFactoryById(id: Int): Factory? {

            if (id < factories.size && id >= 0) {
                return factories.get(id)
            }
            return null
        }

        fun saveFactories(ctx: Context) {
            var i = 0
            for (fac in factories) {
                DatabaseFactory.index = i

                fac.save(ctx)

                i++
            }
            DatabaseFactory.index = 0
        }

        fun load(ctx: Context, id: Int): Factory? = DatabaseFactory.getInstance(ctx).getFactory(id)

    }

    fun runTick(ctx: Context) {
        if (productivity > 0 && res.getInventorySlotContents(0).stackSize > 5) {

            var count = res.getInventorySlotContents(0).stackSize / 5
            Log.d("RunTick", count.toString())
            Log.d("RunTick", (production.getInventorySlotContents(0).maxStackSize - production.getInventorySlotContents(0).stackSize).toString())
            if (production.getInventorySlotContents(0).maxStackSize - production.getInventorySlotContents(0).stackSize < count * productivity) {
                count = (production.getInventorySlotContents(0).maxStackSize - production.getInventorySlotContents(0).stackSize) / productivity
            }
            Log.d("RunTick", count.toString())

            if (production.getInventorySlotContents(0).stackSize == 0) {
                production.setInventorySlotContents(0, ItemStack(this.type.getProdType().itemId, count * productivity, production.getInventorySlotContents(0).maxStackSize))
            } else {
                production.getInventorySlotContents(0).stackSize += count * 5
            }
            res.decrStackSize(0, 5 * count)
            Log.d("RunTick", count.toString())


            machine_state -= round((Random().nextInt(50) + 20).toDouble() / 100.0, 2)
        }
    }

    fun save(ctx: Context) {

        if (DatabaseFactory.getInstance(ctx).getFactory(this.id) == null) {
            DatabaseFactory.getInstance(ctx).addFactory(ctx, this)
        } else {
            Log.d("SizeBefore", "${factories.size}")
            DatabaseFactory.getInstance(ctx).updateFactory(this)
            Log.d("SizeAfter", "${factories.size}")
        }

    }

    fun countParams() {
        countProductivity()
        countProd_Cap()
        countRes_Cap()
    }
}
