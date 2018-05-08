package com.yuong.view;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

    // 圆弧的半径
    private float radius;
    private float centerX;


    // 刻度经过角度范围
    private float targetAngle = 180;

    // 圆弧的经过总范围角度角度
    private float sweepAngle = 180;

    private Paint linePaint;
    private Paint arcPaint;

    private ArgbEvaluator argbEvaluator;
    private int startColor = Color.parseColor("#ED1C24");
    private int endColor = Color.parseColor("#22B14C");


    public DialView(Context context) {
        this(context, null);
    }

    public DialView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        // 初始化画笔
        linePaint = new Paint();
        // 设置画笔的宽度（线的粗细）
        linePaint.setStrokeWidth(12);
        // 设置抗锯齿
        linePaint.setAntiAlias(true);

        arcPaint = new Paint();
        // 设置画笔的宽度（线的粗细）
        arcPaint.setStrokeWidth(5);
        // 设置抗锯齿
        arcPaint.setAntiAlias(true);
        arcPaint.setColor(Color.parseColor("#EEEFF2"));

        arcPaint.setStyle(Paint.Style.STROKE);
        argbEvaluator = new ArgbEvaluator();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 通过测量规则获得宽和高
        width = MeasureSpec.getSize(widthMeasureSpec);
        Log.e(TAG, "控件宽度 ： " + width);
        radius = width * 2.0f / 3 / 2;
        centerX = width * 1f / 2;
        Log.e(TAG, "圆弧半径 ： " + radius);
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas);
        drawArc(canvas);
    }
//

    /**
     * 实现画刻度线的功能
     *
     * @param canvas
     */
    private void drawLine(final Canvas canvas) {
        float a = sweepAngle / 30;

        // 保存之前的画布状态
        canvas.save();
        // 移动画布，实际上是改变坐标系的位置
        canvas.translate(centerX, radius);
        // 旋转坐标系,需要确定旋转角度
        canvas.rotate(90);
        // 累计叠加的角度
        float c = 0;
        for (int i = 0; i < 31; i++) {
            if (c <= targetAngle && targetAngle != 0) {// 如果累计画过的角度，小于当前有效刻度
                float offset = (i + 1) * a * 1f / sweepAngle;
                int color = (int) (argbEvaluator.evaluate(offset, startColor, endColor));
                linePaint.setColor(color);
                canvas.drawLine(0, radius, 0, radius - 50, linePaint);
                // 画过的角度进行叠加
                c += a;
            } else {
                linePaint.setColor(Color.WHITE);
                canvas.drawLine(0, radius, 0, radius - 50, linePaint);
            }
            canvas.rotate(a);
        }
        // 恢复画布状态。
        canvas.restore();


    }

    /**
     * 画弧
     * @param canvas
     */
    private  void drawArc(Canvas canvas){
        RectF oval=new RectF(centerX-radius,0,centerX+radius,2*radius);
        canvas.drawArc(oval, -180, 180, false, arcPaint);
    }
}
