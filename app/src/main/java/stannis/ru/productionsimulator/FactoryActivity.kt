package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_factory.*
import kotlinx.android.synthetic.main.date_layout.*
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase
import stannis.ru.productionsimulator.Enums.EnumFactory
import stannis.ru.productionsimulator.Functions.countProd_Cap
import stannis.ru.productionsimulator.Functions.countProductivity
import stannis.ru.productionsimulator.Functions.countRes_Cap
import stannis.ru.productionsimulator.Models.DataTime
import stannis.ru.productionsimulator.Models.Factory
import stannis.ru.productionsimulator.Models.Inventory
import stannis.ru.productionsimulator.Models.Player

class FactoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_factory)
        messageUnRead.visibility = if (PlayerStatsDatabase.getInstance(this).getMessage().size > 0) View.VISIBLE else View.INVISIBLE

        val player = Player.getInstance(this)
        if (player != null) {
            money.text = player.money.toString()
            rep.progress = player.reputation
        }
        rep.setEnabled(false)
        val curData = DataTime.getInstance(this)
        if (curData != null) {
            curDate.text = curData.toString()
        }
        endDay.setOnClickListener {
            val intent = Intent(this, EndDayActivity::class.java)
            startActivity(intent)
            finish()
        }

        mail.setOnClickListener {
            val intent = Intent(this, MailActivity::class.java)
            startActivity(intent)
        }

        var factory = Factory.getFactoryById(DatabaseFactory.index)//intent.getIntExtra("FACTORY_ID", 0))
        Log.d("WHATThe", factory!!.toDetailedString())
        factory.countParams(this)

        // if (factory == null)
        //  factory = Factory.load(this, intent.getIntExtra("FACTORY_ID", 0))


        factory_name.text = factory!!.type.getName()

        val data = arrayOf("Сырьё: ${factory!!.res.getInventorySlotContents(0).stackSize}/${factory!!.res.getInventoryStackLimit()}",
                /*"Потребление сырья: ${factory!!.consumption}/сек",*/
                "Выпуск продукции: ${factory!!.productivity}/5кг сырья",
                "Продукция: ${factory!!.production.getInventorySlotContents(0).stackSize}/${factory!!.production.getInventoryStackLimit()}",
                "Состояние оборудования: ${factory!!.machine_state}")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data)
        adapter.notifyDataSetChanged()
        stats.adapter = adapter

        fillRes.setOnClickListener {
            val inv = Inventory.getInventory()
            for (i in 0..(inv.getInventorySize() - 1)) {
                val item = inv.getInventorySlotContents(i)
                Log.d("ITEM_1", item.toString())
                if (item.getType() == factory!!.type.getResType().getId()) {
                    Log.d("ITEM_2", item.toString())
                    while (!factory!!.res.getInventorySlotContents(0).isStackFull() && item.stackSize > 0) {
                        Log.d("ITEM_3", item.toString())
                        Inventory.transferItem(inv, factory!!.res, i, 1)
                    }
                }
            }
            data.set(0, "Сырьё: ${factory!!.res.getInventorySlotContents(0).stackSize}/${factory!!.res.getInventoryStackLimit()}")
            adapter.notifyDataSetChanged()
        }

        storeProd.setOnClickListener {
            val inv = Inventory.getInventory()
            val i: Int? = inv.findFirstEqualSlot(factory!!.type.getResType().getId())
            Log.d("Store", i.toString())
            if (i != null) {
                while (!factory!!.production.isSlotEmpty(0) && inv.getInventorySlotContents(i).stackSize < Inventory.getInventory().getInventoryStackLimit())
                    Inventory.transferItem(factory!!.production, inv, 0, 1)
            } else
                Toast.makeText(this, "Ваш инвентарь заполнен", Toast.LENGTH_SHORT).show()

            data.set(2, "Продукция: ${factory!!.production.getInventorySlotContents(0).stackSize}/${factory!!.production.getInventoryStackLimit()}")
            adapter.notifyDataSetChanged()
        }
    }

    override fun onBackPressed() {

    }
}
