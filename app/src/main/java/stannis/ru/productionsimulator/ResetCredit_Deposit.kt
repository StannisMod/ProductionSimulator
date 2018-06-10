package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_reset_credit__deposit.*
import stannis.ru.productionsimulator.Models.Credit_Deposit


class ResetCredit_Deposit : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_credit__deposit)
        val type = 0
        if (intent.hasExtra("ID")) {
            val id = Integer.parseInt(intent.getStringExtra("ID"))
            val credit_Deposit: Credit_Deposit
            //type = credit_Deposit.tyoe
            /*if(credit_Deposit.type==1){
                //
            }
            */
        }
        confirm.setOnClickListener {
            //

            val intent = Intent(this, BankActivity::class.java)
            startActivity(intent)
        }
    }
}
