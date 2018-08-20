package am.i.activitytransitiontwo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ImageData(var images: List<Image>) : Parcelable