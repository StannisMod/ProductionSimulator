package stannis.ru.productionsimulator.Functions

import android.content.Context
import android.util.Log
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase
import stannis.ru.productionsimulator.Enums.EnumFactory
import stannis.ru.productionsimulator.Enums.Items
import stannis.ru.productionsimulator.Enums.Nations
import stannis.ru.productionsimulator.Enums.Profs
import stannis.ru.productionsimulator.Models.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

var GO = false
var isPromotioned: ArrayList<Boolean> = ArrayList()
var senderWorker : String = ""

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
    Worker.saveAll(ctx)
    Credit_Deposit.saveAll(ctx)
    Message.saveAll(ctx)
    DatabaseFactory.index = 0
}

fun setBeginToAll() {
    Inventory.setBegin()
    Worker.setBegin()
    Credit_Deposit.setBegin()
    Message.setBegin()
}

fun loadAll(ctx: Context) {
    setBeginToAll()
    for (i in 0 until EnumFactory.getSize()) {
        DatabaseFactory.index = i
        val fac = Factory.load(ctx, i)
        if (fac != null) {
            Factory.factories.add(fac)
        }

        Inventory.inventories[i].put("buy", Inventory.load(ctx, "buy"))
        Inventory.inventories[i].put("sell", Inventory.load(ctx, "sell"))
        Inventory.inventories[i].put(Inventory.TAG, Inventory.load(ctx, Inventory.TAG))
        val lab = DatabaseFactory.getInstance(ctx).getListOfLaborExchange()
        for (wk in lab) {
            wk.generate()
        }
        val st = DatabaseFactory.getInstance(ctx).getListOfStaff()
        for (wk in st) {
            wk.addToStaff()
        }
    }
    Message.loadAll(ctx)
    Credit_Deposit.loadAll(ctx)
    DatabaseFactory.index = 0

}

fun countReputation(ctx: Context, tax: Int) {
    val player = Player.getInstance(ctx)
    val trueTax = player.tax
    val dif = tax - trueTax
    if (player.reputation < 10) {
        player.reputation = (10 * (dif.toDouble() / trueTax.toDouble())).toInt()
    }
    player.reputation = player.reputation + (player.reputation.toDouble() * (dif.toDouble() / trueTax.toDouble())).toInt()

    if (player.reputation < 0) {
        player.reputation = 0
    }
    if (player.reputation > 100) {
        player.reputation = 100
    }
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
        salary *= (quality / 10)
        if (salary == 0) {
            salary = 1
        }
        var birthDay = (Random().nextInt(10) + 10).toString()
        var birthMonth = "0${(Random().nextInt(10))}"
        Worker(name, age, spec, quality, nationality, salary, Pair(birthDay, birthMonth)).generate()
        Log.d("WORKER", Worker.getListOfLabor().toString())
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
                fac.res.maxStackSize += (Items.findById(inv.getInventorySlotContents(i).itemId).price / 50) * inv.getInventorySlotContents(i).stackSize
            }
        }
        fac.res.getInventorySlotContents(0).maxStackSize = fac.res.maxStackSize
    }

}

fun countProd_Cap() {
    val inv = Inventory.inventories[DatabaseFactory.index].get(Inventory.TAG)!!
    val fac = Factory.getFactoryById(DatabaseFactory.index)
    if (fac != null) {
        fac.production.maxStackSize = EnumFactory.findById(DatabaseFactory.index).productivity_cap
        for (i in 0 until inv.size) {
            if (inv.getInventorySlotContents(i).itemId in Items.getNumOfProdCap().first..Items.getNumOfProdCap().second) {
                fac.production.maxStackSize += (Items.findById(inv.getInventorySlotContents(i).itemId).price / 40) * inv.getInventorySlotContents(i).stackSize
            }
        }
        fac.production.getInventorySlotContents(0).maxStackSize = fac.production.maxStackSize
    }
}

fun randomInRange(pair: Pair<Int, Int>): Int {
    val a = Math.min(pair.first, pair.second)
    var b = Math.max(pair.first, pair.second)
    return a + Random().nextInt(b - a + 1)
}

fun clearInstances() {
    Player.clear()
    DataTime.clear()
    Factory.clear()
    Inventory.setNulls()
    MoneyForDay.clear()
    Message.clear()
}

fun isEqualDate(date1: Array<String>, date2: Array<String>): Boolean {
    return date1[0] == date2[0] && date1[1] == date1[1] && date1[2] == date2[2]
}

