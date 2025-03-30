package vn.edu.fpt.lunarrotationapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class LunarView extends View {
    private Bitmap earthBitmap;
    private Bitmap moonBitmap;
    private float moonAngle = 0; // Angle in degrees for moon rotation
    private float orbitRadius;
    private Handler handler;
    private Runnable updater;

    // Constructors
    public LunarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public (Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        // In edit mode, skip bitmap loading to avoid preview issues
        if (isInEditMode()) {
            return;
        }

        // Load scaled-down images from drawable resources
        // Adjust the target width/height as necessary for your design
        earthBitmap = decodeSampledBitmapFromResource(getResources(), R.drawable.earth, 100, 100);
        moonBitmap = decodeSampledBitmapFromResource(getResources(), R.drawable.moon, 50, 50);

        handler = new Handler();
        updater = new Runnable() {
            @Override
            public void run() {
                moonAngle += 2; // Update angle (2 degrees per tick)
                if (moonAngle >= 360) {
                    moonAngle -= 360;
                }
                invalidate(); // Request to redraw the view
                handler.postDelayed(this, 30); // Update every 30 milliseconds
            }
        };
        handler.post(updater);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Get canvas center
        int width = getWidth();
        int height = getHeight();
        float centerX = width / 2f;
        float centerY = height / 2f;

        // Draw the Earth at the center
        float earthX = centerX - (earthBitmap.getWidth() / 2f);
        float earthY = centerY - (earthBitmap.getHeight() / 2f);
        canvas.drawBitmap(earthBitmap, earthX, earthY, null);

        // Set a fixed radius for the moon's orbit (e.g., one-third of the minimum screen dimension)
        orbitRadius = Math.min(width, height) / 3f;

        // Calculate moon's position using the current angle (convert degrees to radians)
        double radians = Math.toRadians(moonAngle);
        float moonX = (float) (centerX + orbitRadius * Math.cos(radians) - (moonBitmap.getWidth() / 2f));
        float moonY = (float) (centerY + orbitRadius * Math.sin(radians) - (moonBitmap.getHeight() / 2f));

        // Draw the Moon at the calculated position
        canvas.drawBitmap(moonBitmap, moonX, moonY, null);
    }

    // Helper method to load a scaled-down version of the image
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    // Helper method to calculate an appropriate inSampleSize value
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
