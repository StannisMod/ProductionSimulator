package stannis.ru.productionsimulator

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_start.*
import stannis.ru.productionsimulator.Models.DatabaseFactory
import stannis.ru.productionsimulator.Models.fillDb

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        if(DatabaseFactory.getInstance(this).getPlayerStats()==null){
            continueGame.visibility = View.GONE
            fillDb(this)
        }
        continueGame.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        newGame.setOnClickListener {
            fillDb(this)
            //startActivity(Intent(this, //ListProductionActivity::class.java))
        }


    }
    override fun onBackPressed(){

    }
}
