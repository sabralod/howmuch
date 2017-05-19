package hackathlon.howmuch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    lateinit var button1: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button1 = findViewById(R.id.button_1) as Button
        button1.setOnClickListener { Toast.makeText(this, "button works.", Toast.LENGTH_SHORT).show() }
    }


}
