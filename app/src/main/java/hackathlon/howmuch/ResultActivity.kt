package hackathlon.howmuch

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import hackathlon.howmuch.R.id.textView
import jp.wasabeef.picasso.transformations.BlurTransformation
import java.io.File
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import org.json.JSONObject




/**
 * Created by konstantin on 20.05.17.
 */

class ResultActivity : Activity() {

    lateinit var imageView: ImageView
    lateinit var textView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        imageView = findViewById(R.id.imageView2) as ImageView
        textView = findViewById(R.id.textView) as TextView


        val pictureFile = getIntent().extras.get("image") as File
        Picasso.with(this).load(pictureFile).into(imageView)

        val content = getIntent().extras.get("content") as String

        var json = JSONObject(content)

        textView.setText(json.getJSONArray("objects").toString())


        Log.d("ResultActivity", json.getJSONArray("objects").toString())



    }
}