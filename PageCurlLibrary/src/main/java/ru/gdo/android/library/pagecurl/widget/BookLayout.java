package ru.gdo.android.library.pagecurl.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.Date;

import ru.gdo.android.library.pagecurl.interfaces.IAdapter;
import ru.gdo.android.library.pagecurl.BookState;
import ru.gdo.android.library.pagecurl.Corner;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 03.09.15.
 */

public class BookLayout extends FrameLayout {

    public static final String LOG_TAG = "ru.gdo.android.library";
    private int totalPageNum;
    private Context mContext;
    private boolean hasInit = false;
    private final int defaultWidth = 600, defaultHeight = 400;
    private int contentWidth = 0;
    private int contentHeight = 0;
    private View currentPage, middlePage, nextPage, prevPage;
    private LinearLayout invisibleLayout;
    private LinearLayout mainLayout;
    private BookView mBookView;
    private Handler aniEndHandle;
    private static boolean closeBook = false;


    private Corner mSelectCorner;
    private final int clickCornerLen = 250 * 250; // 50dip
    private float scrollX = 0, scrollY = 0;
//    private int indexPage = 0;


    private BookState mState;
    private Point aniStartPos;
    private Point aniStopPos;
    private Date aniStartTime;
    private long aniTime = 800;
    private long timeOffset = 10;

    //	private Listener mListener;
    private IAdapter mPageAdapter;

    private GestureDetector mGestureDetector;
    private BookOnGestureListener mGestureListener;

    public BookLayout(Context context) {
        super(context);
        Init(context);
    }

