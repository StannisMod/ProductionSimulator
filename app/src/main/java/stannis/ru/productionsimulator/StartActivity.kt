package stannis.ru.productionsimulator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_start.*
import stannis.ru.productionsimulator.Databases.DatabaseFactory
import stannis.ru.productionsimulator.Databases.PlayerStatsDatabase

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val ins = PlayerStatsDatabase.getInstance(this)
        DatabaseFactory.index = 1
        if(ins.getPlayerStats()==null){
            continueGame.visibility = View.GONE
        }
        continueGame.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        newGame.setOnClickListener {
            startActivity(Intent(this, ListProductionActivity::class.java))
            finish()
        }


    }
    override fun onBackPressed(){

    }
}
