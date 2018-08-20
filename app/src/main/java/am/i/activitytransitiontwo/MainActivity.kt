package am.i.activitytransitiontwo

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.SharedElementCallback
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ListImageAdapter.ListImageClickListener {

    private lateinit var imageData: ImageData
    private lateinit var listImageAdapter: ListImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupGallery()
        prepareTransitions()
    }

    @SuppressLint("RestrictedApi")
    override fun onListImageClick(position: Int, imageView: ImageView) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("IMAGE_DATA", imageData)
        val activityOptions = ActivityOptions.makeSceneTransitionAnimation(this, imageView,
                ViewCompat.getTransitionName(imageView))

        startActivityForResult(intent, 101, activityOptions.toBundle())
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        data?.let { intent ->
            if (intent.hasExtra("IMAGE_DATA")) {
                imageData = intent.getParcelableExtra("IMAGE_DATA")
                listImageAdapter.images = imageData.images
                val position = imageData.images.indexOfFirst { it.selected }
                itemGallery.scrollToPosition(position)

            }
        }
        super.onActivityReenter(resultCode, data)
    }

    private fun setupGallery() {
        imageData = ImageData(getGalleryItems())
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(itemGallery)
        listImageAdapter = ListImageAdapter(imageData.images, this)
        itemGallery.adapter = listImageAdapter
        itemGallery.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val selectedView = snapHelper.findSnapView(itemGallery.layoutManager)
                    selectedView?.let {
                        val selectedPosition = itemGallery.layoutManager?.getPosition(selectedView)
                        selectedPosition?.let { onMediumGalleryItemHighlighted(selectedPosition) }
                    }

                }
            }
        })

    }

    private fun onMediumGalleryItemHighlighted(position: Int) {
        imageData.images = imageData.images.mapIndexed { index, galleryItem ->
            when {
                index == position -> galleryItem.copy(selected = true)
                galleryItem.selected -> galleryItem.copy(selected = false)
                else -> galleryItem
            }
        }
    }

    private fun prepareTransitions() {

        setExitSharedElementCallback(
                object : SharedElementCallback() {
                    override fun onMapSharedElements(names: List<String>?, sharedElements: MutableMap<String, View>?) {
                        val selectedPosition = imageData.images.indexOfFirst { it.selected }
                        val selectedViewHolder = itemGallery
                                .findViewHolderForAdapterPosition(selectedPosition)
                        if (selectedViewHolder?.itemView == null) {
                            return
                        }
                        sharedElements!![names!![0]] = selectedViewHolder.itemView.findViewById(R.id.listItemImage)
                    }
                })
    }

    private fun getGalleryItems(): List<Image> {
        return listOf(
                Image(R.drawable.cat, true),
                Image(R.drawable.lion, false),
                Image(R.drawable.tortoise, false)
        )
    }
}
