package stannis.ru.productionsimulator.Models

import android.content.Context
import android.util.Log
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase
import stannis.ru.productionsimulator.Enums.EnumFactory
import stannis.ru.productionsimulator.Functions.generateMessage
import stannis.ru.productionsimulator.Enums.Items
import stannis.ru.productionsimulator.Enums.ItemsBuy
import stannis.ru.productionsimulator.Enums.Nations
import stannis.ru.productionsimulator.Enums.Profs
import stannis.ru.productionsimulator.Functions.saveAllExceptInventory
import java.util.*

class DataTime(var currentDay: String, var currentMonth: String, var currentYear: String, var tookCreditToday: Int, var tookDepositToday: Int) {
    companion object {
        var instance: DataTime? = null
        fun getInstance(ctx: Context): DataTime {
            if (instance == null) {
                instance = PlayerStatsDatabase.getInstance(ctx).getDataTime()
            }
            return instance!!
        }

        fun save(ctx: Context) {
            PlayerStatsDatabase.getInstance(ctx).setDataTimeWithProperties(getInstance(ctx))
        }
    }

    fun nextDay(ctx: Context) {
        val ins = PlayerStatsDatabase.getInstance(ctx)
        var day = currentDay.toInt()
        if (day < 28) {
            if (day + 1 < 10) {
                currentDay = "0${day + 1}"
            } else {
                currentDay = "${day + 1}"
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
                } else {
                    currentDay = "${day + 1}"
                }
                "09", "11" -> if (day == 30) {
                    currentMonth = "${currentMonth.toInt() + 1}"

                } else {
                    currentDay = "${day + 1}"
                }
            }
        }
        checkCreditsDeposits(ctx)
        this.tookCreditToday = 0
        this.tookDepositToday = 0
        saveAllExceptInventory(ctx)
        Inventory.saveInventories(ctx)
        for (fac in Factory.factories) {
            if (fac != null) {
                fac.runTick(ctx)
            }
        }
        for (i in 0 until Factory.factories.size) {
            DatabaseFactory.index = i
            Inventory.setNulls()
            getAllWages(ctx)
            sellItems(ctx)
            checkBirthDays(ctx)
            generateBuyInv(ctx)
            generateLabor(ctx)
            Inventory.saveInventories(ctx)

        }
        Player.getInstance(ctx).money += MoneyForDay.getIns(ctx).sellings - MoneyForDay.getIns(ctx).wages
        generateMessage(ctx)

    }

    fun checkCreditsDeposits(ctx: Context) {
        val list = PlayerStatsDatabase.getInstance(ctx).getListOfCreditDeposit()
        for (crDep in list) {
            if (this.currentDay == crDep.date[0]) {
                crDep.rise(ctx)
//                if(crDep.type == 2){
//                    generateCreditBankMessage(crDep, ctx)
//                }
            }
        }
    }

    fun sellItems(ctx: Context) {
        val player = Player.getInstance(ctx)

        val invent = Inventory.load(ctx, "sell")
        var tmp = 0;
        var sum = 0
        if (invent != null) {
            for (v in invent.inv) {
                if (!v.isEmpty()) {
                    tmp++
                }
            }
            for (i in 0 until tmp) {
                val p = Random().nextInt(2)


                if (p == 0) {
                    var count = Random().nextInt((((player.reputation / 100.0) + 0.2) * invent.inv[i].stackSize).toInt() + 1)
                    Log.d("Sell", count.toString())
                    if (count > invent.inv[i].stackSize) {
                        count = invent.inv[i].stackSize
                    }
                    sum += count * (ItemsBuy.findById(invent.getInventorySlotContents(i).itemId).getItemPrice())
                    invent.decrStackSize(i, count)
                }
            }
        }
        MoneyForDay.getIns(ctx).sellings += sum
        Log.d("Sell", sum.toString())
        Inventory.inventories.put("sell", invent)

    }

    fun generateLabor(ctx: Context) {
        val ins = DatabaseFactory.getInstance(ctx)
        for (i in 1..3) {
            val sz = ins.getListOfLaborExchange().size
            val p = Random().nextInt(sz + 1)
            if (p == 0) {
                val age = 25 + Random().nextInt(30)
                val prof = Profs.findById(Random().nextInt(299) / 50)
                val nat = Nations.findById(Random().nextInt(410) / 80)
                val qual = nat.getAvQuality() - (Random().nextInt(5) - 3)
                val day = Random().nextInt(10) + 10
                val month = Random().nextInt(8) + 1
                val staff = Staff(PlayerStatsDatabase.getInstance(ctx).getName(), age, prof.getProff(), qual, nat.getNationality(), (qual / 12) * prof.getSalary(), Pair("$day", "0$month"))
                Log.d("Added", "paren Added")
                ins.addLaborExchangeWithProperties(staff)
            }
        }

    }

    fun generateBuyInv(ctx: Context) {
        var invent = Inventory.load(ctx, "buy")

        for (i in 1..3) {
            var tmp = 0;
            for (v in invent!!.inv) {
                if (!v.isEmpty()) {
                    tmp++
                }
            }

            val p = Random().nextInt(tmp + 1)
            if (p == 0) {
                val id = EnumFactory.findById(DatabaseFactory.index).res_type.itemId

                invent.setInventorySlotContents(invent.findFirstEqualSlot(Items.findById(id).getId()), ItemStack(Items.findById(id)
                        .itemId, invent.getInventorySlotContents(invent.findFirstEqualSlot(Items.findById(id).getId())).stackSize + Random().nextInt(14) + 1, invent.getInventoryStackLimit()))
            }
        }

        Inventory.inventories.put("buy", invent)
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

    fun getAllWages(ctx: Context) {
        var res = 0
        val list1 = DatabaseFactory.getInstance(ctx).getListOfStaff()
        for (st in list1) {
            res += st.salary
        }
        MoneyForDay.getIns(ctx).wages += res
    }

    override fun toString(): String = "${currentDay}.${currentMonth}.${currentYear}"


}