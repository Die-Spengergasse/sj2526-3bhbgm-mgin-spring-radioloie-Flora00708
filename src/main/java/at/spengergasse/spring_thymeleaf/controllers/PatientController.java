package at.spengergasse.spring_thymeleaf.controllers;


import at.spengergasse.spring_thymeleaf.entities.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Controller
@RequestMapping("/patient")
public class PatientController {


    private final PatientRepository patientRepository;
    private final MachineRepository machineRepository;
    private final ReservationRepository reservationRepository;

    public PatientController(PatientRepository patientRepository, MachineRepository machineRepository, ReservationRepository reservationRepository) {
        this.patientRepository = patientRepository;
        this.machineRepository = machineRepository;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/list")
    public String patients(Model model) {
        model.addAttribute("patients", patientRepository.findAll());
        return "patlist";
    }

    @GetMapping("/add")
    public String addPatient(Model model) {
        model.addAttribute("patient", new Patient());
        return "add_patient";
    }

    @PostMapping("/add")
    public String addPatient(Model model, @ModelAttribute("patient") Patient patient, BindingResult result) throws Exception {

        //Gebdat
        if (patient.getBirth().isAfter(LocalDate.now())) {
            model.addAttribute("error", "Geburtsdatum darf nicht in der Zukunft liegen!");
            return "add_patient";
        }
        //ssn
        String ssn = String.valueOf(patient.getSsn());
        if (ssn.length() != 10) {
            model.addAttribute("error", "SVN muss genau 10 Stellen haben!");
            return "add_patient";
        }
        
        patientRepository.save(patient);
        return  "redirect:/patient/list";
    }

    @GetMapping("/addRev")
    public String addRev(Model model){
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("machines", machineRepository.findAll());
        return "add_rev";

    }
    @PostMapping("/addRev")
    public String addRev(Model model, @ModelAttribute("reservation") Reservation reservation, @RequestParam("patientId") int patientId, @RequestParam("machineId") int machineId,  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam("reservationDateTime") LocalDateTime datetime) {
        patientRepository.findById(patientId).ifPresent(reservation::setPatient);
        machineRepository.findById(machineId).ifPresent(reservation::setMachine);
        reservation.setReservationDateTime(datetime);

        // Maschinene prüfung
        if (reservationRepository.existsByMachineAndReservationDateTime(reservation.getMachine(), reservation.getReservationDateTime())) {
            model.addAttribute("error", "Gerät ist schon belegt um die Uhrzeit");

            model.addAttribute("patients", patientRepository.findAll());
            model.addAttribute("machines", machineRepository.findAll());

            return "add_rev";
        }
        //Patienten
        if (reservationRepository.existsByPatientAndReservationDateTime(reservation.getPatient(), reservation.getReservationDateTime())) {
            model.addAttribute("error", "Patient hat schon einen Termin um die Uhrzeit");

            model.addAttribute("patients", patientRepository.findAll());
            model.addAttribute("machines", machineRepository.findAll());

            return "add_rev";
        }
        //termin
        if (datetime.isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Termin darf nicht in der Vergangenheit liegen!");
            model.addAttribute("patients", patientRepository.findAll());
            model.addAttribute("machines", machineRepository.findAll());
            return "add_rev";
        }

        reservationRepository.save(reservation);  // erst ganz am Ende!
        return "redirect:/patient/list";
    }

    @GetMapping("/reservationsByMachine")
    public String reservationsByMachine(Model model) {
        Map<Machine, List<Reservation>> machineReservations = new LinkedHashMap<>();
        List<Machine> machines = machineRepository.findAll();
        for (Machine machine : machines) {
            List<Reservation> reservations = reservationRepository.findByMachine(machine);
            machineReservations.put(machine, reservations);
        }
        model.addAttribute("machineReservations", machineReservations);

        return "revlist";
    }
    @ExceptionHandler(Exception.class)
    public ModelAndView errors(Exception exception) {
        System.out.println(exception);
        //extra für DB fehler:
        if (exception.getMessage() != null && exception.getMessage().contains("Communications link failure")) {
            String message = "Datenbankverbindung fehlgeschlagen – läuft MySQL?";
        }
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("error", exception.getMessage());
        return modelAndView;
    }
    
}
