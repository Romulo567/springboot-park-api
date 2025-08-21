package spring_park_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring_park_api.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

}
