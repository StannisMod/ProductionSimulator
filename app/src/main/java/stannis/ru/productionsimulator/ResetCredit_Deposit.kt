package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_reset_credit__deposit.*
import stannis.ru.productionsimulator.Models.Credit_Deposit
import stannis.ru.productionsimulator.Models.DatabaseFactory


class ResetCredit_Deposit : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_credit__deposit)
        var type = 0
        var crDep: Credit_Deposit? = null
        if (intent.hasExtra("infoAboutCreditDeposit")) {
            val arr = intent.getStringExtra("infoAboutCreditDeposit").split(" ", ",", "%", "$")
            val amount = arr[3]
            val percent = arr[8]
            val date = arr[12]
            amountT.text = "${amount} $"
            percentT.text = "${percent} %/месяц"
            dateT.text = date
            if (arr[1].trim() == "депозит") {
                type = 1;
                top.text = "Информация о вашем депозите"
                amountStatic.text = "Сумма Вашего депозита:"
                percentStatic.text = "Ставка Вашего депозита:"
                dataStatic.text = "Дата открытия депозита:"
                amountTakeGet.text = "Сколько Вы хотите взять:"
            } else {
                type = 2
            }
            val ar = date.split(".")
            crDep = Credit_Deposit(amount.toInt(), percent.toDouble(), arrayOf(ar[0], ar[1], ar[2]), type)
        }
        confirm.setOnClickListener {
            if (resetAmount.text.toString() != "") {
                if (crDep != null) {

                    val cond = crDep.takeDep_payOff(resetAmount.text.toString().toInt())

                    if (cond) {
                        DatabaseFactory.getInstance(this).removeCreditDeposit(crDep.type, crDep.date[0], crDep.date[1], crDep.date[2])
                    } else {
                        DatabaseFactory.getInstance(this).setCreditDepositProperties(crDep)
                    }
                    Toast.makeText(this, "Операция прошла успешно", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, BankActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
