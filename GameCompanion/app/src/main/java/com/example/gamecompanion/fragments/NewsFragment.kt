package com.example.gamecompanion.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamecompanion.R
import com.example.gamecompanion.adapter.NewsAdapter
import com.example.gamecompanion.models.News
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NewsFragment: Fragment() {

    private var LOG = "NewsFragment"

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var progressBar: ProgressBar


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = Firebase.firestore

        initViews(view)
        initRecyclerView()
        getNews()
    }

    private fun getNews() {
        firestore.collection("news")
                .get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        progressBar.visibility = View.GONE
                        val news: List<News> = it.result?.documents?.mapNotNull { it.toObject(News::class.java) }.orEmpty()
                        newsAdapter.newsList = news.sortedBy { it.datePublished }
                        newsAdapter.notifyDataSetChanged()

                    } else {
                        Log.w("news", "Error downloading")
                    }
                }
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById((R.id.recyclerViewNews))
        progressBar = view.findViewById((R.id.progressBarNewsid))
    }

    private fun initRecyclerView() {
        // Layout manager
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        // Adapter
        newsAdapter = NewsAdapter(newsList = listOf())
        recyclerView.adapter = newsAdapter
    }
    private fun sendToast(message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }
}