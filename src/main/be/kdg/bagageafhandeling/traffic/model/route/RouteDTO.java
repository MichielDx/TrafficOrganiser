package main.be.kdg.bagageafhandeling.traffic.model.route;

import java.util.List;

/**
 * Created by Michiel on 5/11/2016.
 */
public class RouteDTO {
    private List<Route> routes;

    private class Route{
        private String route;

        public String getRoute() {
            return route;
        }
    }

    public List<Route> getRoutes() {
        return routes;
    }
    public String getRoute(int index){
        return routes.get(index).getRoute();
    }
}
