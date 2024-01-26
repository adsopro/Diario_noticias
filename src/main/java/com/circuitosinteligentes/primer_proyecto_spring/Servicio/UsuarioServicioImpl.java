/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.circuitosinteligentes.primer_proyecto_spring.Servicio;

import com.circuitosinteligentes.primer_proyecto_spring.Entidades.Imagen;
import com.circuitosinteligentes.primer_proyecto_spring.Entidades.Noticia;
import com.circuitosinteligentes.primer_proyecto_spring.Entidades.Usuario;
import com.circuitosinteligentes.primer_proyecto_spring.Enumeraciones.Rol;
import com.circuitosinteligentes.primer_proyecto_spring.Repositorio.RepositorioUsuario;
import com.circuitosinteligentes.primer_proyecto_spring.exceptions.ApellidoInvalidoException;
import com.circuitosinteligentes.primer_proyecto_spring.exceptions.ArchivoInvalidoException;
import com.circuitosinteligentes.primer_proyecto_spring.exceptions.EmailInvalidoException;
import com.circuitosinteligentes.primer_proyecto_spring.exceptions.NombreInvalidoException;
import com.circuitosinteligentes.primer_proyecto_spring.exceptions.Password2InvalidoException;
import com.circuitosinteligentes.primer_proyecto_spring.exceptions.PasswordInvalidoException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;

import com.circuitosinteligentes.primer_proyecto_spring.interfaces.IUsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Julian_Velasco
 */
@Service
public class UsuarioServicioImpl implements IUsuarioServicio, UserDetailsService {

    @Autowired
    private RepositorioUsuario usuarioRepositorio;
    
    @Autowired
    private ImagenServicioImp imagenServicio;

   @Override
    public List<Usuario> findAll() {
        return usuarioRepositorio.findAll();
    }

    @Override
    public Optional<Usuario> getById(Integer id) {
        return usuarioRepositorio.findById(id);
    }


   @Override
    public void delete(Integer id) {
        usuarioRepositorio.deleteById(id);
    }

   @Override
    public Usuario save(Usuario usuario) {
        return usuarioRepositorio.save(usuario);
    }
    
    public Usuario getOne(Integer id) {
        return usuarioRepositorio.getOne(id);
    }

    @Transactional
        public void registrar(MultipartFile file, String nombre, String apellido, String email, String password, String password2)
            throws NombreInvalidoException, ApellidoInvalidoException, EmailInvalidoException,
            PasswordInvalidoException, Password2InvalidoException, ArchivoInvalidoException {
        validar(nombre, apellido, email, password, password2);
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setRol(Rol.USER);
        Imagen imagen = imagenServicio.save(file);
        usuario.setImagen(imagen);
        usuarioRepositorio.save(usuario);
    }

    @Transactional
    public void update(MultipartFile file, String nombre, String apellido, String email, String password,String password2, Integer idUsuario) throws NombreInvalidoException, ApellidoInvalidoException, EmailInvalidoException,
            PasswordInvalidoException, Password2InvalidoException, ArchivoInvalidoException {
        
        validar(nombre, apellido, email, password, password2);
        Optional<Usuario> respuesta= usuarioRepositorio.findById(idUsuario); 
        if(respuesta.isPresent()){
        Usuario usuario = respuesta.get();    
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setRol(Rol.USER);
        Integer idImagen = null;
        if(usuario.getImagen()!= null){
            idImagen = usuario.getImagen().getId();
        }
        Imagen imagen= imagenServicio.update(file, idImagen);
        usuario.setImagen(imagen);
        usuarioRepositorio.save(usuario);
        }
    }
    
    public void validar(String nombre, String apellido, String email, String password, String password2)
            throws NombreInvalidoException, ApellidoInvalidoException, EmailInvalidoException,
            PasswordInvalidoException, Password2InvalidoException {
        if (nombre.isEmpty()) {
            throw new NombreInvalidoException();
        }

        if (apellido.isEmpty()) {
            throw new ApellidoInvalidoException();
        }

        if (email.isEmpty()) {
            throw new EmailInvalidoException();
        }

        if (password == null || password.isEmpty() || password.length() <= 5) {
            throw new PasswordInvalidoException();
        }

        if (!password.equals(password2)) {
            throw new Password2InvalidoException();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.findByEmail(email);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()));
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        session.setAttribute("usuariosession", usuario);

        return new User(usuario.getEmail(), usuario.getPassword(), authorities);
    }

   
   
}
