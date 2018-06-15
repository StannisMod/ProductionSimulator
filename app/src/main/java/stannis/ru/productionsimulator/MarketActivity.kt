package stannis.ru.productionsimulator

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
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
        if (curData != null) {
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

        var tabSpec: TabHost.TabSpec = tabHost.newTabSpec("tag1")
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

        val slots =  Inventory.load(this, "buy")!!.inv

        val arrayList: ArrayList<ItemStack> = ArrayList()
        for (inv in slots) {
            if (!inv.isEmpty()) {
                arrayList.add(inv)
            }
        }
        val adapter = ItemAdapterBuy(this, arrayList/*slots.toCollection(ArrayList())*/)

        tvTab1.adapter = adapter


//        val slots1 =  Inventory.load(this, "sell")!!.inv
//
//        val arrayList1: ArrayList<ItemStack> = ArrayList()
//        for (inv in slots1) {
//            if (!inv.isEmpty()) {
//                arrayList1.add(inv)
//            }
//        }
//        val adapter1 = ItemAdapterSell(this, arrayList1/*slots.toCollection(ArrayList())*/)
//
//        tvTab2.adapter = adapter1


        var listview3: ListView = findViewById(R.id.tvTab3)
        val dataArray3 = DatabaseFactory.getInstance(this).getListOfLaborExchange()
        val adapter3 = ArrayAdapter<Staff>(this, android.R.layout.simple_list_item_1, dataArray3)

        listview3.adapter = adapter3
        listview3.setOnItemClickListener { adapterView, view, i, l ->
            val extra = "${dataArray3[i].name}.MarketWorker.$i"
            val intent = Intent(this, WorkerActivity::class.java)
            intent.putExtra("TAG", extra)
            startActivity(intent)

        }

        imageButton.setOnClickListener {
            val intent = Intent(this, BankActivity::class.java)
            startActivity(intent)

        }

    }

    companion object {
        fun kek(ctx: Context) {

        }
    }

}


class ItemAdapterBuy : BaseAdapter {

    var inv = ArrayList<ItemStack>()
    var context: Context? = null

    constructor(context: Context, inv: ArrayList<ItemStack>) : super() {
        this.context = context
        this.inv = inv
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = inv.get(position)
        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var itemView = inflator.inflate(R.layout.item_buy, null)

        itemView.imageItemSell.setImageResource(ItemsBuy.findById(item.itemId).getItemImage())
        itemView.nameBuy.text = "${ItemsBuy.findById(item.itemId).getName()} (${item.stackSize}"
        itemView.price.text = "${ItemsBuy.findById(item.itemId).getItemPrice()}$"
        itemView.buy.setOnClickListener {
            val player = DatabaseFactory.getInstance(context!!).getPlayerStats()
            if (player!!.money > ItemsBuy.findById(item.itemId).getItemPrice()) {
                player!!.money -= ItemsBuy.findById(item.itemId).getItemPrice()
                DatabaseFactory.getInstance(context!!).setPlayerWithProperties(player)
                Inventory.transferItem(Inventory.getInventory("buy"), Inventory.getInventory(), position, 1)
                Inventory.getInventory("buy").save(context!!)
                Inventory.getInventory().save(context!!)

                val intent = Intent(context!!, MarketActivity::class.java)
                ContextCompat.startActivity(context!!, intent, Bundle.EMPTY)
            }
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
class ItemAdapterSell : BaseAdapter {

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

        itemView.setOnClickListener {
            Toast.makeText(context!!, "Ваш товар продастся по окончанию дня (количество зависит от репутации)", Toast.LENGTH_SHORT).show()
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



