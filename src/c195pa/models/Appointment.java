/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195pa.models;

import static c195pa.AMS.APPT_INTERVAL;
import static c195pa.AMS.USER;
import static c195pa.AMS.connect;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author alex
 */
public class Appointment {

    private final int id;
    private final int customerId;
    private final int userId;
    private final String title;
    private final String description;
    private final String location;
    private final String contact;
    private final String type;
    private final String url;
    private final ZonedDateTime start;
    private final ZonedDateTime end;
    private final ZonedDateTime createDate;
    private final String createdBy;
    private final ZonedDateTime lastUpdate;
    private final String lastUpdateBy;

    public Appointment(int apptId) throws SQLException {
        ResultSet rs = connect().createStatement().executeQuery(""
                + "SELECT * "
                + "FROM appointment "
                + "WHERE appointmentId='" + apptId + "';");

        rs.next();
        this.id = rs.getInt("appointmentId");
        this.customerId = rs.getInt("customerId");
        this.userId = rs.getInt("userId");
        this.title = rs.getString("title");
        this.description = rs.getString("description");
        this.location = rs.getString("location");
        this.contact = rs.getString("contact");
        this.type = rs.getString("type");
        this.url = rs.getString("url");
        this.start = ZonedDateTime.ofInstant(rs.getTimestamp("start")
                .toLocalDateTime()
                .atZone(ZoneId.of("UTC")).toInstant(), ZoneId.systemDefault());
        this.end = ZonedDateTime.ofInstant(rs.getTimestamp("end")
                .toLocalDateTime()
                .atZone(ZoneId.of("UTC")).toInstant(), ZoneId.systemDefault());
        this.createDate = ZonedDateTime.ofInstant(rs.getTimestamp("createDate")
                .toLocalDateTime()
                .atZone(ZoneId.of("UTC")).toInstant(), ZoneId.systemDefault());
        this.createdBy = rs.getString("createdBy");
        this.lastUpdate = ZonedDateTime.ofInstant(rs.getTimestamp("lastUpdate")
                .toLocalDateTime()
                .atZone(ZoneId.of("UTC")).toInstant(), ZoneId.systemDefault());
        this.lastUpdateBy = rs.getString("lastUpdateBy");
    }

