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
     private Patient patient;
     private Machine machine;
     private LocalDateTime reservationDateTime;
     private String bodyRegion;
     private String comment;
}
