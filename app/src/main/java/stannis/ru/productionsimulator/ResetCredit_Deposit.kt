package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_reset_credit__deposit.*
import kotlinx.android.synthetic.main.date_layout.*
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Models.Credit_Deposit
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase
import stannis.ru.productionsimulator.Models.DataTime
import stannis.ru.productionsimulator.Models.Message
import stannis.ru.productionsimulator.Models.Player


class ResetCredit_Deposit : AppCompatActivity() {
    override fun onBackPressed() {
        startActivity(Intent(this, BankActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_credit__deposit)
        messageUnRead.visibility = if (Message.sizeOfUnRead() > 0) View.VISIBLE else View.INVISIBLE

        val ins = PlayerStatsDatabase.getInstance(this)
        val player = Player.getInstance(this)
        if (player != null) {
            money.text = player.money.toString()
            rep.progress = player.reputation
        }
        rep.setEnabled(false)
        val curData = DataTime.getInstance(this)
        if (curData != null) {
            curDate.text = curData.toString()
        }
        endDay.setOnClickListener {
            val intent = Intent(this, EndDayActivity::class.java)
            startActivity(intent)
            finish()
        }
        mail.setOnClickListener {
            val intent = Intent(this, MailActivity::class.java)
            startActivity(intent)
        }
        var type = 0
        var crDep: Credit_Deposit? = null
        var condition: Boolean = false
        if (intent.hasExtra("infoAboutCreditDeposit")) {
            val arr = intent.getStringExtra("infoAboutCreditDeposit").split(" ", ",", "%", "$")
            val amount = arr[3]
            val percent = arr[8]
            val date = arr[12]
            amountT.text = "${amount} $"
            percentT.text = "${percent} %/месяц"
            dateT.text = date
            condition = arr[1].trim() == "депозит"
            if (condition) {
                type = 1;
                top.text = "Информация о вашем депозите"
                amountStatic.text = "Сумма Вашего депозита:"
                percentStatic.text = "Ставка Вашего депозита:"
                dataStatic.text = "Дата открытия депозита:"
                amountTakeGet.text = "Сколько Вы хотите взять:"
            } else {
                type = 0
            }
            val ar = date.split(".")
            crDep = Credit_Deposit.getCr_Dep(date.split(".").toTypedArray(), type)
        }
        confirm.setOnClickListener {
            if (resetAmount.text.toString() != "") {
                if (crDep != null) {

                    val cond = crDep.takeDep_payOff(resetAmount.text.toString().toInt())
                    if (player != null && curData != null) {
                        if (!condition && resetAmount.text.toString().toInt() > player.money) {
                            Toast.makeText(this, "Вы не можете внести больше денег, чем Вы сейчас имеете(", Toast.LENGTH_SHORT)
                        } else {
                            if (condition) {
                                player.money += resetAmount.text.toString().toInt()
                            } else {
                                player.money -= resetAmount.text.toString().toInt()
                            }



                            if (cond) {
                                crDep.remove()
                            }
                            Toast.makeText(this, "Операция прошла успешно", Toast.LENGTH_SHORT).show()


                            val intent = Intent(this, BankActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                    }
                }
            }
        }
    }
}
