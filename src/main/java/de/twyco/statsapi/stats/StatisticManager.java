package de.twyco.statsapi.stats;

import de.twyco.statsapi.misc.Database;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Diese Klasse bietet eine Schnittstelle für das Speichern und Auslesen der Stats eines Spielers.
 *
 * @author Twyco
 * @see Stat
 */

public class StatisticManager {
//
//    private final Statistic statistic;
//
//    /**
//     * Erstellt ein neues Verwaltungsobjekt,
//     * dass die Stats eines Spielers, mit dessen UUID, ändern, so wie auslesen kann.
//     *
//     * @param uuid       UUID des Spielers, dessen Stats du verwalten möchtest.
//     * @param minigameID Die ID des Minigames dessen Stats du verwalten möchtest.
//     * @param seasonID   Die ID der Season, dessen Stats du verwalten möchtest.
//     * @param database   File, in der die Stats gespeichert werden. (BETA wird entfernt und in config file verschoben, zu ip, port, username, passwort für MonogDB geändert)
//     */
//    public StatisticManager(UUID uuid, int minigameID, int seasonID, File database) { //TODO ADD DATABASE OR DATABASE LOGIN INFOS
//        this.statistic = new Statistic(uuid, minigameID, seasonID, new Database(database));
//    }
//
//    /**
//     * Lädt die Stats zu dem akutellen Minigame und Season neu.
//     */
//    public void reloadStats() {
//        this.statistic.reloadStats();
//    }
//
//    /**
//     * Setzt die Statistik in dem aktuellen Minigame und der Season auf den gegebenen Wert.
//     *
//     * @param statName Der Name der Statistik, die geändert werden soll.
//     * @param newValue Der neue Wert der Statistik.
//     */
//    public void setStatValue(String statName, double newValue) {
//        if (!statistic.containsSavedStat(statName)) {
//            System.err.println("Das angegebene Stat gehört nicht zu dem Spieler!");
//        }
//        this.statistic.getSavedStat(statName).setValue(newValue);
//    }
//
//    /**
//     * Setzt die Statistik in dem aktuellen Minigame und der Season auf den gegebenen Wert.
//     *
//     * @param stat     Die Statistik, die geändert werden soll.
//     * @param newValue Der neue Wert der Statistik.
//     */
//    public void setStatValue(SavedStat stat, double newValue) {
//        if (!statistic.containsSavedStat(stat)) {
//            System.err.println("Das angegebene Stat gehört nicht zu dem Spieler!");
//        }
//        stat.setValue(newValue);
//    }
//
//    /**
//     * Setzt die Statistik in dem aktuellen Minigame und der Season um die gegebene Differenz.
//     *
//     * @param statName   Der Name der Statistik, die geändert werden soll.
//     * @param difference Der neue Wert der Statistik.
//     */
//    public void changeStatValue(String statName, double difference) {
//        if (!statistic.containsSavedStat(statName)) {
//            System.err.println("Das angegebene Stat gehört nicht zu dem Spieler!");
//        }
//        double currentValue = this.statistic.getSavedStat(statName).getValue();
//        double newValue = currentValue + difference;
//        this.statistic.getSavedStat(statName).setValue(newValue);
//    }
//
//    /**
//     * Ändert die Statistik in dem aktuellen Minigame und der Season um die gegebene Differenz.
//     *
//     * @param stat       Die Statistik, die geändert werden soll.
//     * @param difference Der neue Wert der Statistik.
//     */
//    public void changeStatValue(SavedStat stat, double difference) {
//        if (!statistic.containsSavedStat(stat)) {
//            System.err.println("Das angegebene Stat gehört nicht zu dem Spieler!");
//        }
//        double currentValue = stat.getValue();
//        double newValue = currentValue + difference;
//        stat.setValue(newValue);
//    }
//
//    /**
//     * Speichert alle Statistiken zu dem aktuellen Minigame und der Season;
//     */
//    public void saveStatistic() {
//        this.statistic.save();
//    }
//
//    /**
//     * Ändert das Minigame, dessen Statistiken verwaltet werden.
//     * Lädt die Statistiken anschließend neu.
//     *
//     * @param minigameID Die ID des neuen Minigames, dessen Statistiken verwaltet werden.
//     */
//    public void setMinigameID(int minigameID) {
//        this.statistic.setMinigameID(minigameID);
//    }
//
//    /**
//     * Auslesen der aktuellen MinigameID.
//     *
//     * @return Die ID des Minigames
//     */
//    public int getMinigameID() {
//        return this.statistic.getMinigameID();
//    }
//
//    /**
//     * Ändert die Season, dessen Statistiken verwaltet werden.
//     * Lädt die Statistiken anschließend neu.
//     *
//     * @param seasonID Die ID der neuen Season, dessen Statistiken verwaltet werden.
//     */
//    public void setSeasonID(int seasonID) {
//        this.statistic.setSeasonID(seasonID);
//    }
//
//    /**
//     * Auslesen der ID der aktuellen Season.
//     *
//     * @return Die ID der Season
//     */
//    public int getSeasonID() {
//        return this.statistic.getSeasonID();
//    }
//
//    /**
//     * Gibt die Namen aller Statistiken des aktuellen Minigames aus.
//     *
//     * @return Ein {@link Set} aus allen Namen
//     * @see SavedStat
//     */
//    public Set<String> getSavedStatsNames() {
//        List<SavedStat> savedStatsList = this.statistic.getSavedStats().stream().toList();
//        Set<String> savedStatsNames = new HashSet<>();
//        for (SavedStat stat : savedStatsList) {
//            savedStatsNames.add(stat.getStatName());
//        }
//        return savedStatsNames;
//    }
//
//    /**
//     * Gibt die Namen aller gespeicherten Statistiken des aktuellen Minigames aus.
//     *
//     * @return Ein {@link Set} aller {@link SavedStat}
//     */
//    public Set<SavedStat> getSavedStats() {
//        return this.statistic.getSavedStats();
//    }
//
//    /**
//     * Auslesen einer gespeicherten Statistik mit bestimmten Namen.
//     *
//     * @param savedStatName Der Name der Statistik.
//     * @return Ein {@link Set} aus allen {@link SavedStat}
//     */
//    public SavedStat getSavedStat(String savedStatName) {
//        return this.statistic.getSavedStat(savedStatName);
//    }
//
//    /**
//     * Überprüft, ob es bei dem aktuellen Minigame eine gespeicherte Statistik mit dem Namen gibt.
//     *
//     * @param savedStatName Der Name der Statistik.
//     * @return true, wenn es eine Statistik mit dem Namen gibt. false, sonst
//     * @see SavedStat
//     */
//    public boolean containSavedStat(String savedStatName) {
//        return this.statistic.containsSavedStat(savedStatName);
//    }
//
//    /**
//     * Überprüft, ob es bei dem aktuellen Minigame genau diese gespeicherte Statistik gibt.
//     *
//     * @param savedStat Die {@link SavedStat}.
//     * @return true, wenn es genau diese Statistik gibt. false, sonst.
//     */
//    public boolean containSavedStat(SavedStat savedStat) {
//        return this.statistic.containsSavedStat(savedStat);
//    }
//
//    /**
//     * Gibt die Namen aller displayed Statistiken, des aktuellen Minigames aus.
//     *
//     * @return Ein {@link Set} aus allen Namen
//     * @see DisplayedStat
//     */
//    public Set<String> getDisplayedStatsNames() {
//        List<DisplayedStat> displayedStatsList = this.statistic.getDisplayedStats().stream().toList();
//        Set<String> displayedStatsNames = new HashSet<>();
//        for (DisplayedStat stat : displayedStatsList) {
//            displayedStatsNames.add(stat.getStatName());
//        }
//        return displayedStatsNames;
//    }
//
//    /**
//     * Gibt die displayed Statistiken, des aktuellen Minigames aus.
//     *
//     * @return Ein {@link Set} aller {@link DisplayedStat}
//     * @see DisplayedStat
//     */
//    public Set<DisplayedStat> getDisplayedStats() {
//        return this.statistic.getDisplayedStats();
//    }
//
//    /**
//     * Auslesen einer displayed Statistik mit bestimmten Namen.
//     *
//     * @param displayedStatName Der Name der {@link DisplayedStat}
//     * @return Ein {@link Set} aus allen {@link DisplayedStat}
//     * @see DisplayedStat
//     */
//    public DisplayedStat getDisplayedStat(String displayedStatName) {
//        return this.statistic.getDisplayedStat(displayedStatName);
//    }
//
//    /**
//     * Überprüft, ob es bei dem aktuellen Minigame genau diese displayed Statistik gibt.
//     *
//     * @param displayedStatName Der Name der Statistik.
//     * @return true, wenn es genau diese Statistik gibt. false, sonst.
//     * @see DisplayedStat
//     */
//    public boolean containDisplayedStat(String displayedStatName) {
//        return this.statistic.containsDisplayedStat(displayedStatName);
//    }
//
//    /**
//     * Überprüft, ob es bei dem aktuellen Minigame genau diese displayed Statistik gibt.
//     *
//     * @param displayedStat Die Statistik.
//     * @return true, wenn es genau diese Statistik gibt. false, sonst.
//     * @see DisplayedStat
//     */
//    public boolean containDisplayedStat(DisplayedStat displayedStat) {
//        return this.statistic.containsDisplayedStat(displayedStat);
//    }
//
//    /**
//     * Auslesen der UUID, dessen Statistiken verwaltet werden.
//     *
//     * @return die UUID
//     */
//    public UUID getUUID() {
//        return this.statistic.getUUID();
//    }
//
//
//    /**
//     * Auslesen aller UUIDs, die in des aktuellen Season und Minigame Statistiken besitzen.
//     * TODO, doesnt work right now
//     * @return Ein {@link Set} der UUIDs
//     */
//    public Set<UUID> getUUIDS() {
//        return null; //TODO
//    }
}
