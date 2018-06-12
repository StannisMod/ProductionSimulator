package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_factory.*
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Models.DatabaseFactory
import stannis.ru.productionsimulator.Models.Factory
import stannis.ru.productionsimulator.Models.Inventory

class FactoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_factory)
        val player = DatabaseFactory.getInstance(this).getPlayerStats()
        if (player != null) {
            money.text = player.money.toString()
            res.text = player.stuff.toString()
            staff.text = player.staff.toString()
            rep.progress = player.reputation
        }

        mail.setOnClickListener {
            val intent = Intent(this, MailActivity::class.java)
            startActivity(intent)
        }

        // val factory = Factory.getFactoryById(intent.getIntExtra("TAG", 0))

        val factory = Factory(0, EnumFactory.SAWMILL, 0, 10, 1, 2, 1, 5,  10.0)
        val data = arrayOf("Сырьё: ${factory.res}/${factory.res.getInventoryStackLimit()}",
                "Потребление сырья: ${factory.consumption}/сек",
                "Выпуск продукции: ${factory.productivity}/сек",
                "Продукция: ${factory.production}/${factory.production_cap}",
                "Состояние оборудования: ${factory.machine_state}")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data)
        stats.adapter = adapter

        fillRes.setOnClickListener {
            val inv = Inventory.getInventory()
            for (i in 0..inv.getInventorySize()) {
                var item = inv.getInventorySlotContents(i)
                if (item.getType() == factory.type.getResType())
                    while (!factory.res.getInventorySlotContents(0).isStackFull() && item.stackSize > 0)
                        Inventory.transferItem(inv, factory.res, i, 1)   // transfer 1 item from item to factory.res
            }
        }

        storeProd.setOnClickListener {
            /*
            val inv = inventory.getInventory()
            for (item : inv)
                if (item == null)
                    while (factory.production > 0 && item.stackSize < inventory.getInventoryStackLimit())
                        Inventory.transferItem(factory.production, item, slotIndex, 1)   // transfer 1 item from item to factory.res
              */
        }
    }
}