package am.i.activitytransitiontwo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.SharedElementCallback
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var detailImageAdapter: DetailImageAdapter
    private lateinit var imageData: ImageData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        imageData = intent.extras.getParcelable("IMAGE_DATA")
        initViews()
        prepareTransitions()
        resetScrolledPosition()
    }

    private fun initViews() {
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(detailGallery)
        detailImageAdapter = DetailImageAdapter(imageData.images)
        detailGallery.adapter = detailImageAdapter
        detailGallery.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val selectedView = snapHelper.findSnapView(detailGallery.layoutManager)
                    selectedView?.let {
                        val selectedPosition = detailGallery.layoutManager?.getPosition(selectedView)
                        selectedPosition?.let { onItemSelected(selectedPosition) }
                    }
                }
            }
        })
    }

    private fun resetScrolledPosition() {
        val position = imageData.images.indexOfFirst { it.selected }
        imageData.images = imageData.images.mapIndexed { index, galleryItem ->
            when {
                index == position -> {
                    galleryItem.copy(selected = true)
                }
                galleryItem.selected -> galleryItem.copy(selected = false)
                else -> galleryItem
            }
        }
        detailImageAdapter.images = imageData.images
        detailGallery.scrollToPosition(position)
        supportStartPostponedEnterTransition()
    }


    private fun onItemSelected(position: Int) {
        imageData.images = imageData.images.mapIndexed { index, galleryItem ->
            when {
                index == position -> galleryItem.copy(selected = true)
                galleryItem.selected -> galleryItem.copy(selected = false)
                else -> galleryItem
            }
        }
    }

    override fun onBackPressed() {
        var resultIntent = Intent()
        resultIntent = resultIntent.putExtra("IMAGE_DATA", imageData)
        setResult(Activity.RESULT_OK, resultIntent)
        super.onBackPressed()

    }

    private fun prepareTransitions() {

        setEnterSharedElementCallback(
                object : SharedElementCallback() {
                    override fun onMapSharedElements(names: List<String>?, sharedElements: MutableMap<String, View>?) {
                        val selectedPosition = imageData.images.indexOfFirst { it.selected }
                        val selectedViewHolder = detailGallery.findViewHolderForAdapterPosition(selectedPosition)
                        if (selectedViewHolder?.itemView == null) {
                            return
                        }
                        sharedElements!![names!![0]] = selectedViewHolder.itemView.findViewById(R.id.detailItemImage)
                    }
                })
    }

}
