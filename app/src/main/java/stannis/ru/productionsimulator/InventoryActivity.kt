package stannis.ru.productionsimulator

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_inventory.*
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Models.DatabaseFactory
import stannis.ru.productionsimulator.Models.Inventory
import stannis.ru.productionsimulator.Models.ItemStack

class InventoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

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

        inventory_name.text = "Инвентарь"

        val slots = Inventory.getInventory().inv
        val adapter = ItemAdapter(this, slots.toCollection(ArrayList()))

        inventory.adapter = adapter
    }
}

class ItemAdapter : BaseAdapter {

    var inv = ArrayList<ItemStack>()
    var context : Context? = null

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
