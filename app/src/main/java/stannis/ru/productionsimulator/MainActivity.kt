package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Models.DatabaseFactory
import stannis.ru.productionsimulator.Models.Factory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mail.setOnClickListener {
            val intent = Intent(this, MailActivity::class.java)
            startActivity(intent)
        }

        toinventory.setOnClickListener {
            DatabaseFactory.getInstance(this).addFactoryWithProperties(1, 1, 1, 1, 1, 1, 11, 1, 1.0)
            val fac : Factory?  = DatabaseFactory.getInstance(this).getFactory(1)
            if (fac != null) {
                Log.d("KEEEK", fac.toDetailedString());
            }
            // val intent = Intent(this, InventoryActivity::class.java)
            // startActivity(inventory)
        }

        tomarket.setOnClickListener {
            DatabaseFactory.getInstance(this).addFactoryWithProperties(1, 1, 1, 1, 1, 1, 11, 1, 1.0)
            val fac : Factory?  = DatabaseFactory.getInstance(this).getFactory(1)
            if (fac != null) {
                Log.d("KEEEK", fac.toDetailedString());
            }
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
