package stannis.ru.productionsimulator.Models

import android.content.Context
import android.util.Log
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import java.util.*
import stannis.ru.productionsimulator.Enums.EnumFactory

class Factory {

    val id: Int
    val type: EnumFactory
    var res: Inventory
    var consumption: Int
    var productivity: Int
    var production: Inventory
    var machine_state: Double

    constructor(addToList: Boolean = true, id: Int, type: EnumFactory, res: Int = 5, res_cap: Int = 10, consumption: Int = 2, productivity: Int = 1, production: Int = 2, production_cap: Int = 5, machine_state: Double = 10.0) {
        this.id = id
        this.type = type
        this.res = Inventory("${id}_Res", 1, res_cap)
        this.res.setInventorySlotContents(0, ItemStack(this.type.getResType().getId(), res, res_cap))
        this.consumption = consumption
        this.productivity = productivity
        this.production = Inventory("${id}_Prod", 1, production_cap)
        this.production.setInventorySlotContents(0, ItemStack(this.type.getProdType().getId(), production, production_cap))
        this.machine_state = machine_state
        if (addToList)
            factories.add(this)
    }

    fun toDetailedString(): String {
        return "Factory[ID: $id, RES: $res, RES_CAPACITY: ${res.getInventoryStackLimit()}, CONS: $consumption, PROD: $productivity, PRODUCTION: $production, PROD_CAPACITY: ${production.getInventoryStackLimit()}, MASHINE_STAT: $machine_state]"
    }

    companion object {
        val factories = ArrayList<Factory>()

        fun getFactoryById(id: Int): Factory? {

            if (id < factories.size && id >= 0) {
                return factories.get(id)
            }
            return null
        }

        fun saveFactories(ctx: Context) {
            for (fac in factories) {

                fac.save(ctx)

            }
        }

        fun load(ctx: Context, id: Int): Factory? = DatabaseFactory.getInstance(ctx).getFactory(id)
    }

    fun runTick() {
        Log.d("EndDay", "runTick!!")
        res.decrStackSize(0, consumption)
        production.getInventorySlotContents(0).stackSize += productivity
        machine_state -= Random().nextInt(10).toDouble() / 100.0
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
}
