package stannis.ru.productionsimulator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

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
            // startActivity(market)
        }

        /*
        stats_panel.setOnClickListener {
            // startActivity(stats)
        }*/

        tofactory.setOnClickListener {
            // startActivity(factory)
        }
    }
}
