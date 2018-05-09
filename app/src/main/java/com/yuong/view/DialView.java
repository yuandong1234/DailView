package com.yuong.view;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by yuandong on 2018/5/8.
 */

public class DialView extends View {

    private static String TAG = DialView.class.getSimpleName();

    //view的宽度
    private int width;

    //view的高度
    private int height;

    //边距
    private int padding;

    //刻度盘与弧之间的间隔
    private int interval;

    // 圆弧的半径
    private float radius;

    //每个刻度的长度
    private int len = 50;

    //弧线宽度
    private float strokeWidth;

    //圆心X、Y坐标
    private float centerX, centerY;

    // 刻度经过角度范围
    private float targetAngle = 90;

    // 圆弧的经过总范围角度角度
    private float sweepAngle = 180;

    //刻度线画笔
    private Paint linePaint;

    //圆弧画笔
    private Paint textPaint;

    //文字画笔
    private Paint arcPaint;

    //起始颜色
    private int startColor = Color.parseColor("#ED1C24");

    //结束颜色
    private int endColor = Color.parseColor("#22B14C");

    //颜色(优)
    private int COLOR_EXCELLENT = Color.parseColor("#50C647");
    private int COLOR_MEDIUM = Color.parseColor("#E0CE49");
    private int COLOR_BAD = Color.parseColor("#FF654C");

    private int CURRENT_COLOR=COLOR_EXCELLENT;

    //颜色渐变
    private ArgbEvaluator argbEvaluator;

    //文字描述
    private String desc = "优";

    //图片
    private Bitmap img;

    public DialView(Context context) {
        this(context, null);
    }

    public DialView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        // 初始化画笔
        linePaint = new Paint();
        // 设置画笔的宽度（线的粗细）
        linePaint.setStrokeWidth(12);
        // 设置抗锯齿
        linePaint.setAntiAlias(true);

        arcPaint = new Paint();
        // 设置画笔的宽度（线的粗细）
        arcPaint.setStrokeWidth(10);
        // 设置抗锯齿
        arcPaint.setAntiAlias(true);
        arcPaint.setColor(Color.parseColor("#EEEFF2"));
        arcPaint.setStyle(Paint.Style.STROKE);
        //arcPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint();
        arcPaint.setAntiAlias(true);

        argbEvaluator = new ArgbEvaluator();

        padding = DensityUtil.dp2px(context, 10);
        interval = DensityUtil.dp2px(context, 5);

