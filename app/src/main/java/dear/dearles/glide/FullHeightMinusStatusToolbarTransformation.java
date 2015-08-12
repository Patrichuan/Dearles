package dear.dearles.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import dear.dearles.DearApp;


// Custom transformation for GLIDE full profile picture
public class FullHeightMinusStatusToolbarTransformation extends BitmapTransformation {

    protected DearApp app;
    int FullHeightMinusStatusToolbar;

    public FullHeightMinusStatusToolbarTransformation(Context context, DearApp app) {
        super(context);
        this.app = app;
        FullHeightMinusStatusToolbar = app.getScreenHeight()-app.getStatusBarHeight()-app.getToolbarHeight()-3;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        System.out.println("FROM: ImgWidth: " + outWidth + " ................. ImgHeight: " + outHeight);
        System.out.println("TO:   DesiredWidth: " + app.getScreenWidth() + " ................. DesiredHeight: " + FullHeightMinusStatusToolbar);
        return bitmapChanger(toTransform, app.getScreenWidth(), FullHeightMinusStatusToolbar);
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

        System.out.println("ResultWidth: " + result.getWidth() + " ................. ResultHeight: " + result.getHeight());

        return result;
    }
}