package aneem.drumMachine;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Window;
import android.view.WindowManager;

public class DrumMachineActivity extends Activity {
    /** Called when the activity is first created. */
	private PowerManager.WakeLock wl;

    
    DrumMachineView t;
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	super.onCreate(savedInstanceState);
    	t = new DrumMachineView(this);
    	//t.setClickable(true);
        setContentView(t);
        
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
    }
    
    @Override
    protected void onPause() {
            super.onPause();
            wl.release();
    }

    @Override
    protected void onResume() {
            super.onResume();
            wl.acquire();
    }
    

}