package stannis.ru.productionsimulator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class MailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail)

        //var list : List<Message> = List(5){i -> Message()}
        //list[1].sender = "Власть"
        //list[1].sub = "Новый закон о налогообложении"
        //list[1].text = "Здравстыуйте.\nМы сообщаем Вам, что теперь всем владельцам лесопилок и других лесных сооруженний, нужно платить налог на сохранение лесов, в размере 1% от стоимости недвижимости на територии леса."
        //list[1].date = arrayOf(1, 06, 2018)
        var listview : ListView = findViewById(R.id.listView)
        val dataArray = arrayOf("Банк\n    Заплатите деньги!", "Власть\n    Новый налог на пилы", "Помощник\n  Принес газету") //val dataArray = Array(list.size()){i -> list[i].toCaption}
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataArray)

        listview.adapter = adapter

        listview.setOnItemClickListener { parent, view, position, id ->

        }
    }
}
