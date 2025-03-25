package roig.utils;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;

/**
 *
 * @author Admin
 */
public class PlaceHolder {
    public static void addPlaceholder(JTextField textField, String placeholderText) {
        textField.setText(placeholderText);
        textField.setForeground(Color.GRAY);
        
        textField.addFocusListener(new FocusAdapter(){
            @Override
            public void focusGained(FocusEvent e){
                if(textField.getText().equals(placeholderText)){
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e){
                if(textField.getText().isEmpty()){
                    textField.setText(placeholderText);
                    textField.setForeground(Color.BLACK);
                }
            }
        });
    }
}
