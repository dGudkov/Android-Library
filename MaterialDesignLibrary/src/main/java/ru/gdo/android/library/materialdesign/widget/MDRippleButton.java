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
import android.view.View.OnTouchListener;
import android.widget.Button;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 30.09.15.
 */

public class MDRippleButton extends Button implements OnTouchListener {

    private MDRipple mdRipple;

    public MDRippleButton(Context context) {
        this(context, null);
    }

    public MDRippleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MDRippleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MDRippleButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        return this.mdRipple.onTouch(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        this.mdRipple.onSizeChanged(w, h);
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return MDRippleButton.class.getName();
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
    @SuppressWarnings("unused")
    public void setRippleColor(int rippleColor) {
        this.mdRipple.setRippleColor(rippleColor);
        invalidate();
    }

    @SuppressWarnings("unused")
    public void setRippleOverlay(boolean rippleOverlay) {
        this.mdRipple.setRippleOverlay(rippleOverlay);
    }

    @SuppressWarnings("unused")
    public void setRippleDiameter(int rippleDiameter) {
        this.mdRipple.setRippleDiameter(rippleDiameter);
    }

    @SuppressWarnings("unused")
    public void setRippleDuration(int rippleDuration) {
        this.mdRipple.setRippleDuration(rippleDuration);
    }

    @SuppressWarnings("unused")
    public void setRippleBackground(int color) {
        this.mdRipple.setRippleBackground(color);
        invalidate();
    }

    @SuppressWarnings("unused")
    public void setRippleHover(boolean rippleHover) {
        this.mdRipple.setRippleHover(rippleHover);
    }

    @SuppressWarnings("unused")
    public void setRippleDelayClick(boolean rippleDelayClick) {
        this.mdRipple.setRippleDelayClick(rippleDelayClick);
    }

    @SuppressWarnings("unused")
    public void setRippleFadeDuration(int rippleFadeDuration) {
        this.mdRipple.setRippleFadeDuration(rippleFadeDuration);
    }

    @SuppressWarnings("unused")
    public void setRipplePersistent(boolean ripplePersistent) {
        this.mdRipple.setRipplePersistent(ripplePersistent);
    }

    @SuppressWarnings("unused")
    public void setRippleInAdapter(boolean rippleInAdapter) {
        this.mdRipple.setRippleInAdapter(rippleInAdapter);
    }

    @SuppressWarnings("unused")
    public void setRippleRoundedCorners(int rippleRoundedCorner) {
        this.mdRipple.setRippleRoundedCorners(rippleRoundedCorner);
    }

    @SuppressWarnings("unused")
    public void setDefaultRippleAlpha(int alpha) {
        this.mdRipple.setRippleAlpha(alpha);
        invalidate();
    }

    @SuppressWarnings("unused")
    public void performRipple() {
        this.mdRipple.performRipple();
    }

    @SuppressWarnings("unused")
    public void performRipple(Point anchor) {
        this.mdRipple.performRipple(anchor);
    }

}
