package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.model.City;
import com.coolweather.app.model.Country;
import com.coolweather.app.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {
	/*
	 * ���ݿ���
	 */
	public static final String DB_NAME = "cool_weather";
	
	/*
	 * ���ݿ�汾
	 */
	public static final int VERSION = 1;
	
	private static CoolWeatherDB coolWeatherDB;
	
	private SQLiteDatabase db;
	
	//�����췽������Ϊ˽��
	private CoolWeatherDB(Context context){
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	//ʹ�õ���ģʽ ��ȡCoolWeatherDB
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolWeatherDB == null){
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	
	
	//��Provinceʵ���洢�����ݿ�
	public void saveProvince(Province province){
		if(province != null){
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code",	province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}
	
	//�����ݿ��ȡȫ������ʡ����Ϣ
	public List<Province> loadProvince(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		//cursor.moveToFirst()��������This method will return false if the cursor is empty. 
		if(cursor.moveToFirst()){
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));	//getColumnIndex��ȡ�е�����,����������ȡ��Ӧ��ֵ
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			} while (cursor.moveToNext());
		}
		
		return list;
	}
	
	//��Cityʵ�����浽���ݿ���
	public void saveCity(City city){
		ContentValues values = new ContentValues();
		values.put("city_name", city.getCityName());
		values.put("city_code", city.getCityCode());
		values.put("province_id", city.getProvinceId());
		db.insert("City", null, values);
	}
	//����ĳʡ�µ����е���
	public List<City> loadCity(String provinceId){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("city", null, "province_id=?", new String[]{String.valueOf(provinceId)}, null, null, null);
		
		if(cursor.moveToFirst()){
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));		
				list.add(city);
			} while (cursor.moveToNext());
		}
				
		return list;
	}
	
	//����Country
	public void saveCountry(Country country){
		ContentValues values = new ContentValues();
		values.put("country_code", country.getCountryCode());
		values.put("country_name", country.getCountryName());
		values.put("city_id", country.getCityId());
		db.insert("Country", null, values);
	}
	
	//��ȡĳ�����µ�������
	public List<Country> loadCountry(String cityId){
		List<Country> list = new ArrayList<Country>();
		Cursor cursor = db.query("Country", null, "city_id=?", new String[]{String.valueOf(cityId)}, null, null, null);
		if(cursor.moveToFirst()){
			do {
				Country country = new Country();
				country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
				country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
				country.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
				list.add(country);				
			} while (cursor.moveToNext());
		}
		return list;
	}
	
}
