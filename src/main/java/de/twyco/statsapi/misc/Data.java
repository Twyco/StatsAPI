package de.twyco.statsapi.misc;

import java.util.*;

public abstract class Data {

    private static int currentSeason;
    private static final Map<Integer, Date> seasonStartDate;
    private static final Map<Integer, Date> seasonEndDate;
    private static final Map<Integer, Set<Integer>> statsIDsFromSeason;

    private static final Set<Integer> statsIDs;

    static {
        currentSeason = 0;
        seasonStartDate = new HashMap<>();
        seasonEndDate = new HashMap<>();
        statsIDsFromSeason = new HashMap<>();
        statsIDs = new HashSet<>();
    }

    public static void setSeasonStartDate(int seasonID, Date startDate) {
        seasonStartDate.put(seasonID, startDate);
    }

    public static void setSeasonEndDate(int seasonID, Date startDate) {
        seasonEndDate.put(seasonID, startDate);
    }

    public static void setCurrentSeason(int currentSeason) {
        Data.currentSeason = currentSeason;
    }

    public static void addSeasonStatsIDs(int seasonID, int statID) {
        Set<Integer> statIDs;
        if(Data.statsIDsFromSeason.containsKey(seasonID)){
            statIDs = Data.statsIDsFromSeason.get(seasonID);
        }else {
            statIDs = new HashSet<>();
        }
        statIDs.add(statID);
        Data.statsIDsFromSeason.put(seasonID, statIDs);
    }

    public static void addStatsID(int statID) {
        Data.statsIDs.add(statID);
    }

    public Date getSeasonStartDate(int seasonID) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2003, Calendar.OCTOBER, 16);
        Date defaultDate = calendar.getTime();
        return Data.seasonStartDate.getOrDefault(seasonID, defaultDate);
    }

    public Date getSeasonEndDate(int seasonID) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2003, Calendar.OCTOBER, 16);
        Date defaultDate = calendar.getTime();
        return Data.seasonEndDate.getOrDefault(seasonID, defaultDate);
    }

    public static int getCurrentSeason() {
        return Data.currentSeason;
    }

    public static Set<Integer> getSeasonStatsIDs(int seasonID) {
        return Data.statsIDsFromSeason.getOrDefault(seasonID, new HashSet<>());
    }

    public static Set<Integer> getStatsIDs() {
        return statsIDs;
    }
}
