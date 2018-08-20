package am.i.activitytransitiontwo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class ListImageAdapter(var images: List<Image>,
                       private val itemClickListener: ListImageClickListener?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parentView: ViewGroup, itemType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parentView.context).inflate(R.layout.list_item, parentView, false)
        return ListImageViewHolder(view)

    }

    override fun getItemCount() = images.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        viewHolder as ListImageViewHolder
        viewHolder.bind(images[position])

    }

    inner class ListImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById<ImageView>(R.id.listItemImage)

        fun bind(galleryItem: Image) {
            imageView.run {
                setImageResource(galleryItem.image)
                if (itemClickListener != null) setOnClickListener { itemClickListener.onListImageClick(adapterPosition, imageView) }
            }
        }

    }

    interface ListImageClickListener {
        fun onListImageClick(position: Int, imageView: ImageView)
    }
}