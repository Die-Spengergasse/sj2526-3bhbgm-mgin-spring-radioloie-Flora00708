package at.spengergasse.spring_thymeleaf.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Integer> {
    List<Reservation>  findByMachine(Machine machine);
    //im repository, weil es sonst keinen zugriff auf die db hat
    // Prüft Reservation Datetime für maschine
    boolean existsByMachineAndReservationDateTime(Machine machine, LocalDateTime dateTime);
    // prüft ob patient schon besetzt
    boolean existsByPatientAndReservationDateTime(Patient patient, LocalDateTime dateTime);
}
