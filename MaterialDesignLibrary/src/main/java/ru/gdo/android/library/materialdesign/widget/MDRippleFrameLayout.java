package ru.gdo.android.library.materialdesign.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 05.10.15.
 */

public class MDRippleFrameLayout extends FrameLayout implements View.OnTouchListener {

    private MDRipple mdRipple;

    public MDRippleFrameLayout(Context context) {
        this(context, null);
    }

    public MDRippleFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MDRippleFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MDRippleFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    protected void initView(Context context, AttributeSet attrs) {

        this.mdRipple = new MDRipple(this, context, attrs);
        setWillNotDraw(false);
        this.setOnTouchListener(this);
        this.mdRipple.setLayerType();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return this.mdRipple.onTouch(v, event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mdRipple.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return MDRippleFrameLayout.class.getName();
    }

    /*
     * Drawing
     */
    @Override
    public void draw(@NonNull Canvas canvas) {
        final boolean positionChanged = this.mdRipple.adapterPositionChanged();
        if (this.mdRipple.getRippleOverlay()) {
            if (!positionChanged) {
                this.mdRipple.getRippleBackground().draw(canvas);
            }
            super.draw(canvas);
            if (!positionChanged) {
                if (this.mdRipple.getRippleRoundedCorners() != 0) {
                    Path clipPath = new Path();
                    RectF rect = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
                    clipPath.addRoundRect(
                            rect,
                            this.mdRipple.getRippleRoundedCorners(),
                            this.mdRipple.getRippleRoundedCorners(),
                            Path.Direction.CW);
                    canvas.clipPath(clipPath);
                }
                canvas.drawCircle(
                        this.mdRipple.getCurrentCoords().x,
                        this.mdRipple.getCurrentCoords().y,
                        this.mdRipple.getRadius(),
                        this.mdRipple.getPaint());
            }
        } else {
            if (!positionChanged) {
                this.mdRipple.getRippleBackground().draw(canvas);
                canvas.drawCircle(
                        this.mdRipple.getCurrentCoords().x,
                        this.mdRipple.getCurrentCoords().y,
                        this.mdRipple.getRadius(),
                        this.mdRipple.getPaint());
            }
            super.draw(canvas);
        }
    }

    /*
    * Accessor
    */

    public void setRippleColor(int rippleColor) {
        this.mdRipple.setRippleColor(rippleColor);
        invalidate();
    }

    public void setRippleOverlay(boolean rippleOverlay) {
        this.mdRipple.setRippleOverlay(rippleOverlay);
    }

    public void setRippleDiameter(int rippleDiameter) {
        this.mdRipple.setRippleDiameter(rippleDiameter);
    }

    public void setRippleDuration(int rippleDuration) {
        this.mdRipple.setRippleDuration(rippleDuration);
    }

    public void setRippleBackground(int color) {
        this.mdRipple.setRippleBackground(color);
        invalidate();
    }

    public void setRippleHover(boolean rippleHover) {
        this.mdRipple.setRippleHover(rippleHover);
    }

    public void setRippleDelayClick(boolean rippleDelayClick) {
        this.mdRipple.setRippleDelayClick(rippleDelayClick);
    }

    public void setRippleFadeDuration(int rippleFadeDuration) {
        this.mdRipple.setRippleFadeDuration(rippleFadeDuration);
    }

    public void setRipplePersistent(boolean ripplePersistent) {
        this.mdRipple.setRipplePersistent(ripplePersistent);
    }

    public void setRippleInAdapter(boolean rippleInAdapter) {
        this.mdRipple.setRippleInAdapter(rippleInAdapter);
    }

    public void setRippleRoundedCorners(int rippleRoundedCorner) {
        this.mdRipple.setRippleRoundedCorners(rippleRoundedCorner);
    }

    public void setDefaultRippleAlpha(int alpha) {
        this.mdRipple.setRippleAlpha(alpha);
        invalidate();
    }

    public void performRipple() {
        this.mdRipple.performRipple();
    }

    public void performRipple(Point anchor) {
        this.mdRipple.performRipple(anchor);
    }

}
