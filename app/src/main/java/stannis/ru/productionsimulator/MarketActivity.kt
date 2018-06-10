package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TabHost
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_market.*
import stannis.ru.productionsimulator.Models.Staff
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Models.DatabaseFactory

class MarketActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market)

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

        var listview1 : ListView = findViewById(R.id.tvTab1)
        val dataArray1 = arrayOf( "Бензопила", "Грузовик", "Обрабатывающий станок")
        val adapter1 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataArray1)

        listview1.adapter = adapter1

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
