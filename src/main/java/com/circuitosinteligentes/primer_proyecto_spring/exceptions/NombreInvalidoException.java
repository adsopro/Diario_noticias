/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.circuitosinteligentes.primer_proyecto_spring.exceptions;
/**
 *
 * @author Julian_Velasco
 */
public class NombreInvalidoException extends Exception {
    public NombreInvalidoException() {
        super("El nombre no puede ser nulo o estar vacío");
    }
}