package stannis.ru.productionsimulator.Models

class Factory(val id : Int, val type : Int, val res_type : Int, val res : Int, val res_cap : Int, val consumption : Int, val productivity : Int, val production : Int, val production_cap : Int, val machine_stat : Int) {

    fun toDetailedString(): String {
        return "Factory[ID: $id, RES: $res, RES_CAPACITY: $res_cap, CONS: $consumption, PROD: $productivity, PRODUCTION: $production, PROD_CAPACITY: $production_cap, MASHINE_STAT: $machine_stat]"
    }

    companion object {
        val factories = ArrayList<Factory>()

        fun getFactoryById(id : Int): Factory = factories.get(id)
    }

    // TODO Write tick loop

    fun runTick() {

    }
}
