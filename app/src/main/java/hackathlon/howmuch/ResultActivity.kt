package hackathlon.howmuch

import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import hackathlon.howmuch.R.id.textView

import hackathlon.howmuch.data.Box
import jp.wasabeef.picasso.transformations.BlurTransformation
import java.io.File
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import org.json.JSONArray
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

        var objects = JSONObject(content).getJSONArray("objects")

        var persons: Array<Box> = emptyArray()
        var faces: Array<Box> = emptyArray()

        for (i in 0..(objects.length() - 1)) {
            val item = objects.getJSONObject(i)

            when(item["type"]) {
                "person" -> {
                    var type = item["type"] as String
                    var boundingBox = item["boundingBox"] as JSONObject
                    var x = boundingBox["x"] as Int
                    var y = boundingBox["y"] as Int
                    var h = boundingBox["height"] as Int
                    var w = boundingBox["width"] as Int

                    persons = persons.plus(Box(type, x, y, h, w))
                }
                "face" -> {
                    var type = item["type"] as String
                    var boundingBox = item["boundingBox"] as JSONObject
                    var x = boundingBox["x"] as Int
                    var y = boundingBox["y"] as Int
                    var h = boundingBox["height"] as Int
                    var w = boundingBox["width"] as Int

                    faces = faces.plus(Box(type, x, y, h, w))
                }
            }
        }

        for (item in persons) {
            Log.d("ResultActivity", item.toString())
        }

        for (item in faces) {
            Log.d("ResultActivity", item.toString())
        }

        textView.setText("There are roughly " + objects.length() + " people in this picture.")

    }
}