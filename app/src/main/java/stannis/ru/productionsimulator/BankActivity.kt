package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
import stannis.ru.productionsimulator.Models.Message
import stannis.ru.productionsimulator.Models.Player

class BankActivity : AppCompatActivity() {


    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank)
        messageUnRead.visibility = if (Message.sizeOfUnRead() > 0) View.VISIBLE else View.INVISIBLE

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
                    Toast.makeText(this, "Вы можете брать кредит, только раз в 20 дней", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this, "Вы можете открывать депозит только раз в 20 день", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val dataArray = Credit_Deposit.getListOfCredit_Deposit()
        val adapter= ArrayAdapter<Credit_Deposit>(this, android.R.layout.simple_list_item_1, dataArray)
        bankListView.adapter = adapter
        bankListView.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this, ResetCredit_Deposit::class.java)
            intent.putExtra("infoAboutCreditDeposit", (view as TextView).text.toString() )
            startActivity(intent)
        }

    }
}
