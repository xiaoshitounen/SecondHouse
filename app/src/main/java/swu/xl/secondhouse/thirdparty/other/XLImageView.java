package swu.xl.secondhouse.thirdparty.other;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;

public class XLImageView extends androidx.appcompat.widget.AppCompatImageView {
    public XLImageView(Context context) {
        super(context);
    }

    public XLImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void draw(Canvas canvas) {
        //裁剪画布
        Path path = new Path();
        path.addCircle(getPivotX(),getPivotY(), getWidth() >> 1, Path.Direction.CW);
        canvas.clipPath(path);

        //绘制
        super.draw(canvas);
    }
}
