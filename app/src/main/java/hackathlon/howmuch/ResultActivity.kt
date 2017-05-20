package hackathlon.howmuch

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.BlurTransformation
import java.io.File
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import org.json.JSONObject




/**
 * Created by konstantin on 20.05.17.
 */

class ResultActivity : Activity() {

    lateinit var imageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        imageView = findViewById(R.id.imageView2) as ImageView


        val pictureFile = getIntent().extras.get("image") as File
        Picasso.with(this).load(pictureFile).transform(BlurTransformation(this, 24)).into(imageView)

        val content = getIntent().extras.get("content") as String

        var json = JSONObject(content)


        Log.d("ResultActivity", json.getJSONArray("objects").toString())



    }
}