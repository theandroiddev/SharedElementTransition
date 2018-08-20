package am.i.activitytransitiontwo

import android.os.Parcelable
import android.support.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(@DrawableRes val image: Int, val selected: Boolean = false) : Parcelable
