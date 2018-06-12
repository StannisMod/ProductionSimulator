package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_credit__deposit.*
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Models.DatabaseFactory
import stannis.ru.productionsimulator.R.id.percent
import kotlin.math.roundToInt


class Credit_DepositActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit__deposit)
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
                        percent = myToDouble((v as EditText).text.toString()) / 100.0
                    } else {                                                                  //Write Some Normal Formula also there is a bug...
                        percent = myToDouble((v as EditText).text.toString()) / 101.0
                    }

                    countedPercent.text = " ${round(percent, 2)}%/месяц"
                } else {
                    countedPercent.text = " 0%"
                }
                false

            }
            confirmCredit.setOnClickListener {
                if ( player != null) {
                    if (!isCredit&&inputAmountOfCredit.text.toString().toInt() > player.money) {
                        Toast.makeText(this, "Вы не вложите вложить больше денег, чем у вас сейчас есть", Toast.LENGTH_SHORT).show()
                    } else {
                        if(!isCredit) {
                            player.money -= inputAmountOfCredit.text.toString().toInt()
                        }else{
                            player.money+=inputAmountOfCredit.text.toString().toInt()
                        }
                        DatabaseFactory.getInstance(this).setPlayerWithProperties(player.money, player.stuff, player.staff, player.reputation)


                        val type = if (isCredit) 2 else 1
                        DatabaseFactory.getInstance(this).addCrDepWithProperties(type, inputAmountOfCredit.text.toString().toInt(), countedPercent.text.toString().split("%")[0].toDouble(), "01", "02", "2018")
                        val mes = if (isCredit) "Кредит взят" else "Депозит открыт"
                        Toast.makeText(this, mes, Toast.LENGTH_SHORT)

                        val intent = Intent(this, BankActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }


    }

    fun round(a: Double, radix: Int): Double {
        var b = a
        b *= Math.pow(10.0, radix.toDouble())
        b = b.roundToInt().toDouble()
        b /= Math.pow(10.0, radix.toDouble())
        return b

    }

    fun myToDouble(str: String?): Double {
        if (str == null || str == "") {
            return 0.0
        }
        return str.toDouble()
    }
}

