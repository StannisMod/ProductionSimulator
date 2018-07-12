package stannis.ru.productionsimulator.Databases

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import stannis.ru.productionsimulator.Models.*
import java.util.*

class PlayerStatsDatabase(val ctx: Context) : ManagedSQLiteOpenHelper(ctx, "PlayerStats", null, 4) {
    companion object {
        var instance: PlayerStatsDatabase? = null
        @Synchronized
        fun getInstance(ctx: Context): PlayerStatsDatabase {
            if (instance == null) {
                instance = PlayerStatsDatabase(ctx.applicationContext)
            }
            return instance!!
        }
    }

    var sz = 0
    val MESSAGE_NAME = "Message"
    val MESSAGE_READED_NAME = "MessageReaded"
    val PLAYER_STATS_NAME = "PlayerStats"
    val DATA_TIME_NAME = "DataTime"
    val CREDIT_DEPOSIT_NAME = "creditDeposit"
    val MONEY_FOR_DAY_NAME = "moneyForDay"
    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(MESSAGE_NAME, true,
                "hash" to INTEGER,
                "caption" to TEXT,
                "sender" to TEXT,
                "text" to TEXT,
                "day" to TEXT,
                "month" to TEXT,
                "year" to TEXT,
                "readed" to TEXT
        )
        db.createTable(MESSAGE_READED_NAME, true,
                "hash" to INTEGER,
                "caption" to TEXT,
                "sender" to TEXT,
                "text" to TEXT,
                "day" to TEXT,
                "month" to TEXT,
                "year" to TEXT
        )
        db.createTable(PLAYER_STATS_NAME, true,
                "money" to INTEGER,//Весь Integer
                "stuff" to INTEGER,//>=0
                "staff" to INTEGER,//>=0
                "reputation" to INTEGER,
                "tax" to INTEGER)//-100<= =>100
        db.createTable(DATA_TIME_NAME, true,
                "currentDay" to TEXT,
                "currentMonth" to TEXT,
                "currentYear" to TEXT,
                "tookCreditToday" to INTEGER,
                "tookDepositToday" to INTEGER

        )
        db.createTable(CREDIT_DEPOSIT_NAME, true,
                "type" to INTEGER,
                "amount" to INTEGER,
                "percent" to REAL,
                "dayOfStart" to TEXT,
                "monthOfStart" to TEXT,
                "yearOfStart" to TEXT
        )

