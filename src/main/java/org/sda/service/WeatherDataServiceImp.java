package org.sda.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sda.dao.WeatherDataDao;
import org.sda.model.Location;
import org.sda.model.WeatherData;

import java.util.List;
import java.util.UUID;

import static org.sda.util.HibernateUtil.sessionFactory;

public class WeatherDataServiceImp implements WeatherDataDao {
    private WeatherDataDao weatherDataDao;
    public WeatherDataServiceImp(WeatherDataDao weatherDataDao){
        this.weatherDataDao=weatherDataDao;
    }
    @Override
    public WeatherData findById(UUID id) {
        return weatherDataDao.findById(id);
    }

    @Override
    public List<WeatherData> findByLocation(Location location) {
        return weatherDataDao.findByLocation(location);
    }
    public void addWeatherData(WeatherData weatherData){
        Session session= sessionFactory.openSession();
        Transaction tx=null;

        try {
            tx=session.beginTransaction();
            session.save(weatherData);
            tx.commit();
        }
        catch (Exception e){
            if (tx != null){
                tx.rollback();
            }
            throw e;
        }
        finally {
            session.close();
        }

    }


    @Override
    public void save(WeatherData weatherData) {
        weatherDataDao.save(weatherData);

    }

    @Override
    public void update(WeatherData weatherData) {
        weatherDataDao.update(weatherData);

    }

    @Override
    public void delete(WeatherData weatherData) {
        weatherDataDao.delete(weatherData);

    }
}
