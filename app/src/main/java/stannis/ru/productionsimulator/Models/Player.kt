package stannis.ru.productionsimulator.Models

import android.content.Context
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase

class Player(var money: Int, var stuff: Int, var staff: Int, var reputation: Int, var tax: Int) {
    companion object {
        var instance: Player? = null
        fun getInstance(ctx: Context): Player {
            if (instance == null) {
                instance = PlayerStatsDatabase.getInstance(ctx).getPlayerStats()
            }
            return instance!!
        }

        fun save(ctx: Context) {
            PlayerStatsDatabase.getInstance(ctx).setPlayerWithProperties(getInstance(ctx))
        }
        fun clear(){
            instance = null
        }
    }

    fun countReputationChange(paid: Int) {
        val diff = paid - tax
        var per = diff.toDouble() / tax.toDouble()
        val coef = 2.3
        if (per < 0) {
            val tmp = reputation.toDouble() * ((coef / 2.0) * (1.0 + per))
            reputation = tmp.toInt()
        } else {

            val tmp = if (reputation < 20) (reputation.toDouble() + 10) * (1.0 + 0.1 + per / 8.0) else (reputation.toDouble()) * (1.0 + 0.05 + per / 15.0)
            reputation = tmp.toInt()
        }
        if (reputation < 0) {
            reputation = 0
        }
        if (reputation > 100) {
            reputation = 100
        }
    }

    fun countCreditPercent(sum: Int): Double {
        var percent = 12.0
        val bigSum = Math.sqrt(sum.toDouble() / 10000.0).toInt() / 100.0
        percent -= bigSum

        percent *= (1.0 + ((50 - reputation) / 100.0))

        return percent
    }

    fun countDepositPercent(sum: Int): Double {
        var percent = 2.0
        val bigSum = Math.sqrt(sum.toDouble() / 100000.0).toInt() / 1000.0
        percent -= bigSum
        percent *= (1.0 + ((reputation - 50) / 130.0))
        return percent

    }

}