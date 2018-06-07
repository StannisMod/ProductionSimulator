package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_factory.*
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Models.DatabaseFactory
import stannis.ru.productionsimulator.Models.Factory

class FactoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_factory)

        mail.setOnClickListener {
            val intent = Intent(this, MailActivity::class.java)
            startActivity(intent)
        }

        // val factory = Factory.getFactoryById(intent.getIntExtra("TAG", 0))

        val factory = Factory(0, 0, 0, 10, 100, 2, 1, 5, 100, 10)
        val data = arrayOf("Сырьё: ${factory.res}/${factory.res_cap}",
                "Потребление сырья: ${factory.consumption}/сек",
                "Выпуск продукции: ${factory.productivity}/сек",
                "Продукция: ${factory.production}/${factory.production_cap}",
                "Состояние оборудования: ${factory.machine_stat}")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data)
        stats.adapter = adapter

        fillRes.setOnClickListener {
            /*
            val inv = inventory.getInventory()
            for (item : inv)
                if (item.getType() == factory.res_type)
                    while (factory.res < factory.res_cap && item.stackSize > 0)
                        Inventory.transferItem(item, factory.res, 1)   // transfer 1 item from item to factory.res

              */
        }

        storeProd.setOnClickListener {
            /*
            val inv = inventory.getInventory()
            for (item : inv)
                if (item == null)
                    while (factory.production > 0 && item.stackSize < inventory.getInventoryStackLimit())
                        Inventory.transferItem(factory.production, item, 1)   // transfer 1 item from item to factory.res

              */
        }
    }
}
