package aneem.drumMachine;

import java.util.HashMap;
import java.util.Random;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

public class DrumMachineView extends View{
	SoundPool database = new SoundPool(6,AudioManager.STREAM_MUSIC,0);

	String getString = null;

	HashMap<Point, Integer> pointMap = new HashMap<Point, Integer>();

	int height; 
	int width; 
	int widthIncrement;
	int heightIncrement;
	int screenFactor;

	float x = 0;
	float y = 0;

	private Paint paint;
	private Paint backgroundPaint;
	public DrumMachineView(Context context) {
		super(context);
		paint = new Paint();
		backgroundPaint = new Paint();
		paint.setColor(Color.GREEN);
		paint.setAntiAlias(true);
		paint.setTextSize(25);

	}

	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		canvas.drawLine(0, (height/2), width, (height/2), paint);
		canvas.drawLine((width/3), 0, (width/3), height, paint);
		canvas.drawLine((2*(width/3)), 0, (2*(width/3)), height, paint);
	}


	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		screenFactor = (int)((height*width)/6);

		//have to add all points to the hash map
		widthIncrement = (int)(width/3);
		heightIncrement = (int)(height/2);

		pointMap.put(new Point(0,0), database.load(getContext(),R.raw.clv,1));
		pointMap.put(new Point(widthIncrement,0), database.load(getContext(),R.raw.pedalhh,1));
		pointMap.put(new Point((widthIncrement*2),0), database.load(getContext(),R.raw.processedshake,1));
		pointMap.put(new Point(0,heightIncrement), database.load(getContext(),R.raw.analoghat,1));
		pointMap.put(new Point(widthIncrement,heightIncrement), database.load(getContext(),R.raw.kick,1));
		pointMap.put(new Point((widthIncrement*2),heightIncrement), database.load(getContext(),R.raw.snare,1));

	}

	//TODO: add more kits with the menu
	public boolean onTouchEvent(final MotionEvent ev) {
		if(ev.getAction()==ev.ACTION_DOWN||ev.getActionMasked()==ev.ACTION_POINTER_DOWN){					
			AudioManager mgr = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
			int streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
			PointF[] touchPoints = new PointF[ev.getPointerCount()];
			for(int i = 0; i < ev.getPointerCount(); i++){
				touchPoints[i] = new PointF(ev.getX(i),ev.getY(i));
			}
			for(final PointF point : touchPoints){
				x = ev.getX(ev.getActionIndex());
				y = ev.getY(ev.getActionIndex());
				int ptx = (int) (x - x%widthIncrement);
				int pty = (int) (y - y%heightIncrement);
				playSound(pointMap.get(new Point(ptx,pty)));
			}
			return true;
		}
		return true;
	}

	public void playSound(int sound){
		AudioManager mgr = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
		int streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		database.play(sound, streamVolume, streamVolume, 1, 0, 1);
	}


}
