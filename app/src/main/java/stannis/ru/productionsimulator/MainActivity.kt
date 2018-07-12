package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_message.view.*
import kotlinx.android.synthetic.main.date_layout.*
import kotlinx.android.synthetic.main.stats_panel.*
import org.jetbrains.anko.find
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase
import stannis.ru.productionsimulator.Enums.EnumFactory
import stannis.ru.productionsimulator.Enums.Items
import stannis.ru.productionsimulator.Functions.saveAll
import stannis.ru.productionsimulator.Functions.str
import stannis.ru.productionsimulator.Models.Inventory
import stannis.ru.productionsimulator.Models.ItemStack
import stannis.ru.productionsimulator.Models.*

class MainActivity : AppCompatActivity() {
    override fun onBackPressed() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //
        str = getString(R.string.names)
        //
        if (intent.hasExtra("TAG")) {
            if (intent.getStringExtra("TAG") == "firstTime") {
                Log.d("Dialog", "Problem with builder")
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("Здравствуйте")
                builder.setMessage("Здравствуйте. Вы владелец производства.\n" +
                        "\n" +
                        "Ваша цель: сделать предприятие прибыльным, заработать денег и купить следующее призводство.\n" +
                        "\n" +
                        "В нашей игре есть несколько важных компонентов:\n" +
                        "Репутация - число, которое менятся в зависимости от уплаты налогов и кредитов.\n" +
                        "Рынок - место, где можно купить сырьё, оборудование, нанять работников, продать вашу продукцию.\n" +
                        "Банк - место, где можно взять кредит или положить деньги в банк под депозит. Процент менятся в зависимости от величины репутации.\n" +
                        "Инвентарь - место, где хранятся ваши вещи, если вы хотите что-то продать, то нажмите на предмет инвентаря.\n" +
                        "Производство - место, куда можно попасть нажав на домик в главном окне, где производится и выгружается на рынок продукция, хранится состояние оборудования и место, где можно пополнить сырьём производство.\n" +
                        "Ваши рабочие - место, где можно посмотреть ваших рабочих, повысить им зарплату и уволить\n" +
                        "Почта - место, куда стоит заглядывать, после перехода на каждый следующий день. Там появляется информация о повышении налогов, напоминание о выплачивании кредита и многое другое. Повышение налогов зависит от вашей репутации.")

                builder.setNeutralButton("CANCEL") { dialog, which ->
                    Toast.makeText(this, "Приятной игры!", Toast.LENGTH_SHORT).show()
                }
                val dialog = builder.create()
                dialog.show()
            }
        }



        Log.d("CURRENTindex", DatabaseFactory.index.toString())
        var index = DatabaseFactory.index



        Log.d("NotNUll", "${Inventory.getInventory("buy").maxStackSize}")
        var factory = Factory.getFactoryById(index)
        Log.d("FACTORY", factory.toString())




        mail.setOnClickListener {
            val intent = Intent(this, MailActivity::class.java)
            startActivity(intent)
        }
        rep.setEnabled(false)

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
            finish()
        }
        messageUnRead.visibility = if (Message.sizeOfUnRead() > 0) View.VISIBLE else View.INVISIBLE



        fun setFactorySettings() {
            index = DatabaseFactory.index
            tofactory.setBackgroundResource(EnumFactory.findById(DatabaseFactory.index).getImg())
            label.text = EnumFactory.findById(index).factory
            var fac = Factory.getFactoryById(DatabaseFactory.index)
            if (fac == null) {
                fac = Factory(DatabaseFactory.index)
            }
            if (!fac.isBought) {
                buyFac.visibility = View.VISIBLE
                navigationButtons.visibility = View.INVISIBLE
                buyFac.text = "Купить\n${fac.price}$"
                buyFac.setOnClickListener {
                    if (player.money < fac.price) {
                        Toast.makeText(this, "У вас недостаточно средств", Toast.LENGTH_SHORT).show()
                    } else {
                        fac.isBought = true
                        player.money -= fac.price
                        player.tax += (0.2 * fac.price).toInt()
                        DatabaseFactory.index = fac.id
                        Inventory.getAllInventories()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }
            } else {
                buyFac.visibility = View.GONE
                navigationButtons.visibility = View.VISIBLE
            }
        }
        setFactorySettings()
        left.setOnClickListener {
            Log.d("LEFT", index.toString())
            if (DatabaseFactory.index > 0) {
                DatabaseFactory.index = index
                DatabaseFactory.index--
                setFactorySettings()
            }
        }
        right.setOnClickListener {
            if (DatabaseFactory.index < EnumFactory.getSize() - 1) {
                DatabaseFactory.index = index
                DatabaseFactory.index++
                setFactorySettings()
            }
        }

        toinventory.setOnClickListener {

            val intent = Intent(this, InventoryActivity::class.java)
            startActivity(intent)
        }

        tomarket.setOnClickListener {
            val intent = Intent(this, MarketActivity::class.java)
            startActivity(intent)
        }


        tofactory.setOnClickListener {
            Log.d("WHATThe", DatabaseFactory.index.toString())

            val intent = Intent(this, FactoryActivity::class.java)
            startActivity(intent)
        }
        topersonal.setOnClickListener {
            val intent = Intent(this, StaffActivity::class.java)
            startActivity(intent)
        }

    }
}