    public BookLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init(context);
    }

    public BookLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Init(context);
    }

    protected void onFinishInflate() {
        Log.d(LOG_TAG, "onFinishInflate");
        super.onFinishInflate();
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        contentWidth = this.getWidth();
        contentHeight = this.getHeight();
        if (contentWidth == 0)
            contentWidth = defaultWidth;
        if (contentHeight == 0)
            contentHeight = defaultHeight;
        Log.d(LOG_TAG, "onLayout, width:" + contentWidth + " height:" + contentHeight);
    }


    class BookView extends SurfaceView implements SurfaceHolder.Callback {
        DrawThread dt;
        final SurfaceHolder surfaceHolder;
        Paint mDarkPaint = new Paint();
        Paint mPaint = new Paint();
        Bitmap tmpBmp = Bitmap.createBitmap(contentWidth, contentHeight, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(tmpBmp);

        Paint bmpPaint = new Paint();
        Paint ivisiblePaint = new Paint();

        public BookView(Context context) {
            super(context);
            surfaceHolder = getHolder();
            //Add a Callback interface for this holder. There can several Callback interfaces associated to a holder.
            surfaceHolder.addCallback(this);

            mDarkPaint.setColor(0x88000000);
            /*
            * * Android is available in Shader class designed to render images as well as some geometry, Shader directly below includes several sub-categories, namely BitmapShader, ComposeShader,
            * LinearGradient, RadialGradient, SweepGradient. BitmapShader mainly used to render an image, LinearGradient gradient used for rendering, RadialGradient
            * Used for rendering ring, SweepGradient gradient used for rendering, ComposeShader is a hybrid rendering, and several other sub-categories may be used in combination.
            ʱ
            * Create LinearGradient and set the gradient array of colors explain these parameters
            * First start of the x-coordinate
            * Y coordinate of the start of the second
            * X coordinate of the end of the third
            * Y coordinates of the end of the fourth
            * Fifth array of colors
            * This is an array of the sixth to the relative position of the specified color on the array is null if uniformly distributed along the slope line
            * Seventh rendering mode, tile mode, here is set to Mirror
            */
            Shader mLinearGradient = new LinearGradient(0, 0, contentWidth, 0, new int[]{0x00000000, 0x33000000,
                    0x00000000}, new float[]{0.35f, 0.5f, 0.65f}, Shader.TileMode.MIRROR);
            // Set whether to use anti-aliasing
            mPaint.setAntiAlias(true);

            // Set the rendering objects
            mPaint.setShader(mLinearGradient);

            // If this is set to true, the image in the animation will be filtered out of the Bitmap
            // image optimization operations, speed up the display
            // Speed, this setting items depend on the dither and xfermode settings
            bmpPaint.setFilterBitmap(true);
            // Set whether to use anti-aliasing, it will consume more resources, graphing will be slower.
            bmpPaint.setAntiAlias(true);

            // Set graphing transparency.
            ivisiblePaint.setAlpha(0);
            ivisiblePaint.setFilterBitmap(true);
            ivisiblePaint.setAntiAlias(true);


            // Set the handling of graphics overlap, such as mergers, take the intersection or union, often used to make rubber erasing effect
            // PorterDuffXfermode any one is a very powerful conversion mode, it can use an image synthetic 16 Porter-Duff rules to control how the existing Canvas Paint image interaction.
            // Used here Mode.DST_IN rule: take two draws intersection. Display lower.
            /*
              1.PorterDuff.Mode.CLEAR not submit to the canvas.
              2.PorterDuff.Mode.SRC show the upper draw a picture
              3.PorterDuff.Mode.DST show lower draw a picture
              4.PorterDuff.Mode.SRC_OVER normal drawing display, the upper and lower draw overlap.
              5.PorterDuff.Mode.DST_OVER upper and lower layers are displayed. The lower ranking on the display.
              6.PorterDuff.Mode.SRC_IN take two draws intersection. Upper display.
              7.PorterDuff.Mode.DST_IN take two draws intersection. Display lower.
              8.PorterDuff.Mode.SRC_OUT drawn from the upper portion of the non-intersection.
              9.PorterDuff.Mode.DST_OUT take the lower part of drawing non-intersection.
              10.PorterDuff.Mode.SRC_ATOP take the lower portion of the non-intersection with the upper portion of the intersection
              11.PorterDuff.Mode.DST_ATOP take the upper part and the lower intersection of the non-intersection portion
              12.PorterDuff.Mode.XOR take non-intersection of the upper and lower portions
              13.PorterDuff.Mode.DARKEN [Sa + Da - Sa * Da, Sc * (1 - Da) + Dc * (1 - Sa) + min (Sc, Dc)]
              14.PorterDuff.Mode.LIGHTEN [Sa + Da - Sa * Da, Sc * (1 - Da) + Dc * (1 - Sa) + max (Sc, Dc)]
              15.PorterDuff.Mode.MULTIPLY [Sa * Da, Sc * Dc], take the intersection portion, corresponding to the intersection point portion of the pixel is multiplied by two pictures, and then divided by 255, and then to re-draw the new pixel display After the image synthesis
              16.PorterDuff.Mode.SCREEN [Sa + Da - Sa * Da, Sc + Dc - Sc * Dc]
            */
            ivisiblePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        }

        /*
         * Start page
         */
        public void startAnimation() {
            if (dt == null) {
                Log.d(LOG_TAG, "startAnimation");
                dt = new DrawThread(this, getHolder());
                dt.start();
            }
        }

        public void stopAnimation() {
            Log.d(LOG_TAG, "stopAnimation");
            if (dt != null) {
                dt.flag = false;
                Thread t = dt;
                dt = null;
                t.interrupt();
            }
        }

        public void drawLT(Canvas canvas) {
            double dx = contentWidth - scrollX, dy = scrollY;
            double len = Math.sqrt(dx * dx + dy * dy);
            if (len > contentWidth) {
                scrollX = (float) (contentWidth - contentWidth * dx / len);
                scrollY = (float) (contentWidth * dy / len);
            }

            double px = scrollX;
            double py = scrollY;
            double arc = 2 * Math.atan(py / px) * 180 / Math.PI;

            Matrix m = new Matrix();
            m.postTranslate(scrollX - contentWidth, scrollY);
            m.postRotate((float) (arc), scrollX, scrollY);

            middlePage.draw(mCanvas);

            Paint ps = new Paint();
			/*
			 *Shader.TileMode.CLAMP 	replicate the edge color if the shader draws outside of its original bounds
			 *Shader.TileMode.MIRROR 	repeat the shader's image horizontally and vertically, alternating mirror images
			 *                          so that adjacent images always seam
			 *Shader.TileMode.REPEAT 	repeat the shader's image horizontally and vertically
			*/
            Shader lg1 = new LinearGradient(contentWidth, 0, contentWidth - (float) px, (float) py, new int[]{
                    0x00000000, 0x33000000, 0x00000000}, new float[]{0.35f, 0.5f, 0.65f}, Shader.TileMode.CLAMP);
            ps.setShader(lg1);
            mCanvas.drawRect(0, 0, contentWidth, contentHeight, ps);
            canvas.drawBitmap(tmpBmp, m, bmpPaint);

            prevPage.draw(mCanvas);
            Shader lg2 = new LinearGradient(scrollX, scrollY, 0, 0, new int[]{0x00000000, 0x33000000, 0x00000000},
                    new float[]{0.35f, 0.5f, 0.65f}, Shader.TileMode.CLAMP);
            ps.setShader(lg2);
            mCanvas.drawRect(0, 0, contentWidth, contentHeight, ps);

            arc = arc * Math.PI / 360;
            Path path = new Path();
            double r = Math.sqrt(px * px + py * py);
            double p1 = r / (2 * Math.cos(arc));
            double p2 = r / (2 * Math.sin(arc));
            Log.d(LOG_TAG, "p1: " + p1 + " p2:" + p2);
            if (arc == 0) {
                path.moveTo((float) p1, 0);
                path.lineTo(contentWidth, 0);
                path.lineTo(contentWidth, contentHeight);
                path.lineTo((float) p1, contentHeight);
                path.close();
            } else if (p2 > contentHeight || p2 < 0) {
                double p3 = (p2 - contentHeight) * Math.tan(arc);
                path.moveTo((float) p1, 0);
                path.lineTo(contentWidth, 0);
                path.lineTo(contentWidth, contentHeight);
                path.lineTo((float) p3, contentHeight);
                path.close();
            } else {
                path.moveTo((float) p1, 0);
                path.lineTo(contentWidth, 0);
                path.lineTo(contentWidth, contentHeight);
                path.lineTo(0, contentHeight);
                path.lineTo(0, (float) p2);
                path.close();
            }
            mCanvas.drawPath(path, ivisiblePaint);
            canvas.drawBitmap(tmpBmp, 0, 0, null);
        }

        public void drawLB(Canvas canvas) {
            double dx = contentWidth - scrollX, dy = contentHeight - scrollY;
            double len = Math.sqrt(dx * dx + dy * dy);
            if (len > contentWidth) {
                scrollX = (float) (contentWidth - contentWidth * dx / len);
                scrollY = (float) (contentHeight - contentWidth * dy / len);
            }
            double px = scrollX;
            double py = contentHeight - scrollY;
            double arc = 2 * Math.atan(py / px) * 180 / Math.PI;

            Matrix m = new Matrix();
            m.postTranslate(scrollX - contentWidth, scrollY - contentHeight);
            m.postRotate((float) (-arc), scrollX, scrollY);

            middlePage.draw(mCanvas);

            Paint ps = new Paint();
            Shader lg1 = new LinearGradient(contentWidth, contentHeight, contentWidth - (float) px,
                    contentHeight - (float) py, new int[]{0x00000000, 0x33000000, 0x00000000}, new float[]{0.35f,
                    0.5f, 0.65f}, Shader.TileMode.CLAMP);
            ps.setShader(lg1);
            mCanvas.drawRect(0, 0, contentWidth, contentHeight, ps);
            canvas.drawBitmap(tmpBmp, m, bmpPaint);

            prevPage.draw(mCanvas);
            Shader lg2 = new LinearGradient(scrollX, scrollY, 0, contentHeight, new int[]{0x00000000, 0x33000000,
                    0x00000000}, new float[]{0.35f, 0.5f, 0.65f}, Shader.TileMode.CLAMP);
            ps.setShader(lg2);
            mCanvas.drawRect(0, 0, contentWidth, contentHeight, ps);

            arc = arc * Math.PI / 360;
            Path path = new Path();
            double r = Math.sqrt(px * px + py * py);
            double p1 = r / (2 * Math.cos(arc));
            double p2 = r / (2 * Math.sin(arc));
            Log.d(LOG_TAG, "p1: " + p1 + " p2:" + p2);
            if (arc == 0) {
                path.moveTo((float) p1, 0);
                path.lineTo(contentWidth, 0);
                path.lineTo(contentWidth, contentHeight);
                path.lineTo((float) p1, contentHeight);
                path.close();
            } else if (p2 > contentHeight || p2 < 0) {
                double p3 = (p2 - contentHeight) * Math.tan(arc);
                path.moveTo((float) p3, 0);
                path.lineTo(contentWidth, 0);
                path.lineTo(contentWidth, contentHeight);
                path.lineTo((float) p1, contentHeight);
                path.close();
            } else {
                path.moveTo(0, 0);
                path.lineTo(contentWidth, 0);
                path.lineTo(contentWidth, contentHeight);
                path.lineTo((float) p1, contentHeight);
                path.lineTo(0, contentHeight - (float) p2);
                path.close();
            }
            mCanvas.drawPath(path, ivisiblePaint);
            canvas.drawBitmap(tmpBmp, 0, 0, null);
        }

        public void drawRT(Canvas canvas) {
            double dx = scrollX, dy = scrollY;
            double len = Math.sqrt(dx * dx + dy * dy);
            if (len > contentWidth) {
                scrollX = (float) (contentWidth * dx / len);
                scrollY = (float) (contentWidth * dy / len);
            }

            double px = contentWidth - scrollX;
            double py = scrollY;
            double arc = 2 * Math.atan(py / px) * 180 / Math.PI;

            Matrix m = new Matrix();
            m.postTranslate(scrollX, scrollY);
            m.postRotate((float) (-arc), scrollX, scrollY);

            middlePage.draw(mCanvas);

            Paint ps = new Paint();
            Shader lg1 = new LinearGradient(0, 0, (float) px, (float) py, new int[]{0x00000000, 0x33000000,
                    0x00000000}, new float[]{0.35f, 0.5f, 0.65f}, Shader.TileMode.CLAMP);
            ps.setShader(lg1);
            mCanvas.drawRect(0, 0, contentWidth, contentHeight, ps);
            canvas.drawBitmap(tmpBmp, m, bmpPaint);

            nextPage.draw(mCanvas);
            Shader lg2 = new LinearGradient(scrollX - contentWidth, scrollY, contentWidth, 0, new int[]{
                    0x00000000, 0x33000000, 0x00000000}, new float[]{0.35f, 0.5f, 0.65f}, Shader.TileMode.CLAMP);
            ps.setShader(lg2);
            mCanvas.drawRect(0, 0, contentWidth, contentHeight, ps);

            arc = arc * Math.PI / 360;
            Path path = new Path();
            double r = Math.sqrt(px * px + py * py);
            double p1 = contentWidth - r / (2 * Math.cos(arc));
            double p2 = r / (2 * Math.sin(arc));
            Log.d(LOG_TAG, "p1: " + p1 + " p2:" + p2);
            if (arc == 0) {
                path.moveTo(0, 0);
                path.lineTo((float) p1, 0);
                path.lineTo((float) p1, contentHeight);
                path.lineTo(0, contentHeight);
                path.close();
            } else if (p2 > contentHeight || p2 < 0) {
                double p3 = contentWidth - (p2 - contentHeight) * Math.tan(arc);
                path.moveTo(0, 0);
                path.lineTo((float) p1, 0);
                path.lineTo((float) p3, contentHeight);
                path.lineTo(0, contentHeight);
                path.close();
            } else {
                path.moveTo(0, 0);
                path.lineTo((float) p1, 0);
                path.lineTo(contentWidth, (float) p2);
                path.lineTo(contentWidth, contentHeight);
                path.lineTo(0, contentHeight);
                path.close();
            }
            mCanvas.drawPath(path, ivisiblePaint);
            canvas.drawBitmap(tmpBmp, 0, 0, null);
        }

        public void drawRB(Canvas canvas) {
            double dx = scrollX, dy = contentHeight - scrollY;
            double len = Math.sqrt(dx * dx + dy * dy);
            if (len > contentWidth) {
                scrollX = (float) (contentWidth * dx / len);
                scrollY = (float) (contentHeight - contentWidth * dy / len);
            }

            double px = contentWidth - scrollX;
            double py = contentHeight - scrollY;
            double arc = 2 * Math.atan(py / px) * 180 / Math.PI;

            Matrix m = new Matrix();
            m.postTranslate(scrollX, scrollY - contentHeight);
            m.postRotate((float) (arc), scrollX, scrollY);

            middlePage.draw(mCanvas);

            Paint ps = new Paint();
            Shader lg1 = new LinearGradient(0, contentHeight, (float) px, contentHeight - (float) py, new int[]{
                    0x00000000, 0x33000000, 0x00000000}, new float[]{0.35f, 0.5f, 0.65f}, Shader.TileMode.CLAMP);
            ps.setShader(lg1);
            mCanvas.drawRect(0, 0, contentWidth, contentHeight, ps);
            canvas.drawBitmap(tmpBmp, m, bmpPaint);

            nextPage.draw(mCanvas);
            Shader lg2 = new LinearGradient(scrollX - contentWidth, scrollY, contentWidth, contentHeight,
                    new int[]{0x00000000, 0x33000000, 0x00000000}, new float[]{0.35f, 0.5f, 0.65f},
                    Shader.TileMode.CLAMP);
            ps.setShader(lg2);
            mCanvas.drawRect(0, 0, contentWidth, contentHeight, ps);

            arc = arc * Math.PI / 360;
            Path path = new Path();
            double r = Math.sqrt(px * px + py * py);
            double p1 = contentWidth - r / (2 * Math.cos(arc));
            double p2 = r / (2 * Math.sin(arc));
            Log.d(LOG_TAG, "p1: " + p1 + " p2:" + p2);
            if (arc == 0) {
                path.moveTo(0, 0);
                path.lineTo((float) p1, 0);
                path.lineTo((float) p1, contentHeight);
                path.lineTo(0, contentHeight);
                path.close();
            } else if (p2 > contentHeight || p2 < 0) {
                double p3 = contentWidth - (p2 - contentHeight) * Math.tan(arc);
                path.moveTo(0, 0);
                path.lineTo((float) p3, 0);
                path.lineTo((float) p1, contentHeight);
                path.lineTo(0, contentHeight);
                path.close();
            } else {
                path.moveTo(0, 0);
                path.lineTo(contentWidth, 0);
                path.lineTo(contentWidth, contentHeight - (float) p2);
                path.lineTo((float) p1, contentHeight);
                path.lineTo(0, contentHeight);
                path.close();
            }
            mCanvas.drawPath(path, ivisiblePaint);
            canvas.drawBitmap(tmpBmp, 0, 0, null);
        }

        public void drawPrevPageEnd(Canvas canvas) {
            prevPage.draw(mCanvas);
            canvas.drawBitmap(tmpBmp, 0, 0, null);
        }

        public void drawNextPageEnd(Canvas canvas) {
            nextPage.draw(mCanvas);
            canvas.drawBitmap(tmpBmp, contentWidth, 0, null);
        }

        public void drawPage(Canvas canvas) {
            if (mSelectCorner == Corner.LeftTop) {
                Log.d(LOG_TAG, "click left top");
                drawLT(canvas);
            } else if (mSelectCorner == Corner.LeftBottom) {
                Log.d(LOG_TAG, "click left bottom");
                drawLB(canvas);
            } else if (mSelectCorner == Corner.RightTop) {
                Log.d(LOG_TAG, "click right top");
                drawRT(canvas);
            } else if (mSelectCorner == Corner.RightBottom) {
                Log.d(LOG_TAG, "click right bottom");
                drawRB(canvas);
            }
        }

        public void update() {
            Canvas canvas = surfaceHolder.lockCanvas(null);
            try {
                synchronized (surfaceHolder) {
                    doDraw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

        protected void doDraw(Canvas canvas) {
            Log.d(LOG_TAG, "bookView doDraw");
            mainLayout.draw(canvas);

        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        public void surfaceCreated(SurfaceHolder holder) {
            update();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            if (dt != null) {
                dt.flag = false;
                dt = null;
            }
        }
    }

    public boolean getAnimateData() {
        Log.d(LOG_TAG, "getAnimateData");
        long time = aniTime;
        Date date = new Date();
        //Returns this Date as a millisecond value
        long t = date.getTime() - aniStartTime.getTime();
        Log.d(LOG_TAG, t + "");
        t += timeOffset;
        Log.d(LOG_TAG, t + "");
        if (t < 0 || t > time) {
            mState = BookState.ANIMATE_END;
            return false;
        } else {
            mState = BookState.ANIMATING;
            double sx = aniStopPos.x - aniStartPos.x;
            //计算起点至终点在间隔时间内X轴需移动的距离
            scrollX = (float) (sx * t / time + aniStartPos.x);
            double sy = aniStopPos.y - aniStartPos.y;
            //计算起点至终点在间隔时间内Y轴需移动的距离
            scrollY = (float) (sy * t / time + aniStartPos.y);
            return true;
        }
    }

    public void handleAniEnd(Canvas canvas) {
        Log.d(LOG_TAG, "handleAniEnd");
        if (closeBook) {
            closeBook = false;
            // Upper left or lower left corner
            if (mSelectCorner == Corner.LeftTop || mSelectCorner == Corner.LeftBottom) {
                // Setting whether to trigger page
                if (scrollX > contentWidth / 2) {
//                    indexPage -= 1;
                    // Previous painting
                    mBookView.drawPrevPageEnd(canvas);
                    // Causes the Runnable r to be added to the message queue. The runnable will be run on the thread to which this handler is attached.
                    // But pay attention to this Runnable run in the main thread, that would block the main thread
                    aniEndHandle.post(new Runnable() {
                        public void run() {
                            //Update the current picture
                            updatePageView(-1);
                            Log.d("Test","test");
                        }
                    });
                } else {
                    mBookView.doDraw(canvas);
                }
            } else if (mSelectCorner == Corner.RightTop || mSelectCorner == Corner.RightBottom) {
                if (scrollX < contentWidth / 2) {
//                    indexPage += 1;
                    mBookView.drawNextPageEnd(canvas);
                    aniEndHandle.post(new Runnable() {
                        public void run() {
                            updatePageView(1);
                            Log.d("Test", "test");
                        }
                    });
                } else {
                    mBookView.doDraw(canvas);
                }
            }
            mSelectCorner = Corner.None;
            mState = BookState.READY;
        } else {
            mState = BookState.TRACKING;
        }
        mBookView.stopAnimation();
//		aniEndHandle.post(new Runnable() {
//			public void run() {
////				BookLayout.this.invalidate();
//			}
//		});
    }


    protected void dispatchDraw(Canvas canvas) {
        Log.d(LOG_TAG, "dispatchDraw");
        super.dispatchDraw(canvas);
        if (!hasInit) {
            hasInit = true;
            if (mPageAdapter == null) {
                throw new RuntimeException("please set the PageAdapter on init");
            }
            // Initialization
            totalPageNum = mPageAdapter.getCount();

            this.removeAllViews();

            if (mainLayout != null) {
                mainLayout.removeAllViews();
            }
            mainLayout = new LinearLayout(mContext);
            mainLayout.setLayoutParams(new LayoutParams(contentWidth, contentHeight));
            mainLayout.setBackgroundColor(0xffffffff);
            mState = BookState.READY;

            if (invisibleLayout != null) {
                invisibleLayout.removeAllViews();
            }
            invisibleLayout = new LinearLayout(mContext);
            invisibleLayout.setLayoutParams(new LayoutParams(contentWidth, contentHeight));

            this.addView(invisibleLayout);
            this.addView(mainLayout);

            mBookView = new BookView(mContext);
            mBookView.setLayoutParams(new LayoutParams(contentWidth, contentHeight));
            this.addView(mBookView);

            updatePageView(0);
            invalidate();
        } else if (mState == BookState.READY) {
            mBookView.update();
        }
    }

    /**
     * This method has been modified, into a display
     * @param shift -
     */
    public void updatePageView(int shift) {
        Log.d(LOG_TAG, "updatePageView");

        mPageAdapter.refreshHistoricallyData(shift);

        int indexPage = this.mPageAdapter.getIndex();

        if (indexPage < 0 || indexPage > totalPageNum - 1) {
            return;
        }
        invisibleLayout.removeAllViews();
        mainLayout.removeAllViews();

		// Current page
        currentPage = mPageAdapter.getView(indexPage);
        currentPage.setLayoutParams(new LayoutParams(contentWidth, contentHeight));
        mainLayout.addView(currentPage);

		// Background page
        middlePage = new WhiteView(mContext);
        middlePage.setLayoutParams(new LayoutParams(contentWidth, contentHeight));
        invisibleLayout.addView(middlePage);

		// Previous page
        prevPage = null;
        if (indexPage > 0) {
            prevPage = mPageAdapter.getView(indexPage - 1);
        }
        prevPage.setLayoutParams(new LayoutParams(contentWidth, contentHeight));
        invisibleLayout.addView(prevPage);

		// Next
        nextPage = null;
        if (indexPage < totalPageNum - 1) {
            nextPage = mPageAdapter.getView(indexPage + 1);
        }
        nextPage.setLayoutParams(new LayoutParams(contentWidth, contentHeight));
        invisibleLayout.addView(nextPage);

        Log.d(LOG_TAG, "updatePageView finish");
    }

    OnTouchListener touchListener = new OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
			/*
			 * Page Right time：
	  (0,0)  _________________________________(2*Width,0)
			|                |                |
		    |                |                |
		    |                |                |
		    | Current Page   |                |
		    |                |                |
		    |                |                |
		    |                |                |
		    |________________|________________|
		    */
            Log.d(LOG_TAG, "onTouch " + " x: " + event.getX() + " y: " + event.getY() + " mState:" + mState);
            mGestureDetector.onTouchEvent(event);
            int action = event.getAction();
            if (action == MotionEvent.ACTION_UP && mSelectCorner != Corner.None && mState == BookState.TRACKING) {
                if (mState == BookState.ANIMATING)
                    return false;
                if (mSelectCorner == Corner.LeftTop) {
                    if (scrollX < contentWidth / 2) {
                        // Not trigger page
                        aniStopPos = new Point(0, 0);
                    } else {
                        // Triggered flip, end point (2 * contentWidth, 0)
                        aniStopPos = new Point(2 * contentWidth, 0);
                    }
                } else if (mSelectCorner == Corner.RightTop) {
                    if (scrollX < contentWidth / 2) {
                        aniStopPos = new Point(-contentWidth, 0);
                    } else {
                        aniStopPos = new Point(contentWidth, 0);
                    }
                } else if (mSelectCorner == Corner.LeftBottom) {
                    if (scrollX < contentWidth / 2) {
                        aniStopPos = new Point(0, contentHeight);
                    } else {
                        aniStopPos = new Point(2 * contentWidth, contentHeight);
                    }
                } else if (mSelectCorner == Corner.RightBottom) {
                    if (scrollX < contentWidth / 2) {
                        aniStopPos = new Point(-contentWidth, contentHeight);
                    } else {
                        aniStopPos = new Point(contentWidth, contentHeight);
                    }
                }
                aniStartPos = new Point((int) scrollX, (int) scrollY);
                aniTime = 800;
                mState = BookState.ABOUT_TO_ANIMATE;
                closeBook = true;
                aniStartTime = new Date();
                mBookView.startAnimation();
            }
            return false;
        }
    };

    class BookOnGestureListener implements GestureDetector.OnGestureListener {
        public boolean onDown(MotionEvent event) {
            Log.d(LOG_TAG, "onDown");
            if (mState == BookState.ANIMATING)
                return false;
            float x = event.getX(), y = event.getY();
            int w = contentWidth, h = contentHeight;
            // Determine the finger placement to (250, 250) as the center, radius circle 250
			/*
			_________________
			|      |         |
		    |      |         |
		    |______.(250,250)|
		    |                |
		    |                |
		    |                |
		    |                |
		    _________________
		    */
            int indexPage = mPageAdapter.getIndex();

            if (x * x + y * y < clickCornerLen) {
                // Top left corner
                if (indexPage > 0) {
                    mSelectCorner = Corner.LeftTop;
                    // Setting page origin (0,0)
                    aniStartPos = new Point(0, 0);
                }
            } else if ((x - w) * (x - w) + y * y < clickCornerLen) {
                // Top right corner
                if (indexPage < totalPageNum - 1) {
                    mSelectCorner = Corner.RightTop;
                    aniStartPos = new Point(contentWidth, 0);
                }
            } else if (x * x + (y - h) * (y - h) < clickCornerLen) {
                // Bottom left corner
                if (indexPage > 0) {
                    mSelectCorner = Corner.LeftBottom;
                    aniStartPos = new Point(0, contentHeight);
                }
            } else if ((x - w) * (x - w) + (y - h) * (y - h) < clickCornerLen) {
                // Bottom right corner
                if (indexPage < totalPageNum - 1) {
                    mSelectCorner = Corner.RightBottom;
                    aniStartPos = new Point(contentWidth, contentHeight);
                }
            }
            if (mSelectCorner != Corner.None) {
                // If you keep your finger on the screen to get the current placement of the fingers, that is flip end (x, y)
                aniStopPos = new Point((int) x, (int) y);
                aniTime = 800;
                mState = BookState.ABOUT_TO_ANIMATE;
                closeBook = false;
                aniStartTime = new Date();
                mBookView.startAnimation();
            }
            return false;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d(LOG_TAG, "onFling velocityX:" + velocityX + " velocityY:" + velocityY);
            if (mSelectCorner != Corner.None) {
                if (mSelectCorner == Corner.LeftTop) {
                    if (velocityX < 0) {
                        aniStopPos = new Point(0, 0);
                    } else {
                        aniStopPos = new Point(2 * contentWidth, 0);
                    }
                } else if (mSelectCorner == Corner.RightTop) {
                    if (velocityX < 0) {
                        aniStopPos = new Point(-contentWidth, 0);
                    } else {
                        aniStopPos = new Point(contentWidth, 0);
                    }
                } else if (mSelectCorner == Corner.LeftBottom) {
                    if (velocityX < 0) {
                        aniStopPos = new Point(0, contentHeight);
                    } else {
                        aniStopPos = new Point(2 * contentWidth, contentHeight);
                    }
                } else if (mSelectCorner == Corner.RightBottom) {
                    if (velocityX < 0) {
                        aniStopPos = new Point(-contentWidth, contentHeight);
                    } else {
                        aniStopPos = new Point(contentWidth, contentHeight);
                    }
                }
                Log.d(LOG_TAG, "onFling animate");
                aniStartPos = new Point((int) scrollX, (int) scrollY);
                aniTime = 1000;
                mState = BookState.ABOUT_TO_ANIMATE;
                closeBook = true;
                aniStartTime = new Date();
                mBookView.startAnimation();
            }
            return false;
        }

        public void onLongPress(MotionEvent e) {
            Log.d(LOG_TAG, "onLongPress");
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mState = BookState.TRACKING;
            if (mSelectCorner != Corner.None) {
                scrollX = e2.getX();
                scrollY = e2.getY();
                mBookView.startAnimation();
            }
            return false;
        }

        public void onShowPress(MotionEvent e) {
            Log.d(LOG_TAG, "onShowPress");
        }

        public boolean onSingleTapUp(MotionEvent e) {
            Log.d(LOG_TAG, "onSingleTapUp");

            if (mSelectCorner != Corner.None) {
                if (mSelectCorner == Corner.LeftTop) {
                    if (scrollX < contentWidth / 2) {
                        aniStopPos = new Point(0, 0);
                    } else {
                        aniStopPos = new Point(2 * contentWidth, 0);
                    }
                } else if (mSelectCorner == Corner.RightTop) {
                    if (scrollX < contentWidth / 2) {
                        aniStopPos = new Point(-contentWidth, 0);
                    } else {
                        aniStopPos = new Point(contentWidth, 0);
                    }
                } else if (mSelectCorner == Corner.LeftBottom) {
                    if (scrollX < contentWidth / 2) {
                        aniStopPos = new Point(0, contentHeight);
                    } else {
                        aniStopPos = new Point(2 * contentWidth, contentHeight);
                    }
                } else if (mSelectCorner == Corner.RightBottom) {
                    if (scrollX < contentWidth / 2) {
                        aniStopPos = new Point(-contentWidth, contentHeight);
                    } else {
                        aniStopPos = new Point(contentWidth, contentHeight);
                    }
                }
                aniStartPos = new Point((int) scrollX, (int) scrollY);
                aniTime = 800;
                mState = BookState.ABOUT_TO_ANIMATE;
                closeBook = true;
                aniStartTime = new Date();
                mBookView.startAnimation();
            }
            return false;
        }
    }


    public void setPageAdapter(IAdapter adapter) {
        Log.d(LOG_TAG, "setPageAdapter");
        mPageAdapter = adapter;
        hasInit = false;
    }

    public void Init(Context context) {
        Log.d(LOG_TAG, "Init");
        totalPageNum = 0;
        mContext = context;
        mSelectCorner = Corner.None;

        mGestureListener = new BookOnGestureListener();
        mGestureDetector = new GestureDetector(getContext(), mGestureListener);
        mGestureDetector.setIsLongpressEnabled(false);
        aniEndHandle = new Handler();

        this.setOnTouchListener(touchListener);
        this.setLongClickable(true);
    }

    class WhiteView extends View {
        public WhiteView(Context context) {
            super(context);
        }

        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawColor(Color.WHITE);
        }
    }


    public class DrawThread extends Thread {
        BookView bv;
        final SurfaceHolder surfaceHolder;
        boolean flag = false;
        int sleepSpan = 30;

        public DrawThread(BookView bv, SurfaceHolder surfaceHolder) {
            this.bv = bv;
            this.surfaceHolder = surfaceHolder;
            this.flag = true;
        }

        public void run() {
            Canvas canvas = null;
            while (flag) {
                try {
                    // Locking a region drawing canvas, unfinished map, will call the following unlockCanvasAndPost to change the display contents
                    // Under Memory requirements are relatively high, it is recommended not to be null parameter
                    canvas = surfaceHolder.lockCanvas(null);
                    if (canvas == null)
                        continue;
                    synchronized (surfaceHolder) {
                        if (mState == BookState.ABOUT_TO_ANIMATE || mState == BookState.ANIMATING) {
                            // The layout of the controls, and so painted on the phone screen
                            bv.doDraw(canvas);
                            //Get X, Y move within a specific time distance
                            getAnimateData();
                            // Draw flip effect
                            bv.drawPage(canvas);
                        } else if (mState == BookState.TRACKING) {
                            bv.doDraw(canvas);
                            bv.drawPage(canvas);
                        } else if (mState == BookState.ANIMATE_END) {
                            handleAniEnd(canvas);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        // End the lockout drawing, and submit changes
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                try {
                    Thread.sleep(sleepSpan);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
