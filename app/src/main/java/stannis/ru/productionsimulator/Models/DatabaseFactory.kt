package stannis.ru.productionsimulator.Models

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.support.annotation.IntegerRes
import org.jetbrains.anko.db.*
import org.w3c.dom.Text
import stannis.ru.productionsimulator.EnumFactory
import stannis.ru.productionsimulator.Models.Message

class DatabaseFactory(val ctx: Context) : ManagedSQLiteOpenHelper(ctx, "ProductionSimulatorDB", null, 13) {

    companion object {
        private var instance: DatabaseFactory? = null

        @Synchronized
        fun getInstance(ctx: Context): DatabaseFactory {
            if (instance == null) {
                instance = DatabaseFactory(ctx.applicationContext)
            }
            return instance!!
        }

    }

    var added = false


    override fun onCreate(db: SQLiteDatabase) {
        db.createTable("Factories", true,
                "id" to INTEGER,
                "type" to INTEGER,
                "res" to INTEGER,
                "res_cap" to INTEGER,
                "consumption" to INTEGER,
                "productivity" to INTEGER,
                "production" to INTEGER,
                "production_cap" to INTEGER,
                "machine_state" to REAL)

        db.createTable("laborExchange", true,
                "name" to TEXT,
                "age" to INTEGER,
                "spec" to TEXT,
                "quality" to INTEGER,
                "nationality" to TEXT,
                "salary" to INTEGER,
                "dayOfBirth" to TEXT,
                "monthOfBirth" to TEXT)

        db.createTable("staff", true,
                "name" to TEXT,
                "age" to INTEGER,
                "spec" to TEXT,
                "quality" to INTEGER,
                "nationality" to TEXT,
                "salary" to INTEGER,
                "dayOfBirth" to TEXT,
                "monthOfBirth" to TEXT)

        db.createTable("buy", true,
                "name" to TEXT,
                "id" to INTEGER,
                "price" to INTEGER)
        db.createTable("creditDeposit", true,
                "type" to INTEGER,
                "amount" to INTEGER,
                "percent" to REAL,
                "dayOfStart" to TEXT,
                "monthOfStart" to TEXT,
                "yearOfStart" to TEXT
        )
        db.createTable("message", true,
                "hash" to INTEGER,
                "caption" to TEXT,
                "sender" to TEXT,
                "text" to TEXT,
                "day" to TEXT,
                "month" to TEXT,
                "year" to TEXT
        )
        db.createTable("PlayerStats", true,
                "money" to INTEGER,//Весь Integer
                "stuff" to INTEGER,//>=0
                "staff" to INTEGER,//>=0
                "reputation" to INTEGER)//-100<= =>100
        db.createTable("DataTime", true,
                "currentDay" to TEXT,
                "currentMonth" to TEXT,
                "currentYear" to TEXT,
                "currentTime" to INTEGER,
                "tookCreditToday" to INTEGER,
                "tookDepositToday" to INTEGER
        )

        db.createTable(Inventory.getInventory().name, true,
                "num" to INTEGER,
                "id" to INTEGER,
                "stackSize" to INTEGER,
                "maxStackSize" to INTEGER)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable("Factories", true)
        db.dropTable("laborExchange", true)
        db.dropTable("staff", true)
        db.dropTable("buy", true)
        db.dropTable("creditDeposit", true)
        db.dropTable("message", true)
        db.dropTable("PlayerStats", true)

        db.dropTable(Inventory.getInventory().name, true)
        onCreate(db)
    }

    val Context.database: DatabaseFactory
        get() = DatabaseFactory.getInstance(applicationContext)

    // To manage factory DB

    fun addFactoryWithProperties(ctx : Context, id: Int, type: Int, res: Int, res_cap: Int, consumption: Int, productivity: Int, production: Int, production_cap: Int, machine_state: Double) {
        getInstance(ctx).use {
            insert("Factories",
                    "id" to id, "type" to type, "res" to res, "res_cap" to res_cap, "consumption" to consumption,
                    "productivity" to productivity, "production" to production, "production_cap" to production_cap, "machine_state" to machine_state)
        }
    }

