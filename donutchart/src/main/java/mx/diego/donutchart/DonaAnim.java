package mx.diego.donutchart;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RemoteViews.RemoteView;

@RemoteView
public class DonaAnim extends View{
	
	private static final int MINVAL = 300;
	
	private Paint pArc;
	private Paint pTex;
    
    private ArcosAnim arcos;
    private List<Arco> lArcos;
    
    public DonaAnim(Context context) {
		this(context, null);
	}
	
	public DonaAnim(Context context, AttributeSet attrs){
		super(context, attrs);
		pArc = new Paint();
		pTex = new Paint();
		lArcos = new ArrayList<Arco>();
	}
	
	public void setDatos(int total, int altas, int bajas){
		arcos = new ArcosAnim(total, altas, bajas);
		removeCallbacks(animator);
		post(animator);
	}
	
	private Runnable animator = new Runnable() {
		@Override
		public void run() {
			arcos.update();
			if(!arcos.complete()){
				postDelayed(this, 10);
			}
			invalidate();
		}
	};

	public void onDraw(Canvas canvas){
		if(!lArcos.isEmpty()){
			drawArcs(canvas);
			drawTexts(canvas);
		}
	}
	
	private void drawArcs(Canvas canvas){
		for (Arco arco : lArcos){
			pArc.setColor(arco.color);
		    canvas.drawArc(arcos.mRect, -90, arco.posAct, false, pArc);
	    }
	}
	
	private void drawTexts(Canvas canvas){
//		canvas.drawText(Utils.formatIntNumberToString(arcos.sumTotal), arcos.posXText, arcos.posYText, pTex);
	}
	
	class ArcosAnim {
		private float densidad;
		
		private RectF mRect = new RectF(0,0,MINVAL,MINVAL);
		
		private float posYText = 0;
		private float posXText = 0;
		private float textSize = 15;
		private int textColor = Color.rgb(100, 100, 100);
		
		private float radio;
		private float strokeWidth;
		
		private int sumTotal = 0;
		
		public ArcosAnim(int total, int altas, int bajas) {
			sumTotal = total + altas + bajas;
			float pAltas = (float) altas / sumTotal;
			float pBajas = (float) bajas / sumTotal;
			if((pAltas + pBajas) < 1.0f) {
				lArcos.add(new Arco((1.0f), Color.rgb(51, 181, 229)));
			}
			if(pBajas > 0) {
				lArcos.add(new Arco(pAltas + pBajas, Color.rgb(255, 68, 68)));
			}
			if(pAltas > 0){
				lArcos.add(new Arco(pAltas, Color.rgb(153, 204, 0)));
			}
			densidad = getResources().getDisplayMetrics().density;
		}
		
		public void update(){
			initValues();
			for(Arco arco: lArcos){
				arco.act();
			}
		}
		
		private void initValues() {
			int width = getWidth() - getPaddingLeft() - getPaddingRight();
			int height = getHeight() - getPaddingTop() - getPaddingBottom();
			
			radio = (((width<height) ? width : height) / 2) - (10 * densidad);
			strokeWidth = radio * 0.2f;
			mRect = new RectF((width/2) - radio , (height / 2) - radio, (width / 2) + radio, (height / 2) + radio);
			
			posXText = width / 2;
			posYText = (height / 2) + ( 7 * densidad);
			textSize = 20 * densidad;
			
			pArc.setStrokeWidth(strokeWidth);
		    pArc.setAntiAlias(true);
		    pArc.setStrokeCap(Paint.Cap.BUTT);
		    pArc.setStyle(Paint.Style.STROKE);
		    pArc.setColor(Color.RED);
		    
			pTex.setColor(textColor);
			pTex.setAntiAlias(true);
			pTex.setTextSize(textSize);
			pTex.setTypeface(Typeface.DEFAULT_BOLD);
			pTex.setTextAlign(Paint.Align.CENTER);
		}

		public boolean complete(){
			boolean ok = true;
			for(Arco arco : lArcos){
				ok &= arco.complete();
			}
			return ok;
		}
	}
	
	class Arco {
		private float posAct;
		private float posFin;
		private float avance;
		private int color;
		
		public Arco(float porcentaje, int color) {
			posFin = 360 * porcentaje;
			avance = posFin / 80;
			this.color = color;
		}
		
		public void act(){
			posAct += avance;
			if(posAct > posFin)
				posAct = posFin;
		}
		
		public boolean complete(){
			return (posAct == posFin);
		}
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		int alto = obtenDim(heightMeasureSpec, MINVAL);
		int ancho = obtenDim(widthMeasureSpec, MINVAL);
		setMeasuredDimension(ancho,alto);
	}
	
	private int obtenDim( int measureSpec, int deseado){
		int dimension = MeasureSpec.getSize(measureSpec);
		int modo = MeasureSpec.getMode(measureSpec);
		if (modo == MeasureSpec.EXACTLY){
			return dimension;
		} else if (modo == MeasureSpec.AT_MOST){
			return Math.min(dimension, deseado);
		} else {
			return deseado;
		}
	}
}