package stannis.ru.productionsimulator

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_worker.*
import kotlinx.android.synthetic.main.date_layout.*
import kotlinx.android.synthetic.main.staff_view.view.*
import kotlinx.android.synthetic.main.stats_panel.*
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase
import stannis.ru.productionsimulator.Models.DataTime
import stannis.ru.productionsimulator.Models.Player
import stannis.ru.productionsimulator.Models.Staff


class WorkerActivity : AppCompatActivity() {
    override fun onBackPressed() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker)
        messageUnRead.visibility = if (PlayerStatsDatabase.getInstance(this).getMessage().size > 0) View.VISIBLE else View.INVISIBLE
        val player = Player.getInstance(this)
        if (player != null) {
            money.text = player.money.toString()

            rep.progress = player.reputation
        }
        val curData = DataTime.getInstance(this)
        rep.setEnabled(false)
        if (curData != null) {
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
                val adapter = WorkerAdapter(Array(8) { i -> i }.toCollection(ArrayList<Int>()), this, worker!!)
                gridWorker.adapter = adapter

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
                var al = Array(8) { i -> i }.toCollection(ArrayList<Int>())
                var adapter = WorkerAdapter(al, this, worker!!)
                gridWorker.adapter = adapter

                if (worker != null) {
                    hire_prom.setOnClickListener {
                        val tmp: Staff? = DatabaseFactory.getInstance(this).getWorkerFromStaff(arr[0].trim())
                        if (tmp != null) {
                            tmp.getPromotion()
                            DatabaseFactory.getInstance(this).setStaffWithProperties(tmp)
                            al = Array(8) { i -> i }.toCollection(ArrayList<Int>())
                            adapter = WorkerAdapter(al, this, tmp)
                            gridWorker.adapter = adapter


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


                                Toast.makeText(this, "${tmp.name}, ты уволен", Toast.LENGTH_SHORT).show()

                                val intent = Intent(this, StaffActivity::class.java)
                                startActivity(intent)

                            }
                        }
                    }
                }


            }


        }
    }
}

class WorkerAdapter : BaseAdapter {
    var list: ArrayList<Int> = ArrayList()
    var ctx: Context? = null
    var st: Staff? = null

    constructor(list: ArrayList<Int>, ctx: Context, st: Staff) {
        this.list = list
        this.ctx = ctx
        this.st = st
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var inflator = ctx!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var itemView = inflator.inflate(R.layout.staff_view, null)
        if (position == 0) {
            itemView.param.text = "Имя: "
            itemView.paramValue.text = "${st!!.name.split(" ")[0]}"
        } else if (position == 1) {
            itemView.param.text = "Фамилия: "
            itemView.paramValue.text = "${st!!.name.split(" ")[1]}"
        } else if (position == 2) {
            itemView.param.text = "Возраст: "
            itemView.paramValue.text = "${st!!.age}"
        } else if (position == 3) {
            itemView.param.text = "Специальность: "
            itemView.paramValue.text = "${st!!.prof}"
        } else if (position == 4) {
            itemView.param.text = "Квалификация: "
            itemView.paramValue.text = "${st!!.quality}"
        } else if (position == 5) {
            itemView.param.text = "Национальность: "
            itemView.paramValue.text = "${st!!.nation}"

        } else if (position == 6) {
            itemView.param.text = "Зарплата: "
            itemView.paramValue.text = "${st!!.salary}$"
        } else if (position == 7) {
            itemView.param.text = "Дата рождения: "
            itemView.paramValue.text = "${st!!.birth.first}.${st!!.birth.second}"
        }
        return itemView
    }

    override fun getItem(position: Int): Any {
        return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}



