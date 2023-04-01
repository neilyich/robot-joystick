package com.example.robotjoystick.view.bluetoothdevice

import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

object StringDiff : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(
        oldItem: String,
        newItem: String
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: String,
        newItem: String
    ): Boolean {
        return oldItem == newItem
    }
}

class RecyclerViewTextAdapter(
    private val clickCallback: (String) -> Unit = {},
    private val sizeChangedCallback: (Int) -> Unit = {}
) : ListAdapter<String, RecyclerViewTextAdapter.ViewHolder>(StringDiff) {

    class ViewHolder(val textView: TextView, clickCallback: (String) -> Unit) : RecyclerView.ViewHolder(textView) {
        lateinit var item: String

        init {
            textView.setOnClickListener { clickCallback(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = TextView(parent.context)
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
        return ViewHolder(view, clickCallback)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.item = item
        holder.textView.text = item
    }

    override fun onCurrentListChanged(
        previousList: MutableList<String>,
        currentList: MutableList<String>
    ) {
        if (previousList.size != currentList.size) {
            sizeChangedCallback(currentList.size)
        }
    }
}