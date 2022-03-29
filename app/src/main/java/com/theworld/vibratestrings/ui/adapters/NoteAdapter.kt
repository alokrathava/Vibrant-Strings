package com.theworld.vibratestrings.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.theworld.vibratestrings.data.Note
import com.theworld.vibratestrings.databinding.LayoutNoteItemBinding

class NoteAdapter(val listener: NoteListener) :
    ListAdapter<Note, NoteAdapter.CustomerViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val binding =
            LayoutNoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }


    inner class CustomerViewHolder(private val binding: LayoutNoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition

                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    listener.onNoteClick(item)
                }
            }
        }

        fun bind(item: Note) {

            binding.apply {
                title.text = item.title
            }
        }


    }


    class DiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(old: Note, aNew: Note) =
            old.note_id == aNew.note_id

        override fun areContentsTheSame(old: Note, aNew: Note) =
            old == aNew
    }

    interface NoteListener {
        fun onNoteClick(note: Note)
    }

}