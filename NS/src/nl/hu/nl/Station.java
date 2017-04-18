package nl.hu.nl;

import java.util.ArrayList;

public class Station {
    private static XMLParser xmlParser = new XMLParser();
    private String code;
    private String type;
    private String nameKort;
    private String nameMiddel;
    private String nameLang;
    private String country;
    private int UICode;
    private double lat;
    private double lon;
    private ArrayList<String> synoniemen;

    public Station(String code, String type, String nameK, String nameM, String nameL, String land, int UICode, double lat, double lon){
            this.code = code;
            this.type = type;
            this.nameKort = nameK;
            this.nameMiddel = nameM;
            this.nameLang = nameL;
            this.country = land;
            this.UICode = UICode;
            this.lat = lat;
            this.lon = lon;
    }

    public String getName(){
            return nameLang;
    }

    public double getLat(){
            return lat;
    }

    public double getLon(){
            return lon;
    }

    public String toString(){
            return nameLang;
    }
    
    public static String findLowest(){
        try{
            double lowestStationHeight = 0;
            String station = "geen";
            ArrayList<Station> stations = (ArrayList<Station>) xmlParser.GetStationsFromNS();
            for(int i = 0; i < stations.size(); i++){
                double stationHeight = stations.get(i).findHeight();//xmlParser.getHeight(stations.get(i).getName());
                if (stationHeight < lowestStationHeight){
                    station = stations.get(i).getName();
                    lowestStationHeight = stationHeight;
                }
            }
            return "het laagste station van nederland is = " + station + " op een hoogte van "+ lowestStationHeight;
        }catch(Exception e){
            e.printStackTrace();
            return "Er ging iets fout";
        }
    }
    
    public double findHeight(){
        return xmlParser.getHeight(nameLang);
    }
    
    public static double findHeightStation(String stationName) throws Exception{
        xmlParser.GetStationsFromNS();
        return xmlParser.getHeight(stationName);
    }
}
