/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.circuitosinteligentes.primer_proyecto_spring.interfaces;

import com.circuitosinteligentes.primer_proyecto_spring.Entidades.Usuario;
import com.circuitosinteligentes.primer_proyecto_spring.exceptions.ApellidoInvalidoException;
import com.circuitosinteligentes.primer_proyecto_spring.exceptions.ArchivoInvalidoException;
import com.circuitosinteligentes.primer_proyecto_spring.exceptions.EmailInvalidoException;
import com.circuitosinteligentes.primer_proyecto_spring.exceptions.NombreInvalidoException;
import com.circuitosinteligentes.primer_proyecto_spring.exceptions.Password2InvalidoException;
import com.circuitosinteligentes.primer_proyecto_spring.exceptions.PasswordInvalidoException;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Julian_Velasco
 */
public interface IUsuarioServicio {

    public List<Usuario> findAll();
    public Optional<Usuario> getById(Integer id);
    public void update(MultipartFile file, String nombre, String apellido, String email, String password,String password2, Integer idUsuario)
            throws NombreInvalidoException, ApellidoInvalidoException, EmailInvalidoException,
            PasswordInvalidoException, Password2InvalidoException, ArchivoInvalidoException;
    public void delete(Integer id);
    public Usuario save(Usuario usuario);
    public void registrar(MultipartFile file, String nombre, String apellido, String email, String password, String password2)
            throws NombreInvalidoException, ApellidoInvalidoException, EmailInvalidoException,
            PasswordInvalidoException, Password2InvalidoException, ArchivoInvalidoException;

    public void validar(String nombre, String apellido, String email, String password, String password2)
            throws NombreInvalidoException, ApellidoInvalidoException, EmailInvalidoException,
            PasswordInvalidoException, Password2InvalidoException;
    
}
