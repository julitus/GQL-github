package com.example.graphql

import android.content.res.Resources
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.graphql.adapter.AdapterUserRepository
import kotlinx.android.synthetic.main.user_repositories.*

class GetUserRepository : AppCompatActivity() {

    private lateinit var apolloClient: ApolloClientHandler
    private var adapterUserRepository: AdapterUserRepository? = null
    private var listUserRepositoryView: ListView? = null
    private var btnBack: Button? = null
    private var txtUserLogin: TextView? = null
    private var txtResponseRepo: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_repositories)

        btnBack = btnBackUsers as Button
        txtUserLogin = titleUserLogin as TextView
        txtResponseRepo = responseRepos as TextView

        apolloClient = ApolloClientHandler()
        apolloClient.setupApollo()

        val intent = intent
        var login = intent.getSerializableExtra("login") as String
        txtUserLogin!!.text = login

        apolloClient.apolloCall().query(
            FindUserRepositoriesQuery
                .builder()
                .login(login)
                .items(50)
                .build()
        )
        .enqueue(object : ApolloCall.Callback<FindUserRepositoriesQuery.Data>() {

            override fun onFailure(e: ApolloException) {
                MainActivity.Log.info(e.message.toString())
            }

            override fun onResponse(response: Response<FindUserRepositoriesQuery.Data>) {
                MainActivity.Log.info(" " + response.data()?.user())

                runOnUiThread {
                    val userRepositoriesQueryList = response.data()?.user()?.repositories()?.nodes()

                    if (userRepositoriesQueryList!!.isNotEmpty()) {
                        listUserRepositoryView = listUserRepositories as ListView
                        adapterUserRepository = AdapterUserRepository(
                            this@GetUserRepository,
                            userRepositoriesQueryList!!
                        )
                        listUserRepositoryView!!.adapter = adapterUserRepository

                    } else {
                        txtResponseRepo!!.setText(R.string.msg_no_repos)
                    }
                }
            }
        })

        btnBack!!.setOnClickListener {
            onBackPressed();
        }

    }
}