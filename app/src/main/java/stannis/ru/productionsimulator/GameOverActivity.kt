package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_game_over.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_market.*
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Enums.EnumFactory

class GameOverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

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

        tostart.setOnClickListener {
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
        }

    }
}
