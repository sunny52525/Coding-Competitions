package com.shaun.codingcompetitions

class RecyclerViewClickListener {

}
//
//class RecyclerItemClickListener(
//    context: Context,
//    recylerview: RecyclerView, private val listener: MainActivity,id: Int
//) : RecyclerView.SimpleOnItemTouchListener() {
//
//    private val TAG = "RecyclerItemClickList"
//
//    interface OnRecyclerClickListener {
//        fun onItemClick(view: View, postion: Int,id:Int)
//        fun onItemLongClick(view: View, postion: Int)
//
//    }
//
//    private val gestureDetector =
//        GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
//            override fun onSingleTapUp(e: MotionEvent): Boolean {
//                Log.d(TAG, "OnSingleTapUp:Starts")
//                val childview = recylerview.findChildViewUnder(e.x, e.y)
//                if (childview != null) {
//                    listener.onItemClick(childview, recylerview.getChildAdapterPosition(childview),id)
//                }
//                return true
//            }
//
//            override fun onLongPress(e: MotionEvent) {
//                val childview = recylerview.findChildViewUnder(e.x, e.y)
//                if (childview != null) {
//                    listener.onItemLongClick(
//                        childview,
//                        recylerview.getChildAdapterPosition(childview)
//                    )
//                }
//            }
//        })
//
//    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
//
//        Log.d(TAG, "OnInterceptTouchEventCalled $e")
//        val result = gestureDetector.onTouchEvent(e)
//        return result
//    }
//
//
//}