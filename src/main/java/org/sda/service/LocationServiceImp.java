package org.sda.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sda.dao.LocationDao;
import org.sda.model.Location;

import java.util.List;
import java.util.UUID;

import static org.sda.util.HibernateUtil.sessionFactory;

public class LocationServiceImp implements LocationService {
    private LocationDao locationDao;

    public LocationServiceImp(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    @Override
    public Location getLocationById(UUID id) {
        return locationDao.findById(id);
    }

    @Override
    public List<Location> getAllLocations() {
        return locationDao.findAll();
    }

    @Override
    public void addLocation(Location location) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.save(location);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }

    }

    @Override
    public void updateLocation(Location location) {
        locationDao.update(location);


    }

    @Override
    public void deleteLocation(Location location) {
        locationDao.delete(location);

    }
}
