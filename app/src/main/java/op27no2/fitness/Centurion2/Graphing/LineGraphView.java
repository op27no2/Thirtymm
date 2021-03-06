package op27no2.fitness.Centurion2.Graphing;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import java.util.Calendar;

import op27no2.fitness.Centurion2.Graphing.GraphViewSeries.GraphViewSeriesStyle;


/**
 * Line Graph View. This draws a line chart.
 */
public class LineGraphView extends GraphView {
	private final Paint paintBackground;
	private boolean drawBackground;
	private boolean drawDataPoints;
	private float dataPointsRadius = 10f;
	public String colorflag;
	public double sum = 0;
	public double xAxixMax;
	public double xAxisMin;
	public int daycount;
	public int day;
	public SharedPreferences prefs;
	public SharedPreferences prefs2;
	public int dayset;

	public int listsize;
	public String aTitle = "";
	
	public LineGraphView(Context context, AttributeSet attrs) {
		super(context, attrs);

		paintBackground = new Paint();
		paintBackground.setColor(Color.rgb(20, 40, 60));
		paintBackground.setStrokeWidth(4);
		paintBackground.setAlpha(128);
	}

	public LineGraphView(Context context, String title, String activeTitle) {
		super(context, title);
		aTitle = activeTitle;
		SharedPreferences prefs3 = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE);
	    int widgetId = prefs3.getInt("currentId", 2);
		
