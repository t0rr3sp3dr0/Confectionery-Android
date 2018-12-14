package me.t0rr3sp3dr0.confectionery.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * A {@link ImageView} that scales the image uniformly (maintain the image's
 * aspect ratio) so that both dimensions (width and height) of the image will
 * be equal to or larger than the corresponding dimension of the view (minus
 * padding), crops it in circle shape, then finally scales the image uniformly
 * (maintain the image's aspect ratio) so that both dimensions (width and
 * height) of the image will be equal to or less than the corresponding
 * dimension of the view (minus padding).
 *
 * @author Pedro Tôrres
 * @since 0.0.2
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class CircularImageView extends AppCompatImageView {

    public CircularImageView(Context context) {
        super(context);
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();

        int paddingTop = getPaddingTop();
        int paddingEnd = getPaddingRight();
        int paddingStart = getPaddingLeft();
        int paddingBottom = getPaddingBottom();

        int width = getWidth();
        int height = getHeight();

        int w = width - paddingStart - paddingEnd;
        int h = height - paddingTop - paddingBottom;

        if (drawable == null || w == 0 || h == 0)
            return;

        canvas.drawBitmap(getRoundedBitmap(getBitmap(drawable).copy(Config.ARGB_8888, true)), paddingStart, paddingTop, null);
    }

    private Bitmap getBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null)
                return bitmapDrawable.getBitmap();
        }

        final Bitmap bitmap = Bitmap.createBitmap(Math.max(drawable.getIntrinsicWidth(), 1), Math.max(drawable.getIntrinsicHeight(), 1), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private Bitmap getRoundedBitmap(Bitmap bitmap) {
        int paddingTop = getPaddingTop();
        int paddingEnd = getPaddingRight();
        int paddingStart = getPaddingLeft();
        int paddingBottom = getPaddingBottom();

        int width = getWidth();
        int height = getHeight();

        int w = width - paddingStart - paddingEnd;
        int h = height - paddingTop - paddingBottom;

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        int min = Math.min(bitmapWidth, bitmapHeight);
        int max = Math.max(bitmapWidth, bitmapHeight);

        bitmap = Bitmap.createBitmap(bitmap, bitmapWidth <= bitmapHeight ? 0 : max / 2 - min / 2, bitmapWidth >= bitmapHeight ? 0 : max / 2 - min / 2, min, min);
        bitmap = Bitmap.createScaledBitmap(bitmap, Math.min(w, h), Math.min(w, h), false);

        final Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawCircle(w / 2f - (w > h ? (w - h) / 2 : 0), h / 2f - (h > w ? (h - w) / 2 : 0), Math.min(w, h) / 2f, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
