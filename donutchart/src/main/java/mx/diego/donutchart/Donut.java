package mx.diego.donutchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews.RemoteView;

import java.util.ArrayList;
import java.util.List;

@RemoteView
public class Donut extends View {
	private static float MINVAL;
	protected static float MINPAD;
	protected static final float GINI = -90;
	
	protected Paint pArc;
    protected RectF rectf;

    private List<Integer> colors;
	protected List<Float> percentages;

	public Donut(Context context) {
		this(context, null);
	}

	public Donut(Context context, AttributeSet attrs){
		super(context, attrs);

		MINVAL = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
		MINPAD = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());

		pArc = new Paint();
		pArc.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));
		pArc.setAntiAlias(true);
		pArc.setStrokeCap(Paint.Cap.BUTT);
		pArc.setStyle(Paint.Style.STROKE);

		percentages = new ArrayList<>();
		colors = new ArrayList<>();

		colors.add(Color.BLUE);
		colors.add(Color.RED);
		colors.add(Color.YELLOW);
		colors.add(Color.CYAN);
		colors.add(Color.GRAY);
		colors.add(Color.DKGRAY);
	}

	public void setPercentages(List<Float> percentages) {
		this.percentages = percentages;
	}

	public void setColors(List<Integer> colors) {
		this.colors = colors;
	}

	@Override
	public void onDraw(Canvas canvas){
		drawBackgroundCircle(canvas);
		drawArcs(canvas);
	}

	private void drawBackgroundCircle(Canvas canvas) {
		pArc.setColor(Color.LTGRAY);
		canvas.drawArc(rectf, GINI, 360, false, pArc);
	}

	protected void drawArcs(Canvas canvas) {
		float posIni = GINI;
		for(Float value : percentages) {
			pArc.setColor(colors.get(percentages.indexOf(value)));

			float posEnd = (float) (value * 360);

			canvas.drawArc(rectf, posIni, posEnd, false, pArc);
			posIni += posEnd;
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		rectf = new RectF(MINPAD,MINPAD,w-MINPAD,h-MINPAD);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		int height = (int) getDim(heightMeasureSpec, MINVAL);
		int width = (int) getDim(widthMeasureSpec, MINVAL);
		setMeasuredDimension(width,height);
	}

	private float getDim(int measureSpec, float wanted){
		int dimention = MeasureSpec.getSize(measureSpec);
		int mode = MeasureSpec.getMode(measureSpec);
		if (mode == MeasureSpec.EXACTLY){
			return dimention;
		} else if (mode == MeasureSpec.AT_MOST){
			return Math.min(dimention, wanted);
		} else {
			return wanted;
		}
	}
}