package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.stats_panel.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mail.setOnClickListener {
            // startActivity(mail)
        }

        toinventory.setOnClickListener {
            // startActivity(inventory)
        }


        tomarket.setOnClickListener {
            val intent = Intent(this, MarketActivity::class.java)
            startActivity(intent)
        }

        stats_panel.setOnClickListener {
            // startActivity(stats)
        }

        tofactory.setOnClickListener {
            // startActivity(factory)
        }
    }
}
