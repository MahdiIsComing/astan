package ir.upcoders.sheydaee;

/**
 * Created by mahdi on 7/30/16.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.widget.TextView;

import com.up_coders.astan.R;

public class QRreader extends Activity implements QRCodeReaderView.OnQRCodeReadListener {

    private TextView myTextView;
    private QRCodeReaderView mydecoderview;
    //private ImageView line_image;
    public String result;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrreader);

        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrreaderview);
        mydecoderview.setOnQRCodeReadListener(this);

        myTextView = (TextView) findViewById(R.id.exampleTextView);

//        //TODO: mahdi: Change with vector
//        line_image = (ImageView) findViewById(R.id.red_line_png);
//
//        TranslateAnimation mAnimation =
//                new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.ABSOLUTE, 0f,
//                        TranslateAnimation.RELATIVE_TO_PARENT, 0f, TranslateAnimation.RELATIVE_TO_PARENT, 0.4f);
//        mAnimation.setDuration(1000);
//        mAnimation.setRepeatCount(-1);
//        mAnimation.setRepeatMode(Animation.REVERSE);
//        mAnimation.setInterpolator(new LinearInterpolator());
//        line_image.setAnimation(mAnimation);
    }

    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed
    @Override public void onQRCodeRead(String text, PointF[] points) {
        myTextView.setText(text);

        Intent returnIntent = getIntent();
        returnIntent.putExtra("result", text);
        setResult(RESULT_OK,returnIntent);
        finish();
    }

    // Called when your device have no camera
    @Override public void cameraNotFound() {

    }

    // Called when there's no QR codes in the camera preview image
    @Override public void QRCodeNotFoundOnCamImage() {

    }

    @Override protected void onResume() {
        super.onResume();
        mydecoderview.getCameraManager().startPreview();
    }

    @Override protected void onPause() {
        super.onPause();
        mydecoderview.getCameraManager().stopPreview();
    }
}
