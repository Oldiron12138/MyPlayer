package com.example.myplayer.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.myplayer.MainActivity
import com.example.myplayer.widget.ExitDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseFragment: Fragment(),GestureDetector.OnGestureListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mGestureDetector = GestureDetector(
            activity, this
        )
        val myOnTouchListener: MainActivity.MyOnTouchListener =
            object : MainActivity.MyOnTouchListener {
                override fun onTouch(ev: MotionEvent?): Boolean {
                    return mGestureDetector.onTouchEvent(ev)
                }
            }

        (activity as MainActivity?)
            ?.registerMyOnTouchListener(myOnTouchListener)
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onDown(e: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onShowPress(e: MotionEvent?) {
        TODO("Not yet implemented")
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun onLongPress(e: MotionEvent?) {
        TODO("Not yet implemented")
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }

}