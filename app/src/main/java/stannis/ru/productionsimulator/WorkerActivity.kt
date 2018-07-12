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
import stannis.ru.productionsimulator.Enums.EnumFactory
import stannis.ru.productionsimulator.Functions.factory
import stannis.ru.productionsimulator.Functions.isPromotioned
import stannis.ru.productionsimulator.Models.DataTime
import stannis.ru.productionsimulator.Models.Message
import stannis.ru.productionsimulator.Models.Player
import stannis.ru.productionsimulator.Models.Worker


class WorkerActivity : AppCompatActivity() {
    var str: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker)
        messageUnRead.visibility = if (Message.sizeOfUnRead() > 0) View.VISIBLE else View.INVISIBLE
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
            str = arr[1].trim()
            var worker: Worker? = null


            if (cond) {
                worker = Worker.getStaff(this, "labor", arr[0].trim())
                val adapter = WorkerAdapter(Array(8) { i -> i }.toCollection(ArrayList<Int>()), this, worker!!)
                gridWorker.adapter = adapter

                fire.visibility = View.INVISIBLE
                hire_prom.text = "Нанять"

                hire_prom.setOnClickListener {
                    val tmp = Worker.getStaff(this, "labor", arr[0].trim())
                    if (player != null && tmp != null) {
                        if (player.money < tmp.salary && Worker.sizeOfStaff() < EnumFactory.findById(DatabaseFactory.index).workerkLimit) {
                            Toast.makeText(this, "У вас недостаточно средств, или на вашей фабрике недостаточно метса ", Toast.LENGTH_SHORT).show()
                        } else {
                            player.money -= tmp.salary
                            player.staff++
                            tmp.hire()
                            Toast.makeText(this, "${tmp.name}, ты нанят", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this, MarketActivity::class.java)
                            startActivity(intent)

                        }
                    }
                }
            } else {
                worker = Worker.getStaff(this, "staff", arr[0].trim())
                var al = Array(8) { i -> i }.toCollection(ArrayList<Int>())
                var adapter = WorkerAdapter(al, this, worker!!)
                gridWorker.adapter = adapter

                if (worker != null) {
                    hire_prom.setOnClickListener {
                        val tmp: Worker? = Worker.getStaff(this, "staff", arr[0].trim())
                        if (tmp != null) {
                            tmp.getPromotion()
                            if (!isPromotioned.isEmpty() && factory == DatabaseFactory.index) {
                                if (arr[2].toInt() < isPromotioned.size) {
                                    isPromotioned[arr[2].toInt()] = true
                                }
                            }
                            al = Array(8) { i -> i }.toCollection(ArrayList<Int>())
                            adapter = WorkerAdapter(al, this, tmp)
                            gridWorker.adapter = adapter


                        }
                    }
                    fire.setOnClickListener {
                        val tmp: Worker? = Worker.getStaff(this, "staff", arr[0].trim())
                        if (tmp != null && player != null) {
                            if (tmp.salary > player.money) {
                                Toast.makeText(this, "У вас недостаточно средств, необходимо выплатить компенсацию работнику", Toast.LENGTH_SHORT).show()
                            } else {


                                player.staff--;
                                player.money -= tmp.salary
                                tmp.fire()

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

    override fun onBackPressed() {
        if (str != null) {
            startActivity(Intent(this, if (str == "YourWorker") StaffActivity::class.java else MarketActivity::class.java))
            finish()
        }
    }
}

class WorkerAdapter : BaseAdapter {
    var list: ArrayList<Int> = ArrayList()
    var ctx: Context? = null
    var st: Worker? = null

    constructor(list: ArrayList<Int>, ctx: Context, st: Worker) {
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



