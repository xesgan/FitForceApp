/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package roig.utils;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

/**
 *
 * @author Admin
 */
public class UIUtils {

    public static void addHoverEffect(JButton button, Color glowColor, int thickness) {
        Border defaultBorder = button.getBorder(); // Guardar el borde original

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Aplicar el borde con efecto de "aura"
                button.setBorder(new GlowBorder(glowColor, thickness));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Restaurar el borde original
                button.setBorder(defaultBorder);
            }
        });
    }
}
