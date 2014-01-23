package com.howintsui.hexInfect;

//import android.util.Log;

public class Grid {
	private static final String TAG = Grid.class.getSimpleName();

	 int[][] grid;
	boolean[][] isMine;
	private int arr[][] ={{0,1},{0,-1},{1,0},{1,-1},{-1,0},{-1,-1}};
	private int arr2[][] ={{0,1},{0,-1},{1,0},{1,1},{-1,0},{-1,1}};
	Grid(int width, int height,int colors){
		grid = new int[width][height];
		isMine = new boolean[width][height];
		generate(colors);
		for (int i = 0 ; i < isMine.length; i++){
			for (int k = 0 ;  k < isMine[0].length; k++){
				isMine[i][k] = false;
			}
		}
		isMine[0][0] = true;
	}
	public boolean isOneColor(){
		int color = grid[0][0];
		for (int i = 0 ; i < isMine.length; i++){
			for (int k = 0 ;  k < isMine[0].length; k++){
				if (grid[i][k] != color){
					return false;
				}
			}
		}
		return true;
	}
	public void generate(int colors){
		for(int i =0;i<grid.length; i++){
			for (int j=0; j<grid[0].length; j++){
				grid[i][j] = (int) (Math.random()*colors);
			}
		}
	}

	public int getWidth(){
		return grid.length;
	}
	public int getHeight(){
		return grid[0].length;
	}
	public int getColor(int col,int row){
		return grid[col][row];
	}
	public boolean isColor(int color, int col, int row){
		return grid[col][row] == color;
	}
	public boolean isMine(int col, int row){
		return isMine[col][row];
	}
	public void changeColor(int color, int col, int row){
		grid[col][row] = color;
	}
	
	//This could have been done recursively, but it requires a rather deep stack for bigger boards, so I decided do it iteratively,
	//was only able to test on a Nexus4, but there seems to be no significant lag.
	public void spreadIsMine(){
		int color = grid[0][0];
		boolean spread = true;
		while (spread){
			spread = false;//on this iteration, we have not spread at all yet.
			//if this is still false at the end of the loop, it means there are no more spreadables,
			//and we can exit the loop.
			
			for (int row = 0 ; row < isMine[0].length; row++){
				for (int col = 0 ;  col < isMine.length; col++){
					//for each row and column
					
					if (isMine[col][row]){//if the current block is mine, check neighboring blocks for spreadables
						if (row < grid[0].length-1 && isColor(color,col,row+1)){//up 1
							if (!isMine[col][row+1]){spread = true;}
							isMine[col][row+1] = true;
						}
						if (row > 0 && isColor(color,col,row-1)){// down 1
							if (!isMine[col][row-1]){spread = true;}
							isMine[col][row-1] = true;
						}
						if (col < grid.length-1 && isColor(color,col+1,row)){//right
							if (!isMine[col+1][row]){spread = true;}
							isMine[col+1][row] = true;
						}
						if (col > 0 && isColor(color,col-1,row)){//left
							if (!isMine[col-1][row]){spread = true;}
							isMine[col-1][row] = true;
						}

						//depending on whether we are in a "down column" or an "up column", we need to check either up-left and up-right,
						//or down-left and down-right.
						if (col % 2 == 0){
							if (row > 0 && col > 0 && isColor(color,col-1,row-1)){
								if (!isMine[col-1][row-1]){spread = true;}
								isMine[col-1][row-1] = true;
							}
							if (col < grid.length-1 && row > 0 && isColor(color,col+1,row-1)){
								if (!isMine[col+1][row-1]){spread = true;}
								isMine[col+1][row-1] = true;
							} 
						} else {
							if (col > 0 && row < grid[0].length-1 && isColor(color,col-1,row+1)){
								if (!isMine[col-1][row+1]){spread = true;}
								isMine[col-1][row+1] = true;
							}
							if (row < grid[0].length-1 && col < grid.length-1 && isColor(color,col+1,row+1)){
								if (!isMine[col+1][row+1]){spread = true;}
								isMine[col+1][row+1] = true;
							}
						}
					}
				}
			}
		}
	}

	public void clickityClick(int color){
		for (int row = 0 ; row < isMine[0].length; row++){
			for (int col = 0 ;  col < isMine.length; col++){
				if (isMine[col][row]){
					grid[col][row] = color;
				}
			}
		}
	}
}
