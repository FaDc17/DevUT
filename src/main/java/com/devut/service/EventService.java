package com.devut.service;

import com.devut.model.Event;
import com.devut.repository.EventRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    private final List<Event> showcase = new ArrayList<>();

    public EventService(EventRepository repository) {
        add(1L, "Data & AI Lima Meetup", "Datos e IA", "Jue, 24 de abril", "7:00 p. m.", "UTEC, Barranco", "Casos reales de IA generativa, datos modernos y aprendizajes de equipos peruanos.", "#7557ff");
        add(2L, "Cloud Native Lima", "Nube y DevOps", "Sáb, 26 de abril", "10:00 a. m.", "WeWork, Miraflores", "Una mañana de Kubernetes, observabilidad y plataformas cloud con la comunidad local.", "#1591e8");
        add(3L, "Software Architecture Night", "Ingeniería de software", "Mar, 29 de abril", "7:30 p. m.", "PUCP, San Miguel", "Conversaciones prácticas sobre sistemas escalables, decisiones técnicas y equipos.", "#f97316");
        add(4L, "Startup Talks Lima", "Emprendimiento y startups", "Jue, 8 de mayo", "6:30 p. m.", "CIDE PUCP, San Miguel", "Fundadores peruanos comparten cómo validaron, crecieron y financiaron sus startups.", "#e84d8a");
    }

    private void add(Long id, String title, String category, String date, String time, String place, String description, String accent) {
        Event event = new Event();
        event.setId(id); event.setTitle(title); event.setCategory(category);
        event.setDate(date); event.setTime(time); event.setPlace(place);
        event.setDescription(description); event.setAccent(accent);
        showcase.add(event);
    }

    public List<Event> findAll() { return List.copyOf(showcase); }

    public Event findById(Long id) {
        return showcase.stream().filter(event -> event.getId().equals(id)).findFirst().orElse(showcase.get(0));
    }
}
