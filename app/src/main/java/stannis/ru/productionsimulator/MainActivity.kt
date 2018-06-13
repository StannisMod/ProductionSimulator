package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Models.DatabaseFactory
import stannis.ru.productionsimulator.Models.Inventory
import stannis.ru.productionsimulator.Models.ItemStack
import stannis.ru.productionsimulator.Models.*
import stannis.ru.productionsimulator.R.id.debug

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!DatabaseFactory.getInstance(this).added) {
            DatabaseFactory.getInstance(this).removeLaborExchange("Леха")
            DatabaseFactory.getInstance(this).removeLaborExchange("Вася")
            DatabaseFactory.getInstance(this).removeLaborExchange("Петя")
            DatabaseFactory.getInstance(this).removePlayer()
            DatabaseFactory.getInstance(this).addLaborExchangeWithProperties("Леха", 30, "Токарь", 10, "русский", 1200, "01", "01")
            DatabaseFactory.getInstance(this).addLaborExchangeWithProperties("Вася", 30, "Слесарь", 10, "русский", 1200, "01", "02")
            DatabaseFactory.getInstance(this).addLaborExchangeWithProperties("Петя", 30, "Дровосек", 10, "русский", 1200, "01", "03")
            DatabaseFactory.getInstance(this).addPlayerStatsWithProperties(500, 0, 0, 50)
            DatabaseFactory.getInstance(this).added = true

            DatabaseFactory.getInstance(this).removeMessage("Здравствуйте.\nВаша лесопилка попала в список предприятий, владельцы которых претендуют на новую печку. Для того чтобы получить печку, вам надо взять кредит на сумму 10$. Чтобы мы были уверены в вашей финансовой состоятельности и не оказалось, что вы просто жулик\nС уважением комиссия лото 'ТОТО'".hashCode())
            DatabaseFactory.getInstance(this).removeMessage("Если хочешь увидеть своего брата живым, то положи на свой счет 1000000$ и передай нам номер этого счета. Номер счета напиши на бумажке положи в холодильник на складе и чтобы все покинули лесопилку до 31 июня! Всех кто завтра будет на лесопилке. Убьём!!!".hashCode())
            DatabaseFactory.getInstance(this).removeMessage("Здраствуйте.\nМы сообщаем Вам, что теперь всем владельцам лесопилок и других лесных сооруженний, нужно платить налог на сохранение лесов, в размере 1% от стоимости недвижимости на територии леса.".hashCode())
            DatabaseFactory.getInstance(this).removeMessage("Здраствуйте, Кирилл Юрьевич.\nВаша задолженность банку составляет 5000$. Просим Вас до 31.12.2018 выплатить задолженность, иначе нам придется заблокировать ваш счет и забрать вашу лесопилку.\nС любовью банк!".hashCode())

            DatabaseFactory.getInstance(this).addMessageWithProperties(Message(caption = "Вы выграли приз!", sender = "Лото 'ТОТО'", text = "Здравствуйте.\nВаша лесопилка попала в список предприятий, владельцы которых претендуют на новую печку. Для того чтобы получить печку, вам надо взять кредит на сумму 10$. Чтобы мы были уверены в вашей финансовой состоятельности и не оказалось, что вы просто жулик\nС уважением комиссия лото 'ТОТО'", date = arrayOf("12", "06", "2018")))
            DatabaseFactory.getInstance(this).addMessageWithProperties(Message(sender = "ANONYMOUS", caption = "Твой брат у нас!", text = "Если хочешь увидеть своего брата живым, то положи на свой счет 1000000$ и передай нам номер этого счета. Номер счета напиши на бумажке положи в холодильник на складе и чтобы все покинули лесопилку до 31 июня! Всех кто завтра будет на лесопилке. Убьём!!!", date = arrayOf("30", "06", "2018")))
            DatabaseFactory.getInstance(this).addMessageWithProperties(Message(sender = "Власть", caption = "Новый закон о налогообложении", text = "Здраствуйте.\n" +
                    "Мы сообщаем Вам, что теперь всем владельцам лесопилок и других лесных сооруженний, нужно платить налог на сохранение лесов, в размере 1% от стоимости недвижимости на територии леса.", date = arrayOf("01", "06", "2018")))
            DatabaseFactory.getInstance(this).addMessageWithProperties(Message())
        }

        mail.setOnClickListener {
             val intent = Intent(this, MailActivity::class.java)
             startActivity(intent)
        }

        val player = DatabaseFactory.getInstance(this).getPlayerStats()
        if (player != null) {
            money.text = player.money.toString()
            res.text = player.stuff.toString()
            staff.text = player.staff.toString()
            rep.progress = player.reputation

        }

        toinventory.setOnClickListener {
            // val intent = Intent(this, InventoryActivity::class.java)
            // startActivity(inventory)
            Inventory.getInventory().setInventorySlotContents(0, ItemStack(1, 2, 64))
            DatabaseFactory.getInstance(this).addInventory(this, Inventory.getInventory())    //addFactoryWithProperties(this, 0, 0, 0, 10, 1, 2, 1, 5,  10.0)
            debug.text = DatabaseFactory.getInstance(this).getInventory(Inventory.getInventory().name)?.getInventorySlotContents(0).toString()//.getFactory(0)?.toDetailedString()
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
