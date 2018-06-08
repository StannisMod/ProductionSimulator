package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_bank.*
import kotlinx.android.synthetic.main.stats_panel.*
import java.util.*

class BankActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank)

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
        val dataArray = Array(5) { i -> i.toString() }
        for (i in 0..4) {
            val kek = (Random().nextInt(10)) % 2
            if (kek == 1) {
                dataArray[i] = "Ваш кредит: ${0}, под ${0}%."
            }else{                                                              //Забарать данные из класса пользователя
                dataArray[i] = "Ваш депозит: ${0}, под ${0}%."

            }
        }
        val adapter= ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataArray)
        bankListView.adapter = adapter
        bankListView.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this, ResetCredit_Deposit::class.java)
            intent.putExtra("ID", i.toString())
            startActivity(intent)
        }
    }
}
