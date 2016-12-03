package com.yamankod.webservice_1_soap;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class SoapActivity extends Activity {
	private Button cevirbutton;
	private RadioButton myOption1, myOption2;
	private EditText valuText;
	private TextView result;
	private static final String NAMESPACE = "http://www.w3schools.com/xml/";
	private static final String SERVICEURL = "http://www.w3schools.com/xml/tempconvert.asmx";
	private final static String METHOD_NAME_C2F = "CelsiusToFahrenheit";
	private static final String SOAP_ACTION_C2F = "http://www.w3schools.com/xml/CelsiusToFahrenheit";
	private final static String METHOD_NAME_F2C = "FahrenheitToCelsius";
	private static final String SOAP_ACTION_F2C = "http://www.w3schools.com/xml/FahrenheitToCelsius";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		cevirbutton = (Button) findViewById(R.id.btncevir);
		valuText = (EditText) findViewById(R.id.txtValue);
		result = (TextView) findViewById(R.id.lblResult);
		myOption1 = (RadioButton) findViewById(R.id.radio0);
		myOption2 = (RadioButton) findViewById(R.id.radio1);
		cevirbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyAsyncTask task= new MyAsyncTask();	
				task.execute();	
			}
		});
	}


	public class MyAsyncTask extends AsyncTask<String, Void, String>{
		 private final ProgressDialog dialog = new ProgressDialog(SoapActivity.this);   
		 @Override
         protected void onPreExecute() {
                         this.dialog.setMessage("Kontrol ediliyor...");              
                          this.dialog.show();  
         }
		  @Override
          protected void onPostExecute(String sonuc) {
                          if (sonuc != null) {
                                          result.setText(sonuc);
                          } else {
                                          Toast.makeText(getApplicationContext(),
                                 "Result Found is ==  " + sonuc + "", Toast.LENGTH_LONG).show();
                          }
                          if (this.dialog.isShowing()) {
                                          this.dialog.dismiss();
                          }
                          super.onPostExecute(sonuc);
          }
		@Override
		protected String doInBackground(String... params) {
			String son = "";
			if (myOption1.isChecked()) {
				son= invokeConverToCelciusWs();
			}
			if (myOption2.isChecked()) {
				son =invokeConverToFahrenWs();
			}
			return son;
		}	
	};
	public String invokeConverToCelciusWs() {
		String responseValue = "";
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_C2F);
		request.addProperty("Celsius", valuText.getText().toString());
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapEnvelope.dotNet = true;
		soapEnvelope.setOutputSoapObject(request);
		HttpTransportSE ahtSE = new HttpTransportSE(SERVICEURL);
		try {
			ahtSE.call(SOAP_ACTION_C2F, soapEnvelope);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		try {
			responseValue = "Fahrenheit -> "
					+ (SoapPrimitive) soapEnvelope.getResponse();
		} catch (SoapFault e) {
			e.printStackTrace();
		}
		return responseValue;
	}
	public String invokeConverToFahrenWs() {
		String responseValue = "";

		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_F2C);

		request.addProperty("Fahrenheit", valuText.getText().toString());

		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapEnvelope.dotNet = true;
		soapEnvelope.setOutputSoapObject(request);


		HttpTransportSE aht = new HttpTransportSE(SERVICEURL);

		try {
			aht.call(SOAP_ACTION_F2C, soapEnvelope);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}

		try {
			responseValue = "Celcius -> "
					+ (SoapPrimitive) soapEnvelope.getResponse();
		} catch (SoapFault e) {
			e.printStackTrace();
		}
		return responseValue;
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Uygulamadan çıkmak istediğinize emin misiniz?")
					.setCancelable(false)
					.setPositiveButton("Evet",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									SoapActivity.this.finish();
								}
							})
					.setNegativeButton("Hayır",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}










