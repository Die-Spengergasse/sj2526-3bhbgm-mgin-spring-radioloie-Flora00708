package at.spengergasse.spring_thymeleaf.entities;

import jakarta.persistence.*;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "m_machines")
public class Machine {
    //Bezeichnung (ID)
    //Art des Gerätes (z.B. MR, CT, Röntgen, ...)
    //Standort (Raumnummer)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private int id;
     private String type;
     private int location;
     @OneToMany(mappedBy="machine", cascade = CascadeType.ALL)
     private List<Reservation> reservations;

    public Machine() {
    }

    public Machine(String type, int location) {
        this.type = type;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Machine{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", location=" + location +
                '}';
    }
}