    public Appointment(int id, int customerId, int userId, String title,
            String description, String location, String contact, String type,
            String url, ZonedDateTime start, ZonedDateTime end,
            ZonedDateTime createDate, String createdBy, ZonedDateTime lastUpdate,
            String lastUpdateBy) throws SQLException {
        this.id = id;
        this.customerId = customerId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    public int getId() {
        return this.id;
    }

    public int getCustId() {
        return this.customerId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLocation() {
        return this.location;
    }

    public String getType() {
        return this.type;
    }

    public String getContact() {
        return this.contact;
    }

    public String getUrl() {
        return this.url;
    }

    public ZonedDateTime getStart() {
        return this.start;
    }

    public ZonedDateTime getEnd() {
        return this.end;
    }

    public static ObservableList<String> getHours(LocalTime start, LocalTime end) {
        ObservableList<String> hours = FXCollections.observableArrayList();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm a");

        while (start.isBefore(end) || start.equals(end)) {
            hours.add(start.format(dtf));
            start = start.plusMinutes(APPT_INTERVAL);
        }

        return hours;
    }

    public SimpleStringProperty getTitleProperty() {
        return new SimpleStringProperty(this.title);
    }

    public SimpleStringProperty getTypeProperty() {
        return new SimpleStringProperty(this.type);
    }

    public SimpleObjectProperty getStartProperty() {
        return new SimpleObjectProperty(this.start);
    }

    public SimpleObjectProperty getEndProperty() {
        return new SimpleObjectProperty(this.end);
    }

    public static void insertAppointment(int customerId, String title,
            String description, String location, String contact, String type,
            String url, ZonedDateTime start, ZonedDateTime end) throws SQLException {
        String un = USER.getUsername();
        int userId = USER.getId();
        Connection conn = connect();

        Timestamp tsStart = Timestamp.valueOf(start.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
        Timestamp tsEnd = Timestamp.valueOf(end.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());

        conn.createStatement().executeUpdate(""
                + "INSERT INTO appointment "
                + "(customerId, userId, title, description, location, contact, "
                + "type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "VALUES('" + customerId + "', '" + userId + "', '" + title + "', "
                + "'" + description + "', '" + location + "', '" + contact + "', "
                + "'" + type + "', '" + url + "', '" + tsStart + "', '" + tsEnd + "', "
                + "NOW(), '" + un + "', NOW(), '" + un + "');");
    }

    public static boolean updateAppointment(int appointmentId, int customerId,
            String title, String description, String location, String contact,
            String type, String url, ZonedDateTime start, ZonedDateTime end) {
        String un = USER.getUsername();
        int userId = USER.getId();
        boolean success = true;

        Timestamp tsStart = Timestamp.valueOf(start.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
        Timestamp tsEnd = Timestamp.valueOf(end.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());

        try {
            Connection conn = connect();
            conn.createStatement().executeUpdate("UPDATE appointment "
                    + "SET "
                    + "customerId = '" + customerId + "', "
                    + "userId = '" + userId + "', "
                    + "title = '" + title + "', "
                    + "description = '" + description + "', "
                    + "location = '" + location + "', "
                    + "contact = '" + contact + "', "
                    + "type = '" + type + "', "
                    + "url = '" + url + "', "
                    + "start = '" + tsStart + "', "
                    + "end = '" + tsEnd + "', "
                    + "lastUpdate = NOW(), "
                    + "lastUpdateBy = '" + un + "' "
                    + "WHERE appointmentId = '" + appointmentId + "';");
        } catch (SQLException e) {
            success = false;
        }
        return success;
    }

    public static boolean deleteAppointment(int appointmentId) throws SQLException {
        boolean success = false;
        Connection conn = connect();

        try {
            conn.createStatement().executeUpdate(""
                    + "DELETE FROM appointment "
                    + "WHERE appointmentId='" + appointmentId + "';");
            success = true;
        } catch (SQLException e) {
            System.out.println("No Appointment Found.");
        }

        return success;
    }
    
    public static int checkUpcomingAppt(int minutes) throws SQLException {
        int apptId = -1;
        Connection conn = connect();
        ZonedDateTime start = ZonedDateTime.now();
        ZonedDateTime end = start.plusMinutes(minutes);
        Timestamp tsStart = Timestamp.valueOf(start.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
        Timestamp tsEnd = Timestamp.valueOf(end.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
        
        try {
            ResultSet rs = connect().createStatement().executeQuery(""
                    + "SELECT appointmentId "
                    + "FROM appointment "
                    + "WHERE start >= '" + tsStart + "' "
                    + "AND start <= '" + tsEnd + "' "
                    + "AND userId = '" + USER.getId() + "';");
            if (rs.next()) {
                apptId = rs.getInt("appointmentId");
            }
        } catch (SQLException e) {
            System.out.println("No Appointment Found.");
        }
        
        return apptId;
    }
    public static boolean apptOverlap(ZonedDateTime newStart, ZonedDateTime newEnd) throws SQLException {
        boolean overlap = false;
        Timestamp tsStart = Timestamp.valueOf(newStart.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
        Timestamp tsEnd = Timestamp.valueOf(newEnd.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
        Timestamp tsStartDate = Timestamp.valueOf(ZonedDateTime.of(newStart.getYear(), newStart.getMonthValue(), newStart.getDayOfMonth(), 0, 0, 0, 0, ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
        Timestamp tsEndDate = Timestamp.valueOf(ZonedDateTime.of(newStart.getYear(), newStart.getMonthValue(), newStart.getDayOfMonth() + 1, 0, 0, 0, 0, ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
        ResultSet rs = connect().createStatement().executeQuery(""
                + "SELECT start, end "
                + "FROM appointment "
                + "WHERE start >= '" + tsStartDate + "' "
                + "AND start < '" + tsEndDate + "' "
                + "AND userId = '" + USER.getId() + "';");
        
        // if new start and end time is between any existing start and end times
//            System.out.println(tsStart);
//            System.out.println(tsEnd);
//            System.out.println(tsStartDate);
//            System.out.println(tsEndDate);
        while (rs.next()) {
//            System.out.println(tsStart);
//            System.out.println(tsEnd);
//            System.out.println(rs.getTimestamp("start"));
//            System.out.println(rs.getTimestamp("end"));
            
            if ((tsStart.after(rs.getTimestamp("start")) && tsStart.before(rs.getTimestamp("end"))) || tsStart.equals(rs.getTimestamp("start"))) {
                overlap = true;
            }
            if ((tsEnd.after(rs.getTimestamp("start")) && tsEnd.before(rs.getTimestamp("end"))) || tsEnd.equals(rs.getTimestamp("end"))) {
                overlap = true;
            }
        }
        
        return overlap;
    }
}
