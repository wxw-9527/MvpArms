package com.rouxinpai.arms.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

import cn.bingoogolapple.badgeview.BGABadgeViewHelper;
import cn.bingoogolapple.badgeview.BGABadgeable;
import cn.bingoogolapple.badgeview.BGADragDismissDelegate;

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2024/2/21 14:38
 * desc   :
 */
public class BGABadgeTextView extends androidx.appcompat.widget.AppCompatTextView implements BGABadgeable {

    private final BGABadgeViewHelper mBadgeViewHelper;

    public BGABadgeTextView(Context context) {
        this(context, null);
    }

    public BGABadgeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public BGABadgeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBadgeViewHelper = new BGABadgeViewHelper(this, context, attrs, BGABadgeViewHelper.BadgeGravity.RightTop);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mBadgeViewHelper.onTouchEvent(event);
    }

    @Override
    public boolean callSuperOnTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mBadgeViewHelper.drawBadge(canvas);
    }

    @Override
    public void showCirclePointBadge() {
        mBadgeViewHelper.showCirclePointBadge();
    }

    @Override
    public void showTextBadge(String badgeText) {
        mBadgeViewHelper.showTextBadge(badgeText);
    }

    @Override
    public void hiddenBadge() {
        mBadgeViewHelper.hiddenBadge();
    }

    @Override
    public void showDrawableBadge(Bitmap bitmap) {
        mBadgeViewHelper.showDrawable(bitmap);
    }

    @Override
    public void setDragDismissDelegate(BGADragDismissDelegate delegate) {
        mBadgeViewHelper.setDragDismissDelegate(delegate);
    }

    @Override
    public boolean isShowBadge() {
        return mBadgeViewHelper.isShowBadge();
    }

    @Override
    public boolean isDraggable() {
        return mBadgeViewHelper.isDraggable();
    }

    @Override
    public boolean isDragging() {
        return mBadgeViewHelper.isDragging();
    }

    @Override
    public BGABadgeViewHelper getBadgeViewHelper() {
        return mBadgeViewHelper;
    }
}