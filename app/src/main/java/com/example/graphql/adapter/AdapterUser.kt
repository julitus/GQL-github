package com.example.graphql.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.graphql.FindUsersQuery
import com.example.graphql.R

class AdapterUser (private val context: Context, private val userQueryList: List<FindUsersQuery.Edge>) :
    BaseAdapter(){

    override fun getCount(): Int {
        return userQueryList.size
    }

    override fun getItem(position: Int): Any {
        return userQueryList[position]
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
            convertView = inflater.inflate(R.layout.user_item, null, true)

            holder.itemNameLocation = convertView!!.findViewById(R.id.userNameLocation) as TextView
            holder.itemLogin = convertView.findViewById(R.id.userLogin) as TextView
            holder.itemNumRepos = convertView.findViewById(R.id.userNumRepos) as TextView
            holder.itemPathAvatar = convertView.findViewById(R.id.userAvatar) as ImageView

            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val userNode = userQueryList[position].node()

        if (userNode is FindUsersQuery.AsUser) {
            if (userNode.name() == null) {
                holder.itemNameLocation!!.setText(R.string.no_name)
            } else {
                holder.itemNameLocation!!.text = userNode.name() + (if (userNode.location() != null ) ", " + userNode.location() else "")
            }
            holder.itemLogin!!.text = userNode.login()
            holder.itemNumRepos!!.text = "# Repos:  " + userNode.repositories().totalCount()
            holder.itemPathAvatar!!.setImageResource(R.drawable.github_avatar)
        }

        return convertView
    }

    private inner class ViewHolder {

        var itemNameLocation: TextView? = null
        var itemLogin: TextView? = null
        var itemNumRepos: TextView? = null
        var itemPathAvatar: ImageView? = null
    }
}