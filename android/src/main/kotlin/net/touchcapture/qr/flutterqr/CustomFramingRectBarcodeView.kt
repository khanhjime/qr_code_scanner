package net.touchcapture.qr.flutterqr

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.widget.Toast
import com.journeyapps.barcodescanner.BarcodeView
import com.journeyapps.barcodescanner.Size
import com.journeyapps.barcodescanner.SourceData
import com.journeyapps.barcodescanner.camera.PreviewCallback
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream
import io.flutter.plugin.common.MethodChannel


class CustomFramingRectBarcodeView : BarcodeView {
    private var bottomOffset = BOTTOM_OFFSET_NOT_SET_VALUE

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun calculateFramingRect(container: Rect, surface: Rect): Rect {
        val containerArea = Rect(container)
        val intersects =
            containerArea.intersect(surface) //adjusts the containerArea (code from super.calculateFramingRect)
        val scanAreaRect = super.calculateFramingRect(container, surface)
        if (bottomOffset != BOTTOM_OFFSET_NOT_SET_VALUE) { //if the setFramingRect function was called, then we shift the scan area by Y
            val scanAreaRectWithOffset = Rect(scanAreaRect)
            scanAreaRectWithOffset.bottom -= bottomOffset
            scanAreaRectWithOffset.top -= bottomOffset
            val belongsToContainer = scanAreaRectWithOffset.intersect(containerArea)
            if (belongsToContainer) {
                return scanAreaRectWithOffset
            }
        }
        return scanAreaRect
    }

    fun setFramingRect(rectWidth: Int, rectHeight: Int, bottomOffset: Int) {
        this.bottomOffset = bottomOffset
        framingRectSize = Size(rectWidth, rectHeight)
    }

    fun takePicture(result: MethodChannel.Result)  {
        cameraInstance.requestPreview(object : PreviewCallback {
            override fun onPreview(sourceData: SourceData?) {
                Log.d("Test", "source data")
                val bmp = sourceData!!.bitmap
                try {
                    val dir =
                        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path)
                    dir.mkdirs()
                    val filepath = dir.path + String.format("/%d.png", System.currentTimeMillis())
                    val stream: OutputStream = FileOutputStream(filepath)
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)

                    result.success(filepath)
                    //Make a Toast with cream cheese
                    val bagel =
                        Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT)
                    bagel.show()
                } catch (e: FileNotFoundException) {
                    Log.d("Test", e.message!!)
                }
            }

            override fun onPreviewError(e: Exception?) {
                Log.d("Test", e.toString())
            }

        })
    }

    companion object {
        private const val BOTTOM_OFFSET_NOT_SET_VALUE = -1
    }
}