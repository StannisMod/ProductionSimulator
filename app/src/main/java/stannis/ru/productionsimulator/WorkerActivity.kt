package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_worker.*

class WorkerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker)
        if (intent.hasExtra("TAG")) {
            val arr = intent.getStringExtra("TAG").split(".")
            val dataOfWorker = arr[0].split(",")
            name.text = dataOfWorker[0]
            age.text = dataOfWorker[1]
            prof.text = dataOfWorker[2]
            nation.text = dataOfWorker[3]
            salary.text = dataOfWorker[4]
            birth.text = "${dataOfWorker[5]}.${dataOfWorker[6]}"

            val cond = arr[1].trim() != "YourWorker"


            if (cond) {
                fire.visibility = View.INVISIBLE
                hire_prom.text = "Нанять"
                hire_prom.setOnClickListener {
                    //TODO
                }
            } else {
                hire_prom.setOnClickListener {
                    //TODO
                }
                fire.setOnClickListener {
                    //TODO
                }
            }


        }

    }
}
