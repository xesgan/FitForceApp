/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package roig.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
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

    public static void cambiarOpacidadImagen(JLabel label, float opacidad) {
        // Obtener la imagen original del JLabel
        ImageIcon iconoOriginal = (ImageIcon) label.getIcon();
        if (iconoOriginal == null) {
            return;
        }

        Image imgOriginal = iconoOriginal.getImage();

        // Crear nueva imagen con transparencia
        BufferedImage nuevaImagen = new BufferedImage(
                imgOriginal.getWidth(null),
                imgOriginal.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);

        // Aplicar la transparencia
        Graphics2D g = nuevaImagen.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacidad));
        g.drawImage(imgOriginal, 0, 0, null);
        g.dispose();

        // Establecer la nueva imagen en el label
        label.setIcon(new ImageIcon(nuevaImagen));
    }
}
