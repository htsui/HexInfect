package com.howintsui.hexInfect;

import com.howintsui.hexInfect.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
	GameView gameView;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_main);
		gameView = (GameView) findViewById(R.id.view1);
    	View root = gameView.getRootView();
    	root.setBackgroundColor(0xFFE0E0E0);//
	}

	public void button1(View view){
		gameView.buttonPress(0);
	}
	public void button2(View view){
		gameView.buttonPress(1);
	}
	public void button3(View view){
		gameView.buttonPress(2);
	}
	public void button4(View view){
		gameView.buttonPress(3);
	}
	public void button5(View view){
		gameView.buttonPress(4);
	}
	public void button6(View view){
		gameView.buttonPress(5);
	}
	
	
	
	public static final int MENU_EXIT = Menu.FIRST;
	public static final int MENU_SIZE = Menu.FIRST + 1;
	public static final int MENU_RESTART = Menu.FIRST + 2;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		menu.add(Menu.NONE, MENU_SIZE,Menu.NONE,"Board Size");
		menu.add(Menu.NONE, MENU_RESTART,Menu.NONE,"Restart");
		menu.add(Menu.NONE, MENU_EXIT,Menu.NONE,"Exit");
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId())
        {
            case MENU_EXIT:
                finish();
                return true;
            case MENU_RESTART:
            	gameView.init();
            	return true;
            case MENU_SIZE:
            	if (gameView.boardSize == 2){
            		gameView.boardSize = 0;
            	} else {
            		gameView.boardSize ++;
            		
            	}
            	gameView.init();
            	return true;
            default:
                  return super.onOptionsItemSelected(item);
        }
	}

}