        img = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_air_quality_excellent);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 通过测量规则获得宽和高
        width = MeasureSpec.getSize(widthMeasureSpec);
        radius = width * 2.0f / 3 / 2;
        centerX = width * 1f / 2;
        centerY = radius + padding;
        int tempheight = (int) (2 * radius + 2 * padding);
        if (height == 0) {
            setMeasuredDimension(width, tempheight);
            Log.e(TAG, "默认view的高度 ： " + tempheight);
        } else {
            setMeasuredDimension(width, height);
            Log.e(TAG, "重新设置view的高度 ： " + height);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        drawLine(canvas);
        drawArc(canvas);
        drawText(canvas);
    }

    /**
     * 画刻度线
     *
     * @param canvas
     */
    private void drawLine(final Canvas canvas) {

        // 保存之前的画布状态
        canvas.save();
        // 移动画布，实际上是改变坐标系的位置
        canvas.translate(centerX, centerY);
        // 旋转坐标系,需要确定旋转角度
        canvas.rotate(90);
        // 累计叠加的角度
        float c = 0;
        float a = sweepAngle / 30;
        linePaint.setStrokeWidth(12);
        for (int i = 0; i < 31; i++) {
            if (c <= targetAngle && targetAngle != 0) {// 如果累计画过的角度，小于当前有效刻度
                float offset = (i + 1) * a * 1f / sweepAngle;
                //TODO
                int color = (int) (argbEvaluator.evaluate(offset, CURRENT_COLOR, CURRENT_COLOR));
                linePaint.setColor(color);
                canvas.drawLine(0, radius - interval, 0, radius - len, linePaint);
                // 画过的角度进行叠加
                c += a;
            } else {
                linePaint.setColor(Color.parseColor("#EEEFF2"));
                canvas.drawLine(0, radius - interval, 0, radius - len, linePaint);
            }
            canvas.rotate(a);
        }
        // 恢复画布状态。
        canvas.restore();
    }

    /**
     * 画弧
     *
     * @param canvas
     */
    private void drawArc(Canvas canvas) {
        strokeWidth = DensityUtil.dp2px(getContext(), 3);
        float left = centerX - radius - strokeWidth / 2;
        float top = centerY - radius - strokeWidth / 2;
        float right = centerX + radius + strokeWidth / 2;
        float bottom = centerY + radius + strokeWidth / 2;

        //外弧
        RectF oval = new RectF(left, top, right, bottom);
        arcPaint.setStrokeWidth(strokeWidth);
        canvas.drawArc(oval, -180, 180, false, arcPaint);

        strokeWidth = DensityUtil.dp2px(getContext(), 10);
        left = centerX + interval + len - radius + strokeWidth / 2;
        top = centerY + interval + len - radius + strokeWidth / 2;
        right = centerX + radius - len - interval - strokeWidth / 2;
        bottom = centerY + radius - len - interval - strokeWidth / 2;

        //内弧
        RectF oval2 = new RectF(left, top, right, bottom);
        arcPaint.setStrokeWidth(strokeWidth);
        canvas.drawArc(oval2, -180, 180, false, arcPaint);
    }

    /**
     * 画文字和图片
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {

        //文字
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(DensityUtil.sp2px(getContext(), 50));
        textPaint.setColor(CURRENT_COLOR);
        Rect bounds = new Rect();
        textPaint.getTextBounds(desc, 0, desc.length(), bounds);
        canvas.drawText(desc, centerX - bounds.width() * 7f / 8, centerY + bounds.height() / 4, textPaint);

        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(DensityUtil.sp2px(getContext(), 16));
        textPaint.setColor(Color.parseColor("#4C4C4C"));
        canvas.drawText("车内空气质量", centerX, centerY - bounds.height() * 3f / 4 - padding, textPaint);

        //图片
        Bitmap bitmap = zoomImage(img, 0, bounds.height() * 5 / 6);
        // canvas.drawBitmap(bitmap, centerX + bounds.width() / 2, centerY - bounds.height() * 2f / 3, bitmapPaint);
        canvas.drawBitmap(bitmap, centerX + padding, centerY - bounds.height() * 1f / 2, textPaint);

        //横线
        linePaint.setColor(Color.parseColor("#EEEFF2"));
        linePaint.setStrokeWidth(DensityUtil.dp2px(getContext(), 1));

        float startX = centerX + interval + len - radius + strokeWidth + 1.5f * padding;
        float stopX = centerX - interval - len + radius - strokeWidth - 1.5f * padding;
        float startY = centerY + bounds.height() / 4 + 1.5f * padding;
        canvas.drawLine(startX, startY, stopX, startY, linePaint);


        Rect bounds2 = new Rect();
        String defaultText = "100%";
        textPaint.getTextBounds(defaultText, 0, defaultText.length(), bounds2);
        //画圆角矩形
        float left = centerX - bounds2.width() * 1.2f;
        float top = startY + padding;
        float right = centerX + bounds2.width() * 1.2f;
        float bottom = startY + 2.5f * padding + bounds2.height();
        RectF rectF = new RectF(left, top, right, bottom);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(CURRENT_COLOR);
        canvas.drawRoundRect(rectF, 50, 50, linePaint);

        //画文字
        textPaint.setTextSize(DensityUtil.sp2px(getContext(), 16));
        textPaint.setColor(Color.WHITE);
        canvas.drawText("80%", centerX, startY + 1.75f * padding + bounds2.height(), textPaint);

        textPaint.setTextSize(DensityUtil.sp2px(getContext(), 16));
        textPaint.setColor(Color.parseColor("#4C4C4C"));
        canvas.drawText("剩余滤芯", centerX, bottom + padding + bounds2.height(), textPaint);

        //重新设置view的高度
        height = (int) (bottom + 2 * padding + bounds2.height());
        requestLayout();
        Log.e(TAG, "重新设置view的高度");
    }

    /**
     * 图片缩放到指定尺寸
     *
     * @param src
     * @param newWidth
     * @param newHeight
     * @return
     */
    private Bitmap zoomImage(Bitmap src, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = src.getWidth();
        float height = src.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        if (scaleWidth == 0) {
            scaleWidth = scaleHeight;
        }
        if (scaleHeight == 0) {
            scaleHeight = scaleWidth;
        }
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(src, 0, 0, (int) width, (int) height, matrix, true);
    }
}
