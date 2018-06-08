package stannis.ru.productionsimulator.Models

import java.util.*
import stannis.ru.productionsimulator.EnumFactory

class Factory {

    val id : Int
    val type : EnumFactory
    var res : Inventory
    var consumption : Int
    var productivity : Int
    var production : Int
    var production_cap : Int
    var machine_state : Double

    constructor(id : Int, type : EnumFactory, res : Int, res_cap : Int, consumption : Int, productivity : Int, production : Int, production_cap : Int, machine_state : Double) {
        this.id = id
        this.type = type
        this.res = Inventory("${id}_Inventory", 1, res_cap)
        this.consumption = consumption
        this.productivity = productivity
        this.production = production
        this.production_cap = production_cap
        this.machine_state = machine_state
    }

    fun toDetailedString(): String {
        return "Factory[ID: $id, RES: $res, RES_CAPACITY: ${res.getInventoryStackLimit()}, CONS: $consumption, PROD: $productivity, PRODUCTION: $production, PROD_CAPACITY: $production_cap, MASHINE_STAT: $machine_state]"
    }

    companion object {
        val factories = ArrayList<Factory>()

        fun getFactoryById(id : Int): Factory = factories.get(id)
    }

    fun runTick() {
        res.decrStackSize(0, consumption)
        production += productivity
        machine_state -= Random().nextInt(10) / 100
    }
}
