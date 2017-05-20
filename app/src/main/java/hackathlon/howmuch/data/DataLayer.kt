package hackathlon.howmuch.data

import android.content.Context
import android.media.Image
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import java.io.File

/**
 * Created by simon on 20.05.17.
 */
object DataLayer {

    val APP_TOKEN = "VPU6sebW8oSHQhXQsO96l9q9WJb2B2RM0YFL"

    var CONTEXT: Context? = null


    fun init(context: Context) {
        DataLayer.CONTEXT = context.applicationContext

        FuelManager.instance.basePath = "https://dev.sighthoundapi.com/v1/detections"
        FuelManager.instance.baseHeaders = mapOf("content-type" to "application/octet-stream", "x-access-token" to APP_TOKEN)
    }

    fun analyzePic(file: File) {

        Fuel.post("?type=person").body(file.readBytes()).response { request, response, result ->
            Log.d("Post", request.toString())
            Log.d("Response", response.toString())
            Log.d("Result", result.toString())
        }

    }
}