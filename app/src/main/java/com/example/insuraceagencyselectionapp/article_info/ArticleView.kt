package com.example.insuraceagencyselectionapp.article_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.insuraceagencyselectionapp.R
import com.example.insuraceagencyselectionapp.databinding.ActivityMainBinding

class ArticleView : AppCompatActivity() {

    private lateinit var  tittle: TextView
    private lateinit var autor: TextView
    private lateinit var date: TextView
    private lateinit var content: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_view)

        tittle = findViewById(R.id.articleTitle)
        autor =  findViewById(R.id.articleAutor)
        date =  findViewById(R.id.articleDate)
        content = findViewById(R.id.articleContent)

        tittle.text = intent.getStringExtra("title")
        autor.text = intent.getStringExtra("autor")
        date.text = "Fecha de publicacion: ${intent.getStringExtra("date")}"
        content.text = intent.getStringExtra("content")

    }
}