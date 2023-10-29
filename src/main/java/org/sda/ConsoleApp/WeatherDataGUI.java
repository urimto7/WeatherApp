package org.sda.ConsoleApp;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONObject;
import org.sda.dao.LocationDao;
import org.sda.dao.LocationDaoImplements;
import org.sda.dao.WeatherDataDao;
import org.sda.dao.WeatherDataDaoImplements;
import org.sda.model.Location;
import org.sda.model.WeatherData;
import org.sda.service.LocationService;
import org.sda.service.LocationServiceImp;
import org.sda.service.WeatherDataService;
import org.sda.service.WeatherDataServiceImp;
import org.sda.util.HibernateUtil;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class WeatherDataGUI {
    private JFrame frame;
    private LocationService locationService;
    private WeatherDataService weatherDataService;
    private JTextField cityField;
    private String apiKey = "a475956f51851932d5dd2b66dce5f692";

    public WeatherDataGUI() {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        LocationDao locationDao = new LocationDaoImplements(factory);
        locationService = new LocationServiceImp(locationDao);

        WeatherDataDao weatherDataDao = new WeatherDataDaoImplements(factory);
        weatherDataService = new WeatherDataServiceImp(weatherDataDao);

        frame = new JFrame("Weather Data App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel locationPanel = createLocationPanel();
        JPanel displayPanel = createDaisplayPanel();
        JPanel downloadPanel = createDownloadPanel();

        tabbedPane.add("Add Location", locationPanel);
        tabbedPane.add("Display Location", displayPanel);
        tabbedPane.add("Download Wather Data", downloadPanel);

        frame.add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createLocationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JTextField latitudeField = new JTextField();
        JTextField longitudeField = new JTextField();
        JTextField regionField = new JTextField();
        JTextField countryField = new JTextField();

        JButton addButton = new JButton("Add Location");

        addButton.addActionListener(e -> {
            try {
                double latitude = Double.parseDouble(latitudeField.getText());
                double longitude = Double.parseDouble(longitudeField.getText());
                String region = regionField.getText();
                String country = countryField.getText();
                Location newLocation = new Location(latitude, longitude, region, country);

                locationService.addLocation(newLocation);
                JOptionPane.showMessageDialog(frame, "Location added succesfully.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Input. Please enter valid latitude and longitute");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error adding location" + ex.getMessage());
            }
        });
        panel.add(new JLabel("Lattitude:"));
        panel.add(latitudeField);
        panel.add(new JLabel("Longitude:"));
        panel.add(longitudeField);
        panel.add(new JLabel("Region:"));
        panel.add(regionField);
        panel.add(new JLabel("Country:"));
        panel.add(countryField);
        panel.add(addButton);

        return panel;

    }

    private JPanel createDaisplayPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());


        List<Location> locations = locationService.getAllLocations();


        DefaultListModel<Location> listModel = new DefaultListModel<>();
        for (Location location : locations) {
            listModel.addElement(location);
        }
        JList<Location> locationList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(locationList);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;


    }

    private JPanel createDownloadPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        cityField = new JTextField();
        JButton downloadButton = new JButton("Download Weather Data");
        WeatherPanel weatherPanel = new WeatherPanel();

        downloadButton.addActionListener(e -> {
            String city = cityField.getText();
            WeatherData weatherData = retrieveWeatherDataFromAPI(city, apiKey);
            if (weatherData != null) {
                weatherPanel.updateWeatherData(weatherData);
                weatherDataService.addWeatherData(weatherData);
                JOptionPane.showMessageDialog(frame, "Weather Data added sucsesfully");
            } else {
                JOptionPane.showMessageDialog(frame, "Faild to retreive Weather data");
            }

        });
        panel.add(new JLabel("City"));
        panel.add(cityField);
        panel.add(downloadButton);

        panel.add(weatherPanel);

        return panel;
    }
    public WeatherData retrieveWeatherDataFromAPI(String city, String apiKey) {
        int responseCode = 0;
        try {
            String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

            // Open a connection to the URL
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method to GET
            connection.setRequestMethod("GET");

            // Get the response code
            responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                // Read the response from the API

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }
                reader.close();

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                double temperatureInKelvin = jsonResponse.getJSONObject("main").getDouble("temp");
                double temperatureInCelsius = temperatureInKelvin - 273.15; // Convert to Celsius

                double pressure = jsonResponse.getJSONObject("main").getDouble("pressure");
                double humidity = jsonResponse.getJSONObject("main").getDouble("humidity");
                double windSpeed = jsonResponse.getJSONObject("wind").getDouble("speed");
                double windDirection = jsonResponse.getJSONObject("wind").getDouble("deg");

                // Create a WeatherData object and populate it
                WeatherData weatherData = new WeatherData();
                weatherData.setTemperature(temperatureInCelsius); // Use the converted temperature in Celsius
                weatherData.setPressure(pressure);
                weatherData.setHumidity(humidity);
                weatherData.setWindspeed(windSpeed);
                weatherData.setWindDirection(windDirection);

                return weatherData;
            } else {
                System.out.println("Failed to retrieve weather data. HTTP error code: " + responseCode);
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while fetching weather data.");
            System.out.println("HTTP error code: " + responseCode);
        }

        return null; // Return null if retrieval fails
    }


    public void display() {
        frame.setVisible(true);
    }
}
