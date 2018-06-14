package stannis.ru.productionsimulator

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_market.*
import kotlinx.android.synthetic.main.activity_market.view.*
import kotlinx.android.synthetic.main.activity_message.view.*
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Models.DatabaseFactory
import stannis.ru.productionsimulator.Models.Message

class MailActivity : AppCompatActivity() {

//    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail)

        Toast.makeText(this, "Чтобы удалить сообщение, надо долго нажимать на сообщение", Toast.LENGTH_SHORT).show()

        var list : ArrayList<Message> = DatabaseFactory.getInstance(this).getMessage()

        var listview : ListView = findViewById(R.id.listView)
        val dataArray = Array(list.size){i -> list[i].toCaption()}
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataArray)
        listview.adapter = adapter

        var list2 : ArrayList<Message> = DatabaseFactory.getInstance(this).getMessageReaded()

        var listview2 : ListView = findViewById(R.id.listView2)
        val dataArray2 = Array(list2.size){i -> list2[i].toCaption()}
        val adapter2 = ArrayAdapter<String>(this, R.layout.message, dataArray2)
        listview2.adapter = adapter2

        listview.setOnItemClickListener { parent, view, position, id ->
            DatabaseFactory.getInstance(this).removeMessage(list[position].hashCode())
            list[position].readed = "1"
            DatabaseFactory.getInstance(this).addMessageReadedWithProperties(list[position])
            val intent = Intent(this, MessageActivity::class.java)
            intent.putExtra("message", list[position].toStringArray())
            startActivity(intent)
        }

        listview.setOnItemLongClickListener{ parent, view, position, id ->
            Toast.makeText(this, "Перед тем, как удалить сообщение.\nПрочитайте его!", Toast.LENGTH_LONG).show()
            true
        }

        listview2.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, MessageActivity::class.java)
            intent.putExtra("message", list2[position].toStringArray())
            startActivity(intent)
        }

        listview2.setOnItemLongClickListener { parent, view, position, id ->
            DatabaseFactory.getInstance(this).removeMessageReaded(list2[position].hashCode())
            val intent = Intent(this, MailActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Вы удалили сообщение", Toast.LENGTH_LONG).show()
            true
        }
    }
}
