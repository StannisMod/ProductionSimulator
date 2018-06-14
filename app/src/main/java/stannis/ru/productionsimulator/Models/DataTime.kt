package stannis.ru.productionsimulator.Models

import android.content.Context
import java.time.Year

class DataTime(var currentDay: String, var currentMonth: String, var currentYear: String, var tookCreditToday: Int, var tookDepositToday: Int, var todaysCreditMinus:Int, var todaysDepositGain:Int) {
    fun nextDay(ctx: Context) {
        var day = currentDay.toInt()
        if (day < 28) {
            if (day + 1 < 10) {
                currentDay = "0${day + 1}"
            } else {
                currentDay = "${day+1}"
            }
        } else {
            when (currentMonth) {
                "02" -> if (currentYear.toInt() % 4 == 0 && day == 28) {
                    currentDay = "29"
                } else {
                    currentMonth = "03";currentDay = "01"
                }
                "01", "03", "05", "07", "08" -> if (day == 31) {
                    currentMonth = "0${currentMonth.toInt() + 1}";currentDay = "01"
                } else {
                    currentDay = "${day + 1}"
                }
                "10", "12" -> if (day == 31) {
                    if (currentMonth == "12") {
                        currentMonth = "01"
                        currentYear = "${currentYear.toInt() + 1}"
                    } else {
                        currentMonth = "11"
                    }
                    currentDay = "01"
                } else {
                    currentDay = "${day + 1}"
                }
                "04", "06" -> if (day == 30) {
                    currentMonth = "0${currentMonth.toInt() + 1}"
                    currentDay = "01"
                }
                "09", "11" -> if (day == 30) {
                    currentMonth = "${currentMonth.toInt() + 1}"

                }
            }
        }
        checkCreditsDeposits(ctx)
        checkBirthDays(ctx)
        this.todaysDepositGain=0
        this.todaysCreditMinus=0
        this.tookCreditToday = 0
        this.tookDepositToday = 0
        val fac = DatabaseFactory.getInstance(ctx).getFactory(0)
        if(fac!=null){
            fac.runTick()
        }
        DatabaseFactory.getInstance(ctx).setDataTimeWithProperties(this)
    }

    fun checkCreditsDeposits(ctx: Context) {
        val list = DatabaseFactory.getInstance(ctx).getListOfCreditDeposit()
        for (crDep in list) {
            if (this.currentDay == crDep.date[0]) {
                crDep.rise(ctx)
                if(crDep.type == 2){
                    generateCreditBankMessage(crDep, ctx)
                }
            }
        }
    }

    fun checkBirthDays(ctx: Context) {
        val ins = DatabaseFactory.getInstance(ctx)
        val list1 = ins.getListOfStaff()
        for (st in list1) {
            if (this.currentDay == st.birth.first && this.currentMonth == st.birth.second) {
                st.birth_Day(false, ctx)
            }
        }
        val list2 = ins.getListOfLaborExchange()
        for (st in list2) {
            if (this.currentDay == st.birth.first && this.currentMonth == st.birth.second) {
                st.birth_Day(true, ctx)
            }
        }

    }
    fun getAllWages(ctx:Context):Int{
        val list1 = DatabaseFactory.getInstance(ctx).getListOfStaff()
        var res = 0
        for (st in list1) {
            res+=st.salary
        }
        return res
    }

    override fun toString(): String = "${currentDay}.${currentMonth}.${currentYear}"


}