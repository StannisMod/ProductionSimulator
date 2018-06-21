package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.date_layout.*
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Enums.EnumFactory
import stannis.ru.productionsimulator.Enums.Items
import stannis.ru.productionsimulator.Functions.saveAll
import stannis.ru.productionsimulator.Models.Inventory
import stannis.ru.productionsimulator.Models.ItemStack
import stannis.ru.productionsimulator.Models.*

class MainActivity : AppCompatActivity() {
    override fun onBackPressed() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("CURRENTindex", DatabaseFactory.index.toString())
        var index = DatabaseFactory.index



        Log.d("NotNUll", "${Inventory.getInventory("buy").maxStackSize}")
        var factory = Factory.getFactoryById(index)
        if (factory == null)
            factory = Factory(true, index, false, EnumFactory.findById(index).price, EnumFactory.findById(index))




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


        tofactory.setBackgroundResource(EnumFactory.findById(DatabaseFactory.index).getImg())
        left.setOnClickListener {
            Log.d("LEFT", index.toString())
            if (DatabaseFactory.index > 0) {
                DatabaseFactory.index = index
                DatabaseFactory.index--
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        right.setOnClickListener {
            if (DatabaseFactory.index < EnumFactory.getSize() - 1) {
                DatabaseFactory.index = index
                DatabaseFactory.index++
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        if (factory != null && !factory.isBought) {
            buyFac.visibility = View.VISIBLE
            navigationButtons.visibility = View.INVISIBLE
            buyFac.text = "${EnumFactory.findById(index).factory}. Купить\n${factory.price}$"
            buyFac.setOnClickListener {
                if (player.money < factory.price) {
                    Toast.makeText(this, "У вас недостаточно средств", Toast.LENGTH_SHORT).show()
                } else {
                    factory.isBought = true
                    player.money -= factory.price
                    saveAll(this)
                    DatabaseFactory.index = factory.id
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        } else {

            toinventory.setOnClickListener {

                val intent = Intent(this, InventoryActivity::class.java)
                startActivity(intent)
            }

            tomarket.setOnClickListener {
                val intent = Intent(this, MarketActivity::class.java)
                startActivity(intent)
            }

            stats_panel.setOnClickListener {
                // val intent = Intent(this, StatsActivity::class.java)
                // startActivity(stats)
                Inventory.getInventory().setInventorySlotContents(Inventory.getInventory().findFirstEqualSlot(Items.WOOD.getId()), ItemStack(Items.WOOD.getId(), 32, 64))
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


}
