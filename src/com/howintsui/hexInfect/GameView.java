package com.howintsui.hexInfect;

import com.howintsui.hexInfect.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

public class GameView extends View{
	int maxTurns;
	int currentTurn;
	int boardSize = 0;
	int colors[];
	float hexWidth;
	float hexHeight;
	int col,rows;
	int numColors;
	float leftBuffer;
	float topBuffer;
	float heightBuffer = 2f;
	float widthBuffer = 2f;
	boolean heightBias;
	boolean widthBias;
	TextView scoreBoard;//(TextView)host.findViewById(R.id.textView1);
	TextView size;
	
    boolean initted = false;
    Grid grid;
    Paint paintBrushes[];
	float boardSizes[][];//3 boards, each needing 3 space of information
	//first [] represents which board
	//second [0] represents # of cols, [1] # of rows, [2] # of turns allowed 
    
    void init(){

		boardSizes = new float[3][3];
		boardSizes[0][0] = 17;//small
		boardSizes[0][1] = 9;
		boardSizes[0][2] = 20;
		boardSizes[1][0] = 25;//med
		boardSizes[1][1] = 15;
		boardSizes[1][2] = 30;
		boardSizes[2][0] = 30;//large
		boardSizes[2][1] = 18;
		boardSizes[2][2] = 45;
		
		setBackgroundColor(Color.DKGRAY);
		
		col = (int) boardSizes[boardSize][0];
		rows = (int) boardSizes[boardSize][1];
		numColors = 6;
		currentTurn = 1;
		maxTurns = (int) boardSizes[boardSize][2];
		
		
		colors = new int[numColors];
		colors[0] = Color.BLACK;
		colors[1] = Color.GREEN;
		colors[2] = Color.MAGENTA;
		colors[3] = Color.RED;
		colors[4] = Color.BLUE;
		colors[5] = Color.GRAY;
		
		paintBrushes = new Paint[numColors];
		for (int i = 0; i < paintBrushes.length; i++){
			paintBrushes[i] = new Paint();
			paintBrushes[i].setAntiAlias(true);
			paintBrushes[i].setColor(colors[i]);
			paintBrushes[i].setStyle(Style.FILL);
		}
		//create paintbrushes for each color, saves on processing rather than making a new brush on the fly each time
		

		
		grid = new Grid(col,rows,numColors);
		initted = false;
		
		invalidate();
		
    }

    @Override
    protected void onDraw(Canvas canvas){
    	if (!initted){
    		hexWidth = (float) ((1.33f)*(getWidth()-widthBuffer*grid.getWidth())/(grid.getWidth()));
    		//calculate width of hexagon, pretty sure this isn't an ideal formula, but it works
    		hexHeight = ((getHeight()-heightBuffer*grid.getHeight())/(grid.getHeight()+3f));
    		//calculate ideal height of hexagon, based on how many can fit on the screen, +3 hexagons for margin space
    		scoreBoard = (TextView)this.getRootView().findViewById(R.id.textView1);
    		size = (TextView)this.getRootView().findViewById(R.id.textView2);
    		//update scoreboard/boardsize text
    		leftBuffer = (getWidth() - (2f*hexWidth/3f + widthBuffer) * (grid.getWidth()))/2f;//(hexWidth*.6f);
    		//calculate the left margin, based on hexagon sizes and width of board.
    		topBuffer = (getHeight() - (hexHeight+heightBuffer)*grid.getHeight())/1.5f;
    		//calculate top buffer, /1.5f because we want a little more space at the top than bottom, for aesthetics and space for text
    		scoreBoard.setText("Turn: " + currentTurn + "/" + maxTurns);
    		
    		if (boardSize == 0){
    			size.setText("Board size: Small");
    		} else if (boardSize == 1){
    			size.setText("Board size: Medium");
    		} else {
    			size.setText("Board size: Large");
    		}
    		initted = true;
    	}

    	drawAll(canvas, grid);
    	//draw dem hexagons


    }
    
    public void buttonPress(int color){

		grid.spreadIsMine();
		//propogate "owned" tiles
		grid.clickityClick(color);
		//update the colors of newly owned tiles (this is a stupid way to code, and should be fixed)
		
		currentTurn++;
		if (grid.isOneColor()){
			cleanPopup("Congratulations!","You beat the game in only "+currentTurn+" moves!  A new game has been started for you.");
			init();
		}
		if (currentTurn > maxTurns){
			cleanPopup("Oops!","You ran out of turns and lost!  A new game has been started for you.");
			init();
		}
		
		scoreBoard.setText("Turn: "+ currentTurn + "/" + maxTurns);

		invalidate();
    }

   
	@SuppressWarnings({ "deprecation"})
	private void cleanPopup(String title, String message){
		AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {
		   }
		});

		alertDialog.show();
	}
	private void drawAll(Canvas canvas, Grid grid){
		float width = hexWidth;//this is probably a waste of processing, but I like the prettiness.
		float height = hexHeight;

		float x0 = leftBuffer + 0f;
		float x1 = leftBuffer + width/3f;
		float x2 = leftBuffer + width/3f*2f;
		float x3 = leftBuffer + width;
		float y0 = topBuffer + 0f;
		float y1 = topBuffer + height/2f;
		float y2 = topBuffer + height;
		//initiate the values of x and y for points of the hexagon we are currently drawing (0,0)
		//a hexagon can be represented by 4 x and 3 y values
		

		for (int i = 0; i < grid.getWidth(); i++){
			y0 = topBuffer + 0f;
			y1 = topBuffer + height/2f;
			y2 = topBuffer + height;
			//at the beginning of each column, reset y values
			if (i%2 == 1){
				//on the odd columns, hexagons need to be shifted 1/2 down
				y0 +=  height/2f;
				y1 +=  height/2f;
				y2 +=  height/2f;
			}
			
			for (int j = 0; j < grid.getHeight(); j++){
				drawPoly(canvas, grid.getColor(i, j),
						new Point[]{
						new Point(x0,y1),//left
						new Point(x1,y0),//topleft
						new Point(x2,y0),//topright
						new Point(x3,y1),//right
						new Point(x2,y2),//botright
						new Point(x1,y2)//botleft
				});
				
				//increment height by height of hexagon + bufferspace in between hexagons
				y0 += height + heightBuffer;
				y1 += height + heightBuffer;
				y2 += height + heightBuffer;
			}
			//increment width by (2/3)*width + bufferspace.  2/3 because hexagons overlap each other 1/3
			x0 += 2f*width/3f + widthBuffer;
			x1 += 2f*width/3f + widthBuffer;
			x2 += 2f*width/3f + widthBuffer;
			x3 += 2f*width/3f + widthBuffer;
		}		
	}

	private void drawPoly(Canvas canvas, int color, Point[] points) {
		// line at minimum...
		if (points.length < 2) {
			return;
		}

		Path polyPath = new Path();
		polyPath.moveTo(points[0].x, points[0].y);
		int i, len;
		len = points.length;
		for (i = 0; i < len; i++) {
			polyPath.lineTo(points[i].x, points[i].y);
		}
		polyPath.lineTo(points[0].x, points[0].y);

		// draw
		canvas.drawPath(polyPath, paintBrushes[color]);
	}
	
	public GameView(Context context) {
		super(context);
		init();
	}
    public GameView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }
    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
}
