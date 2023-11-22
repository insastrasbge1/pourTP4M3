/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.coulibaly.projetm3.vuesV2;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import fr.insa.coulibaly.projetm3.model.GestionBDD;
import java.sql.SQLException;
import java.util.Optional;

/**
 *
 * @author Amadou Coulibaly
 */
public class EnteteAvantLogin extends HorizontalLayout {

    private TextField tfNom;
    private Button bLogin;
    private Button bInscription;

    public EnteteAvantLogin() {

        this.tfNom = new TextField("Nom");
        this.bLogin = new Button("Login");
        this.bInscription = new Button("Inscription");
        this.bInscription.getStyle().set("background-color", "rgb(255, 255, 128)");
        this.add(this.tfNom, this.bLogin, this.bInscription);
    }

}
