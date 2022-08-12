package ru.job4j.auth.service;

import org.springframework.stereotype.Service;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.PersonRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public Collection<Person> findAll() {
        Collection<Person> persons = new ArrayList<>();
        repository.findAll().forEach(persons::add);
        return persons;
    }

    public Optional<Person> findById(Long id) {
        return repository.findById(id);
    }

    public Person create(Person person) {
        return repository.save(person);
    }

    public void update(Person person) {
        repository.save(person);
    }

    public void delete(Long id) {
        repository.delete(new Person(id));
    }
}
