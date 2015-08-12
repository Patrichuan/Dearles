package dear.dearles.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import dear.dearles.R;


// Custom transformation for GLIDE profile picture
public class FullHeightTransformation extends BitmapTransformation {

    private Context aContext;

    public FullHeightTransformation(Context context) {
        super(context);
        aContext = context;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return bitmapChanger(toTransform, outWidth, outHeight);
    }

    @Override
    public String getId() {
        return "some_id";
    }

    private static Bitmap bitmapChanger(Bitmap bitmap, int desiredWidth, int desiredHeight) {
        float originalWidth = bitmap.getWidth();
        float originalHeight = bitmap.getHeight();

        float scaleX = desiredWidth / originalWidth;
        float scaleY = desiredHeight / originalHeight;

        //Use the larger of the two scales to maintain aspect ratio
        float scale = Math.max(scaleX, scaleY);

        Matrix matrix = new Matrix();

        matrix.postScale(scale, scale);

        //If the scaleY is greater, we need to center the image
        if (scaleX < scaleY) {
            float tx = (scale * originalWidth - desiredWidth) / 2f;
            matrix.postTranslate(-tx, 0f);
        }

        Bitmap result = Bitmap.createBitmap(desiredWidth, desiredHeight, bitmap.getConfig() != null ? bitmap.getConfig() : Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(bitmap, matrix, new Paint());
        return result;

        //return Bitmap.createBitmap(bitmap, 0, 0, (int) originalWidth, (int) originalHeight, matrix, true);
    }
}