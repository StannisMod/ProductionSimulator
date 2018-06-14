package stannis.ru.productionsimulator.Models

import java.util.*
import stannis.ru.productionsimulator.EnumFactory

class Factory {

    val id : Int
    val type : EnumFactory
    var res : Inventory
    var consumption : Int
    var productivity : Int
    var production : Inventory
    var machine_state : Double

    constructor(id : Int, type : EnumFactory, res : Int, res_cap : Int, consumption : Int, productivity : Int, production : Int, production_cap : Int, machine_state : Double) {
        this.id = id
        this.type = type
        this.res = Inventory("${id}_Res", 1, res_cap)
        this.res.setInventorySlotContents(0, ItemStack(this.type.getResType().getId(), res, res_cap))
        this.consumption = consumption
        this.productivity = productivity
        this.production = Inventory("${id}_Prod", 1, production_cap)
        this.production.setInventorySlotContents(0, ItemStack(this.type.getProdType().getId(), production, production_cap))
        this.machine_state = machine_state
        factories.add(this)
    }

    fun toDetailedString(): String {
        return "Factory[ID: $id, RES: $res, RES_CAPACITY: ${res.getInventoryStackLimit()}, CONS: $consumption, PROD: $productivity, PRODUCTION: $production, PROD_CAPACITY: ${production.getInventoryStackLimit()}, MASHINE_STAT: $machine_state]"
    }

    companion object {
        val factories = ArrayList<Factory>()

        fun getFactoryById(id : Int): Factory? {
            if (id < (factories.size - 1) && id >= 0)
                return factories.get(id)
            return null
        }
    }

    fun runTick() {
        res.decrStackSize(0, consumption)
        production.getInventorySlotContents(0).stackSize += productivity
        machine_state -= Random().nextInt(10) / 100
    }
}
