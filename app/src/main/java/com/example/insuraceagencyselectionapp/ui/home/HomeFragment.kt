package com.example.insuraceagencyselectionapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.insuraceagencyselectionapp.databinding.FragmentHomeBinding
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.example.insuraceagencyselectionapp.ui.datepicker.DatePickerFragment
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class HomeFragment : Fragment() {

    val hosting: String = "https://rossworld.000webhostapp.com/"
    private var progresAsyncTask: ProgressAsyncTask? = null
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        _binding?.registerBtn?.setOnClickListener{

            if(!_binding!!.title.text.isNullOrEmpty() &&
                !_binding!!.autor.text.isNullOrEmpty() &&
                !_binding!!.date.text.isNullOrEmpty() &&
                !_binding!!.content.text.isNullOrEmpty()
                ){

                val json = JSONObject()
                json.put("insertArticle", true)
                json.put("title", _binding!!.title.text.trim())
                json.put("autor", _binding!!.autor.text.trim())
                json.put("date", _binding!!.date.text.toString().trim())
                json.put("content", _binding!!.content.text.trim())
                progresAsyncTask = ProgressAsyncTask()
                progresAsyncTask!!.execute("POST", hosting + "insertArticle.php", json.toString())
            }else{// si no
                Toast.makeText(
                    this.requireContext(),
                    "Se requiere llenar todos los campos",
                    Toast.LENGTH_SHORT).show()// mensaje de error
            }
        }

        _binding!!.date.setOnClickListener{
            showDialogPicker()
        }

    }

    private fun showDialogPicker() {// mostrar dialogo para seleccionar una fecha
        val dataPicker = DatePickerFragment {day, month, year -> onDateSelected(day, month, year)}
        fragmentManager?.let { dataPicker.show(it,"Fecha de la cita") }// creacion del datapickerDialog
    }

    private fun onDateSelected(day:Int, month:Int, year:Int){// cuando se selecciona una fecha
        _binding!!.date.setText("$year-${month+1}-$day")// texto del editText = fecha seleccionada
    }

    inner class ProgressAsyncTask: AsyncTask<String, Unit, String>(){

        val TIMEOUT = 50000

        override fun onPreExecute() {
           _binding!!.registerBtn.isEnabled = false
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String): String? {

            val url = URL(params[1])
            val httpClient = url.openConnection() as HttpURLConnection
            httpClient.readTimeout = TIMEOUT
            httpClient.connectTimeout = TIMEOUT
            httpClient.requestMethod = params[0]

            if(params[0] == "POST"){
                httpClient.instanceFollowRedirects = false
                httpClient.doOutput = true
                httpClient.doInput = true
                httpClient.useCaches = false
                httpClient.setRequestProperty("Content-Type","application/json; charset-utf-8")
            }
            try{
                if (params[0] == "POST"){
                    httpClient.connect()
                    val os = httpClient.outputStream
                    val writer = BufferedWriter(OutputStreamWriter(os,"UTF-8"))
                    writer.write(params[2])
                    writer.flush()
                    writer.close()
                    os.close()
                }
                Log.d("e",""+httpClient.responseCode)
                if(httpClient.responseCode == HttpURLConnection.HTTP_OK){
                    val stream = BufferedInputStream(httpClient.inputStream)
                    val data: String = readStream(inputStream = stream)
                    println("aqui estamos $data")

                    val parser = Parser()
                    val stringBuilder: StringBuilder = StringBuilder(data)

                    val json2 = parser.parse(stringBuilder) as JsonObject
                    if(json2.int("success") == 1){

                    }
                    return data
                } else if((httpClient.responseCode == HttpURLConnection.HTTP_CLIENT_TIMEOUT)){
                    println("Error${httpClient.responseCode}")
                }
                else{

                    println("Error ${httpClient.responseCode}")
                }
            }catch(e:Exception){
                e.printStackTrace()
            }finally {
                httpClient.disconnect()
            }
            return null
        }

        fun readStream(inputStream: BufferedInputStream): String{
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            bufferedReader.forEachLine { stringBuilder.append(it)}
            return stringBuilder.toString()
        }

        override fun onProgressUpdate(vararg values: Unit?) {
            super.onProgressUpdate(*values)
        }

        override fun onPostExecute(result: String?) {
            if(!result.isNullOrBlank() && !result.isNullOrEmpty()) {// si el resutado no es null, vacio o en blanco
                val parser: Parser = Parser()
                val stringBuilder: StringBuilder = StringBuilder(result)

                val json2: JsonObject = parser.parse(stringBuilder) as JsonObject
                if (json2.int("success") == 1) {// si success = 1
                    Toast.makeText(context, "Articulo agregado a la lista", Toast.LENGTH_SHORT).show()//mensaje success
                }else if (json2.int("success") == 0){
                    Toast.makeText(
                        context,
                        "Ocurrio un problema durante el registro, contacte a sopporte tecnico",
                        Toast.LENGTH_SHORT).show()// mensaje de error
                }
            }
            super.onPostExecute(result)
            // vaciar contenido de los editText
            _binding!!.title.setText("")
            _binding!!.autor.setText("")
            _binding!!.date.setText("")
            _binding!!.content.setText("")

            _binding!!.registerBtn.isEnabled = true // agendar cita habilitado
        }

        override fun onCancelled() {
            super.onCancelled()
        }
    }
}