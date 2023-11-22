/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.coulibaly.projetm3.vuesV2;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
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
public class EnteteApresLogin extends HorizontalLayout {
    
   private Button bLogout;
    
    public EnteteApresLogin() {
        this.add(new Paragraph("bonjour toto"));
        this.bLogout = new Button("Logout");
        this.add(this.bLogout);
    }
    
}
