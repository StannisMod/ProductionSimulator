package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_credit__deposit.*
import kotlinx.android.synthetic.main.date_layout.*
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase
import stannis.ru.productionsimulator.Functions.round
import stannis.ru.productionsimulator.Functions.saveAll
import stannis.ru.productionsimulator.Models.Credit_Deposit
import stannis.ru.productionsimulator.Models.DataTime
import stannis.ru.productionsimulator.Models.Message
import stannis.ru.productionsimulator.Models.Player
import kotlin.math.roundToInt


class Credit_DepositActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit__deposit)
        messageUnRead.visibility = if (Message.sizeOfUnRead() > 0) View.VISIBLE else View.INVISIBLE


        val player = Player.getInstance(this)
        if (player != null) {
            money.text = player.money.toString()
            rep.progress = player.reputation
        }
        val curData = DataTime.getInstance(this)
        if (curData != null) {
            curDate.text = curData.toString()
        }
        endDay.setOnClickListener {
            val intent = Intent(this, EndDayActivity::class.java)
            startActivity(intent)
        }

        mail.setOnClickListener {
            val intent = Intent(this, MailActivity::class.java)
            startActivity(intent)
        }
        rep.setEnabled(false)

        var isCredit: Boolean = true
        if (intent.hasExtra("Type")) {
            if (intent.getStringExtra("Type").equals("credit")) {
                enterAmountOfCredit_Deposit.text = "Введите сумму кредита: "

            } else {
                isCredit = false
                enterAmountOfCredit_Deposit.text = "Введите сумму депозита: "
            }
            inputAmountOfCredit.setOnKeyListener { v, keyCode, event ->
                if ((v as EditText).text.toString() != "") {
                    var percent = 0.0
                    if (isCredit) {
                        percent = player.countCreditPercent(v.text.toString().toInt())
                    } else {                                                                  //Write Some Normal Formula also there is a bug...
                        percent = player.countDepositPercent(v.text.toString().toInt())
                    }

                    countedPercent.text = " ${round(percent, 2)}%/месяц"
                } else {
                    countedPercent.text = " 0%"
                }
                false

            }
            confirmCredit.setOnClickListener {
                if (inputAmountOfCredit.text.toString() != "") {
                    if (!isCredit && inputAmountOfCredit.text.toString().toInt() > player.money) {
                        Toast.makeText(this, "Вы не вложите вложить больше денег, чем у вас сейчас есть", Toast.LENGTH_SHORT).show()
                    } else {
                        if (!isCredit) {
                            player.money -= inputAmountOfCredit.text.toString().toInt()

                            curData.tookDepositToday = 1


                        } else {
                            curData.tookCreditToday = 1
                            player.money += inputAmountOfCredit.text.toString().toInt()
                        }


                        val type = if (isCredit) 0 else 1
                        Credit_Deposit(inputAmountOfCredit.text.toString().toInt(), countedPercent.text.toString().split("%")[0].toDouble(), arrayOf(curData.currentDay, curData.currentMonth, curData.currentYear), type).add()
                        val mes = if (isCredit) "Кредит взят" else "Депозит открыт"
                        Toast.makeText(this, mes, Toast.LENGTH_SHORT)

                        val intent = Intent(this, BankActivity::class.java)

                        startActivity(intent)
                        finish()
                    }
                }
            }
        }


    }

    override fun onBackPressed() {
        startActivity(Intent(this, BankActivity::class.java))
        finish()
    }


}

