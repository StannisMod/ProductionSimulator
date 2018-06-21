package stannis.ru.productionsimulator.Functions

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase
import stannis.ru.productionsimulator.Enums.EnumFactory
import stannis.ru.productionsimulator.Enums.Items
import stannis.ru.productionsimulator.Enums.Nations
import stannis.ru.productionsimulator.Enums.Profs
import stannis.ru.productionsimulator.Models.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.roundToInt


fun round(a: Double, radix: Int): Double {
    var b = a
    b *= Math.pow(10.0, radix.toDouble())
    b = b.roundToInt().toDouble()
    b /= Math.pow(10.0, radix.toDouble())
    return b

}

fun saveAll(ctx: Context) {
    Factory.saveFactories(ctx)
    Player.save(ctx)
    DataTime.save(ctx)
    MoneyForDay.save(ctx)
    Inventory.saveInventories(ctx)

}

fun loadAll(ctx: Context) {
    for (i in 0 until EnumFactory.getSize()) {
        DatabaseFactory.index = i
        val fac = Factory.load(ctx, i)
        if (fac != null) {
            Factory.factories.add(fac)
        }
        Inventory.inventories.add(HashMap())
        Inventory.inventories[i].put("buy", Inventory.load(ctx, "buy"))
        Inventory.inventories[i].put("sell", Inventory.load(ctx, "sell"))
        Inventory.inventories[i].put(Inventory.TAG, Inventory.load(ctx, Inventory.TAG))
    }
    DatabaseFactory.index = 0

}

fun countReputation(ctx: Context, nalog: Int) {
    val player = Player.getInstance(ctx)
    val trueNalog = player.nalog
    val dif = nalog - trueNalog
    if (player.reputation == 0) {
        player.reputation = (10 * (dif.toDouble() / trueNalog.toDouble())).toInt()
    }
    player.reputation = player.reputation + (player.reputation.toDouble() * (dif.toDouble() / trueNalog.toDouble())).toInt()
}

fun generateWorker(ctx: Context) {
    val name = PlayerStatsDatabase.getInstance(ctx).getName()
    val age = Random().nextInt(40) + 20
    var p = Random().nextInt(20)
    var prof: Profs? = null
    when {
        p < 12 -> prof = Profs.GENERAL_WORKER
        p in 12..17 -> prof = Profs.FOREMAN
        p >= 18 -> prof = Profs.MANAGER
    }
    var nation: Nations? = null
    p = Random().nextInt(20)
    when {
        p < 10 -> nation = Nations.RUS
        p in 10..14 -> nation = Nations.BEL
        p in 15..17 -> nation = Nations.UKR
        p >= 18 -> nation = Nations.KZH
    }
    if (nation != null && prof != null) {
        var spec = prof.prof
        var nationality = nation.nation
        var quality = nation.quality + (Random().nextInt(6) - 3)
        var salary = prof.averSalary + (Random().nextInt(prof.averSalary / 2) - prof.averSalary / 4)
        if (salary == 0) {
            salary = 1
        }
        salary *= (quality / 10)
        var birthDay = (Random().nextInt(10) + 10).toString()
        var birthMonth = "0${(Random().nextInt(10))}"
        DatabaseFactory.getInstance(ctx).addLaborExchangeWithProperties(Staff(name, age, spec, quality, nationality, salary, Pair(birthDay, birthMonth)))
    }

}

fun countProductivity(ctx: Context) {
    val fac = Factory.getFactoryById(DatabaseFactory.index)
    if (fac != null) {
        val list = DatabaseFactory.getInstance(ctx).getListOfStaff()
        var qualityOfGW = 0
        var qualityMN = 0
        var qualityFM = 0
        for (st in list) {
            when (st.prof) {
                Profs.GENERAL_WORKER.prof -> qualityOfGW += st.quality
                Profs.MANAGER.prof -> qualityMN += st.quality
                Profs.FOREMAN.prof -> qualityFM += st.quality
            }
        }

        var quality = qualityOfGW + 2 * (qualityFM) + 3 * (qualityMN)

        var res = fac.machine_state * quality
        if (res == 0.0) {
            fac.productivity = 0
        } else {
            var coef = EnumFactory.findById(DatabaseFactory.index).coef
            res = ((coef).toDouble() / res) + 5.0
            if (res < 0.0) {
                res = 0.0
            }
            fac.productivity = res.roundToInt()
        }

    }
}

fun countRes_Cap() {
    val inv = Inventory.inventories[DatabaseFactory.index].get(Inventory.TAG)!!
    val fac = Factory.getFactoryById(DatabaseFactory.index)
    if (fac != null) {
        fac.res.maxStackSize = EnumFactory.findById(DatabaseFactory.index).res_cap
        for (i in 0 until inv.size) {
            if (inv.getInventorySlotContents(i).itemId in Items.getNumOfResCap().first..Items.getNumOfResCap().second) {
                fac.res.maxStackSize += Items.findById(inv.getInventorySlotContents(i).itemId).price / 50
            }
        }
    }
}

fun countProd_Cap() {
    val inv = Inventory.inventories[DatabaseFactory.index].get(Inventory.TAG)!!
    val fac = Factory.getFactoryById(DatabaseFactory.index)
    if (fac != null) {
        fac.production.maxStackSize = EnumFactory.findById(DatabaseFactory.index).productivity_cap
        for (i in 0 until inv.size) {
            if (inv.getInventorySlotContents(i).itemId in Items.getNumOfProdCap().first..Items.getNumOfProdCap().second) {
                fac.production.maxStackSize += Items.findById(inv.getInventorySlotContents(i).itemId).price / 40
            }
        }
    }
}
