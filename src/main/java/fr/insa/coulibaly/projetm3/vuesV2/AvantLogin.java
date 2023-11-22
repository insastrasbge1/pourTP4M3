/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.coulibaly.projetm3.vuesV2;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 *
 * @author Amadou Coulibaly
 */
public class AvantLogin extends VerticalLayout{
        
    public AvantLogin(VuePrincipale main) {
        this.add(new H3("merci de vous connecter ou vous inscrire"));
    }
    
}
