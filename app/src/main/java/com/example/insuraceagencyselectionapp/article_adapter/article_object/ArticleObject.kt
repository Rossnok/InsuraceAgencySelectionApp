package com.example.insuraceagencyselectionapp.article_adapter.article_object

import java.util.Date

class ArticleObject {
    private var title: String
    private var autor: String
    private var date: String
    private var content: String

    constructor(title : String, autor: String, date: String, content: String){
        this.title = title
        this.autor = autor
        this.date = date
        this.content = content
    }

    fun getTitle(): String {
        return this.title
    }

    fun getAutor(): String {
            return this.autor
        }

    fun getDate(): String {
            return this.date
        }

    fun getContent(): String {
            return this.content
        }
}