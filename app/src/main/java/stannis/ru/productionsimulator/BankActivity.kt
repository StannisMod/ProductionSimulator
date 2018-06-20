package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_bank.*
import kotlinx.android.synthetic.main.date_layout.*
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Models.Credit_Deposit
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase
import stannis.ru.productionsimulator.Models.DataTime
import stannis.ru.productionsimulator.Models.Player

class BankActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank)
        val player =Player.getInstance(this)
        if (player != null) {
            money.text = player.money.toString()
            rep.progress = player.reputation
        }
        val curData = DataTime.getInstance(this)
        if(curData!=null){
            curDate.text = curData.toString()
        }

        mail.setOnClickListener {
            val intent = Intent(this, MailActivity::class.java)
            startActivity(intent)
        }
        endDay.setOnClickListener {
            val intent = Intent(this, EndDayActivity::class.java)
            startActivity(intent)
            finish()
        }
        rep.setEnabled(false)

        takeCredit.setOnClickListener {
            if(curData!=null) {
                if(curData.tookCreditToday==0) {

                    val intent1 = Intent(this, Credit_DepositActivity::class.java)
                    intent1.putExtra("Type", "credit")
                    startActivity(intent1)
                    finish()
                }else{
                    Toast.makeText(this, "Вы не можете брать больше одного кредита в день", Toast.LENGTH_SHORT).show()
                }
            }
        }
        enterDeposit.setOnClickListener {
            if(curData!=null) {
                if(curData.tookDepositToday==0) {
                    val intent1 = Intent(this, Credit_DepositActivity::class.java)
                    intent1.putExtra("Type", "deposit")
                    startActivity(intent1)
                    finish()
                }else{
                    Toast.makeText(this, "Вы не можете открывать более одного депозита в день", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val dataArray = PlayerStatsDatabase.getInstance(this).getListOfCreditDeposit()
        val adapter= ArrayAdapter<Credit_Deposit>(this, android.R.layout.simple_list_item_1, dataArray)
        bankListView.adapter = adapter
        bankListView.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this, ResetCredit_Deposit::class.java)
            intent.putExtra("infoAboutCreditDeposit", (view as TextView).text.toString() )
            startActivity(intent)
        }

    }
}
