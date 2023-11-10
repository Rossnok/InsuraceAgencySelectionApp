package com.example.insuraceagencyselectionapp.article_adapter

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.insuraceagencyselectionapp.R
import com.example.insuraceagencyselectionapp.article_adapter.article_object.ArticleObject
import com.example.insuraceagencyselectionapp.article_info.ArticleView

class ArticleAdapter (context: Context, articleList:List<ArticleObject>, activity: Activity) :
    RecyclerView.Adapter<ArticleAdapter.ProductViewHolder>() {

    private val context = context
    private val activity: Activity

    private var articleList : List<ArticleObject>


    init{
        this.activity = activity
        this.articleList = articleList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ProductViewHolder{
        val inflate : LayoutInflater = LayoutInflater.from(context)
        val view: View = inflate.inflate(R.layout.article_card_view, parent,false)

        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int){
        val article: ArticleObject = articleList[position]

        holder.title!!.text = article.getTitle()
        holder.autor!!.text = article.getAutor()
        holder.date!!.text = article.getDate()
        holder.content!!.text = article.getContent()

        holder.itemView.setOnClickListener(){
            showDialog(
                article.getTitle(),
                article.getAutor(),
                article.getDate(),
                article.getContent())
        }

    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    inner class ProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var title: TextView? = null
        var autor: TextView? = null
        var date: TextView? = null
        var content: TextView? = null

        init {
            //Inicializar las variables anteriores con findByViewId
            title = itemView.findViewById(R.id.articleTitle)
            autor = itemView.findViewById(R.id.articleAutor)
            date = itemView.findViewById(R.id.articleDate)
            content = itemView.findViewById(R.id.articleContent)
        }

    }

    fun updateList(articleList:List<ArticleObject>){
        this.articleList = articleList
        notifyDataSetChanged()
    }

    private fun showDialog(title: String, autor: String, date: String, content: String) {
        val builder = AlertDialog.Builder(this.activity).setMessage("Ver toda la informacion del articulo?")
        builder.apply {
            setPositiveButton(R.string.ok,
                DialogInterface.OnClickListener { dialog, id ->
                    val intent = Intent(activity, ArticleView::class.java).apply {
                        putExtra("title", title)
                        putExtra("autor", autor)
                        putExtra("date", date)
                        putExtra("content", content)

                        activity.startActivity(this)
                        dialog.dismiss()
                }
            })
            setNegativeButton(R.string.cancel,
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                })
        }

        builder.create().show()
    }
}