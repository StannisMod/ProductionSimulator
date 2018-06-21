package stannis.ru.productionsimulator.Models

import android.content.Context
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase

class MoneyForDay(var wages: Int, var sellings: Int) {
    companion object {
        var instance: MoneyForDay? = null
        fun getIns(ctx: Context): MoneyForDay {
            if (instance == null) {
                instance = load(ctx)
            }
            return instance!!
        }

        fun load(ctx: Context): MoneyForDay? {
            return PlayerStatsDatabase.getInstance(ctx).getMoneyForDay()
        }

        fun save(ctx: Context) {
            PlayerStatsDatabase.getInstance(ctx).setMoneyForDay(getIns(ctx))
        }
        fun clear(){
            instance = null
        }

    }

    fun setNull() {
        this.wages = 0
        this.sellings = 0
    }

    fun getAll(): Int = this.sellings - this.wages

    override fun toString(): String = "${this.wages} ${this.sellings}"
}