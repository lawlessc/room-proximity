package com.estimote.examples.demos;

public class SplashActivity {
	int counter = 0;
	while(counter < 1300) {
	    try {
	            Thread.sleep(1000);
	        }catch(InterruptedException e) {
	            e.printStackTrace();
	        }
	        counter+=100;
	    }

	    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
	    startActivity(intent);
}
