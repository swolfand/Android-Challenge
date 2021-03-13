package com.swolfand.ticktock.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swolfand.ticktock.R
import com.swolfand.ticktock.databinding.ItemActivityBinding

class ActivityAdapter(items: List<TimerUiModel>) : RecyclerView.Adapter<ActivityViewHolder>() {
    var currentItems: List<TimerUiModel> = emptyList()

    init {
        currentItems = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_activity, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val item = currentItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = currentItems.size

    fun clear() {
        currentItems = emptyList()
        notifyDataSetChanged()
    }
}

class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemActivityBinding.bind(itemView)

    fun bind(item: TimerUiModel) {
        binding.activityPlayer.text = item.instructorName
        binding.activityTitle.text = item.activityName
    }
}