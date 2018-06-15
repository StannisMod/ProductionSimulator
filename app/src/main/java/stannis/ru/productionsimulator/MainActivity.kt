package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.date_layout.*
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Models.DatabaseFactory
import stannis.ru.productionsimulator.Models.Inventory
import stannis.ru.productionsimulator.Models.ItemStack
import stannis.ru.productionsimulator.Models.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Inventory.instance == null)
            Inventory.instance = Inventory.load(this, Inventory.TAG)

        if (Inventory.inventories.get("sell") == null)
            Inventory.inventories.put("sell", Inventory.load(this, "sell"))
        if (Inventory.inventories.get("buy") == null)
            Inventory.inventories.put("buy", Inventory.load(this, "buy"))


        Log.d("Inv_main", Inventory.instance?.getInventorySlotContents(0).toString())


        mail.setOnClickListener {
            val intent = Intent(this, MailActivity::class.java)
            startActivity(intent)
        }
        rep.setEnabled(false)

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
            finish()
        }

        toinventory.setOnClickListener {

            val intent = Intent(this, InventoryActivity::class.java)
            startActivity(intent)
        }

        tomarket.setOnClickListener {
            val intent = Intent(this, MarketActivity::class.java)
            startActivity(intent)
        }

        stats_panel.setOnClickListener {
            // val intent = Intent(this, StatsActivity::class.java)
            // startActivity(stats)
            Inventory.getInventory().setInventorySlotContents(Inventory.getInventory().findFirstEqualSlot(Items.WOOD.getId()), ItemStack(Items.WOOD.getId(), 32, 64))
        }

        tofactory.setOnClickListener {
            val intent = Intent(this, FactoryActivity::class.java)
            intent.putExtra("FACTORY_ID", 0)
            startActivity(intent)
        }
        topersonal.setOnClickListener {
            val intent = Intent(this, StaffActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onStop() {
        super.onStop()
        Log.d("Shutdown thread", "Destroy!")
        // Inventory.getInventory().save(this)
        Inventory.saveInventories(this)
        Factory.saveFactories(this)
    }
}