        db.createTable(MONEY_FOR_DAY_NAME, true,
                "wages" to INTEGER,
                "sellings" to INTEGER)


    }

    override fun onUpgrade(db: SQLiteDatabase, newVersion: Int, oldVersion: Int) {
        db.dropTable(CREDIT_DEPOSIT_NAME, true)
        db.dropTable(MESSAGE_NAME, true)
        db.dropTable(MESSAGE_READED_NAME, true)
        db.dropTable(PLAYER_STATS_NAME, true)
        db.dropTable(DATA_TIME_NAME, true)
        db.dropTable("Names", true)
        db.dropTable(MONEY_FOR_DAY_NAME, true)
        onCreate(db)

    }

    fun removeAllMessage() {
        PlayerStatsDatabase.getInstance(ctx).use {
            delete(MESSAGE_NAME)
        }
    }

    fun removeAllMessageReaded() {
        PlayerStatsDatabase.getInstance(ctx).use {
            delete(MESSAGE_READED_NAME)
        }
    }

    fun addMessageWithProperties(message: Message) {

        PlayerStatsDatabase.getInstance(ctx).use {
            insert(MESSAGE_NAME,
                    "hash" to message.hashCode(), "caption" to message.caption, "sender" to message.sender, "text" to message.text, "day" to message.date[0], "month" to message.date[1], "year" to message.date[2])
        }
    }


    fun addMessageReadedWithProperties(message: Message) {

        PlayerStatsDatabase.getInstance(ctx).use {
            insert(MESSAGE_READED_NAME,
                    "hash" to message.hashCode(), "caption" to message.caption, "sender" to message.sender, "text" to message.text, "day" to message.date[0], "month" to message.date[1], "year" to message.date[2])
        }
    }



    fun getMessage(): ArrayList<Message> {
        val query = "SELECT * FROM ${MESSAGE_NAME}"
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

                list.add(Message(caption = caption, text = text, date = arrayOf(day, month, year), sender = sender))
            } while (cursor.moveToNext())
            cursor.close()
        }
        db.close()
        return list
    }

    fun getMessageReaded(): ArrayList<Message> {
        val query = "SELECT * FROM ${MESSAGE_READED_NAME}"
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
                list.add(Message(caption = caption, text = text, date = arrayOf(day, month, year), sender = sender))
            } while (cursor.moveToNext())
            cursor.close()
        }
        db.close()
        return list
    }

    fun addCrDepWithProperties(type: Int, amount: Int, percent: Double, dayOfStart: String, monthOfStart: String, yearOfStart: String) {
        PlayerStatsDatabase.getInstance(ctx).use {
            insert(CREDIT_DEPOSIT_NAME,
                    "type" to type, "amount" to amount, "percent" to percent, "dayOfStart" to dayOfStart, "monthOfStart" to monthOfStart,
                    "yearOfStart" to yearOfStart)
        }
    }

    fun getListOfCreditDeposit(): ArrayList<Credit_Deposit> {
        val query = "SELECT * FROM ${CREDIT_DEPOSIT_NAME}"
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

        val query = "SELECT * FROM ${CREDIT_DEPOSIT_NAME} WHERE (type = \"$type\" AND dayOfStart = \"$dayOfStart\" AND monthOfStart = \"$monthOfStart\" AND yearOfStart = \"$yearOfStart\")"
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
        PlayerStatsDatabase.getInstance(ctx).use {
            update(CREDIT_DEPOSIT_NAME, "amount" to amount, "percent" to percent)
                    .whereArgs("(type = {type}) and (dayOfStart = {dayOfStart}) and (monthOfStart = {monthOfStart}) and (yearOfStart = {yearOfStart})", "type" to type, "dayOfStart" to dayOfStart, "monthOfStart" to monthOfStart, "yearOfStart" to yearOfStart).exec()
        }
    }

    fun setCreditDepositProperties(crDep: Credit_Deposit) {
        setCreditDepositProperties(crDep.type, crDep.amount, crDep.percent, crDep.date[0], crDep.date[1], crDep.date[2])
    }

    fun removeCreditDeposit(type: Int, dayOfStart: String, monthOfStart: String, yearOfStart: String): Int {
        var result: Int = 0
        PlayerStatsDatabase.getInstance(ctx).use {
            result = delete(CREDIT_DEPOSIT_NAME, "(type = {type}) and (dayOfStart = {dayOfStart}) and (monthOfStart = {monthOfStart}) and (yearOfStart = {yearOfStart})", "type" to type, "dayOfStart" to dayOfStart, "monthOfStart" to monthOfStart, "yearOfStart" to yearOfStart)
        }
        return result
    }

    fun removeAllCredits() {
        for (cDep in getListOfCreditDeposit()) {
            removeCreditDeposit(cDep.type, cDep.date[0], cDep.date[1], cDep.date[2])
        }
    }

    fun addPlayerStatsWithProperties(money: Int, stuff: Int, staff: Int, reputation: Int, nalog: Int) {
        PlayerStatsDatabase.getInstance(ctx).use {
            insert(PLAYER_STATS_NAME, "money" to money, "stuff" to stuff, "staff" to staff, "reputation" to reputation, "tax" to nalog)
        }
    }

    fun getPlayerStats(): Player? {
        val query = "SELECT*FROM ${PLAYER_STATS_NAME}"
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

            val nalog = cursor.getString(i).toInt()
            player = Player(money, stuff, staff, reputation, nalog)
            cursor.close()

        }
        db.close()
        return player
    }

    fun setPlayerWithProperties(money: Int, stuff: Int, staff: Int, reputation: Int, nalog: Int) {
        PlayerStatsDatabase.getInstance(ctx).use {
            update(PLAYER_STATS_NAME, "money" to money, "stuff" to stuff, "staff" to staff, "reputation" to reputation, "tax" to nalog).exec()
        }
    }

    fun setPlayerWithProperties(player: Player) {
        setPlayerWithProperties(player.money, player.stuff, player.staff, player.reputation, player.tax)
    }

    fun removePlayer(): Int {
        var res = 0
        PlayerStatsDatabase.getInstance(ctx).use {
            res = delete(PLAYER_STATS_NAME)
        }
        return res
    }

    fun addDataTimeWithProperties(currentDay: String, currentMonth: String, currentYear: String, tookCreditToday: Int, tookDepositToday: Int) {
        PlayerStatsDatabase.getInstance(ctx).use {
            insert(DATA_TIME_NAME, "currentDay" to currentDay, "currentMonth" to currentMonth, "currentYear" to currentYear, "tookCreditToday" to tookCreditToday, "tookDepositToday" to tookDepositToday)
        }
    }

    fun setDataTimeWithProperties(currentDay: String, currentMonth: String, currentYear: String, tookCreditToday: Int, tookDepositToday: Int) {
        PlayerStatsDatabase.getInstance(ctx).use {
            update(DATA_TIME_NAME, "currentDay" to currentDay, "currentMonth" to currentMonth, "currentYear" to currentYear, "tookCreditToday" to tookCreditToday, "tookDepositToday" to tookDepositToday).exec()
        }
    }

    fun setDataTimeWithProperties(currentData: DataTime) {
        setDataTimeWithProperties(currentData.currentDay, currentData.currentMonth, currentData.currentYear, currentData.tookCreditToday, currentData.tookDepositToday)
    }

    fun getDataTime(): DataTime? {
        val query = "SELECT * FROM ${DATA_TIME_NAME}"
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

            curDate = DataTime(currentDay, currentMonth, currentYear, tookCreditToday, tookDepositToday)
            cursor.close()
        }
        db.close()
        return curDate
    }

    fun removeDataTime(): Int {
        var res = 0
        PlayerStatsDatabase.getInstance(ctx).use {
            res = delete(DATA_TIME_NAME)
        }
        return res
    }



    fun addMoneyForDay(wages: Int, sellings: Int) {
        getInstance(ctx).use {
            insert(MONEY_FOR_DAY_NAME, "wages" to wages, "sellings" to sellings)
        }
    }

    fun addMoneyForDay(mfd: MoneyForDay) {
        addMoneyForDay(mfd.wages, mfd.sellings)
    }

    fun getMoneyForDay(): MoneyForDay? {
        val query = "SELECT*FROM ${MONEY_FOR_DAY_NAME}"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var mfd: MoneyForDay? = null
        if (cursor.moveToFirst()) {
            cursor.moveToFirst()
            var i = 0
            val wages = cursor.getString(i).toInt()
            i++
            val sellings = cursor.getString(i).toInt()
            i++
            mfd = MoneyForDay(wages, sellings)
            cursor.close()
        }
        db.close()
        return mfd

    }

    fun setMoneyForDay(wages: Int, sellings: Int) {
        getInstance(ctx).use {
            update(MONEY_FOR_DAY_NAME, "wages" to wages, "sellings" to sellings).exec()
        }
    }

    fun setMoneyForDay(mfd: MoneyForDay) {
        setMoneyForDay(mfd.wages, mfd.sellings)
    }

    fun removeMoneyForDay() {
        getInstance(ctx).use {
            delete(MONEY_FOR_DAY_NAME)
        }
    }

}