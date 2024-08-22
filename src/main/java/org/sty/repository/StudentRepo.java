package org.sty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.sty.model.Student;

@Repository
public interface StudentRepo extends JpaRepository<Student, Integer> {
}
