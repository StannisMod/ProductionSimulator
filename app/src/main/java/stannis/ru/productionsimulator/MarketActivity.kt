package stannis.ru.productionsimulator

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_market.*
import kotlinx.android.synthetic.main.date_layout.*
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.item_buy.view.*
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase
import stannis.ru.productionsimulator.Enums.Items
import stannis.ru.productionsimulator.Models.*

class MarketActivity : AppCompatActivity() {
    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market)
        messageUnRead.visibility = if (Message.sizeOfUnRead() > 0) View.VISIBLE else View.INVISIBLE

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
            finish()
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
        Inventory.getInventory(Inventory.BUY_NAME).normalize()
        var slots = Inventory.getInventory(Inventory.BUY_NAME).inv

        val arrayList: ArrayList<ItemStack> = ArrayList()
        for (inv in slots) {
            if (!inv.isEmpty()) {
                arrayList.add(inv)
            }
        }
        val adapter = ItemAdapterBuy(this, arrayList/*slots.toCollection(ArrayList())*/)

        tvTab1.adapter = adapter
        tvTab1.setOnItemClickListener { adapterView, view, i, l ->
            val player = Player.getInstance(this)
            Log.d("ItemClicker", i.toString())
            if (player!!.money > Items.findById(Inventory.getInventory(Inventory.BUY_NAME).getInventorySlotContents(i).itemId).price) {
                player!!.money -= Items.findById(Inventory.getInventory(Inventory.BUY_NAME).getInventorySlotContents(i).itemId).price

                Log.d("ItemClicker", "Pressed")
                if (Inventory.transferItem(Inventory.getInventory(Inventory.BUY_NAME), Inventory.getInventory(), i, 1)) {
                    startActivity(Intent(this, MarketActivity::class.java))
                    finish()
                }

                money.text = player.money.toString()
                slots = Inventory.getInventory(Inventory.BUY_NAME).inv
                adapter.notifyDataSetChanged()

            }
        }
        tvTab1.setOnItemLongClickListener { adapterView, view, i, l ->
            val item = Items.findById(Inventory.getInventory(Inventory.BUY_NAME).getInventorySlotContents(i).itemId)
            var sz = Inventory.getInventory(Inventory.BUY_NAME).getInventorySlotContents(i).stackSize
            if (player.money / item.price > 10) {
                if (sz > 10) {
                    sz = 10
                }
            } else {
                sz = player.money / item.price
            }
            player.money -= sz * item.price
            if (Inventory.transferItem(Inventory.getInventory(Inventory.BUY_NAME), Inventory.getInventory(), i, sz)) {
                startActivity(Intent(this, MarketActivity::class.java))
                finish()

            } else {
                money.text = player.money.toString()
                slots = Inventory.getInventory(Inventory.BUY_NAME).inv
                adapter.notifyDataSetChanged()
            }
            true
        }
        Inventory.getInventory(Inventory.SELL_NAME).normalize()
        var slots1 = Inventory.getInventory(Inventory.SELL_NAME).inv

        val arrayList1: ArrayList<ItemStack> = ArrayList()

        for (inv in slots1) {
            if (!inv.isEmpty()) {
                arrayList1.add(inv)
            }
        }
        val adapter1 = ItemAdapterSell(this, arrayList1/*slots.toCollection(ArrayList())*/)

        tvTab2.adapter = adapter1
        tvTab2.setOnItemLongClickListener { adapterView, view, i, l ->
            if (Inventory.transferItem(Inventory.getInventory(Inventory.SELL_NAME), Inventory.getInventory(), i, 1)) {
                startActivity(Intent(this, MarketActivity::class.java))
                finish()
            }
            slots1 = Inventory.getInventory(Inventory.SELL_NAME).inv
            adapter.notifyDataSetChanged()
            true
        }


        var listview3: ListView = findViewById(R.id.tvTab3)
        val dataArray3 = Worker.getListOfLabor()
        val adapter3 = ArrayAdapter<Worker>(this, android.R.layout.simple_list_item_1, dataArray3)

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

}


class ItemAdapterBuy : BaseAdapter {

    var inv = ArrayList<ItemStack>()
    var context: Context? = null


    constructor(context: Context, inv: ArrayList<ItemStack>) : super() {
        this.context = context
        this.inv = inv
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val ins = PlayerStatsDatabase.getInstance(context!!)
        var item = inv.get(position)
        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var itemView = inflator.inflate(R.layout.item_buy, null)

        itemView.imageItemSell.setImageResource(Items.findById(item.itemId).getItemImage())
        itemView.nameBuy.text = "${Items.findById(item.itemId).getName()} (${item.stackSize})"
        itemView.price.text = "${Items.findById(item.itemId).price}$"


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



