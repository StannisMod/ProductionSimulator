package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Models.DatabaseFactory
import stannis.ru.productionsimulator.Models.Inventory
import stannis.ru.productionsimulator.Models.ItemStack

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mail.setOnClickListener {
             val intent = Intent(this, MailActivity::class.java)
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
        topersonal.setOnClickListener{
            val intent = Intent(this, StaffActivity::class.java)
            startActivity(intent)
        }


    }
}
