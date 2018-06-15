package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import stannis.ru.productionsimulator.Models.Factory
import stannis.ru.productionsimulator.Models.fillDb

class ListProductionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_production)

        var listview : ListView = findViewById(R.id.listView)
        val dataArray = Array(1){i -> "Лесопилка"}
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataArray)
        listview.adapter = adapter

        listview.setOnItemClickListener { parent, view, position, id ->
            Factory(0, EnumFactory.findById(position))
            val intent = Intent(this, MainActivity::class.java)
            fillDb(this)
            startActivity(intent)
        }
    }
}
