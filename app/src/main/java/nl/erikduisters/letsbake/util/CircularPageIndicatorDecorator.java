package nl.erikduisters.letsbake.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import nl.erikduisters.letsbake.R;

/**
 * Created by Erik Duisters on 03-04-2018.
 *
 * This code is based on
 *
 * https://github.com/bleeding182/recyclerviewItemDecorations/blob/master/app/src/main/java/com/github/bleeding182/recyclerviewdecorations/viewpager/LinePagerIndicatorDecoration.java
 * by David Medenjak
 *
 * and
 *
 * https://stackoverflow.com/a/48071124/8368569 by lbgupta
 *
 */
public class CircularPageIndicatorDecorator extends RecyclerView.ItemDecoration {
    private final int indicatorHeight;
    private final float indicatorOutlineStrokeWidth;
    private final float indicatorOutlineDiameter;
    private final float indicatorPadding;
    private final Interpolator interpolator;
    private final Paint outlinePaint;
    private final Paint indicatorPaint;
    private final int activeColor;
    private final int inActiveColor;

    public CircularPageIndicatorDecorator(Context context) {
        float DP = Resources.getSystem().getDisplayMetrics().density;

        indicatorHeight = (int) (DP * 36);
        indicatorOutlineStrokeWidth = DP * 2;
        indicatorOutlineDiameter = DP * 8;
        indicatorPadding = DP * 8;

        activeColor = context.getResources().getColor(R.color.colorAccent);
        inActiveColor = 0x66000000;

        interpolator = new AccelerateDecelerateInterpolator();
        outlinePaint = new Paint();
        outlinePaint.setStrokeWidth(indicatorOutlineStrokeWidth);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setAntiAlias(true);
        outlinePaint.setColor(inActiveColor);

        indicatorPaint = new Paint();
        indicatorPaint.setStyle(Paint.Style.FILL);
        indicatorPaint.setAntiAlias(true);
        indicatorPaint.setColor(activeColor);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        int itemCount = parent.getAdapter().getItemCount();

        // center horizontally, calculate width and subtract half from center
        float totalLength = indicatorOutlineDiameter * itemCount;
        float paddingBetweenItems = Math.max(0, itemCount - 1) * indicatorPadding;
        float indicatorTotalWidth = totalLength + paddingBetweenItems;
        float indicatorStartX = (parent.getWidth() - indicatorTotalWidth) / 2F;

        // center vertically in the allotted space
        float indicatorPosY = parent.getHeight() - indicatorHeight / 2F;

        drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount);

        // find active page (which should be highlighted)
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        int activePosition = layoutManager.findFirstVisibleItemPosition();
        if (activePosition == RecyclerView.NO_POSITION) {
            return;
        }

        // find offset of active page (if the user is scrolling)
        final View activeChild = layoutManager.findViewByPosition(activePosition);
        int left = activeChild.getLeft();
        int width = activeChild.getWidth();

        // on swipe the active item will be positioned from [-width, 0]
        // interpolate offset for smooth animation
        float progress = interpolator.getInterpolation(left * -1 / (float) width);

        drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress);
    }

    private void drawInactiveIndicators(Canvas c, float indicatorStartX, float indicatorPosY, int itemCount) {
        // width of item indicator including padding
        final float itemWidth = indicatorOutlineDiameter + indicatorPadding;

        float start = indicatorStartX;
        for (int i = 0; i < itemCount; i++) {

            c.drawCircle(start, indicatorPosY, indicatorOutlineDiameter / 2F, outlinePaint);

            start += itemWidth;
        }
    }

    private void drawHighlights(Canvas c, float indicatorStartX, float indicatorPosY,
                                int highlightPosition, float progress) {

        // width of item indicator including padding
        final float itemWidth = indicatorOutlineDiameter + indicatorPadding;

        if (progress == 0F) {
            // no swipe, draw a normal indicator
            float highlightStart = indicatorStartX + itemWidth * highlightPosition;

            c.drawCircle(highlightStart, indicatorPosY, (indicatorOutlineDiameter-indicatorOutlineStrokeWidth) / 2F, indicatorPaint);

        } else {
            float highlightStart = indicatorStartX + itemWidth * highlightPosition;
            // calculate partial highlight
            float partialLength = indicatorOutlineDiameter * progress + indicatorPadding * progress;

            c.drawCircle(highlightStart + partialLength, indicatorPosY, (indicatorOutlineDiameter-indicatorOutlineStrokeWidth) / 2F, indicatorPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = indicatorHeight;
    }
}
