package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Models.Credit_Deposit
import stannis.ru.productionsimulator.Models.DatabaseFactory
import stannis.ru.productionsimulator.Models.Factory
import stannis.ru.productionsimulator.Models.Staff

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(!DatabaseFactory.getInstance(this).added){
            DatabaseFactory.getInstance(this).removeLaborExchange("Леха")
            DatabaseFactory.getInstance(this).removeLaborExchange("Вася")
            DatabaseFactory.getInstance(this).removeLaborExchange("Петя")

            DatabaseFactory.getInstance(this).addLaborExchangeWithProperties( "Леха", 30, "Токарь", 10, "русский", 1200, "01", "01")
            DatabaseFactory.getInstance(this).addLaborExchangeWithProperties( "Вася", 30, "Слесарь", 10, "русский", 1200, "01", "02")
            DatabaseFactory.getInstance(this).addLaborExchangeWithProperties( "Петя", 30, "Дровосек", 10, "русский", 1200, "01", "03")

            DatabaseFactory.getInstance(this).added = true

            DatabaseFactory.getInstance(this).addMessageWithProperties(0, caption = "Вы выграли приз!", sender = "Лото 'ТОТО'", text = "Здравствуйте.\nВаша лесопилка попала в список предприятий, владельцы которых претендуют на новую печку. Для того чтобы получить печку, вам надо взять кредит на сумму 10$. Чтобы мы были уверены в вашей финансовой состоятельности и не оказалось, что вы просто жулик\nС уважением комиссия лото 'ТОТО'", day = "12", month =  "06",year =  "2018")
            DatabaseFactory.getInstance(this).addMessageWithProperties(1, sender = "ANONYMOUS", caption = "Твой брат у нас!", text = "Если хочешь увидеть своего брата живым, то положи на свой счет 1000000$ и передай нам номер этого счета. Номер счета напиши на бумажке положи в холодильник на складе и чтобы все покинули лесопилку до 31 июня! Всех кто завтра будет на лесопилке. Убьём!!!", day = "30", month = "06",year = "2018")
            DatabaseFactory.getInstance(this).addMessageWithProperties(2, sender = "Власть", caption = "Новый закон о налогообложении", text = "Здраствуйте.\n" +
                    "Мы сообщаем Вам, что теперь всем владельцам лесопилок и других лесных сооруженний, нужно платить налог на сохранение лесов, в размере 1% от стоимости недвижимости на територии леса.", day = "01",month =  "06",year =  "2018")
//            DatabaseFactory.getInstance(this).addMessageWithProperties(3, )
        }

        mail.setOnClickListener {
            val intent = Intent(this, MailActivity::class.java)
            startActivity(intent)
        }

        toinventory.setOnClickListener {

            // val intent = Intent(this, InventoryActivity::class.java)
            // startActivity(inventory)
        }

        tomarket.setOnClickListener {

            val intent = Intent(this, MarketActivity::class.java)
            startActivity(intent)

        }

        stats_panel.setOnClickListener {
            // val intent = Intent(this, StatsActivity::class.java)
            // startActivity(stats)
        }

        tofactory.setOnClickListener {
            val intent = Intent(this, FactoryActivity::class.java)
            startActivity(intent)
        }
        topersonal.setOnClickListener {
            val intent = Intent(this, StaffActivity::class.java)
            startActivity(intent)
        }

    }
}

