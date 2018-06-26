package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_workers.*
import kotlinx.android.synthetic.main.date_layout.*
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase
import stannis.ru.productionsimulator.Models.DataTime
import stannis.ru.productionsimulator.Models.Message
import stannis.ru.productionsimulator.Models.Player
import stannis.ru.productionsimulator.Models.Worker

class StaffActivity : AppCompatActivity() {
    override fun onBackPressed() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workers)
        messageUnRead.visibility = if (Message.sizeOfUnRead() > 0) View.VISIBLE else View.INVISIBLE

        val player = Player.getInstance(this)
        if (player != null) {
            money.text = player.money.toString()
            rep.progress = player.reputation
        }
        val curData = DataTime.getInstance(this)
        if(curData!=null){
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

        val dataArray = Worker.getListOfStaff()
        val adapter = ArrayAdapter<Worker>(this, android.R.layout.simple_list_item_1, dataArray)
        listView.adapter = adapter
        listView.setOnItemClickListener { adapterView, view, i, l ->
            val extra = "${dataArray[i].name}.YourWorker.$i"
            val intent = Intent(this, WorkerActivity::class.java)
            intent.putExtra("TAG", extra)
            startActivity(intent)

        }

    }
}
