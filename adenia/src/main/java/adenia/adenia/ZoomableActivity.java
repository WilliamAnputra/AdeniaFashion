package adenia.adenia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import adenia.Zooming.MoveGestureDetector;
import adenia.Zooming.RotateGestureDetector;
import adenia.Zooming.ShoveGestureDetector;

public class ZoomableActivity extends AppCompatActivity implements View.OnTouchListener {


    String imageUrl;
    ImageView view;
    private Matrix mMatrix= new Matrix();
    private float mScaleFactor=1.5f;
    private float mFocusX = 0.f;
    private float mFocusY = 0.f;
    private int mAlpha = 255;
    private float mRotationDegrees =0.f;
    private int mImageHeight, mImageWidth;

    private ScaleGestureDetector mScaleDetector;
    private MoveGestureDetector mMoveDetector;
    private ShoveGestureDetector mShoveDetector;
    private RotateGestureDetector mRotateDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoomable);
        //get the width and height of the screen
        Display display=getWindowManager().getDefaultDisplay();
        Point size= new Point();
        display.getRealSize(size);
        mFocusX= size.x/2f;
        mFocusY= size.y/2f;

        Intent getIntent= getIntent();
        imageUrl= getIntent.getStringExtra("imageurl");

        view=(ImageView)findViewById(R.id.imageViewZoom);

        view.setOnTouchListener(this);

        mScaleDetector 	= new ScaleGestureDetector(getApplicationContext(), new ScaleListener());
        mMoveDetector 	= new MoveGestureDetector(getApplicationContext(), new MoveListener());
        mShoveDetector 	= new ShoveGestureDetector(getApplicationContext(), new ShoveListener());
        mRotateDetector = new RotateGestureDetector(getApplicationContext(),new RotateListener());
        new getBitmap().execute();

    }

    public class getBitmap extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {

            Bitmap bitmap;
            try {
                URL url = new URL(imageUrl);
                InputStream inputStream = url.openConnection().getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            Drawable d= new BitmapDrawable(getResources(),bitmap);
            mImageHeight= d.getIntrinsicHeight();
            mImageWidth=d.getIntrinsicWidth();

            float scaledImageCenterX = (mImageWidth*mScaleFactor)/2;
            float scaledImageCenterY = (mImageHeight * mScaleFactor) / 2;

            mMatrix.postScale(mScaleFactor, mScaleFactor);
            mMatrix.postRotate(mRotationDegrees, scaledImageCenterX, scaledImageCenterY);
            mMatrix.postTranslate(mFocusX - scaledImageCenterX, mFocusY - scaledImageCenterY);
            view.setImageMatrix(mMatrix);
            view.setImageDrawable(d);
        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mRotateDetector.onTouchEvent(event);
        mScaleDetector.onTouchEvent(event);
        mMoveDetector.onTouchEvent(event);
        mShoveDetector.onTouchEvent(event);

        float scaledImageCenterX=(mImageWidth*mScaleFactor)/2;
        float scaledImageCenterY=(mImageHeight*mScaleFactor)/2;

        mMatrix.reset();
        mMatrix.postScale(mScaleFactor, mScaleFactor);
        mMatrix.postRotate(mRotationDegrees,  scaledImageCenterX, scaledImageCenterY);
        mMatrix.postTranslate(mFocusX - scaledImageCenterX, mFocusY - scaledImageCenterY);

        ImageView view=(ImageView) v;
        view.setImageMatrix(mMatrix);

        return true;
    }
    private class RotateListener extends RotateGestureDetector.SimpleOnRotateGestureListener {
        @Override
        public boolean onRotate(RotateGestureDetector detector) {
            mRotationDegrees -= detector.getRotationDegreesDelta();
            return true;
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            mScaleFactor*=detector.getScaleFactor();

            mScaleFactor=Math.max(0.1f, Math.min(mScaleFactor, 10.0f));

            return true;
        }
    }


    private class MoveListener extends MoveGestureDetector.SimpleOnMoveGestureListener {

        @Override
        public boolean onMove(MoveGestureDetector detector) {
            PointF d= detector.getFocusDelta();
            mFocusX +=d.x;
            mFocusY +=d.y;

            return true;
        }
    }

    private class ShoveListener extends ShoveGestureDetector.SimpleOnShoveGestureListener {
        @Override
        public boolean onShove(ShoveGestureDetector detector) {
            mAlpha += detector.getShovePixelsDelta();
            if (mAlpha > 255)
                mAlpha = 255;
            else if (mAlpha < 0)
                mAlpha = 0;

            return true;
        }
    }
}
