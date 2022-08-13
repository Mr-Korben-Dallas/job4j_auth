package ru.job4j.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.job4j.auth.domain.Employee;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.service.EmployeeService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private static final String API_PERSON = "http://localhost:8080/person/";
    private static final String API_PERSON_ID = "http://localhost:8080/person/{id}";
    private final RestTemplate restTemplate;
    private final EmployeeService service;

    public EmployeeController(RestTemplate restTemplate, EmployeeService service) {
        this.restTemplate = restTemplate;
        this.service = service;
    }

    @GetMapping("/")
    public Iterable<Employee> findAll() {
        return service.findAll();
        /*List<Employee> employeeList = new ArrayList<>();
        List<Person> persons = restTemplate
                .exchange(
                        API_PERSON,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Person>>() { }
                )
                .getBody();
        for (Person person : persons) {
            employeeList.add(Employee.of(person.getId(), person.getLogin(), "Surname", person.getId(), person));
        }
        return employeeList;*/
    }

    @PostMapping("/")
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        return new ResponseEntity<>(
                service.create(employee),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Employee> update(@RequestBody Employee employee) {
        return new ResponseEntity<>(
                service.update(employee),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/person")
    public ResponseEntity<Employee> addPerson(@PathVariable Long id, @RequestBody List<Person> persons) {
        if (persons.isEmpty()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        Optional<Employee> optionalEmployee = service.findById(id);
        if (optionalEmployee.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Employee employee = optionalEmployee.get();
        persons.forEach(p -> employee.addPerson(restTemplate.patchForObject(API_PERSON, p, Person.class)));
        return new ResponseEntity<>(
                service.update(employee),
                HttpStatus.OK
        );
    }

    @PutMapping("/person")
    public ResponseEntity<Void> updatePerson(@RequestBody List<Person> persons) {
        persons.forEach(p -> restTemplate.put(API_PERSON, p));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/person/{personId}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id, @PathVariable Long personId) {
        Optional<Employee> optionalEmployee = service.findById(id);
        if (optionalEmployee.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Employee employee = optionalEmployee.get();
        if (!employee.getPersons().removeIf(el -> Objects.equals(el.getId(), personId))) {
            return ResponseEntity.notFound().build();
        }
        service.update(employee);
        restTemplate.delete(API_PERSON_ID, personId);
        return ResponseEntity.ok().build();
    }
}
