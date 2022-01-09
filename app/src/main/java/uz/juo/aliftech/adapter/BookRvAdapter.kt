package uz.juo.aliftech.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.juo.aliftech.R
import uz.juo.aliftech.databinding.BookRvItemBinding
import uz.juo.domain.models.Data

class BookRvAdapter(var itemClickListener: itemOnClick, var context: Context) :
    ListAdapter<Data, BookRvAdapter.Vh>(MyDiffUtil()) {
    inner class Vh(var itemBinding: BookRvItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(data: Data, position: Int) {
            itemBinding.root.setOnClickListener {
                itemClickListener.itemClickListener(data, position)
            }
            var anim = AnimationUtils.loadAnimation(context, R.anim.rv)
            itemBinding.itemEndTv.text = data.endDate
            itemBinding.itemNameTv.text = data.name
            itemBinding.itemUrlTv.text = data.url
            Picasso.get().load(data.icon).placeholder(R.drawable.holder).into(itemBinding.itemImage)
            itemBinding.root.startAnimation(anim)
        }
    }

    class MyDiffUtil : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(
            (BookRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
        )
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position), position)
    }

    interface itemOnClick {
        fun itemClickListener(data: Data, position: Int)
    }
}