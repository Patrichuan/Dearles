package dear.dearles.glide;


import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

// Custom transformation for GLIDE profile picture
public class CropSquareTransformation extends BitmapTransformation {

    public CropSquareTransformation(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap MyBitmapTransformed;
        int size = Math.min(toTransform.getWidth(), toTransform.getHeight());
        int x = (toTransform.getWidth() - size) / 2;
        int y = (toTransform.getHeight() - size) / 2;
        MyBitmapTransformed = Bitmap.createBitmap(toTransform, x, y, size, size);
        return MyBitmapTransformed;
    }

    @Override
    public String getId() {
        return "square";
    }
}
