package com.example.graphql

import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient

class ApolloClientHandler {

    private lateinit var apolloClient: ApolloClient

    companion object {
        private const val BASE_GQL_URL = "https://api.github.com/graphql"
    }

    fun setupApollo() {
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
        apolloClient = ApolloClient.builder()
            .serverUrl(BASE_GQL_URL)
            .okHttpClient(okHttp)
            .build()
    }

    fun apolloCall(): ApolloClient {
        return apolloClient;
    }
}