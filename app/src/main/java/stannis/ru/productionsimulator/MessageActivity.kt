package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.TextView

class MessageActivity : AppCompatActivity() {
    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)


        if (intent.hasExtra("message")) {
            val message = stannis.ru.productionsimulator.Models.Message.messages[intent.getIntExtra("message", 0)]

            var date: TextView = findViewById(R.id.date)
            date.setText(message.dateToString())

            var sender: TextView = findViewById(R.id.sender)
            sender.setText(message.sender)

            var text: TextView = findViewById(R.id.text)
            text.setText(message.text)

            var caption: TextView = findViewById(R.id.caption)
            caption.setText(message.caption)

        }


    }
}
