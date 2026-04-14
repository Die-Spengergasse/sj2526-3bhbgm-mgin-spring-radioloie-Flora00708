package at.spengergasse.spring_thymeleaf.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "r_reservations")
public class Reservation {
    //Reservierung (ID)
    //Patient (Verweis auf Patient)
    //Maschine (Verweis auf Maschine)
    //Datum und Uhrzeit der Reservierung
    //Körperreginon (z.B. Kopf, Brust, Bauch, ...)
    //Kommentarfeld
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
     private int id;
     @ManyToOne
     @JoinColumn(name = "patient_id")
     private Patient patient;
     @ManyToOne
     @JoinColumn(name = "machine_id")
     private Machine machine;
     private LocalDateTime reservationDateTime;
     private String bodyRegion;
     private String comment;

    public Reservation(Patient patient, String bodyRegion, Machine machine, LocalDateTime reservationDateTime, String comment) {
        this.patient = patient;
        this.bodyRegion = bodyRegion;
        this.machine = machine;
        this.reservationDateTime = reservationDateTime;
        this.comment = comment;
    }

    public Reservation() {
    }

    public int getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public LocalDateTime getReservationDateTime() {
        return reservationDateTime;
    }

    public void setReservationDateTime(LocalDateTime reservationDateTime) {
        this.reservationDateTime = reservationDateTime;
    }

    public String getBodyRegion() {
        return bodyRegion;
    }

    public void setBodyRegion(String bodyRegion) {
        this.bodyRegion = bodyRegion;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", patient=" + patient +
                ", machine=" + machine +
                ", reservationDateTime=" + reservationDateTime +
                ", bodyRegion='" + bodyRegion + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
