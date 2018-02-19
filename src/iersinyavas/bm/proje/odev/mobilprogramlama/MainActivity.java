package iersinyavas.bm.proje.odev.mobilprogramlama;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener{
	
	// Kullan�lacak bile�enler tan�mlan�yor
	TextView textViewX, textViewY, textViewZ, durum;
	SensorManager sensorManager;
	BroadcastReceiver broadcastReceiver;
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//G�nderilen yay�n burada al�nmaktad�r.Telefon d�z m�, yan m�, veya dik mi bu belirtiliyor.
		if(broadcastReceiver == null)
			broadcastReceiver = new BroadcastReceiver() {
				
				@Override
				public void onReceive(Context context, Intent intent) {
					durum.setText(""+intent.getExtras().get("durum"));							
				}
			};
			
			registerReceiver(broadcastReceiver, new IntentFilter("Durumlar"));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//T�m bile�enlerin layout dosyas�ndaki idleri tan�mlan�yor.
		setContentView(R.layout.main_activity);
		textViewX = (TextView) findViewById(R.id.textViewX);
		textViewY = (TextView) findViewById(R.id.textViewY);
		textViewZ = (TextView) findViewById(R.id.textViewZ);
		durum = (TextView) findViewById(R.id.durum);
		
		//Proje i�in gerekli ivme sens�r� tan�mlamas� yap�l�yor.
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), sensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		Intent intent = new Intent("Durumlar");
		//Sens�r�n anl�k dinlenmesi durumunda textviewlere yazd�rmas� sa�lan�yor.
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			textViewX.setText("X Ekseni : "+Float.toString(event.values[0]));
			textViewY.setText("Y Ekseni : "+Float.toString(event.values[1]));
			textViewZ.setText("Z Ekseni : "+Float.toString(event.values[2]));
			
			//Projede birde cihaz�n titre�im �zelli�i kullan�l�yor.
			Vibrator titresim = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			
			//Telefon d�z, yan ve dik durmad��� zaman titreme yapmaktad�r.D�z zemin olmad��� zaman titreme yap�yor.
			//Bunun i�in gerekli ko�ul ifadeleri
			//Ayr�ca telefonun durumunu cihazda textview i�erisinde g�stermek i�in yay�n g�ndermektedir.
			if(((event.values[0]<1 && event.values[0]>-1)&&(event.values[1]<1 && event.values[1]>-1)&&(event.values[2]>9 || event.values[2]<-9)))
			{
				intent.putExtra("durum", "Telefon d�z duruyor");
				sendBroadcast(intent);
			}
			else if(((event.values[1]<1 && event.values[1]>-1)&&(event.values[2]<1 && event.values[2]>-1)&&(event.values[0]>9 || event.values[0]<-9)))
			{
				intent.putExtra("durum", "Telefon yan duruyor");
				sendBroadcast(intent);
			}
			else if(((event.values[0]<1 && event.values[0]>-1)&&(event.values[2]<1 && event.values[2]>-1)&&(event.values[1]>9 || event.values[1]<-9)))
			{
				intent.putExtra("durum", "Telefon dik duruyor");
				sendBroadcast(intent);
			}
			else titresim.vibrate(250);
			
		}
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

		
	}

	
}
