package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_credit__deposit.*


class Credit_DepositActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit__deposit)
        var isCredit: Boolean = true
        if (intent.hasExtra("Type")) {
            if (intent.getStringExtra("Type").equals("credit")) {
                enterAmountOfCredit_Deposit.text = "Введите сумму кредита: "

            } else {
                isCredit = false
                enterAmountOfCredit_Deposit.text = "Введите сумму депозита: "
            }
            inputAmountOfCredit.setOnKeyListener { v, keyCode, event ->
                if ((event as KeyEvent).action == KeyEvent.ACTION_DOWN) {
                    var percent = 0.0
                    if (isCredit) {
                        percent = myToDouble((v as EditText).text.toString()) / 100.0
                    } else {                                                                  //Write Some Normal Formula also there is a bug...
                        percent = myToDouble((v as EditText).text.toString()) / 101.0
                    }

                    countedPercent.text = " ${percent.format(2)}%"
                }
                false
            }
        }
        confirmCredit.setOnClickListener {
            /*
                Some Code. Actually adding this action to data of player
             */
            val intent = Intent(this, BankActivity::class.java)
            startActivity(intent)
        }
    }

    fun Double.format(radix: Int) = java.lang.String.format("%.${radix}f", this)

    fun myToDouble(str: String?): Double {
        if (str == null || str == "") {
            return 0.0
        }
        return str.toDouble()
    }
}

