package com.basics.fooddeliveryapp.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basics.fooddeliveryapp.Adapter.FaqsAdapter
import com.basics.fooddeliveryapp.Adapter.RestaurantMenuAdapter
import com.basics.fooddeliveryapp.R
import com.basics.fooddeliveryapp.model.FrequentQuestion
import com.basics.fooddeliveryapp.model.RestaurantMenu

class FaqsFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var restaurantMenuAdapter: RestaurantMenuAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    var faqsListInfo = arrayListOf<FrequentQuestion>()

    val questionList = arrayListOf<String>(
        "Q1. How will the training be delivered?",
        "Q2. what will be the timing of the training?",
        "Q3. What is the duration of training?",
        "q4. When can i start my training?"

    )
    val answerList = arrayListOf<String>(
        "A1. You will be taught using pre-recorded videos and text tutorails.The training has quizzes,assignmnets,and tests to help you learn better.",
        "A2. This will be an option provided in the navigation drawer to log out of the application. This will be a simple log out which redirects the user to the login page while clearing all the preferences stored. Make sure that after logout, if the user presses the back button then the app should exit and not take the user back to the home page",
        "A3. The training duration is of 6 weeks",
        "A4. You can choose your preferred batch date while signing up for the training program."
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_faqs, container, false)

        recyclerView = view.findViewById(R.id.faqsRecyclerView)
        layoutManager = LinearLayoutManager(activity as Context)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)

        progressLayout.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE

        for (i in 0 until questionList.size) {
            val frequentQuestion = FrequentQuestion(
                questionList[i],
                answerList[i]
            )
            faqsListInfo.add(frequentQuestion)
        }
        progressLayout.visibility = View.GONE
        progressBar.visibility = View.GONE


        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = FaqsAdapter(activity as Context, faqsListInfo)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )






        return view
    }

}
