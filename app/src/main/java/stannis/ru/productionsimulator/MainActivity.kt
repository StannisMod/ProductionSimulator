package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Models.DatabaseFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mail.setOnClickListener {
            // val intent = Intent(this, MailActivity::class.java)
            // startActivity(mail)
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
            DatabaseFactory.getInstance(this).addLaborExchangeWithProperties(this, "Petr", 0, "Tokar", 10, "Rus", 1200, "01", "02")
            Log.d("db", DatabaseFactory.getInstance(this).getLaborExchangeWithProperties(this, 0))

            // val intent = Intent(this, FactoryActivity::class.java)
            // startActivity(factory)
        }
        topersonal.setOnClickListener{
            val intent = Intent(this, StaffActivity::class.java)
            startActivity(intent)
        }

    }
}
