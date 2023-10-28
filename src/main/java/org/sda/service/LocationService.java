package org.sda.service;

import org.sda.model.Location;

import java.util.List;
import java.util.UUID;

public interface LocationService {

    Location getLocationById (UUID id);
    List<Location> getAllLocations ();
    public void addLocation(Location location);

    public void  updateLocation (Location location);
    public void deleteLocation (Location location);
}
