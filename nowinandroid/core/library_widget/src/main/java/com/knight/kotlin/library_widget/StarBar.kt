package com.knight.kotlin.library_widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import kotlin.math.max

/**
 * @Description 星星进度条 / 五星评价
 * @Author knight
 * @Time 2025/8/13 21:22
 *
 */

class StarBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    enum class Orientation{
        HORIZONTAL,VERTICAL
    }

    enum class SelectMethod{
        Unable,Click,Sliding
    }

    /**
     * Will be thrown when the param is illegal.
     * @property message String?
     */
    class StarBarIllegalParamException(override val message: String?): Throwable(message)

    /**
     * Will be thrown when starbar bitmap has wrong.
     * @property message String?
     */
    class StarBarBitmapException(override val message: String?): Throwable(message)

    data class StarSize(val width: Int, val height: Int)

    //Star Bitmap
    private var starSelectedBitmap: Bitmap
    private var starUnselectedBitmap: Bitmap

    // Star interval width(in pixels).
    private var starIntervalWidth: Int

    // Star Bitmap width(in pixels).
    private var starWidth: Int

    // Star Bitmap height(in pixels).
    private var starHeight: Int

    // Max number of stars.
    private var starMaxNumber: Int

    // Star rating.
    private var starRating: Float

    // Selected Star Number.
    private var solidStarNum = 0

    // Will be use when extra selected star bitmap part.
    private var rectSrc: Rect = Rect()
    private var dstF: Rect = Rect()

    private val paint: Paint = Paint()

    /**
     * Star select method
     * @see [SelectMethod.Click] [SelectMethod.Sliding] [SelectMethod.Unable]
     */
    var starSelectMethod: SelectMethod
        private set

    /**
     * Star orientation
     * @see Orientation
     */
    private val starOrientation: Orientation

    private val density = context.resources.displayMetrics.density

    override fun onDraw(canvas: Canvas) {
        // Draw selected star.
        var solidStartPoint = 0
        when (starOrientation) {
            Orientation.HORIZONTAL -> for (i in 1..solidStarNum) {
                canvas.drawBitmap(starSelectedBitmap, solidStartPoint.toFloat(), 0f, paint)
                solidStartPoint += starIntervalWidth + starSelectedBitmap.width
            }

            Orientation.VERTICAL -> for (i in 1..solidStarNum) {
                canvas.drawBitmap(starSelectedBitmap, 0f, solidStartPoint.toFloat(), paint)
                solidStartPoint += starIntervalWidth + starSelectedBitmap.height
            }
        }
        // Unselected Star Bitmap start point.
        var hollowStartPoint = solidStartPoint
        // Unselected Star number.
        val hollowStarNum = starMaxNumber - solidStarNum
        when (starOrientation) {
            Orientation.HORIZONTAL -> for (j in 1..hollowStarNum) {
                canvas.drawBitmap(starUnselectedBitmap, hollowStartPoint.toFloat(), 0f, paint)
                hollowStartPoint += starIntervalWidth + starUnselectedBitmap.width
            }
            Orientation.VERTICAL -> for (j in 1..hollowStarNum) {
                canvas.drawBitmap(starUnselectedBitmap, 0f, hollowStartPoint.toFloat(), paint)
                hollowStartPoint += starIntervalWidth + starUnselectedBitmap.width
            }
        }
        // Extra selected star bitmap Length.
        when (starOrientation) {
            Orientation.HORIZONTAL -> {
                canvas.drawBitmap(starSelectedBitmap, rectSrc, dstF, paint)
            }
            Orientation.VERTICAL -> {
                canvas.drawBitmap(starSelectedBitmap, rectSrc, dstF, paint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (starSelectMethod) {
            SelectMethod.Unable -> {
                return super.onTouchEvent(event)
            }
            SelectMethod.Click -> {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    if (starOrientation == Orientation.HORIZONTAL) {
                        val totalWidth = (starMaxNumber * (starWidth + starIntervalWidth)).toFloat()
                        if (event.x <= totalWidth) {
                            val newStarRating =
                                (event.x.toInt() / (starHeight + starIntervalWidth) + 1).toFloat()
                            setStarRating(newStarRating)
                        }
                    } else {
                        val totalHeight =
                            (starMaxNumber * (starHeight + starIntervalWidth)).toFloat()
                        if (event.y <= totalHeight) {
                            val newStarRating =
                                (event.y.toInt() / (starHeight + starIntervalWidth) + 1).toFloat()
                            setStarRating(newStarRating)
                        }
                    }
                }
                performClick()
                return super.onTouchEvent(event)
            }
            SelectMethod.Sliding -> {
                if (starOrientation == Orientation.HORIZONTAL) {
                    val totalWidth = (starMaxNumber * (starWidth + starIntervalWidth)).toFloat()
                    if (event.x <= totalWidth) {
                        val newStarRating = event.x / (starWidth + starIntervalWidth) + 1
                        setStarRating(max(newStarRating, 1f))
                    }
                } else {
                    val totalHeight = (starMaxNumber * (starHeight + starIntervalWidth)).toFloat()
                    if (event.y <= totalHeight) {
                        val newStarRating = event.y / (starHeight + starIntervalWidth) + 1
                        setStarRating(max(newStarRating, 1f))
                    }
                }
                performClick()
                return true
            }
            else -> return super.onTouchEvent(event)
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    /**
     * Set star rating.
     * @param starRating Float
     * @throws StarBarIllegalParamException
     */
    @Throws(StarBarIllegalParamException::class)
    fun setStarRating(@FloatRange(from = 1.0) starRating: Float) = apply {
        when {
            starRating.toInt() > starMaxNumber -> {
                throw StarBarIllegalParamException("The number of selected stars must less than $starMaxNumber")
            }
            else -> {
                this.starRating = starRating
                solidStarNum = starRating.toInt()
                val extraSolidLength =
                    ((starRating - solidStarNum) * starUnselectedBitmap.width).toInt()
                var extraSolidStarPoint = 0
                when (starOrientation) {
                    Orientation.HORIZONTAL -> {
                        for (i in 1..solidStarNum) {
                            extraSolidStarPoint += starIntervalWidth + starSelectedBitmap.width
                        }
                        rectSrc = Rect(0, 0, extraSolidLength, starUnselectedBitmap.height)
                        dstF = Rect(
                            extraSolidStarPoint,
                            0,
                            extraSolidStarPoint + extraSolidLength,
                            starUnselectedBitmap.height
                        )
                    }
                    Orientation.VERTICAL -> {
                        for (i in 1..solidStarNum) {
                            extraSolidStarPoint += starIntervalWidth + starSelectedBitmap.height
                        }
                        rectSrc = Rect(0, 0, starUnselectedBitmap.width, extraSolidLength)
                        dstF = Rect(
                            0,
                            extraSolidStarPoint,
                            starUnselectedBitmap.width,
                            extraSolidStarPoint + extraSolidLength
                        )
                    }
                }
                invalidate()
            }
        }
    }

    fun getStarRating() = starRating

    /**
     * Set Star Select Method
     * @param starSelectMethod Int
     */
    fun setStarSelectMethod(starSelectMethod: SelectMethod) = apply {
        this.starSelectMethod = starSelectMethod
    }

    /**
     * Set star bitmap size(in pixels).
     * @param starWidth Int
     * @param starHeight Int
     */
    @Throws(StarBarIllegalParamException::class)
    fun setStarBitMapSize(@IntRange(from = 0) starWidth: Int,@IntRange(from = 0) starHeight: Int) = apply {
        this.starWidth = (starWidth * density).toInt()
        this.starHeight = (starHeight * density).toInt()
        starSelectedBitmap = getZoomBitmap(starSelectedBitmap)
        starUnselectedBitmap = getZoomBitmap(starUnselectedBitmap)
        invalidate()
    }

    /**
     * Get star bitmap size(in pixels).
     * @return starSize
     */
    fun getStarBitMapSize() =
        StarSize((starWidth / density).toInt(), (starHeight / density).toInt())

    /**
     * Get star bitmap width(in pixels).
     */
    fun getStarBitMapWidth() = (starWidth / density).toInt()

    /**
     * Get star bitmap height(in pixels).
     */
    fun getStarBitMapHeight() = (starWidth / density).toInt()

    /**
     * Set star bitmap interval(in pixels).
     * @param starSpaceWidth Int
     * @throws StarBarIllegalParamException
     */
    fun setStarIntervalWidth(@IntRange(from = 0) starSpaceWidth: Int) = apply {
        this.starIntervalWidth = (starSpaceWidth * density).toInt()
        invalidate()
    }

    /**
     * Get star bitmap interval(in pixels).
     */
    fun getStarIntervalWidth() = (starIntervalWidth / density).toInt()

    /**
     * Set the number of star.
     */
    fun setStarMaxNumber(@IntRange(from = 0) starMaxNumber: Int) = apply {
        this.starMaxNumber = starMaxNumber
        invalidate()
    }

    /**
     * Get the number of star.
     */
    fun getStarMaxNumber() = starMaxNumber

    /**
     * Set the bitmap of the be selected star.
     */
    fun setStarSelectedBitmap(bitmap: Bitmap) = apply {
        starSelectedBitmap = getZoomBitmap(bitmap)
    }

    /**
     * Set the bitmap of the be selected star by drawableId.
     * @param drawableId Int
     */
    @Throws(StarBarBitmapException::class)
    fun setStarSelectedBitmap(@DrawableRes drawableId: Int) = apply {
        if (BitmapFactory.decodeResource(context.resources, drawableId) == null) {
            throw StarBarBitmapException("Drawable resource conversion BitMap failed")
        } else {
            starSelectedBitmap =
                getZoomBitmap(BitmapFactory.decodeResource(context.resources, drawableId))
        }
    }

    /**
     * Get the bitmap of the be selected star.
     * @return Bitmap
     */
    fun getStarSelectedBitmap() = starSelectedBitmap

    /**
     * Set the bitmap of the be unselected star.
     * @param bitmap Bitmap
     */
    fun setStarNormalBitmap(bitmap: Bitmap) = apply {
        starUnselectedBitmap = getZoomBitmap(bitmap)
    }

    /**
     * Set the bitmap of the be unselected star by drawableId.
     * @param drawableId Int
     */
    @Throws(StarBarBitmapException::class)
    fun setStarNormalBitmap(@DrawableRes drawableId: Int) = apply {
        if (BitmapFactory.decodeResource(context.resources, drawableId) == null) {
            throw StarBarBitmapException("Drawable resource conversion BitMap failed")
        } else {
            starUnselectedBitmap =
                getZoomBitmap(BitmapFactory.decodeResource(context.resources, drawableId))
        }
    }

    /**
     * Get the bitmap of the be unselected star.
     * @return Bitmap
     */
    fun getStarUnselectedBitmap() = starUnselectedBitmap

    private fun getZoomBitmap(bitmap: Bitmap): Bitmap {
        if (starWidth == 0 || starHeight == 0) {
            return bitmap
        }
        // Calculate the zoom ratio, the desired size/the original width and height of the image
        val scaleWidth = starWidth.toFloat() / bitmap.width
        val scaleHeight = starHeight.toFloat() / bitmap.height
        // Get the matrix parameters you want to scale
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        when (starOrientation) {
            Orientation.HORIZONTAL -> setMeasuredDimension(
                measureLong(widthMeasureSpec),
                measureShort(heightMeasureSpec)
            )
            Orientation.VERTICAL -> setMeasuredDimension(
                measureShort(widthMeasureSpec),
                measureLong(heightMeasureSpec)
            )
        }
    }

    private fun measureLong(measureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = (paddingLeft + paddingRight + (starIntervalWidth + starWidth) * starMaxNumber)
            if (specMode == MeasureSpec.AT_MOST) {
                result = result.coerceAtMost(specSize)
            }
        }
        return result
    }

    private fun measureShort(measureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = (starHeight + paddingTop + paddingBottom)
            if (specMode == MeasureSpec.AT_MOST) {
                result = result.coerceAtMost(specSize)
            }
        }
        return result
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.StarBar, defStyle, 0)
        starIntervalWidth = a.getDimensionPixelSize(R.styleable.StarBar_star_interval_width, 0)
        starWidth = a.getDimensionPixelSize(R.styleable.StarBar_star_width, 0)
        starHeight = a.getDimensionPixelSize(R.styleable.StarBar_star_height, 0)
        starMaxNumber = a.getInt(R.styleable.StarBar_star_max, 0)
        starRating = a.getFloat(R.styleable.StarBar_star_rating, 0f)
        // This prevents the set resource from failing to parse and returning null, so a default value is given
        val tempSelectedBitmap = BitmapFactory.decodeResource(
            context.resources,
            a.getResourceId(R.styleable.StarBar_star_selected, 0)
        )
        starSelectedBitmap = if (tempSelectedBitmap == null) {
            getZoomBitmap(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.mipmap.widget_star_yellow_selected
                )
            )
        } else {
            getZoomBitmap(tempSelectedBitmap)
        }
        val tempUnselectedBitmap = BitmapFactory.decodeResource(
            context.resources,
            a.getResourceId(R.styleable.StarBar_star_unselected, 0)
        )
        starUnselectedBitmap = if (tempUnselectedBitmap == null) {
            getZoomBitmap(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.mipmap.widget_star_yellow_unselected
                )
            )
        } else {
            getZoomBitmap(tempUnselectedBitmap)
        }
        starOrientation = when(a.getInt(R.styleable.StarBar_star_orientation, 0)){
            0 -> Orientation.HORIZONTAL
            1 -> Orientation.VERTICAL
            else -> Orientation.HORIZONTAL
        }
        starSelectMethod = when(a.getInt(R.styleable.StarBar_star_select_method, 0)){
            0 -> SelectMethod.Unable
            1 -> SelectMethod.Click
            2 -> SelectMethod.Sliding
            else -> SelectMethod.Sliding
        }
        a.recycle()
    }
}