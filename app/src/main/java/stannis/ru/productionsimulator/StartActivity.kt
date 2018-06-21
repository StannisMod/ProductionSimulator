package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.activity_start.*
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase
import stannis.ru.productionsimulator.Enums.EnumFactory
import stannis.ru.productionsimulator.Functions.fillDb
import stannis.ru.productionsimulator.Functions.loadAll
import stannis.ru.productionsimulator.Models.Factory

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val ins = PlayerStatsDatabase.getInstance(this)
        if (ins.getPlayerStats() == null) {
            continueGame.visibility = View.GONE
        }
        continueGame.setOnClickListener {
            DatabaseFactory.index = 0
            loadAll(this)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        newGame.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            if (ins.getPlayerStats() == null) {

               intent.putExtra("TAG", "firstTime")
            }else{
                intent.putExtra("TAG", "1")

            }
            fillDb(this)
            startActivity(intent)
            finish()
        }


    }

    override fun onBackPressed() {

    }
}
