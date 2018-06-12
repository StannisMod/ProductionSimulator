package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Models.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!DatabaseFactory.getInstance(this).added) {
            DatabaseFactory.getInstance(this).removeLaborExchange("Леха")
            DatabaseFactory.getInstance(this).removeLaborExchange("Вася")
            DatabaseFactory.getInstance(this).removeLaborExchange("Петя")
            DatabaseFactory.getInstance(this).removePlayer()
            DatabaseFactory.getInstance(this).addLaborExchangeWithProperties("Леха", 30, "Токарь", 10, "русский", 1200, "01", "01")
            DatabaseFactory.getInstance(this).addLaborExchangeWithProperties("Вася", 30, "Слесарь", 10, "русский", 1200, "01", "02")
            DatabaseFactory.getInstance(this).addLaborExchangeWithProperties("Петя", 30, "Дровосек", 10, "русский", 1200, "01", "03")
            DatabaseFactory.getInstance(this).addPlayerStatsWithProperties(500, 0, 0, 50)
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

        toinventory.setOnClickListener {

            // val intent = Intent(this, InventoryActivity::class.java)
            // startActivity(inventory)
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

