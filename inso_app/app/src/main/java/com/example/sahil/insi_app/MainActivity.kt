package com.example.sahil.insi_app

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    protected fun updateBtnClicked(view: View){
        val url = "https://insi.herokuapp.com/getStats"
        MyAsynTask().execute(url)
    }

    inner class MyAsynTask: AsyncTask<String,String,String>(){
        override fun onPreExecute() {
            //before task started


        }
        override fun doInBackground(vararg p0: String?): String {
            //TOdo http call
            try {
                val url = URL(p0[0])
                val urlConnect = url.openConnection() as HttpURLConnection/*HttpsURLConnection*/
                urlConnect.connectTimeout= 7000
                var inString= ConvertStreamTOstring(urlConnect.inputStream)
                //cannot access to ui,
                //so you have to send your data to onProgressUpdate method (mentioned below this method)
                // by calling publishProgress method

                publishProgress(inString)

            }catch (e:Exception){
                Log.d("hello1", e.toString())
            }
            return " "
        }
 
        override fun onProgressUpdate(vararg values: String?) {
            //during task run
            try {
                var json =JSONObject(values[0])
                /*val nextInsideJson = json.getJSONObject("nextInsideJsonKey")
                val finalValue = nextInsideJson.getString("finalKey")*/
                val p1= json.getJSONObject("temp_uptime")
                val p2= p1.getJSONObject("2018-03-18")
                val p3= p2.getJSONArray("total_obs")
                val answer = p3.toString()

                textView.text = answer
            }catch (e:Exception){
                Log.d("hello2", e.toString())
            }

        }

        override fun onPostExecute(result: String?) {
            //after task done

        }
    }
    fun ConvertStreamTOstring(inputstream: InputStream):String{
        val bufferReader = BufferedReader(InputStreamReader(inputstream))
        var line:String
        var AllString:String = ""
        try {
            do {
                line = bufferReader.readLine()
                if (line!=null){
                    AllString+=line
                }

            }while (line!=null)
            inputstream.close()
        }catch (e:Exception){
            Log.d("hello3", e.toString())
        }
        return AllString
    }
}
