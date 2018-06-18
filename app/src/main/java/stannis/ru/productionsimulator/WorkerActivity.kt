package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_worker.*
import kotlinx.android.synthetic.main.date_layout.*
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase
import stannis.ru.productionsimulator.Models.Staff


class WorkerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker)
        val ins = PlayerStatsDatabase.getInstance(this)

        val player = ins.getPlayerStats()
        if (player != null) {
            money.text = player.money.toString()
            res.text = player.stuff.toString()
            staff.text = player.staff.toString()
            rep.progress = player.reputation
        }
        val curData = ins.getDataTime()
        rep.setEnabled(false)
        if(curData!=null){
            curDate.text = curData.toString()
        }
        endDay.setOnClickListener {
            val intent = Intent(this, EndDayActivity::class.java)
            startActivity(intent)
            finish()
        }

        mail.setOnClickListener {
            val intent = Intent(this, MailActivity::class.java)
            startActivity(intent)
        }

        if (intent.hasExtra("TAG")) {
            val arr = intent.getStringExtra("TAG").split(".")
            val cond = arr[1].trim() != "YourWorker"
            var worker: Staff? = null
            if (cond) {
                worker = DatabaseFactory.getInstance(this).getWorkerFromLabor(arr[0].trim())
                fire.visibility = View.INVISIBLE
                hire_prom.text = "Нанять"

                hire_prom.setOnClickListener {
                    val tmp = DatabaseFactory.getInstance(this).getWorkerFromLabor(arr[0].trim())
                    if (player != null && tmp != null) {
                        if (player.money < tmp.salary) {
                            Toast.makeText(this, "У вас недостаточно средств", Toast.LENGTH_SHORT).show()
                        } else {
                            player.money -= tmp.salary
                            player.staff++
                            ins.setPlayerWithProperties(player)

                            DatabaseFactory.getInstance(this).removeLaborExchange(arr[0].trim())

                            DatabaseFactory.getInstance(this).addStaffWithProperties(tmp)
                            DatabaseFactory.getInstance(this).removeLaborExchange(tmp.name)
                            Toast.makeText(this, "${tmp.name}, ты нанят", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this, MarketActivity::class.java)
                            startActivity(intent)

                        }
                    }
                }
            } else {
                worker = DatabaseFactory.getInstance(this).getWorkerFromStaff(arr[0].trim())
                if (worker != null) {
                    hire_prom.setOnClickListener {
                        val tmp: Staff? = DatabaseFactory.getInstance(this).getWorkerFromStaff(arr[0].trim())
                        if (tmp != null) {
                            tmp.getPromotion()
                            salary.text = "${tmp.salary} $"

                            DatabaseFactory.getInstance(this).setStaffWithProperties(tmp)


                        }
                    }
                    fire.setOnClickListener {
                        val tmp: Staff? = DatabaseFactory.getInstance(this).getWorkerFromStaff(arr[0].trim())
                        if (tmp != null && player != null) {
                            if (tmp.salary > player.money) {
                                Toast.makeText(this, "У вас недостаточно средств, необходимо выплатить компенсацию работнику", Toast.LENGTH_SHORT).show()
                            } else {
                                DatabaseFactory.getInstance(this).removeStaff(tmp.name)

                                player.staff--;
                                player.money -= tmp.salary
                                ins.setPlayerWithProperties(player)

                                Toast.makeText(this, "${tmp.name}, ты уволен", Toast.LENGTH_SHORT).show()

                                val intent = Intent(this, StaffActivity::class.java)
                                startActivity(intent)

                            }
                        }
                    }
                }


            }
            if (worker != null) {
                name.text = worker.name
                age.text = worker.age.toString()
                prof.text = worker.prof
                nation.text = worker.nation
                salary.text = "${worker.salary} $"
                birth.text = "${worker.birth.first}.${worker.birth.second}"
            }


        }
    }
}



