package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_factory.*
import kotlinx.android.synthetic.main.date_layout.*
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
        val curData = DatabaseFactory.getInstance(this).getDataTime()
        if(curData!=null){
            curDate.text = curData.toString()
        }
        endDay.setOnClickListener {
            val intent = Intent(this, EndDayActivity::class.java)
            startActivity(intent)
        }

        mail.setOnClickListener {
            val intent = Intent(this, MailActivity::class.java)
            startActivity(intent)
        }

        var factory = Factory.getFactoryById(0)//intent.getIntExtra("TAG", 0))

        if (factory == null)
            factory = Factory(0, EnumFactory.SAWMILL, 0, 10, 1, 2, 2, 5,  10.0)

        factory_name.text = EnumFactory.SAWMILL.getName()

        val data = arrayOf("Сырьё: ${factory.res.getInventorySlotContents(0).stackSize}/${factory.res.getInventoryStackLimit()}",
                "Потребление сырья: ${factory.consumption}/сек",
                "Выпуск продукции: ${factory.productivity}/сек",
                "Продукция: ${factory.production.getInventorySlotContents(0).stackSize}/${factory.production.getInventoryStackLimit()}",
                "Состояние оборудования: ${factory.machine_state}")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data)
        adapter.notifyDataSetChanged()
        stats.adapter = adapter

        fillRes.setOnClickListener {
            val inv = Inventory.getInventory()
            for (i in 0..(inv.getInventorySize() - 1)) {
                val item = inv.getInventorySlotContents(i)
                //Log.d("ITEM_1", item.toString())
                if (item.getType() == factory.type.getResType().getId()) {
                    //Log.d("ITEM_2", item.toString())
                    while (!factory.res.getInventorySlotContents(0).isStackFull() && item.stackSize > 0) {
                        //Log.d("ITEM_3", item.toString())
                        Inventory.transferItem(factory.type.getResType().getId(), inv, factory.res, i, 1)
                    }
                }
            }
        }

        storeProd.setOnClickListener {
            val inv = Inventory.getInventory()
            val i : Int? = inv.findFirstEqualSlot(factory.type.getResType().getId())
            if (i != null){
                while (!factory.production.isSlotEmpty(0) && inv.getInventorySlotContents(i).stackSize < Inventory.getInventory().getInventoryStackLimit())
                    Inventory.transferItem(factory.type.getProdType().getId(), factory.production, inv, 0, 1)
            }
            else
                Toast.makeText(this, "Ваш инвентарь заполнен", Toast.LENGTH_SHORT).show()
        }
    }
}