fun fillDb(ctx: Context) {
    clearInstances()
    GO = false
    setBeginToAll()
    for (i in 0 until EnumFactory.getSize()) {
        DatabaseFactory.index = i
        val ins = DatabaseFactory.getInstance(ctx)
        Inventory.setNulls()
        ins.removeInventory("buy")
        ins.removeInventory("PlayerInv")
        ins.removeInventory("sell")
        ins.removeFactory(i)
        ins.removeAllLabor()
        ins.removeAllStaff()
    }
    DatabaseFactory.index = 0
    Inventory.getInventory("sell")
    Inventory.getInventory("buy")
    Inventory.getInventory()
    Factory(true, 0, true, EnumFactory.SAWMILL.price, EnumFactory.SAWMILL)
    Inventory.saveInventories(ctx)
    Log.d("FACTORY", Factory.factories.toString())

    val arrayNames = arrayOf(/*"Абрам", " Август", " Авдей", " Аверкий", " Адам", " Адриан", " Азарий", " Аким", " Александр", " Алексей", " Амвросий", " Амос", " Ананий", " Анатолий", " Андрей", " Андриан", " Андрон", " Аристарх", " Аркадий", " Арсен", " Арсений", " Артём", " Артемий", " Архип", " Аскольд", " Афанасий", " Афиноген", "Кирилл", " Карл", " Касим", " Кастор", " Касьян", " Каюм", " Кеша", " Кирсан", " Клим", " Кондрат", " Корней", " Корнелий", " Косьма", " Кристиан", " Кузьма",
            "Лавр", " Лаврентий", " Ладимир", " Лазарь", " Леонид", " Леонтий", " Лонгин", " Лука", " Наум", " Нестор", " Нестер", " Никандр", " Никанор", " Никита", " Никифор", " Никодим", " Никола", " Николай", " Никон", " Нил", " Нифонт",

            "Олег", " Оскар", " Остап", " Остромир",

            "Павел", " Панкрат", " Парфений", " Пахом", " Петр", " Пимен", " Платон", " Поликарп", " Порфирий", " Потап", " Пров", " Прокл", " Прокоп", " Прокопий", " Прокофий", " Прохор",

            "Радим", " Радислав", " Радован", " Ратибор", " Ратмир", " Рафаил", " Родион", " Роман", " Ростислав", " Руслан", " Рюрик",

            "Стас", " Савва", " Савелий", " Спартак", " Степан",*/

            " Тарас", " Твердислав", " Творимир", " Терентий", " Тимофей", " Тимур", " Тит", " Тихон", " Трифон", " Трофим")
    val arraySecondNames = arrayOf(/*"Смирнов", "Иванов", " Кузнецов", " Соколов", " Попов", " Лебедев", " Козлов", " Новиков ", "Морозов ", "Петров ", "Волков ", "Соловьёв ", "Васильев ", "Зайцев ", "Павлов ", "Семёнов ", "Голубев", ""
            , "Виноградов", "Богданов"
            , "Воробьёв"
            , "Фёдоров"
            , "Михайлов"
            , "Беляев"
            , "Тарасов"
            , "Белов"
            , "Комаров"
            , "Орлов"

            , "Веселов"
            , "Филиппов"
            , "Марков"
            , "Большаков"
            , "Суханов"
            , "Миронов"
            , "Ширяев"
            , "Александров"
            , "Коновалов"
            , "Шестаков"
            , "Казаков"
            , "Ефимов"
            , "Денисов"
            , "Громов"
            , "Фомин"
            , "Давыдов"
            , "Мельников"
            , */"Щербаков"
            , "Блинов"
            , "Колесников"
            , "Карпов"
            , "Афанасьев"
            , "Власов"
            , "Маслов"
    )
    val kek = PlayerStatsDatabase.getInstance(ctx)
    kek.removeAllCredits()
    kek.removeDataTime()
    kek.removePlayer()
    kek.removeAllNames()
    kek.removeMoneyForDay()
    kek.removeAllMessage()
    kek.removeAllMessageReaded()
    kek.addNames(arrayNames, arraySecondNames)
    kek.addPlayerStatsWithProperties(200, 0, 0, 50, 5)
    val data = java.util.Calendar.getInstance()
    var day = data.get(Calendar.DAY_OF_MONTH).toString()
    if (data.get(Calendar.DAY_OF_MONTH) < 10) {
        day = "0${day}"
    }
    var month = (data.get(Calendar.MONTH) + 1).toString()
    if (data.get(Calendar.MONTH) < 10) {
        month = "0${month}"
    }
    kek.addDataTimeWithProperties(day, month, data.get(Calendar.YEAR).toString(), 0, 0)
    kek.addMoneyForDay(0, 0)

    loadAll(ctx)
    Log.d("FACTORY", Factory.factories.toString())
}

fun generateRandomIndexOfFactory(): Int {
    var index = 0
    var i = 0
    for (fac in Factory.factories) {
        DatabaseFactory.index = i
        i++
        if (Worker.sizeOfStaff()>0) {
            index++
        } else {
            break
        }
    }
    DatabaseFactory.index = 0
    Log.d("IIIIRandom index", index.toString())
    return Random().nextInt(index)

}

fun ArrayList<Boolean>.isTrue():Boolean{
    var res = true
    for(kek in this){
        res = res&&kek
    }
    return res
}

