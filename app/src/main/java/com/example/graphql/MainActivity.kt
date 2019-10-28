package com.example.graphql

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.graphql.adapter.AdapterUser
import kotlinx.android.synthetic.main.activity_main.*
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {

    private lateinit var apolloClient: ApolloClientHandler
    private var adapterUser: AdapterUser? = null
    private var listUsersView: ListView? = null
    private var txtResponseUser: TextView? = null

    companion object {
        val Log = Logger.getLogger(MainActivity::class.java.name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtResponseUser = responseUsers as TextView

        apolloClient = ApolloClientHandler()
        apolloClient.setupApollo()

        editTextUserName.setOnEditorActionListener { v, actionId, event ->

            if(actionId == EditorInfo.IME_ACTION_SEARCH) {

                apolloClient.apolloCall().query(
                    FindUsersQuery
                        .builder()
                        .name(editTextUserName.text.toString())
                        .items(50)
                        .build()
                )
                .enqueue(object : ApolloCall.Callback<FindUsersQuery.Data>() {

                    override fun onFailure(e: ApolloException) {
                        Log.info(e.message.toString())
                    }

                    override fun onResponse(response: Response<FindUsersQuery.Data>) {
                        Log.info(" " + response.data()?.search())

                        runOnUiThread {
                            val userQueryList = response.data()?.search()?.edges()

                            if (userQueryList!!.isNotEmpty()) {
                                listUsersView = listSearchedUsers as ListView
                                adapterUser = AdapterUser(
                                    this@MainActivity,
                                    userQueryList!!
                                )
                                listUsersView!!.adapter = adapterUser

                                listUsersView!!.onItemClickListener =
                                    AdapterView.OnItemClickListener { parent, view, position, id ->
                                        val userNode = userQueryList[position].node()
                                        if (userNode is FindUsersQuery.AsUser) {
                                            val intent = Intent(
                                                this@MainActivity,
                                                GetUserRepository::class.java
                                            )
                                            intent.putExtra("login", userNode.login())
                                            startActivity(intent)
                                        }
                                    }
                            } else {
                                txtResponseUser!!.setText(R.string.msg_no_users)
                            }
                        }
                    }
                })
                true
            } else {
                false
            }
        }
    }
}
