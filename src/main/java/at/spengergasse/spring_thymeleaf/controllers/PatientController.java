package at.spengergasse.spring_thymeleaf.controllers;


import at.spengergasse.spring_thymeleaf.entities.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String addPatient(@ModelAttribute("patient") Patient patient) {
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
    public String addRev(@ModelAttribute("reservation") Reservation reservation, @RequestParam("patientId") int patientId, @RequestParam("machineId") int machineId,  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam("reservationDateTime") LocalDateTime datetime) {
        reservation.setReservationDateTime(datetime);
        patientRepository.findById(patientId).ifPresent(reservation::setPatient);
        machineRepository.findById(machineId).ifPresent(reservation::setMachine);
        reservationRepository.save(reservation);
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

}
