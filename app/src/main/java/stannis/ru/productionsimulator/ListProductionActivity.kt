package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import stannis.ru.productionsimulator.Models.Factory
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_market.*
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
            var text : TextView = findViewById(R.id.text2)
            text.text = "Подождите около минуты...\nСпасибо за понимание"
            val intent = Intent(this, MainActivity::class.java)
            fillDb(this)
            startActivity(intent)
        }
    }
}
