package com.example.zengcanwen.myanimator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by zengcanwen on 2018/1/8.
 */

public class MyAnimatorView extends FrameLayout {
    private ImageView redIv  ;
    private ImageView blueIv ;
    private ImageView yellowIv ;
    private ImageView greenIv ;
    private Context context ;
    private float height ;
    private float width ;
    private ViewPath redViewPath , yellowViewPath , blueViewPath , greenViewPath ;
    private AnimatorSet redAnimationSet , yellowAnimationSet , blueAnimationSet , greenAnimationSet ;

    public MyAnimatorView(Context context) {
        this(context , null);
    }

    public MyAnimatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public MyAnimatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context ;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec) ;
        width = MeasureSpec.getSize(widthMeasureSpec) ;

    }

    public void start(){
        init();
        redViewPath();
        blueViewPath() ;
        yellowViewPath();
        greenViewPath() ;
    }

    private void init(){
//        removeAllViews();
        redIv = new ImageView(context) ;
        initIv(redIv , R.drawable.red_drawable);
        yellowIv = new ImageView(context) ;
        initIv(yellowIv , R.drawable.yellow_drawable);
        blueIv = new ImageView(context) ;
        initIv(blueIv , R.drawable.blue_drawable);
        greenIv = new ImageView(context) ;
        initIv(greenIv , R.drawable.green_drawable);

    }

    private void initIv(ImageView imageView , int drawableR){
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT , FrameLayout.LayoutParams.WRAP_CONTENT) ;
        imageView.setImageResource(drawableR);
        layoutParams.height = Util.dp2px(context , 40) ;
        layoutParams.width = Util.dp2px(context , 40);
        layoutParams.gravity = Gravity.CENTER ;
        imageView.setLayoutParams(layoutParams);
        addView(imageView);
    }

    //红色球的运行轨迹
    private void redViewPath(){
        redViewPath = new ViewPath() ;
        redViewPath.moveTo(0  , 0);
        redViewPath.lineTo(-(width / 4.0f) , 0);
        redViewPath.curveTo(-700, -height / 2, width / 3 * 2, -height / 3 * 2, 0, -Util.dp2px(context , 80)) ;
        setAnimation(redIv , redViewPath);
        redAnimationSet.start();
    }

    //蓝色球的运行轨迹
    private void blueViewPath(){
        blueViewPath = new ViewPath() ;
        blueViewPath.moveTo(0, 0);
        blueViewPath.lineTo(width / 5 * 2 - width / 2, 0);
        blueViewPath.curveTo(-300, -height / 2, width, -height / 9 * 5, 0, -Util.dp2px(context , 80)) ;
        setAnimation( blueIv, blueViewPath);
        blueAnimationSet.start();
    }

    //黄色球的运行轨迹
    private void yellowViewPath(){
        yellowViewPath = new ViewPath() ;
        yellowViewPath.moveTo(0, 0);
        yellowViewPath.lineTo(width / 5 * 3 - width / 2, 0);
        yellowViewPath.curveTo(300, height, -width, -height / 9 * 5, 0, -Util.dp2px(context , 80));
        setAnimation(yellowIv , yellowViewPath);
        yellowAnimationSet.start();
    }

    //绿色球的运行轨迹
    private void greenViewPath(){
        greenViewPath = new ViewPath() ;
        greenViewPath.moveTo(0, 0);
        greenViewPath.lineTo(width / 5 * 4 - width / 2, 0);
        greenViewPath.curveTo(700, height / 3 * 2, -width / 2, height / 2, 0, -Util.dp2px(context , 80));
        setAnimation(greenIv , greenViewPath);
        greenAnimationSet.start();
    }

    private void setAnimation(ImageView imageView , ViewPath viewPath){
        ObjectAnimator objectAnimator = ObjectAnimator.ofObject(new ViewObj(imageView) , "fabLoc" , new ViewPathEvaluator() , viewPath.getPoints().toArray()) ;
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.setDuration(2000) ;
        addAnimation(objectAnimator , imageView);
    }

    private void addAnimation(ObjectAnimator objectAnimator , final ImageView imageView){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1 , 1000) ;
        valueAnimator.setDuration(1600) ;
        valueAnimator.setStartDelay(600);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float)animation.getAnimatedValue() ;
                float scale = getScale(imageView);
                if (value <= 500) {
                    scale = 1.0f + (value / 500.0f) * scale;
                } else {
                    scale = 1 + ((1000.0f - value) / 500.0f) * scale;
                }
                imageView.setScaleY(scale);
                imageView.setScaleX(scale);
//                imageView.setScaleType(ImageView.ScaleType.CENTER);
                float alpha = value / 1000.0f ;
                imageView.setAlpha(1.0f - alpha);
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                removeView(imageView);
            }
        });

        if(imageView == redIv){
            redAnimationSet = new AnimatorSet() ;
            redAnimationSet.playTogether(valueAnimator , objectAnimator);
        }else if(imageView == greenIv){
            greenAnimationSet = new AnimatorSet() ;
            greenAnimationSet.playTogether(valueAnimator , objectAnimator);
        }else if(imageView == yellowIv){
            yellowAnimationSet = new AnimatorSet() ;
            yellowAnimationSet.playTogether(valueAnimator , objectAnimator);
        }else if(imageView == blueIv){
            blueAnimationSet = new AnimatorSet() ;
            blueAnimationSet.playTogether(valueAnimator , objectAnimator);
        }
    }

    private float getScale(ImageView imageView){
        if(imageView == redIv){
            return 1.0f ;
        }else if(imageView == blueIv){
            return 1.5f ;
        }else if(imageView == greenIv){
            return 2.0f ;
        }else if(imageView == yellowIv){
            return 2.5f ;
        }else {
            return 1.0f ;
        }
    }

    public class ViewObj {
        private final ImageView imageView;
        public ViewObj(ImageView imageView) {
            this.imageView = imageView;
        }

        public void setFabLoc(ViewPoint newLoc) {
            imageView.setTranslationX(newLoc.x);
            imageView.setTranslationY(newLoc.y);
        }
    }
}
