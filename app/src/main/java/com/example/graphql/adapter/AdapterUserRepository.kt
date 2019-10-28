package com.example.graphql.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.graphql.FindUserRepositoriesQuery
import com.example.graphql.R

class AdapterUserRepository (private val context: Context, private val userRepositoryQueryList: List<FindUserRepositoriesQuery.Node>) :
    BaseAdapter(){

    override fun getCount(): Int {
        return userRepositoryQueryList.size
    }

    override fun getItem(position: Int): Any {
        return userRepositoryQueryList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            holder = ViewHolder()
            val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.repository_item, null, true)

            holder.itemName = convertView!!.findViewById(R.id.repositoryName) as TextView
            holder.itemDescription = convertView.findViewById(R.id.repositoryDesc) as TextView
            holder.itemPRCount = convertView.findViewById(R.id.repositoryRPC) as TextView

            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val userRepositoryNode = userRepositoryQueryList[position]

        holder.itemName!!.text = userRepositoryNode.name()
        if (userRepositoryNode.description() == null)  {
            holder.itemDescription!!.setText(R.string.no_description)
        } else {
            holder.itemDescription!!.text = userRepositoryNode.description()
        }
        holder.itemPRCount!!.text = "PR Count:  " + userRepositoryNode.pullRequests().totalCount()

        return convertView
    }

    private inner class ViewHolder {

        var itemName: TextView? = null
        var itemDescription: TextView? = null
        var itemPRCount: TextView? = null
    }
}