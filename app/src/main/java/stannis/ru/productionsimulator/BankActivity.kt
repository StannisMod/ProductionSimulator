package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_bank.*
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Models.Credit_Deposit
import stannis.ru.productionsimulator.Models.DatabaseFactory
import java.util.*

class BankActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank)
        val player = DatabaseFactory.getInstance(this).getPlayerStats()
        if (player != null) {
            money.text = player.money.toString()
            res.text = player.stuff.toString()
            staff.text = player.staff.toString()
            rep.progress = player.reputation
        }

        mail.setOnClickListener {
            val intent = Intent(this, MailActivity::class.java)
            startActivity(intent)
        }

        takeCredit.setOnClickListener {
            val intent1 = Intent(this, Credit_DepositActivity::class.java)
            intent1.putExtra("Type", "credit")
            startActivity(intent1)
        }
        enterDeposit.setOnClickListener {
            val intent1 = Intent(this, Credit_DepositActivity::class.java)
            intent1.putExtra("Type", "deposit")
            startActivity(intent1)
        }
        val dataArray = DatabaseFactory.getInstance(this).getListOfCreditDeposit()
        val adapter= ArrayAdapter<Credit_Deposit>(this, android.R.layout.simple_list_item_1, dataArray)
        bankListView.adapter = adapter
        bankListView.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this, ResetCredit_Deposit::class.java)
            intent.putExtra("infoAboutCreditDeposit", (view as TextView).text.toString() )
            startActivity(intent)
        }

    }
}
