package hackathlon.howmuch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.provider.MediaStore
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.github.kittinunf.fuel.android.extension.responseJson
import com.squareup.picasso.Picasso
import hackathlon.howmuch.data.DataLayer
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    lateinit var button1: Button
    lateinit var imageView: ImageView
    lateinit var progressBar: ProgressBar
    val CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100
    val MEDIA_TYPE_IMAGE = 1
    lateinit var fileUri: Uri
    var fileString: String? = null
    var mCurrentPhotoPath: String? = null
    var tempFile: File? = null
    var dataLayer = DataLayer()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dataLayer.init()
        button1 = findViewById(R.id.button_1) as Button
        imageView = findViewById(R.id.imageView_1) as ImageView
        progressBar = findViewById(R.id.progressBar) as ProgressBar
        progressBar.visibility = View.INVISIBLE

        button1.setOnClickListener {
            Toast.makeText(this, "button works.", Toast.LENGTH_SHORT).show()
            dispatchTakePictureIntent()
        }

        if(!isDeviceSupportCamera()) {
            Toast.makeText(applicationContext, "Sorry! Device is not supported", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    @Throws(IOException::class)
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //Ensure that there is a camera activity
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            //Create the file where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
                Log.d("Info: ", getExternalFilesDir(Environment.DIRECTORY_PICTURES.toString()).toString())
            } catch (ignored: IOException){

            }
            if (photoFile != null){
                val photoURI = FileProvider.getUriForFile(this,
                        "hackathlon.howmuch",
                        photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                Log.d("Info: ", "Picture saved")
                tempFile = photoFile

            startActivityForResult(takePictureIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE)
            }
        }
    }

    //startActivityForResult doesn't save the image, so we use a class variable to temporarily store it
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Picasso.with(this).load(tempFile).into(imageView)
        progressBar.visibility = View.VISIBLE
        dataLayer.analyzePic(tempFile as File)


//        val intent = Intent(this, ResultActivity::class.java)
//        intent.putExtra("image", tempFile)
//        startActivity(intent)

        dataLayer.analyzePic(tempFile as File).responseJson{ request, response, result ->
            result.fold(
                    { d -> responseHandler(d.content)},
                    { err -> Log.e("Log", err.message)}
            )
        }

    }

    fun responseHandler(content: String) {
        findViewById(R.id.progressBar).visibility = View.GONE

        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("image", tempFile)
        intent.putExtra("content", content)
        startActivity(intent)
    }

    private fun isDeviceSupportCamera(): Boolean {
        return (applicationContext.packageManager.hasSystemFeature(
                PackageManager.FEATURE_CAMERA))
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }


}
