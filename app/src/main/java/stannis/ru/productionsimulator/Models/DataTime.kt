package stannis.ru.productionsimulator.Models

import android.content.Context
import android.util.Log
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase
import stannis.ru.productionsimulator.Enums.EnumFactory
import stannis.ru.productionsimulator.Functions.generateMessage
import stannis.ru.productionsimulator.Enums.Items
import stannis.ru.productionsimulator.Functions.generateWorker
import stannis.ru.productionsimulator.Functions.randomInRange
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

        fun clear() {
            instance = null
        }
    }

    fun nextDay(ctx: Context) {
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


        for (fac in Factory.factories) {
            if (fac != null) {
                fac.runTick(ctx)
            }
        }
        for (i in 0 until Factory.factories.size) {
            if (Factory.getFactoryById(i) != null && Factory.getFactoryById(i)!!.isBought == true) {
                DatabaseFactory.index = i
                getAllWages(ctx)
                sellItems(ctx)
                checkBirthDays()
                generateBuyInv()
                generateLabor(ctx)
            }

        }

        DatabaseFactory.index = 0
        Player.getInstance(ctx).money += MoneyForDay.getIns(ctx).sellings - MoneyForDay.getIns(ctx).wages


    }

    fun checkCreditsDeposits(ctx: Context) {
        var list = Credit_Deposit.instance[0]
        for (crDep in list) {
            if (this.currentDay == crDep.date[0]) {
                crDep.rise()
            }
        }
        list = Credit_Deposit.instance[1]
        for (crDep in list) {
            if (this.currentDay == crDep.date[0]) {
                crDep.rise()
            }
        }

    }

    fun sellItems(ctx: Context) {
        val player = Player.getInstance(ctx)

        val invent = Inventory.getInventory("sell")
        var tmp = 0;
        var sum = 0
        if (invent != null) {
            for (v in invent.inv) {
                if (!v.isEmpty()) {
                    tmp++
                }
            }
            for (i in 0 until tmp) {

                var count = Random().nextInt((((player.reputation / 100.0) + 0.5) * invent.inv[i].stackSize).toInt() + 1) + 1
                Log.d("Sell", count.toString())
                if (count > invent.inv[i].stackSize) {
                    count = invent.inv[i].stackSize
                }
                sum += count * (Items.generateSellPrice(invent.getInventorySlotContents(i).itemId, ctx))
                invent.decrStackSize(i, count)

            }
        }
        MoneyForDay.getIns(ctx).sellings += sum
        Log.d("Sell", sum.toString())


    }

    fun generateLabor(ctx: Context) {
        for (i in 1..3) {
            if (Random().nextInt(17 - Worker.sizeOfLabor()) == 1) {
                if (Worker.sizeOfLabor() > 0) {
                    Worker.getListOfLabor().remove(Worker.getListOfLabor()[Random().nextInt(Worker.sizeOfLabor())])
                }
            }
        }
        for (i in 1..3) {
            Log.d("WORKERi", i.toString())
            val sz = Worker.sizeOfLabor()
            Log.d("WORKER", sz.toString())
            val p = Random().nextInt(sz + 1)
            if (p == 0) {
                generateWorker()
            }
            // Log.d("WORKER", Worker.sizeOfLabor().toString())
        }

    }

    fun generateBuyInv() {
        var invent = Inventory.getInventory("buy")
        if (invent != null) {
            var tmp = 0;
            for (v in invent!!.inv) {
                if (!v.isEmpty()) {
                    tmp++
                }
            }
            if (Random().nextInt(17 - tmp) == 1) {
                val randId = Random().nextInt(tmp + 1)
                invent.decrStackSize(randId, invent.getInventorySlotContents(randId).stackSize)
            }
            val id = EnumFactory.findById(DatabaseFactory.index).res_type.itemId

            invent.setInventorySlotContents(invent.findFirstEqualSlot(Items.findById(id).getId()), ItemStack(Items.findById(id)
                    .itemId, invent.getInventorySlotContents(invent.findFirstEqualSlot(Items.findById(id).getId())).stackSize + Random().nextInt(5) + 10, invent.getInventoryStackLimit()))

            var p = Random().nextInt(tmp + 2)
            if (p == 0) {
                val id = randomInRange(Items.getNumOfProdCap())

                invent.setInventorySlotContents(invent.findFirstEqualSlot(Items.findById(id).getId()), ItemStack(Items.findById(id)
                        .itemId, invent.getInventorySlotContents(invent.findFirstEqualSlot(Items.findById(id).getId())).stackSize + Random().nextInt(2) + 1, invent.getInventoryStackLimit()))
            }
            p = Random().nextInt(tmp + 3)
            if (p == 0) {
                val id = randomInRange(Items.getNumOfResCap())

                invent.setInventorySlotContents(invent.findFirstEqualSlot(Items.findById(id).getId()), ItemStack(Items.findById(id)
                        .itemId, invent.getInventorySlotContents(invent.findFirstEqualSlot(Items.findById(id).getId())).stackSize + Random().nextInt(2) + 1, invent.getInventoryStackLimit()))

            }
            p = Random().nextInt(tmp + 4)
            if (p == 0) {
                val id = Items.getNumOfRepair()

                invent.setInventorySlotContents(invent.findFirstEqualSlot(Items.findById(id).getId()), ItemStack(Items.findById(id)
                        .itemId, invent.getInventorySlotContents(invent.findFirstEqualSlot(Items.findById(id).getId())).stackSize + Random().nextInt(2) + 1, invent.getInventoryStackLimit()))

            }

        }


    }

    fun checkBirthDays() {

        val list1 = Worker.getListOfStaff()
        for (st in list1) {
            if (this.currentDay == st.birth.first && this.currentMonth == st.birth.second) {
                st.birth_Day()
            }
        }
        val list2 = Worker.getListOfLabor()
        for (st in list2) {
            if (this.currentDay == st.birth.first && this.currentMonth == st.birth.second) {
                st.birth_Day()
            }
        }

    }

    fun getAllWages(ctx: Context) {
        var res = 0
        val list1 = Worker.getListOfStaff()
        for (st in list1) {
            res += st.salary
        }
        MoneyForDay.getIns(ctx).wages += res
    }

    override fun toString(): String = "${currentDay}.${currentMonth}.${currentYear}"


}