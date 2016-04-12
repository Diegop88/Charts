package mx.diego.donutchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RemoteViews.RemoteView;

import java.util.ArrayList;
import java.util.List;

@RemoteView
public class DonutAnim extends Donut {
    private PercentageItems items;
    private Rect rect;

    public DonutAnim(Context context) {
        this(context, null);
    }

    public DonutAnim(Context context, AttributeSet attrs) {
        super(context, attrs);
        items = new PercentageItems();
    }

    @Override
    public void setPercentages(List<Float> percentages) {
        super.setPercentages(percentages);
        items = new PercentageItems();
        removeCallbacks(animator);
        post(animator);
    }

    private Runnable animator = new Runnable() {
        @Override
        public void run() {
            items.update();
            if(!items.complete()){
                postDelayed(this, 10);
            }
            invalidate(rect);
        }
    };

    @Override
    protected void drawArcs(Canvas canvas) {
        items.drawItems(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rect = new Rect((int) MINPAD, (int) MINPAD, (int) (w-MINPAD), (int) (h-MINPAD));
        rectf = new RectF(rect);
    }

    private class PercentageItems {
        private List<Item> items;

        public PercentageItems() {
            items = new ArrayList<>();
            for(Float value : percentages) {
                items.add(new Item(value));
            }
        }

        public void update() {
            for(Item item : items) {
                item.update();
            }
        }

        public boolean complete() {
            boolean completed = true;
            for(Item item : items){
                completed = item.isCompleted();
            }
            return completed;
        }

        public void drawItems(Canvas canvas) {
            for(Item item : items){
                item.drawItself(canvas);
            }
        }

        private class Item {
            private float posact;
            private float posend;

            public Item(Float value) {
                posend = value * 360;
            }

            public void drawItself(Canvas canvas) {
//                canvas.drawArc(rectf, posini, posact, false, pArc);
            }

            public void update() {

            }

            public boolean isCompleted() {
                return posact <= posend;
            }
        }
    }
}