	    prefs = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE);
	    //prefs2 = PreferenceManager.getDefaultSharedPreferences(context);
		
	    paintBackground = new Paint();
		paintBackground.setColor(Color.rgb(20, 40, 60));
		paintBackground.setStrokeWidth(4);
		paintBackground.setAlpha(128);
	}

	public void drawSeries(Canvas canvas, GraphViewDataInterface[] values, float graphwidth, float graphheight, float border, double minX, double minY, double diffX, double diffY, float horstart, GraphViewSeriesStyle style) {
		// draw background
		listsize = prefs.getInt("datasize", 1);

		double lastEndY = 0;
		double lastEndX = 0;
		sum = 0;
		// draw data
		paint.setStrokeWidth(style.thickness);
		paint.setColor(style.color);
		xAxixMax = minX+diffX;
		xAxisMin = minX;

		
		Path bgPath = null;
		if (drawBackground) {
			bgPath = new Path();
		}
		
		double valyzero = 0 - minY;
		double ratyzero= valyzero / diffY;
		double yz = graphheight * ratyzero;
		float yzero = (float) (border - yz) + graphheight;

		double valxzero = 0;
		double ratxzero= valxzero / diffX;
		double xz = graphwidth * valxzero;
		float xzero = (float) xz + (horstart + 1);

		lastEndY = 0;
		lastEndX = 0;
		double pyY=0;
		double pxX = 0;
		float prevX = xzero;
		float prevY = yzero;

		
		float firstX = 0;
		for (int i = 0; i < values.length; i++) {
			double dataY = values[i].getY();
			double valY = values[i].getY() - minY;
			double ratY = valY / diffY;
			double y = graphheight * ratY;
			
			double dataX = values[i].getX();
			double valX = values[i].getX() - minX;
			double ratX = valX / diffX;
			double x = graphwidth * ratX;
			
			if (i > 0) {
				float startX = (float) lastEndX + (horstart + 1);
				float startY = (float) (border - lastEndY) + graphheight;
				float endX = (float) x + (horstart + 1);
				float endY = (float) (border - y) + graphheight;
				
				if (bgPath != null) {
					if (i==1) {
						bgPath.moveTo(xzero, yzero);
						bgPath.lineTo(xzero, (endX/(endX-startX))*(startY-endY)+endY);
						// canvas.drawLine(xzero, yzero, xzero, startY, paint);
					}
				}

				//if previous value is below line, less than zero
				//dataY is the actual value from dataset, so looking at previous value and current value
				if(values[i-1].getY()<0 && dataY >= 0){
					//x point at intercept
					double interx = (-pyY/((dataY-pyY)/(dataX-pxX)))+pxX;
					//subtract minimum x to get distance from minimum x, to get value x
					double valiX = interx - minX;
					//ratio of intercept on screen, value x out of values on screen
					double ratiX = valiX / diffX;
					//fraction of distance times width of graph = position of crossover
					double ix = graphwidth * ratiX;


					double valiY = 0 - minY;
					double ratiY = valiY / diffY;
					double iy = graphheight * ratiY;
					
					float endiX = (float) ix + (horstart + 1);
					float endiY = (float) (border - iy) + graphheight;

					//paints up to zero as red, endix endiy is crossing zero point
					paint.setColor(Color.rgb(255,0,0));
					if(aTitle.equals("Grams")){
						paint.setColor(style.color);
					}
					canvas.drawLine(startX, startY, endiX, endiY, paint);

					//paints from crossing point up to the endX endY, the next point above zero.
					paint.setColor(Color.rgb(0, 255, 0));
					if(aTitle.equals("Grams")){
						paint.setColor(style.color);
					}
					canvas.drawLine(endiX, endiY, endX, endY, paint);
					
					//paint fill color
					paintBackground.setColor(Color.argb(45, 255, 0, 0));
					if (bgPath != null) {
						bgPath.lineTo(endiX, endiY);
						bgPath.lineTo(prevX, prevY);
						bgPath.close();
					}
					prevX = endiX;
					prevY = endiY;
					if (bgPath != null) {
						canvas.drawPath(bgPath, paintBackground);
					}
					if (drawBackground) {
						//shade background the color above, if not its like yellow or some shit?
						bgPath = new Path();
					}
					if (bgPath != null) {
						bgPath.moveTo(endiX, endiY);
					}
					colorflag = "green";
				
					
				}

				//drawing above
				else if(values[i-1].getY()>0 && dataY <= 0){
					double interx = (-pyY/((dataY-pyY)/(dataX-pxX)))+pxX;
					double valiX = interx - minX;
					double ratiX = valiX / diffX;
					double ix = graphwidth * ratiX;
					
					double valiY = 0 - minY;
					double ratiY = valiY / diffY;
					double iy = graphheight * ratiY;
					
					
					float endiX = (float) ix + (horstart + 1);
					float endiY = (float) (border - iy) + graphheight;
					
					paint.setColor(Color.rgb(0,255,0));
					if(aTitle.equals("Grams")){
						paint.setColor(style.color);
					}
					canvas.drawLine(startX, startY, endiX, endiY, paint);
					
					paint.setColor(Color.rgb(255, 0, 0));
					if(aTitle.equals("Grams")){
						paint.setColor(style.color);
					}
					canvas.drawLine(endiX, endiY, endX, endY, paint);
					
					paintBackground.setColor(Color.argb(45 , 0, 255, 0));
					if (bgPath != null) {
						bgPath.lineTo(endiX, endiY);
						bgPath.lineTo(prevX, prevY);
						bgPath.close();
					}
					prevX = endiX;
					prevY = endiY;
					if (bgPath != null) {
						canvas.drawPath(bgPath, paintBackground);
					}
					if (drawBackground) {
						bgPath = new Path();
					}
					if (bgPath != null) {
						bgPath.moveTo(endiX, endiY);
					}
					colorflag="red";
				}
				//if its not a split case like above, its just a normal above or below, red or green.
				else if(dataY < 0){
				paint.setColor(Color.rgb(255, 0, 0));
					if(aTitle.equals("Grams")){
						paint.setColor(style.color);
					}
				canvas.drawLine(startX, startY, endX, endY, paint);
				}
				//if its not a split case like above, its just a normal above or below, red or green.
				else{
				paint.setColor(style.color);
					if(aTitle.equals("Grams")){
						paint.setColor(style.color);
					}
				canvas.drawLine(startX, startY, endX, endY, paint);
				}



				// draw data point, fresh Cal each iteration
		        Calendar iterateCal = Calendar.getInstance();
				System.out.println("today check? "+iterateCal.get(Calendar.MONTH));
		   //     dayset = Integer.parseInt(prefs.getString("tapnum2", "1"));

				//find todays weekday, which is last displayed data point. Then subtract backwards as we move back from that data point dataX are the X data points (e.g. 0 to 30?)
				int weekday = iterateCal.get(Calendar.DAY_OF_WEEK) - (listsize - (int) dataX);

				//if
				if(values.length < 10){
					weekday = dayset;
				}
				if(weekday < 0){
					if(values.length < 10){
						weekday = dayset;
					}
					for(int k=1; k<25; k++){
						if(values.length < 45*k){
							weekday = (int) (weekday + Math.ceil(Math.abs(weekday/(7*k)))*(7*k));
						}
					}

				}
				
				int day = iterateCal.get(Calendar.DAY_OF_YEAR) ;
				//TODO Change this back to +2 if doesn't help?

				//list of 50, displaying 30, today is 283, setting to (283 - (50-0)+1)
				iterateCal.set(Calendar.DAY_OF_YEAR, day - (listsize - (int) dataX)+1);
				System.out.println("today check2? "+iterateCal.get(Calendar.MONTH)+" listize:"+listsize+"datax: "+dataX);

				int dayofmonth = iterateCal.get(Calendar.DAY_OF_MONTH);
				int month = iterateCal.get(Calendar.MONTH)+1;
				int year = iterateCal.get(Calendar.YEAR);

		        if (weekday < 0){
		        dayset = -(6-((int) Float.parseFloat(prefs.getString("tapnum2", "3"))-1));
		        }
		        if (weekday > 0){
			        dayset = (((int) Float.parseFloat(prefs.getString("tapnum2", "3"))));
				}


				//Draw all the data points
				if (drawDataPoints) {
					//fix: last value was not drawn. Draw here now the end values
					canvas.drawCircle(endX, endY, dataPointsRadius, paint);

				//	if(weekday == dayset){
						paint.setStrokeWidth(1);
							double sign = 0;
							//TODO provide better algorithm to thin values during zoom out
						if(values.length > 9) {
						/*		for(int j=0; j<9; j++){
									if(i+j < values.length){
									sign = sign + values[(i+j)].getY();
									}
									else{
									sign = values[i].getY();
									}
								}
								if(sign>=0){
								paint.setColor(Color.rgb(0, 255, 0));
								}
								if(sign<0){
									paint.setColor(Color.rgb(255, 0, 0));
								}
								if(aTitle.equals("Grams")){
									paint.setColor(style.color);
								}*/

							Double divis = values.length / 7.0;

							if (i % Math.floor(divis) == 0) {

										sign = values[i].getY();
										if (sign >= 0) {
											paint.setColor(Color.rgb(0, 255, 0));
										}
										if (sign < 0) {
											paint.setColor(Color.rgb(255, 0, 0));
										}
										if (aTitle.equals("Grams")) {
											paint.setColor(style.color);
										}
										canvas.drawLine(endX, endY + 15, endX, graphheight + 45, paint);
										canvas.drawText(month + "-" + dayofmonth, endX, graphheight + 80, paint);
							}



						}
						//if values not > 9 just display all
									if (values.length < 10) {
										System.out.println("data under ten " + values.length);

										sign = values[i].getY();
										if (sign >= 0) {
											paint.setColor(Color.rgb(0, 255, 0));
										}
										if (sign < 0) {
											paint.setColor(Color.rgb(255, 0, 0));
										}
										if (aTitle.equals("Grams")) {
											paint.setColor(style.color);
										}

										canvas.drawLine(endX, endY + 15, endX, graphheight + 45, paint);
										canvas.drawText(month + "-" + dayofmonth, endX, graphheight + 80, paint);
									}

									paint.setStrokeWidth(style.thickness);

				//	}
				}

				//
				if (bgPath != null) {
					//removing this removes backgrounds
					bgPath.lineTo(endX, endY);
				}
				
				
			} else if (drawDataPoints) {
				//fix: last value not drawn as datapoint. Draw first point here, and then on every step the end values (above)
				float first_X = (float) x + (horstart + 1);
				float first_Y = (float) (border - y) + graphheight;
				canvas.drawCircle(first_X, first_Y, dataPointsRadius, paint);
			}
			
			//if this is the last one we need to close our last triangle to fill
			if (bgPath != null) {

				if (i == (values.length - 1)) {
					float endX = (float) x + (horstart + 1);
					float endY = (float) (border - y) + graphheight;
					bgPath.lineTo(endX, yzero);
					bgPath.lineTo(prevX, prevY);
					bgPath.close();
					if (dataY > 0) {
						paintBackground.setColor(Color.argb(45, 0, 225, 0));
					}
					if (dataY < 0) {
						paintBackground.setColor(Color.argb(45, 255, 0, 0));
					}
					canvas.drawPath(bgPath, paintBackground);

				}
			}
			
			
			lastEndY = y;
			lastEndX = x;
			pyY = dataY;
			pxX = dataX;
			
			//add all values that aren't the first or last, which can be "off the page"
			if(i>0 && i<values.length-1){
			sum = sum+ dataY;
			}

			//if first or last are on the page, actually add them
			if(i == values.length -1){
				if(xAxixMax == dataX || xAxixMax > dataX){
					sum = sum+dataY;
				}
			}
			if(i == 0){
				if(xAxisMin == dataX){
					sum = sum+dataY;
				}
			}




		//ends the loop for each data value
		}

		//checks sum of values and draws calories red or green + or -
		String units = prefs.getString("timeout", "Pounds");
		System.out.println("LineUnits:"+units);
		for(int j=0; j<values.length;j++){
			System.out.println("Y test "+j+": "+values[j].getY());
		}


		int sum1 = (int) Math.floor(sum);
		int sum2 = (int) Math.floor(sum/350);
		double sum3 = (double) sum2/10;
		if (units.equals("Kilograms")){
			sum3 = sum3/2;
		}
		
		if (sum>=0){
			paint.setColor(Color.rgb(0, 255, 0));
		}
		else{
			paint.setColor(Color.rgb(255, 0, 0));
		}


		float hold = paint.getTextSize();
		paint.setTextSize(80);

		//if Cals, display pounds conversion
		if(aTitle.equals("Cals")) {
			canvas.drawText(Integer.toString(sum1) + " " + aTitle + " = " + Double.toString(sum3) + " " + units, graphwidth / 2, graphheight / 8, paint);
		}
		//if Macro display Grams
		if(aTitle.equals("Grams")){
			paint.setColor(Color.rgb(0, 0, 255));
			canvas.drawText(Integer.toString(sum1) + " " + aTitle, graphwidth / 2, graphheight / 8, paint);
		}


		paint.setTextSize(hold);
		/*if (bgPath != null) {
			// end / close path
			bgPath.lineTo((float) lastEndX, graphheight + border);
			bgPath.lineTo(firstX, graphheight + border);
			bgPath.close();
			canvas.drawPath(bgPath, paintBackground);
		}*/
	}

	public int getBackgroundColor() {
		return paintBackground.getColor();
	}

	public float getDataPointsRadius() {
		return dataPointsRadius;
	}

	public boolean getDrawBackground() {
		return drawBackground;
	}

	public boolean getDrawDataPoints() {
		return drawDataPoints;
	}

	/**
	 * sets the background color for the series.
	 * This is not the background color of the whole graph.
	 * @see #setDrawBackground(boolean)
	 */
	@Override
	public void setBackgroundColor(int color) {
		paintBackground.setColor(color);
	}

	/**
	 * sets the radius of the circles at the data points.
	 * @see #setDrawDataPoints(boolean)
	 * @param dataPointsRadius
	 */
	public void setDataPointsRadius(float dataPointsRadius) {
		this.dataPointsRadius = dataPointsRadius;
	}

	/**
	 * @param drawBackground true for a light blue background under the graph line
	 * @see #setBackgroundColor(int)
	 */
	public void setDrawBackground(boolean drawBackground) {
		this.drawBackground = drawBackground;
	}

	/**
	 * You can set the flag to let the GraphView draw circles at the data points
	 * @see #setDataPointsRadius(float)
	 * @param drawDataPoints
	 */
	public void setDrawDataPoints(boolean drawDataPoints) {
		this.drawDataPoints = drawDataPoints;
	}

}