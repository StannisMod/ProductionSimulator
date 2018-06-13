package stannis.ru.productionsimulator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Models.DatabaseFactory

class MessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        val player = DatabaseFactory.getInstance(this).getPlayerStats()
//        if (player != null) {
//            money.text = player.money.toString()
//            res.text = player.stuff.toString()
//            staff.text = player.staff.toString()
//            rep.progress = player.reputation
//        }

        if(intent.hasExtra("message")){
            val message = intent.getStringArrayExtra("message")

            var date : TextView = findViewById(R.id.date)
            date.setText(message[3])

            var sender : TextView = findViewById(R.id.sender)
            sender.setText(message[2])

            var text : TextView = findViewById(R.id.text)
            text.setText(message[1])

            var caption : TextView = findViewById(R.id.caption)
            caption.setText(message[0])

        }


    }
}
