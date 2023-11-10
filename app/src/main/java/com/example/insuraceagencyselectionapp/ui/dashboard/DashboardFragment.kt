package com.example.insuraceagencyselectionapp.ui.dashboard

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.insuraceagencyselectionapp.article_adapter.ArticleAdapter
import com.example.insuraceagencyselectionapp.article_adapter.article_object.ArticleObject
import com.example.insuraceagencyselectionapp.databinding.FragmentDashboardBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.example.medicalonlineapp.http.httpRequest
import org.json.JSONObject

class DashboardFragment : Fragment() {

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private val HOSTING: String = "https://rossworld.000webhostapp.com/getArticles.php"
    private lateinit var articleList: ArrayList<ArticleObject>
    private lateinit var adapter:ArticleAdapter

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        swipeRefreshLayout = _binding!!.swipeToRefreshLayout

        _binding!!.recyclerArticles.setHasFixedSize(true)
        _binding!!.recyclerArticles.layoutManager = LinearLayoutManager(this.requireActivity())

        swipeRefreshLayout!!.setOnRefreshListener {
            loadArticles()
        }

      _binding!!.editFilter.addTextChangedListener { filter ->
            val articleFilter = this.articleList.filter { article ->
                article.getTitle().lowercase().contains(filter.toString().lowercase()) ||
                        article.getAutor().lowercase().contains(filter.toString().lowercase()) ||
                            article.getContent().lowercase().contains(filter.toString().lowercase())
            }
            adapter.updateList(articleFilter)
        }

        loadArticles()
    }

    private fun isOnline(): Boolean{
        val on = (this.requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)

        val activityNetwork: NetworkInfo? = on.activeNetworkInfo

        return activityNetwork != null && activityNetwork.isConnected
    }

    private fun loadArticles() {
        if(isOnline()){
            try {
                val json = JSONObject()

                json.put("getArticles", true)

                val string = httpRequest{
                    if(it == null){
                        print("error conexion")
                        return@httpRequest
                    }
                    println("it $it")
                }.execute("POST", HOSTING, json.toString())

                Log.d("Error_json", string.get())
                val parser = Parser()
                val stringBuilder: StringBuilder = StringBuilder(string.get())
                val json2: JsonObject = parser.parse(stringBuilder) as JsonObject

                articleList = ArrayList()

                if(json2.int("success") == 1){
                    val jsonFinal = JSONObject(string.get())
                    val articlesInfo = jsonFinal.getJSONArray("articles")

                    for(i in 0 until articlesInfo.length()){
                        val title: String = articlesInfo.getJSONObject(i).getString("title").toString()
                        val autor: String = articlesInfo.getJSONObject(i).getString("autor")
                        val date: String = articlesInfo.getJSONObject(i).getString("date")
                        val content: String = articlesInfo.getJSONObject(i).getString("content")

                       articleList.add(ArticleObject(title, autor, date, content))
                    }

                    adapter = ArticleAdapter(this.requireActivity(), articleList, this.requireActivity())
                   _binding!!.recyclerArticles.adapter = adapter

                }else if(json2.int("success") == 0){
                    Toast.makeText(this.activity, "No se encontraron articulos", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this.activity, "Problemas en la conexion", Toast.LENGTH_SHORT).show()
                }

            }catch (ex:Exception){
                Toast.makeText(this.activity, "ha ocurrido un problema", Toast.LENGTH_SHORT).show()
                ex.printStackTrace()
                 swipeRefreshLayout!!.isRefreshing = false
            }

            swipeRefreshLayout!!.isRefreshing = false
        }else{
            swipeRefreshLayout!!.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}