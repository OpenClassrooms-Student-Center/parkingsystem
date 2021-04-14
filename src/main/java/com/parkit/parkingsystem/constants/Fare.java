package com.parkit.parkingsystem.constants;

public class Fare {
	public static final double BIKE_RATE_PER_HOUR = 1.0;
	public static final double CAR_RATE_PER_HOUR = 1.5;

	public static double roundedFare(double price) {
		return (double) Math.round(price * 100) / 100;
	}

}
