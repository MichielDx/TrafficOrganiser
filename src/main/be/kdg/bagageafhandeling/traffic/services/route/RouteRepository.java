package main.be.kdg.bagageafhandeling.traffic.services.route;

import main.be.kdg.bagageafhandeling.traffic.model.route.RouteDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michiel on 5/11/2016.
 */
public class RouteRepository {
    private static Map<String,RouteDTO> conveyorCache;

    public RouteRepository() {
        conveyorCache = new HashMap<>();
    }

    public synchronized void addRouteDTO(String routeIDs,RouteDTO routeDTO){
        conveyorCache.put(routeIDs,routeDTO);
    }

    public boolean contains(String key){
        return conveyorCache.containsKey(key);
    }

    public void clearCache(){
        conveyorCache.clear();
    }

    public RouteDTO getRouteDTO(String key){
        return conveyorCache.get(key);
    }

}