    fun getFactory(id: Int): Factory? {
        var type: Int = 0
        var res: Int = 0
        var res_cap: Int = 0
        var consumption: Int = 0
        var productivity: Int = 0
        var production: Int = 0
        var production_cap: Int = 0
        var machine_state: Double = 0.0;
        var factory: Factory? = null

        val query = "SELECT * FROM Factories WHERE id = \"$id\""
        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            cursor.moveToFirst()

            type = Integer.parseInt(cursor.getString(0))
            res = Integer.parseInt(cursor.getString(1))
            res_cap = Integer.parseInt(cursor.getString(2))
            consumption = Integer.parseInt(cursor.getString(3))
            productivity = Integer.parseInt(cursor.getString(4))
            production = Integer.parseInt(cursor.getString(5))
            production_cap = Integer.parseInt(cursor.getString(6))
            machine_state = cursor.getString(7).toDouble()

            factory = Factory(id, EnumFactory.findById(type), res, res_cap, consumption, productivity, production, production_cap, machine_state)
            cursor.close()
        }
        db.close()

        return factory
    }

    fun setFactoryProperties(id: Int, type: Int, res: Int, res_cap: Int, consumption: Int, productivity: Int, production: Int, production_cap: Int) {
        getInstance(ctx).use {
            update("Factories", "type" to type, "res" to res, "res_cap" to res_cap, "consumption" to consumption,
                    "productivity" to productivity, "production" to production, "production_cap" to production_cap)
                    .whereArgs("id = {id}", "id" to id).exec()
        }
    }

    fun removeFactory(id: Int): Int {
        var result : Int = 0
        getInstance(ctx).use {
            result = delete("Factories", "id = {id}", "id" to id)
        }
        return result
    }

    fun addInventory(inv: Inventory) {
        getInstance(ctx).use {
            for (i in 0..inv.getInventorySize()-1) {
                val slot = inv.getInventorySlotContents(i)
                insert(inv.name, "num" to i, "id" to slot.itemId, "stackSize" to slot.stackSize, "maxStackSize" to slot.maxStackSize)
            }
        }
    }

    fun updateInventory(inv : Inventory) {
        getInstance(ctx).use {
            for (i in 0..inv.getInventorySize()-1) {
                val slot = inv.getInventorySlotContents(i)
                update(inv.name, "id" to slot.itemId, "stackSize" to slot.stackSize, "maxStackSize" to slot.maxStackSize).whereArgs("index = {index}", "index" to i)
            }
        }
    }

    fun getInventory(name : String) : Inventory? {
        // var index : Int
        var id : Int
        var stackSize : Int
        var maxStackSize : Int

        var inv : Inventory? = null
        val slots = ArrayList<ItemStack>()

        val query = "SELECT * FROM \"$name\""
        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                id = Integer.parseInt(cursor.getString(1))
                stackSize = Integer.parseInt(cursor.getString(2))
                maxStackSize = Integer.parseInt(cursor.getString(3))

                slots.add(ItemStack(id, stackSize, maxStackSize))
            } while (cursor.moveToNext())
            inv = Inventory(name, slots.size, maxStackSize)
            for (i in 0..(slots.size - 1))
                inv.setInventorySlotContents(i, slots.get(i))

            cursor.close()
        }
        db.close()

        return inv
    }

    fun removeInventory(name: String) {
        val db = this.writableDatabase
        db.dropTable(name, true)
        db.close()
    }

    //For managing inventory

    fun addLaborExchangeWithProperties(name: String, age: Int, spec: String, quality: Int, nationality: String, salary: Int, dayOfBirth: String, monthOfBirth: String) {
        getInstance(ctx).use {
            insert("laborExchange",
                    "name" to name, "age" to age, "spec" to spec, "quality" to quality, "nationality" to nationality,
                    "salary" to salary, "dayOfBirth" to dayOfBirth, "monthOfBirth" to monthOfBirth)
        }
    }

    fun addLaborExchangeWithProperties(staff: Staff) {
        addStaffWithProperties(staff.name, staff.age, staff.prof, staff.quality, staff.nation, staff.salary, staff.birth.first, staff.birth.second)
    }

    fun getListOfLaborExchange(): List<Staff> {
        val query = "SELECT * FROM laborExchange"
        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)
        var list: ArrayList<Staff> = ArrayList()
        if (cursor.moveToFirst()) {
            do {
                var i = 0;
                val name = cursor.getString(i)
                i++
                val age = Integer.parseInt(cursor.getString(i))
                i++
                val spec = cursor.getString(i)
                i++
                val quality = Integer.parseInt(cursor.getString(i))
                i++
                val nationality = cursor.getString(i)
                i++
                val salary = Integer.parseInt(cursor.getString(i))
                i++
                val dayOfBirth = cursor.getString(i)
                i++
                val monthOfBirth = cursor.getString(i)
                i++
                list.add(Staff(name, age, spec, quality, nationality, salary, Pair(dayOfBirth, monthOfBirth)))
            } while (cursor.moveToNext())
            cursor.close()
        }
        db.close()
        return list
    }

    fun getWorkerFromLabor(name: String): Staff? {
        val query = "SELECT * FROM laborExchange WHERE name = \"$name\""
        val db = this.writableDatabase
        var worker: Staff? = null
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            cursor.moveToFirst()
            var i = 0
            val name = cursor.getString(i)
            i++
            val age = Integer.parseInt(cursor.getString(i))
            i++
            val spec = cursor.getString(i)
            i++
            val quality = Integer.parseInt(cursor.getString(i))
            i++
            val nationality = cursor.getString(i)
            i++
            val salary = Integer.parseInt(cursor.getString(i))
            i++
            val dayOfBirth = cursor.getString(i)
            i++
            val monthOfBirth = cursor.getString(i)
            worker = Staff(name, age, spec, quality, nationality, salary, Pair(dayOfBirth, monthOfBirth))
            cursor.close()
        }
        db.close()
        return worker
    }

    fun addStaffWithProperties(name: String, age: Int, spec: String, quality: Int, nationality: String, salary: Int, dayOfBirth: String, monthOfBirth: String) {
        getInstance(ctx).use {
            insert("staff",
                    "name" to name, "age" to age, "spec" to spec, "quality" to quality, "nationality" to nationality,
                    "salary" to salary, "dayOfBirth" to dayOfBirth, "monthOfBirth" to monthOfBirth)
        }
    }

    fun addStaffWithProperties(staff: Staff) {
        addStaffWithProperties(staff.name, staff.age, staff.prof, staff.quality, staff.nation, staff.salary, staff.birth.first, staff.birth.second)
    }

    fun getListOfStaff(): List<Staff> {
        val query = "SELECT * FROM staff"
        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)
        var list: ArrayList<Staff> = ArrayList()
        if (cursor.moveToFirst()) {
            do {
                var i = 0
                val name = cursor.getString(i)
                i++
                val age = Integer.parseInt(cursor.getString(i))
                i++
                val spec = cursor.getString(i)
                i++
                val quality = Integer.parseInt(cursor.getString(i))
                i++
                val nationality = cursor.getString(i)
                i++
                val salary = Integer.parseInt(cursor.getString(i))
                i++
                val dayOfBirth = cursor.getString(i)
                i++
                val monthOfBirth = cursor.getString(i)
                i++
                list.add(Staff(name, age, spec, quality, nationality, salary, Pair(dayOfBirth, monthOfBirth)))
            } while (cursor.moveToNext())
            cursor.close()
        }
        db.close()
        return list
    }


    fun getWorkerFromStaff(name: String): Staff? {
        val query = "SELECT * FROM staff WHERE name = \"$name\""
        val db = this.writableDatabase
        var worker: Staff? = null
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            cursor.moveToFirst()
            var i = 0
            val name = cursor.getString(i)
            i++
            val age = Integer.parseInt(cursor.getString(i))
            i++
            val spec = cursor.getString(i)
            i++
            val quality = Integer.parseInt(cursor.getString(i))
            i++
            val nationality = cursor.getString(i)
            i++
            val salary = Integer.parseInt(cursor.getString(i))
            i++
            val dayOfBirth = cursor.getString(i)
            i++
            val monthOfBirth = cursor.getString(i)
            worker = Staff(name, age, spec, quality, nationality, salary, Pair(dayOfBirth, monthOfBirth))
            cursor.close()
        }
        db.close()
        return worker
    }

    fun removeStaff(name: String): Int {
        var result: Int = 0
        getInstance(ctx).use {
            result = delete("staff", "name = {name}", "name" to name)
        }
        return result
    }

    fun setLaborExchangeWithProperties(name: String, age: Int, spec: String, quality: Int, nationality: String, salary: Int, dayOfBirth: String, monthOfBirth: String) {
        getInstance(ctx).use {
            update("laborExchange", "age" to age, "spec" to spec, "quality" to quality,
                    "nationality" to nationality, "salary" to salary, "dayOfBirth" to dayOfBirth, "monthOfBirth" to monthOfBirth)
                    .whereArgs("name = {name}", "name" to name).exec()
        }
    }

    fun setLaborExchangeWithProperties(staff: Staff) {
        setLaborExchangeWithProperties(staff.name, staff.age, staff.nation, staff.quality, staff.prof, staff.salary, staff.birth.first, staff.birth.second)
    }

    fun setStaffWithProperties(name: String, age: Int, spec: String, quality: Int, nationality: String, salary: Int, dayOfBirth: String, monthOfBirth: String) {
        getInstance(ctx).use {
            update("staff", "age" to age, "spec" to spec, "quality" to quality,
                    "nationality" to nationality, "salary" to salary, "dayOfBirth" to dayOfBirth, "monthOfBirth" to monthOfBirth)
                    .whereArgs("name = {name}", "name" to name).exec()
        }
    }

    fun setStaffWithProperties(staff: Staff) {
        setStaffWithProperties(staff.name, staff.age, staff.prof, staff.quality, staff.nation, staff.salary, staff.birth.first, staff.birth.second)
    }

    fun removeLaborExchange(name: String): Int {
        var result: Int = 0
        getInstance(ctx).use {
            result = delete("laborExchange", "name = {name}", "name" to name)
        }
        return result
    }

    fun addMessageWithProperties(message : Message) {

        getInstance(ctx).use {
            insert("Message",
                    "hash" to message.hashCode(), "caption" to message.caption, "sender" to message.sender, "text" to message.text, "day" to message.date[0],"month" to message.date[1], "year" to message.date[2], "readed" to message.readed)
        }
    }

    fun removeMessage(hash: Int): Int {
        var result: Int = 0
        getInstance(ctx).use {
            result = delete("Message", "hash = {hash}", "hash" to hash)
        }
        return result
    }

    fun addCrDepWithProperties(type: Int, amount: Int, percent: Double, dayOfStart: String, monthOfStart: String, yearOfStart: String) {
        getInstance(ctx).use {
            insert("creditDeposit",
                    "type" to type, "amount" to amount, "percent" to percent, "dayOfStart" to dayOfStart, "monthOfStart" to monthOfStart,
                    "yearOfStart" to yearOfStart)
        }
    }

    fun getMessage(): ArrayList<Message> {
        val query = "SELECT * FROM message"
        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)
        var list: ArrayList<Message> = ArrayList()

        if (cursor.moveToFirst()) {
            do {
                var i = 0
                val hash = cursor.getString(i).toInt()
                i++
                val caption = cursor.getString(i).toString()
                i++
                val sender = cursor.getString(i).toString()
                i++
                val text = cursor.getString(i).toString()
                i++
                val day = cursor.getString(i).toString()
                i++
                val month = cursor.getString(i).toString()
                i++
                val year = cursor.getString(i).toString()
                i++
                val readed = cursor.getString(i).toString()
                list.add(Message(caption = caption, text = text, date = arrayOf(day, month, year), sender = sender, readed = readed))
            } while (cursor.moveToNext())
            cursor.close()
        }
        db.close()
        return list
    }

    fun getListOfCreditDeposit(): ArrayList<Credit_Deposit> {
        val query = "SELECT * FROM creditDeposit"
        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)
        var list: ArrayList<Credit_Deposit> = ArrayList()

        if (cursor.moveToFirst()) {
            do {
                var i = 0
                val type = cursor.getString(i).toInt()
                i++
                val amount = cursor.getString(i).toInt()
                i++
                val percent = cursor.getString(i).toDouble()
                i++
                val dayOfStart = cursor.getString(i)
                i++
                val monthOfStart = cursor.getString(i)
                i++
                val yearOfStart = cursor.getString(i)
                i++
                list.add(Credit_Deposit(amount, percent, arrayOf(dayOfStart, monthOfStart, yearOfStart), type))
            } while (cursor.moveToNext())
            cursor.close()
        }
        db.close()
        return list
    }

    fun getCreditDeposit(type: Int, dayOfStart: String, monthOfStart: String, yearOfStart: String): Credit_Deposit? {

        val query = "SELECT * FROM Factories WHERE (type = \"$type\" AND dayOfStart = \"$dayOfStart\" AND monthOfStart = \"$monthOfStart\" AND yearOfStart = \"$yearOfStart\")"
        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)
        var creDep: Credit_Deposit? = null
        if (cursor.moveToFirst()) {
            cursor.moveToFirst()
            var i = 0
            val type = Integer.parseInt(cursor.getString(i))
            i++
            val amount = Integer.parseInt(cursor.getString(i))
            i++
            val percent = (cursor.getString(i)).toDouble()
            i++
            val dayOfStart = cursor.getString(i)
            i++
            val monthOfStart = cursor.getString(i)
            i++
            val yearOfStart = cursor.getString(i)


            creDep = Credit_Deposit(amount, percent, arrayOf(dayOfStart, monthOfStart, yearOfStart), type)
            cursor.close()
        }
        db.close()

        return creDep
    }

    fun setCreditDepositProperties(type: Int, amount: Int, percent: Double, dayOfStart: String, monthOfStart: String, yearOfStart: String) {
        getInstance(ctx).use {
            update("creditDeposit", "amount" to amount, "percent" to percent)
                    .whereArgs("(type = {type}) and (dayOfStart = {dayOfStart}) and (monthOfStart = {monthOfStart}) and (yearOfStart = {yearOfStart})", "type" to type, "dayOfStart" to dayOfStart, "monthOfStart" to monthOfStart, "yearOfStart" to yearOfStart).exec()
        }
    }

    fun setCreditDepositProperties(crDep: Credit_Deposit) {
        setCreditDepositProperties(crDep.type, crDep.amount, crDep.percent, crDep.date[0], crDep.date[1], crDep.date[2])
    }

    fun removeCreditDeposit(type: Int, dayOfStart: String, monthOfStart: String, yearOfStart: String): Int {
        var result: Int = 0
        getInstance(ctx).use {
            result = delete("creditDeposit", "(type = {type}) and (dayOfStart = {dayOfStart}) and (monthOfStart = {monthOfStart}) and (yearOfStart = {yearOfStart})", "type" to type, "dayOfStart" to dayOfStart, "monthOfStart" to monthOfStart, "yearOfStart" to yearOfStart)
        }
        return result
    }

    fun removeAllCredits() {
        for (cDep in getListOfCreditDeposit()) {
            removeCreditDeposit(cDep.type, cDep.date[0], cDep.date[1], cDep.date[2])
        }
    }

    fun addPlayerStatsWithProperties(money: Int, stuff: Int, staff: Int, reputation: Int, firstTime: Int, nalog : Int) {
        getInstance(ctx).use {
            insert("PlayerStats", "money" to money, "stuff" to stuff, "staff" to staff, "reputation" to reputation, "firstTime" to firstTime, "nalog" to nalog)
        }
    }

    fun getPlayerStats(): Player? {
        val query = "SELECT*FROM PlayerStats"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var player: Player? = null
        if (cursor.moveToFirst()) {
            cursor.moveToFirst()
            var i = 0
            val money = cursor.getString(i).toInt()
            i++
            val stuff = cursor.getString(i).toInt()
            i++
            val staff = cursor.getString(i).toInt()
            i++
            val reputation = cursor.getString(i).toInt()
            i++
            val firstTime = cursor.getString(i).toInt()
            i++
            val nalog = cursor.getString(i).toInt()
            player = Player(money, stuff, staff, reputation, firstTime, nalog)
            cursor.close()

        }
        db.close()
        return player
    }

    fun setPlayerWithProperties(money: Int, stuff: Int, staff: Int, reputation: Int, firstTime: Int, nalog: Int) {
        getInstance(ctx).use {
            update("PlayerStats", "money" to money, "stuff" to stuff, "staff" to staff, "reputation" to reputation, "firstTime" to firstTime, "nalog" to nalog).exec()
        }
    }

    fun setPlayerWithProperties(player: Player) {
        setPlayerWithProperties(player.money, player.stuff, player.staff, player.reputation, player.firstTime, player.nalog)
    }

    fun removePlayer(): Int {
        var res = 0
        getInstance(ctx).use {
            res = delete("PlayerStats")
        }
        return res
    }

    fun addDataTimeWithProperties(currentDay: String, currentMonth: String, currentYear: String, tookCreditToday: Int, tookDepositToday: Int,  todaysCreditMinus:Int, todaysDepositGain:Int ) {
        getInstance(ctx).use {
            insert("DataTime", "currentDay" to currentDay, "currentMonth" to currentMonth, "currentYear" to currentYear, "tookCreditToday" to tookCreditToday, "tookDepositToday" to tookDepositToday, "todaysCreditMinus" to todaysCreditMinus, "todaysDepositGain" to todaysDepositGain)
        }
    }

    fun setDataTimeWithProperties(currentDay: String, currentMonth: String, currentYear: String, tookCreditToday: Int, tookDepositToday: Int, todaysCreditMinus: Int, todaysDepositGain: Int) {
        getInstance(ctx).use {
            update("DataTime", "currentDay" to currentDay, "currentMonth" to currentMonth, "currentYear" to currentYear, "tookCreditToday" to tookCreditToday, "tookDepositToday" to tookDepositToday, "todaysCreditMinus" to todaysCreditMinus, "todaysDepositGain" to todaysDepositGain).exec()
        }
    }

    fun setDataTimeWithProperties(currentData: DataTime) {
        setDataTimeWithProperties(currentData.currentDay, currentData.currentMonth, currentData.currentYear, currentData.tookCreditToday, currentData.tookDepositToday, currentData.todaysCreditMinus, currentData.todaysDepositGain)
    }

    fun getDataTime(): DataTime? {
        val query = "SELECT * FROM DataTime"
        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)
        var curDate: DataTime? = null
        if (cursor.moveToFirst()) {
            cursor.moveToFirst()

            var i = 0;
            val currentDay = cursor.getString(i)
            i++
            val currentMonth = cursor.getString(i)
            i++
            val currentYear = cursor.getString(i)
            i++
            val tookCreditToday = cursor.getString(i).toInt()
            i++
            val tookDepositToday = cursor.getString(i).toInt()
            i++
            val todaysCreditMinus = cursor.getString(i).toInt()
            i++
            val todaysDepositGain = cursor.getString(i).toInt()
            curDate = DataTime(currentDay, currentMonth, currentYear, tookCreditToday, tookDepositToday, todaysCreditMinus, todaysDepositGain)
            cursor.close()
        }
        db.close()
        return curDate
    }

    fun removeDataTime(): Int {
        var res = 0
        getInstance(ctx).use {
            res = delete("DataTime")
        }
        return res
    }

}