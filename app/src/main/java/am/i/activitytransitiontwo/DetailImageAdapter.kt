package am.i.activitytransitiontwo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class DetailImageAdapter(var images: List<Image>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parentView: ViewGroup, itemType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parentView.context).inflate(R.layout.detail_item, parentView, false)
        return DetailImageViewHolder(view)

    }

    override fun getItemCount() = images.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        viewHolder as DetailImageViewHolder
        viewHolder.bind(images[position])

    }

    inner class DetailImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.detailItemImage)
        fun bind(galleryItem: Image) {
            image.setImageResource(galleryItem.image)
        }

    }

}