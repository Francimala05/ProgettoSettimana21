package com.epicode.Progetto21.controller;

import com.epicode.Progetto21.entities.User;
import com.epicode.Progetto21.entities.Evento;
import com.epicode.Progetto21.repository.EventoRepository;
import com.epicode.Progetto21.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/event")
public class EventoController {
    private final EventoRepository eventRepository;
    private final UserRepository userRepository;

    public EventoController(EventoRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    // CREAZIONE EVENTO
    @PostMapping("/create")
    public String createEvent(@RequestBody Evento event, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername());
        if (user != null && user.getRole() == User.Role.ORGANIZZATORE_EVENTI) {
            event.setNomeOrganizzatore(user.getUsername());
            eventRepository.save(event);
            return "Evento creato con successo!";
        }
        return "Non hai il permesso di creare eventi.";
    }

    // MODIFICA EVENTO
    @PutMapping("/update/{id}")
    public String updateEvent(@PathVariable Long id, @RequestBody Evento event, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Evento> existingEvent = eventRepository.findById(id);
        if (existingEvent.isPresent()) {
            Evento currentEvent = existingEvent.get();
            if (currentEvent.getNomeOrganizzatore().equals(userDetails.getUsername()) || userDetails.getAuthorities().toString().contains("")) {
                currentEvent.setTitolo(event.getTitolo());
                currentEvent.setDescrizione(event.getDescrizione());
                currentEvent.setData(event.getData());
                currentEvent.setLuogo(event.getLuogo());
                currentEvent.setPostiDisponibili(event.getPostiDisponibili());
                eventRepository.save(currentEvent);
                return "Evento modificato con successo!";
            }
            return "Non hai il permesso di modificare eventi.";
        }
        return "Event not found!";
    }

    // ELIMINAZIONE EVENTO
    @DeleteMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Evento> event = eventRepository.findById(id);
        if (event.isPresent()) {
            Evento currentEvent = event.get();
            if (currentEvent.getNomeOrganizzatore().equals(userDetails.getUsername()) || userDetails.getAuthorities().toString().contains("ROLE_ADMIN")) {
                eventRepository.delete(currentEvent);
                return "Evento eliminato con successo!";
            }
            return "Non hai il permesso di eliminare eventi.";
        }
        return "Event not found!";
    }

    // Ottenere tutti gli eventi
    @GetMapping("/all")
    public List<Evento> getAllEvents() {
        return eventRepository.findAll();
    }

    // Ottenere un evento specifico
    @GetMapping("/{id}")
    public Evento getEvent(@PathVariable Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    // Prenotare un posto per un evento
    @PostMapping("/book/{id}")
    public String bookEvent(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        // Trova l'evento
        Optional<Evento> eventOptional = eventRepository.findById(id);
        if (!eventOptional.isPresent()) {
            return "Evento non trovato!";
        }

        Evento event = eventOptional.get();
        User user = userRepository.findByUsername(userDetails.getUsername());
        if (user == null || !user.getRole().equals(User.Role.USER)) {
            return "Solo gli utenti normali possono prenotare.";
        }

        // Verifica la disponibilità dei posti
        try {
            event.prenotaPosto();
            eventRepository.save(event);
            return "Posto prenotato con successo!";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }
}
