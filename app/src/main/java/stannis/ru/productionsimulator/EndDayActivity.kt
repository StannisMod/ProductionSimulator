package stannis.ru.productionsimulator

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_end_day.*
import stannis.ru.productionsimulator.Models.DatabaseFactory
import stannis.ru.productionsimulator.R

class EndDayActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_day)
        val curData = DatabaseFactory.getInstance(this).getDataTime()
        number.text = curData.toString()
        wages.text = "-${curData!!.getAllWages(this)}$"
        creditsPayOff.text = "-${curData!!.todaysCreditMinus}$"
        depositsGain.text = "+${curData!!.todaysDepositGain}$"
        credit.text = "+${curData!!.tookCreditToday}$"
        deposit.text="-${curData!!.tookDepositToday}$"
        sellings.text = "+0$"
        val start = curData!!.todaysDepositGain - curData!!.todaysCreditMinus - curData!!.getAllWages(this)+curData!!.tookCreditToday-curData!!.tookDepositToday
        allinAll.text = "${start}$"
        nalogs.setOnKeyListener { view, i, keyEvent ->
            val tmp = if((view as EditText).text.toString()=="") 0 else view.text.toString().toInt()
            allinAll.text = "${start - tmp}$"
            if (allinAll.text.toString().toCharArray()[0] == '-') {
                allinAll.setTextColor(getColor(R.color.minus))
            } else {
                allinAll.text = "+${allinAll.text}"
                allinAll.setTextColor(getColor(R.color.plus))
            }
            false
        }

        nextDay.setOnClickListener {
            if (nalogs.text.toString() == "") {
                Toast.makeText(this, "Вы должны ввести сумму, которую Вы отдадите в качестве налога", Toast.LENGTH_SHORT).show()
            } else {
                val moneyDiff = (0-(nalogs.text.toString().toInt()))+
                        wages.text.toString().split("$")[0].toInt()+
                        sellings.text.toString().split( "$")[0].toInt()
                val player = DatabaseFactory.getInstance(this).getPlayerStats()
                player!!.countReputationChange(nalogs.text.toString().toInt())
                player!!.money+=moneyDiff
                DatabaseFactory.getInstance(this).setPlayerWithProperties(player!!)
                if (curData != null) {
                    curData.nextDay(this)
                }

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
