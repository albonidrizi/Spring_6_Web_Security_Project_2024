package org.sty.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sty.model.Student;
import org.sty.repository.StudentRepo;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    StudentRepo repo;

    public List<Student> getAllStudents() {
        return repo.findAll();
    }
    public Student getStudentById(int id) {
        return repo.findById(id).get();
    }

    public Student addStudent(Student student) {
        return repo.save(student);
    }

    public Student updateStudent(int id, Student student) {
        Student existingStudent = repo.findById(id).orElseThrow(() -> new RuntimeException("Student not found with id " + id));

        existingStudent.setId(student.getId());
        existingStudent.setName(student.getName());
        existingStudent.setAge(student.getAge());

        return repo.save(existingStudent);
    }

    public void deleteStudent(int id) {
        repo.deleteById(id);
    }


}
