package com.theworld.vibratestrings.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.theworld.vibratestrings.data.Teacher
import com.theworld.vibratestrings.databinding.LayoutTeacherItemBinding

class TeacherAdapter :
    ListAdapter<Teacher, TeacherAdapter.CustomerViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val binding =
            LayoutTeacherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }


    inner class CustomerViewHolder(private val binding: LayoutTeacherItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: Teacher) {

            binding.apply {
                name.text = item.name
            }
        }


    }


    class DiffCallback : DiffUtil.ItemCallback<Teacher>() {
        override fun areItemsTheSame(old: Teacher, aNew: Teacher) =
            old.teacher_id == aNew.teacher_id

        override fun areContentsTheSame(old: Teacher, aNew: Teacher) =
            old == aNew
    }

}