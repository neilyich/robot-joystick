package com.example.robotjoystick.view.terminal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.robotjoystick.databinding.MessageViewBinding

object MessageDiff : DiffUtil.ItemCallback<TerminalState.Message>() {
    override fun areItemsTheSame(
        oldItem: TerminalState.Message,
        newItem: TerminalState.Message
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: TerminalState.Message,
        newItem: TerminalState.Message
    ): Boolean {
        return oldItem == newItem
    }
}

class MessageAdapter : ListAdapter<TerminalState.Message, MessageAdapter.ViewHolder>(MessageDiff) {
    class ViewHolder(private val binding: MessageViewBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

        fun updateMessage(message: TerminalState.Message) {
            binding.tvContent.text = message.from + ": " + message.text
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MessageViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = getItem(position)
        holder.updateMessage(message)
    }
}