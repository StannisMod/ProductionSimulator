package stannis.ru.productionsimulator

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_market.*
import kotlinx.android.synthetic.main.activity_market.view.*
import kotlinx.android.synthetic.main.activity_message.view.*
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Models.DatabaseFactory
import stannis.ru.productionsimulator.Models.Message

class MailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail)


        var list : ArrayList<Message> = DatabaseFactory.getInstance(this).getMessage()

        var listview : ListView = findViewById(R.id.listView)
        val dataArray = Array(list.size){i -> list[i].toCaption()}
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataArray)
        listview.adapter = adapter
//        (listview.getChildAt(0) as TextView).setTextColor(getColor(R.color.minus))

        listview.setOnItemClickListener { parent, view, position, id ->
            DatabaseFactory.getInstance(this).removeMessage(list[position].hashCode())
            list[position].readed = "1"
            DatabaseFactory.getInstance(this).addMessageWithProperties(list[position])
            val intent = Intent(this, MessageActivity::class.java)
            intent.putExtra("message", list[position].toStringArray())
            startActivity(intent)
        }
    }
}
