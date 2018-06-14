package stannis.ru.productionsimulator

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_inventory.*
import kotlinx.android.synthetic.main.activity_market.*
import kotlinx.android.synthetic.main.date_layout.*
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.item_buy.view.*
import stannis.ru.productionsimulator.Models.Staff
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Models.DatabaseFactory
import stannis.ru.productionsimulator.Models.Inventory
import stannis.ru.productionsimulator.Models.ItemStack

class MarketActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market)
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
        rep.setEnabled(false)

        mail.setOnClickListener {
            val intent = Intent(this, MailActivity::class.java)
            startActivity(intent)
        }

        val tabHost: TabHost = findViewById(android.R.id.tabhost)
        tabHost.setup()

        var tabSpec : TabHost.TabSpec = tabHost.newTabSpec("tag1")
        tabSpec.setIndicator("Покупка")
        tabSpec.setContent(R.id.tvTab1)
        tabHost.addTab(tabSpec)

        tabSpec = tabHost.newTabSpec("tag2")
        tabSpec.setIndicator("Продажа")
        tabSpec.setContent(R.id.tvTab2)
        tabHost.addTab(tabSpec)

        tabSpec = tabHost.newTabSpec("tag3")
        tabSpec.setIndicator("Биржа труда")
        tabSpec.setContent(R.id.tvTab3)
        tabHost.addTab(tabSpec)

        val slots = Inventory.getInventory().inv
        val adapter = ItemAdapterBuy(this, slots.toCollection(ArrayList()))

        tvTab1.adapter = adapter


        var listview2 : ListView = findViewById(R.id.tvTab2)
        val dataArray2 = arrayOf( "Android", "IPhone", "Windows Phone", "BlackBerry")
        val adapter2 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataArray2)

        listview2.adapter = adapter2

        var listview3 : ListView = findViewById(R.id.tvTab3)
        val dataArray3 = DatabaseFactory.getInstance(this).getListOfLaborExchange()
        val adapter3 = ArrayAdapter<Staff>(this, android.R.layout.simple_list_item_1, dataArray3)

        listview3.adapter = adapter3
        listview3.setOnItemClickListener { adapterView, view, i, l ->
            val extra = "${dataArray3[i].name}.MarketWorker.$i"
            val intent = Intent(this, WorkerActivity::class.java)
            intent.putExtra("TAG", extra)
            startActivity(intent)

        }

        imageButton.setOnClickListener{
            val intent = Intent(this, BankActivity::class.java)
            startActivity(intent)

        }
    }
}
class ItemAdapterBuy : BaseAdapter {

    var inv = ArrayList<ItemStack>()
    var context : Context? = null

    constructor(context: Context, inv: ArrayList<ItemStack>) : super() {
        this.context = context
        this.inv = inv
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = inv.get(position)
        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var itemView = inflator.inflate(R.layout.item_buy, null)

        itemView.imageItemSell.setImageResource(Items.findById(item.itemId).getItemImage())
        itemView.nameBuy.text = Items.findById(item.itemId).getName()
        if (item.getType() != 0)
            itemView.price.text = "12$"
        itemView.buy.setOnClickListener {
           Toast.makeText(context, "KEK", Toast.LENGTH_SHORT).show()
        }

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


