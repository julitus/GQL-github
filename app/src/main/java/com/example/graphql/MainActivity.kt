package com.example.graphql

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.example.graphql.adapter.AdapterUser
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {

    private lateinit var apolloClient: ApolloClient
    private var adapterUser: AdapterUser? = null
    private var listView: ListView? = null

    companion object {
        val Log = Logger.getLogger(MainActivity::class.java.name)
        private const val BASE_URL = "https://api.github.com/graphql"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(this@MainActivity, "Init!!!!!", Toast.LENGTH_SHORT).show()

        apolloClient = setupApollo()

        /*apolloClient.query(
            FindUsersQuery
                .builder()
                .name("julit")
                .items(20)
                .build()
        )
            .enqueue(object : ApolloCall.Callback<FindUsersQuery.Data>() {

                override fun onFailure(e: ApolloException) {
                    Log.info(e.message.toString())
                }

                override fun onResponse(response: Response<FindUsersQuery.Data>) {
                    Log.info(" " + response.data()?.search())

                    runOnUiThread {


                        listView = listSearchedUsers as ListView
                        /*val user = response.data()?.search()?.edges()?.get(0)?.node()
                        if (user is FindUsersQuery.AsUser) {
                            Toast.makeText(this@MainActivity, user.name, Toast.LENGTH_SHORT).show()
                        }*/
                        val userQueryList = response.data()?.search()?.edges()
                        adapterUser = AdapterUser(this@MainActivity, userQueryList!!)
                        listView!!.adapter = adapterUser
                        //adapterUser!!.notifyDataSetChanged()

                        /*listView!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                            val intent = Intent(this@MainActivity, GetAllUserRepository::class.java)
                            intent.putExtra("user", userQueryList[position])
                            startActivity(intent)
                        }*/
                    }
                }
            })*/

        editTextUserName.setOnEditorActionListener { v, actionId, event ->

            if(actionId == EditorInfo.IME_ACTION_SEARCH) {

                Toast.makeText(this@MainActivity, "Pass Cond!!!!!", Toast.LENGTH_SHORT).show()

                apolloClient.query(
                    FindUsersQuery
                        .builder()
                        .name(editTextUserName.text.toString())
                        .items(20)
                        .build()
                )
                .enqueue(object : ApolloCall.Callback<FindUsersQuery.Data>() {

                    override fun onFailure(e: ApolloException) {
                        Log.info(e.message.toString())
                    }

                    override fun onResponse(response: Response<FindUsersQuery.Data>) {
                        Log.info(" " + response.data()?.search())

                        runOnUiThread {
                            listView = listSearchedUsers as ListView
                            //val user = response.data()?.search()?.edges()?.get(0)?.node()
                            /*if (user is FindUsersQuery.AsUser) {
                                Toast.makeText(this@MainActivity, user.name(), Toast.LENGTH_SHORT).show()
                            }*/
                            val userQueryList = response.data()?.search()?.edges()
                            adapterUser = AdapterUser(this@MainActivity, userQueryList!!)
                            listView!!.adapter = adapterUser
                            //adapterUser!!.notifyDataSetChanged()

                            /*listView!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                                val intent = Intent(this@MainActivity, GetAllUserRepository::class.java)
                                intent.putExtra("user", userQueryList[position])
                                startActivity(intent)
                            }*/
                        }
                    }
                })
                true
            } else {
                false
            }
        }
    }

    private fun setupApollo(): ApolloClient {
        val okHttp = OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder().method(original.method(), original.body())
                builder.addHeader("Authorization"
                    , "Bearer " + BuildConfig.AUTH_TOKEN)
                chain.proceed(builder.build())
            }
            .build()
        return ApolloClient.builder()
            .serverUrl(BASE_URL)
            .okHttpClient(okHttp)
            .build()
    }
}
