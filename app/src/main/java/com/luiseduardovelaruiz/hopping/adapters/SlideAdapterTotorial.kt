package com.luiseduardovelaruiz.hopping.adapters

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.luiseduardovelaruiz.hopping.R

class SlideAdapterTotorial() : PagerAdapter(), Parcelable {

    private lateinit var conext: Context;
    private var layoutInflater: LayoutInflater? = null

    constructor(context: Context) : this() {
        this.conext = context;
        this.layoutInflater = null
    }

    var slide_image: IntArray = intArrayOf(
            R.drawable.tuto_img_uno,
            R.drawable.tuto_img_dos,
            R.drawable.tuto_img_tres,
            R.drawable.tuto_img_cuatro,
            R.drawable.tuto_img_cinco,
            R.drawable.tuto_img_seis,
            R.drawable.tuto_img_siete
    )

    constructor(parcel: Parcel) : this() {
        slide_image = parcel.createIntArray()
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        return slide_image.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        this.layoutInflater = this.conext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = this.layoutInflater!!.inflate(R.layout.tutorial_slide_layout, container, false)

        val background:ImageView = view.findViewById(R.id.image_tutorial)
        background.setImageResource(slide_image.get(position))

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeIntArray(slide_image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SlideAdapterTotorial> {
        override fun createFromParcel(parcel: Parcel): SlideAdapterTotorial {
            return SlideAdapterTotorial(parcel)
        }

        override fun newArray(size: Int): Array<SlideAdapterTotorial?> {
            return arrayOfNulls(size)
        }
    }
}
