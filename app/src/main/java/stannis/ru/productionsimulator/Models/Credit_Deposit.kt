package stannis.ru.productionsimulator.Models

import android.content.Context
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase
import stannis.ru.productionsimulator.Functions.isEqualDate

class Credit_Deposit(var amount: Int, var percent: Double, var date: Array<String>, var type: Int) {
    companion object {
        fun setBegin() {
            instance = Array(2) { i -> ArrayList<Credit_Deposit>() }
        }

        var instance: Array<ArrayList<Credit_Deposit>> = emptyArray()

        fun getListOfCredit_Deposit():ArrayList<Credit_Deposit> {
            val list = ArrayList<Credit_Deposit>()
            for (cr in instance[0]) {
                list.add(cr)
            }
            for (cr in instance[1]) {
                list.add(cr)
            }
            return list
        }

        fun loadAll(ctx: Context) {
            for (cr in PlayerStatsDatabase.getInstance(ctx).getListOfCreditDeposit()) {
                instance[cr.type].add(cr)
            }
        }

        fun getCr_Dep(date: Array<String>, type: Int): Credit_Deposit? {
            for (crdep in instance[type]) {
                if (isEqualDate(crdep.date, date)) {
                    return crdep
                }
            }
            return null
        }

        fun saveAll(ctx: Context) {
            for (al in instance) {
                for (cr in al) {
                    cr.save(ctx)
                }
            }
        }

    }

    fun takeDep_payOff(sum: Int): Boolean {
        if (sum >= this.amount) {
            this.amount = 0
            return true
        }
        this.amount -= sum;
        return false

    }


    override fun toString(): String {
        if (type == 1) {
            return "Ваш депозит состовляет ${amount}$, а ставка ${percent}% \n" +
                    "Дата открытия: ${date[0]}.${date[1]}.${date[2]}"
        }
        return "Ваш кредит состовляет ${amount}$, а ставка ${percent}% \n" +
                "Дата открытия: ${date[0]}.${date[1]}.${date[2]}"
    }


    fun rise() {
        this.amount += (this.amount * (this.percent / 100.0)).toInt()
    }

    fun save(ctx: Context) {
        if (PlayerStatsDatabase.getInstance(ctx).getCreditDeposit(this.type, this.date[0], this.date[1], this.date[2]) == null) {
            PlayerStatsDatabase.getInstance(ctx).addCrDepWithProperties(this.type, this.amount, this.percent, this.date[0], this.date[1], this.date[2])
        } else {
            PlayerStatsDatabase.getInstance(ctx).setCreditDepositProperties(crDep = this)
        }
    }

    fun add() {
        instance[this.type].add(this)
    }

    fun remove() {
        for (cr in instance[this.type]) {
            if (isEqualDate(cr.date, this.date)) {
                instance[this.type].remove(cr)
            }
        }
    }


}