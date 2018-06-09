package stannis.ru.productionsimulator.Models

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import org.jetbrains.anko.db.*
import stannis.ru.productionsimulator.EnumFactory

class DatabaseFactory(val ctx : Context) : ManagedSQLiteOpenHelper(ctx, "ProductionSimulatorDB", null, 5) {

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
                "class" to INTEGER,
                "nationality" to TEXT,
                "salary" to INTEGER)

        db.createTable("buy", true,
                "name" to TEXT,
                "id" to INTEGER,
                "price" to INTEGER)

        db.createTable(Inventory.getInventory().name, true,
                "num" to INTEGER,
                "id" to INTEGER,
                "stackSize" to INTEGER,
                "maxStackSize" to INTEGER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d("MAIN", "Dropped!")
        db.dropTable("Factories", true)
        db.dropTable("laborExchange", true)
        db.dropTable("buy", true)
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

    // For managing inventories

    fun addInventory(ctx : Context, inv : Inventory) {
        getInstance(ctx).use {
            for (i in 0..(inv.getInventorySize() - 1)) {
                val slot = inv.getInventorySlotContents(i)
                insert(inv.name, "num" to i, "id" to slot.itemId, "stackSize" to slot.stackSize, "maxStackSize" to slot.maxStackSize)
            }
        }
    }

    fun updateInventory(inv : Inventory) {
        getInstance(ctx).use {
            for (i in 0..inv.getInventorySize()) {
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
            }
            while (cursor.moveToNext())
            inv = Inventory(name, slots.size, maxStackSize)
            for (i in 0..(slots.size - 1))
                inv.setInventorySlotContents(i, slots.get(i))

            cursor.close()
        }
        db.close()

        return inv
    }

    fun removeInventory(name : String) {
        val db = this.writableDatabase
        db.dropTable(name, true)
        db.close()
    }
}