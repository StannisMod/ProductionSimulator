package stannis.ru.productionsimulator.Databases

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import org.jetbrains.anko.db.*
import stannis.ru.productionsimulator.Enums.EnumFactory
import stannis.ru.productionsimulator.Models.*

import java.util.*
import kotlin.collections.ArrayList


class DatabaseFactory(val ctx: Context, name1: String) : ManagedSQLiteOpenHelper(ctx, name1, null, 26) {

    companion object {
        private val instanceList: ArrayList<DatabaseFactory?> = ArrayList()
        var index = 0

        fun getInstance(ctx: Context): DatabaseFactory {
            if (instanceList.size <= index) {
                for (i in instanceList.size..index)
                    instanceList.add(i, DatabaseFactory(ctx.applicationContext, "Factory${i}"))
            }
            return instanceList[index]!!
        }

    }

    var sz = 0
    var added = false


    override fun onCreate(db: SQLiteDatabase) {
        db.createTable("Factories", true,
                "id" to INTEGER,
                "isBought" to INTEGER,
                "price" to INTEGER,
                "type" to INTEGER,
                "res" to INTEGER,
                "res_cap" to INTEGER,
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
                "num" to INTEGER,
                "id" to INTEGER,
                "stackSize" to INTEGER,
                "maxStackSize" to INTEGER//,

        )
        db.createTable("sell", true,
                "num" to INTEGER,
                "id" to INTEGER,
                "stackSize" to INTEGER,
                "maxStackSize" to INTEGER//,

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
        db.dropTable("Message", true)
        db.dropTable("MessageReaded", true)
        db.dropTable("PlayerStats", true)
        db.dropTable("DataTime", true)
        db.dropTable("Names", true)
        db.dropTable("sell", true)
        db.dropTable(Inventory.getInventory().name, true)
        onCreate(db)
    }

//    val Context.database: DatabaseFactory
//        get() = DatabaseFactory.getInstance(applicationContext)

    // To manage factory DB

    fun addFactory(ctx: Context, factory: Factory) {
        addFactoryWithProperties(ctx, factory.id, factory.isBought, factory.price, factory.type.getFactoryType(), factory.res.getInventorySlotContents(0).stackSize,
                factory.res.getInventoryStackLimit(), factory.productivity,
                factory.production.getInventorySlotContents(0).stackSize, factory.production.getInventoryStackLimit(), factory.machine_state)
    }

    fun addFactoryWithProperties(ctx: Context, id: Int, isBought: Boolean, price: Int, type: Int, res: Int, res_cap: Int, productivity: Int, production: Int, production_cap: Int, machine_state: Double) {
        val isBOught = if (isBought) 1 else 0
        getInstance(ctx).use {
            insert("Factories",
                    "id" to id, "isBought" to isBOught, "price" to price, "type" to type, "res" to res, "res_cap" to res_cap,
                    "productivity" to productivity, "production" to production, "production_cap" to production_cap, "machine_state" to machine_state)
        }
    }

    fun getFactory(id: Int): Factory? {
        var type: Int = 0
        var res: Int = 0
        var res_cap: Int = 0

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
            var i = 1
            var isBought = cursor.getString(i).toInt() == 1
            i++
            var price = cursor.getString(i).toInt()
            Log.d("WHYFACISNULLprice", price.toString())
            i++
            type = Integer.parseInt(cursor.getString(i))
            i++
            res = Integer.parseInt(cursor.getString(i))
            i++
            res_cap = Integer.parseInt(cursor.getString(i))
            i++
            productivity = Integer.parseInt(cursor.getString(i))
            i++
            production = Integer.parseInt(cursor.getString(i))
            i++
            production_cap = Integer.parseInt(cursor.getString(i))
            i++
            machine_state = cursor.getString(i).toDouble()

            factory = Factory(false, id, isBought, price, EnumFactory.findById(type), res, res_cap, productivity, production, production_cap, machine_state)
            cursor.close()
        }
        db.close()

        return factory
    }

    fun updateFactory(factory: Factory) {

        setFactoryProperties(factory.id, factory.isBought, factory.price, factory.type.getFactoryType(), factory.res.getInventorySlotContents(0).stackSize,
                factory.res.getInventoryStackLimit(), factory.productivity,
                factory.production.getInventorySlotContents(0).stackSize, factory.production.getInventoryStackLimit(), factory.machine_state)
    }

    fun setFactoryProperties(id: Int, isBought: Boolean, price: Int, type: Int, res: Int, res_cap: Int, productivity: Int, production: Int, production_cap: Int, machine_state: Double) {
        val isBOught = if (isBought) 1 else 0
        getInstance(ctx).use {
            update("Factories", "isBought" to isBOught, "price" to price, "type" to type, "res" to res, "res_cap" to res_cap,
                    "productivity" to productivity, "production" to production, "production_cap" to production_cap, "machine_state" to machine_state)
                    .whereArgs("id = {id}", "id" to id).exec()
        }
    }

    fun removeFactory(id: Int): Int {
        var result: Int = 0
        getInstance(ctx).use {
            result = delete("Factories", "id = {id}", "id" to id)
        }
        return result
    }

    fun addInventory(inv: Inventory) {
        getInstance(ctx).use {
            for (i in 0..(inv.getInventorySize() - 1)) {
                val slot = inv.getInventorySlotContents(i)
                insert(inv.name, "num" to i, "id" to slot.itemId, "stackSize" to slot.stackSize, "maxStackSize" to slot.maxStackSize)
            }
        }
    }


    fun updateInventory(inv: Inventory) {
        var result = 0;
        getInstance(ctx).use {
            for (i in 0..(inv.getInventorySize() - 1)) {
                val slot = inv.getInventorySlotContents(i)
                result = update(inv.name, "id" to slot.itemId, "stackSize" to slot.stackSize, "maxStackSize" to slot.maxStackSize).whereArgs("num = {num}", "num" to i).exec()
            }
        }
        Log.d("Added", result.toString())
    }

    fun getInventory(name: String): Inventory? {
        // var index : Int
        var id: Int
        var stackSize: Int
        var maxStackSize: Int

        var inv: Inventory? = null
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


    //For managing inventory

    fun addLaborExchangeWithProperties(name: String, age: Int, spec: String, quality: Int, nationality: String, salary: Int, dayOfBirth: String, monthOfBirth: String) {
        var res: Long = 0
        getInstance(ctx).use {
            res = insert("laborExchange",
                    "name" to name, "age" to age, "spec" to spec, "quality" to quality, "nationality" to nationality,
                    "salary" to salary, "dayOfBirth" to dayOfBirth, "monthOfBirth" to monthOfBirth)
        }
        Log.d("Added", "from Db  ${res}")
    }

    fun addLaborExchangeWithProperties(staff: Staff) {
        addLaborExchangeWithProperties(staff.name, staff.age, staff.prof, staff.quality, staff.nation, staff.salary, staff.birth.first, staff.birth.second)
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

    fun removeAllLabor() {
        getInstance(ctx).use {
            delete("laborExchange")
        }
    }

    fun removeAllStaff() {
        getInstance(ctx).use {
            delete("staff")
        }
    }

    fun removeInventory(name: String) {
        getInstance(ctx).use {
            delete(name)
        }
    }

}