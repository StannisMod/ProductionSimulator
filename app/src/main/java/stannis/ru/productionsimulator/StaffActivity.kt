package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_workers.*
import stannis.ru.productionsimulator.Models.Staff

class StaffActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workers)
        val dataArray  = Array(10){i-> Staff("Petrovich", 43, "Токарь", 10, "Russian", 1200) }
        val adapter = ArrayAdapter<Staff>(this, android.R.layout.simple_list_item_1, dataArray)
        listView.adapter = adapter
        listView.setOnItemClickListener { adapterView, view, i, l ->
            val extra = "${dataArray[i].toString()}.YourWorker.${i}"
            val intent = Intent(this, WorkerActivity::class.java)
            intent.putExtra("TAG", extra)
            startActivity(intent)

        }

    }
}
