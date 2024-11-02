package com.assaignment.weatherinfo.service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.assaignment.weatherinfo.dto.WeatherResponse;
import com.assaignment.weatherinfo.entity.Pincode;
import com.assaignment.weatherinfo.entity.WeatherData;
import com.assaignment.weatherinfo.exceptions.ExceptionFromOpenApi;
import com.assaignment.weatherinfo.repo.PincodeRepo;
import com.assaignment.weatherinfo.repo.WeatherDataRepo;

@Service
public class WeatherService {

	@Value("${openweather.api.key}")
	private String apiKey;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PincodeRepo pincodeRepo;

	@Autowired
	private WeatherDataRepo weatherDataRepo;

	private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

	private static final String GEO_API_URL = "https://api.openweathermap.org/data/2.5/weather?zip=%s&appid=%s";

	private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/3.0/onecall/timemachine?lat=%s&lon=%s&dt=%s&appid=%s";

	public Pincode getPincodeFromOpenApi(String pincode) {
		// fetching coordinates from open weathe api
		try {
			String url = String.format(GEO_API_URL, pincode + ",IN", apiKey);
			Map<String, Object> geoData = restTemplate.getForObject(url, Map.class);
			double latitude = (double) ((Map) geoData.get("coord")).get("lat");
			double longitude = (double) ((Map) geoData.get("coord")).get("lon");

			String city = (String) geoData.get("name");
			logger.info("co ordinates fetched from open weather API");
			return new Pincode(pincode, city, latitude, longitude);

		}
		catch(HttpClientErrorException ex) {
			logger.info("HttpClient Exception occoured");
			throw new HttpClientErrorException(ex.getStatusCode(),ex.getStatusText());
		}
		catch (Exception e) {
			logger.error("got unknown Exception ");
			throw new ExceptionFromOpenApi("Unknown error occurred while contacting open weather API for coordinates ");
		}

	}

	public Pincode getPinCodeDetails(String pincode) {

		Pincode zip = pincodeRepo.findById(pincode).orElse(null);
		// if pincode not in db then call open weath api
		
		if (zip == null) {
			logger.info("calling open wether geo coordinates api : ");
			zip = getPincodeFromOpenApi(pincode);
			logger.info("saving co ordinates into pincode");
			pincodeRepo.save(zip);
			return zip;
		}
		logger.info("No need to fetch from open weather api the co ordinates are present in db");
		return zip;
	}

	public WeatherData getWeatherReport(String pincode, LocalDate date) {

		Pincode zip = getPinCodeDetails(pincode);
		WeatherData weatherData = weatherDataRepo.findByPincodeAndDate(zip, date);
		if (weatherData == null) {
			logger.info("weather info not in db calling open weather api to fetch data");
			WeatherResponse weatherResponse = getWeatherReportByOpenApi(pincode, date);
			weatherData = new WeatherData();
			weatherData.setPincode(zip);
			weatherData.setDate(date);
			weatherData.setTemp(weatherResponse.getData().get(0).getTemp());
			weatherData.setHumidity(weatherResponse.getData().get(0).getHumidity());
			weatherData.setPressure(weatherResponse.getData().get(0).getPressure());
			weatherData.setMain(weatherResponse.getData().get(0).getWeather().get(0).getMain());
			weatherData.setDescription(weatherResponse.getData().get(0).getWeather().get(0).getDescription());
			logger.info("saving weather data into db");
			weatherDataRepo.save(weatherData);
			return weatherData;
		}
		logger.info("Weather info presnt in database");
		return weatherData;

	}

	public WeatherResponse getWeatherReportByOpenApi(String pinCode, LocalDate forDate) {

		Pincode zip = getPinCodeDetails(pinCode);
		double lat = zip.getLatitude();
		double lon = zip.getLongitude();
		long date = forDate.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
		try {

			String url = String.format(WEATHER_API_URL, lat, lon, date, apiKey);
			return restTemplate.getForObject(url, WeatherResponse.class);

		} catch (Exception e) {
			logger.error("got unknown Exception ");
			throw new ExceptionFromOpenApi(
					"Unknown error occurred while contacting open weather API for weather report ");
		}

	}

}
