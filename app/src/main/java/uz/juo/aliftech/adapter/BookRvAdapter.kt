package uz.juo.aliftech.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.juo.aliftech.R
import uz.juo.aliftech.databinding.BookRvItemBinding
import uz.juo.domain.models.Data

class BookRvAdapter(
    var list: ArrayList<Data>,
    var itemClickListener: itemOnClick
) :
    RecyclerView.Adapter<BookRvAdapter.BookHolder>() {
    inner class BookHolder(var itemBinding: BookRvItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(data: Data, position: Int) {
            itemBinding.root.setOnClickListener {
                itemClickListener.itemClickListener(data, position)
            }
            itemBinding.itemEndTv.text = data.endDate
            itemBinding.itemNameTv.text = data.name
            itemBinding.itemUrlTv.text = data.url
            Picasso.get().load(data.icon).placeholder(R.drawable.holder).into(itemBinding.itemImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
        return BookHolder(
            (BookRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
        )
    }

    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    interface itemOnClick {
        fun itemClickListener(data: Data, position: Int)
    }
}