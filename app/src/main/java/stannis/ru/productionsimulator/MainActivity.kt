package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.date_layout.*
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Models.DatabaseFactory
import stannis.ru.productionsimulator.Models.Inventory
import stannis.ru.productionsimulator.Models.ItemStack
import stannis.ru.productionsimulator.Models.*
import stannis.ru.productionsimulator.R.id.debug

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!DatabaseFactory.getInstance(this).added) {
            DatabaseFactory.getInstance(this).removeLaborExchange("Леха")
            DatabaseFactory.getInstance(this).removeLaborExchange("Вася")
            DatabaseFactory.getInstance(this).removeLaborExchange("Петя")
            DatabaseFactory.getInstance(this).removeStaff("Леха")
            DatabaseFactory.getInstance(this).removeStaff("Вася")
            DatabaseFactory.getInstance(this).removeStaff("Петя")
            DatabaseFactory.getInstance(this).removePlayer()
            DatabaseFactory.getInstance(this).removeAllCredits()
            DatabaseFactory.getInstance(this).removeDataTime()
            DatabaseFactory.getInstance(this).addLaborExchangeWithProperties("Леха", 30, "Токарь", 10, "русский", 1200, "01", "01")
            DatabaseFactory.getInstance(this).addLaborExchangeWithProperties("Вася", 30, "Слесарь", 10, "русский", 1200, "01", "02")
            DatabaseFactory.getInstance(this).addLaborExchangeWithProperties("Петя", 30, "Дровосек", 10, "русский", 1200, "01", "03")
            DatabaseFactory.getInstance(this).addPlayerStatsWithProperties(500, 0, 0, 50, 0, 500)
            DatabaseFactory.getInstance(this).addDataTimeWithProperties("25", "12", "2018", 0,0, 0, 0)
            DatabaseFactory.getInstance(this).added = true
        }
        mail.setOnClickListener {
            val intent = Intent(this, MailActivity::class.java)
            startActivity(intent)
        }

        val player = DatabaseFactory.getInstance(this).getPlayerStats()
        if (player != null) {
            money.text = player.money.toString()
            res.text = player.stuff.toString()
            staff.text = player.staff.toString()
            rep.progress = player.reputation

        }
        val curData = DatabaseFactory.getInstance(this).getDataTime()
        if (curData != null) {
            curDate.text = curData.toString()
        }
        endDay.setOnClickListener {
            val intent = Intent(this, EndDayActivity::class.java)
            startActivity(intent)
        }

        toinventory.setOnClickListener {
            // val intent = Intent(this, InventoryActivity::class.java)
            // startActivity(inventory)
            Inventory.getInventory().setInventorySlotContents(0, ItemStack(1, 2, 64))
            DatabaseFactory.getInstance(this).addInventory(this, Inventory.getInventory())    //addFactoryWithProperties(this, 0, 0, 0, 10, 1, 2, 1, 5,  10.0)
            debug.text = DatabaseFactory.getInstance(this).getInventory(Inventory.getInventory().name)?.getInventorySlotContents(0).toString()//.getFactory(0)?.toDetailedString()
        }

        tomarket.setOnClickListener {
            val intent = Intent(this, MarketActivity::class.java)
            startActivity(intent)
        }

        stats_panel.setOnClickListener {
            // val intent = Intent(this, StatsActivity::class.java)
            // startActivity(stats)
        }

        tofactory.setOnClickListener {
            val intent = Intent(this, FactoryActivity::class.java)
            startActivity(intent)
        }
        topersonal.setOnClickListener {
            val intent = Intent(this, StaffActivity::class.java)
            startActivity(intent)
        }

    }
}
