package com.flop.test;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

import org.joda.time.DateTime;

public class Test {
	public static void main(String[] args) {
		DateTime lastUpdate = new DateTime();
		System.out.println(lastUpdate.toString("yyyy-MM-dd HH:mm:ss"));
	}
}
