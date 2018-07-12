package stannis.ru.productionsimulator

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_inventory.*
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.date_layout.*
import kotlinx.android.synthetic.main.item_buy.view.*
import kotlinx.android.synthetic.main.stats_panel.*
import kotlinx.android.synthetic.main.to_sell_view_alert_dialog.view.*
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase
import stannis.ru.productionsimulator.Enums.Items
import stannis.ru.productionsimulator.Models.*

class InventoryActivity : AppCompatActivity() {
    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)
        messageUnRead.visibility = if (Message.sizeOfUnRead() > 0) View.VISIBLE else View.INVISIBLE

        rep.setEnabled(false)
        mail.setOnClickListener {
            val intent = Intent(this, MailActivity::class.java)
            startActivity(intent)
        }
        val ins = PlayerStatsDatabase.getInstance(this)
        val player = Player.getInstance(this)
        if (player != null) {
            money.text = player.money.toString()
            rep.progress = player.reputation
        }
        val curData = DataTime.getInstance(this)
        if (curData != null) {
            curDate.text = curData.toString()
        }
        endDay.setOnClickListener {
            val intent = Intent(this, EndDayActivity::class.java)
            startActivity(intent)
        }


        inventory_name.text = "Инвентарь"

        Inventory.getInventory().normalize()
        var slots = Inventory.getInventory().inv
        val arrayList: ArrayList<ItemStack> = ArrayList()
        for (inv in slots) {
            if (!inv.isEmpty()) {
                arrayList.add(inv)
            }
        }
        val adapter = ItemAdapter(this, arrayList)

        inventory.adapter = adapter

        inventory.setOnItemClickListener { adapterView, view, i, l ->


            if (Inventory.getInventory().getInventorySlotContents(i).itemId == Items.getNumOfRepair()) {
                val fac = Factory.getFactoryById(DatabaseFactory.index)
                if (fac != null) {
                    fac.machine_state = 10.0
                    if (Inventory.getInventory().decrStackSize(i, 1)) {
                        startActivity(Intent(this, InventoryActivity::class.java))
                        finish()
                    }

                    Toast.makeText(this, "Вы починили вашу фабрику", Toast.LENGTH_SHORT).show()
                }
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("На продажу")
                val view = (this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.to_sell_view_alert_dialog, null)
                val item = Items.findById(Inventory.getInventory().inv[i].itemId)
                builder.setView(view)
                view.imageInventoryToSell.setImageResource(item.image)
                view.progressToSell.max = Inventory.getInventory().getInventorySlotContents(i).stackSize
                class tmp() : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        view.countToSell.text = "${view.progressToSell.progress}шт."
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {

                    }
                }
                view.progressToSell.setOnSeekBarChangeListener(tmp())
                builder.setNeutralButton("Отмена") { j, k -> }
                builder.setPositiveButton("Переместить") { j, k ->
                    if (Inventory.transferItem(Inventory.getInventory(), Inventory.getInventory("sell"), i, view.progressToSell.progress)) {
                        startActivity(Intent(this, InventoryActivity::class.java))
                        finish()
                    }else{

                        slots = Inventory.getInventory().inv
                        adapter.notifyDataSetChanged()
                    }
                }
                builder.create().show()
            }

        }

        inventory.setOnItemLongClickListener { adapterView, view, i, l ->
            if (Inventory.getInventory().decrStackSize(i, 1)) {
                startActivity(Intent(this, InventoryActivity::class.java))
                finish()
            }
            slots = Inventory.getInventory().inv
            adapter.notifyDataSetChanged()
            true
        }
    }
}

class ItemAdapter : BaseAdapter {

    var inv = ArrayList<ItemStack>()
    var context: Context? = null

    constructor(context: Context, inv: ArrayList<ItemStack>) : super() {
        this.context = context
        this.inv = inv
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = inv.get(position)
        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var itemView = inflator.inflate(R.layout.item, null)

        itemView.imageView.setImageResource(Items.findById(item.itemId).getItemImage())
        itemView.name.text = Items.findById(item.itemId).getName()
        if (item.getType() != 0)
            itemView.count.text = item.stackSize.toString()

        return itemView
    }

    override fun getItem(position: Int): Any {
        return inv.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return inv.size
    }

}
