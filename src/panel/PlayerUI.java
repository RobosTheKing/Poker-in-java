package panel;

import java.awt.Button;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AttributeSet;

public class PlayerUI extends JPanel {
    private final Color border = new Color(255, 255, 255);
    private final Color inside = new Color(0, 0, 0, 50);
    private final DocumentFilter numberFilter = new DocumentFilter() {
        @Override
        public void insertString(final FilterBypass fb, final int offset, final String string, final AttributeSet attr)
                throws BadLocationException {
            if (string.matches("\\d+")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(final FilterBypass fb, final int offset, final int length, final String text, final AttributeSet attrs)
                throws BadLocationException {
            if (text.matches("\\d+")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    };

    public PlayerUI() {
        Button fold = new Button("fold");
        Button call = new Button("call");
        Button raise = new Button("raise");
        Button check = new Button("check");
        this.setLayout(null);
        this.add(fold);
        this.add(call);
        this.add(raise);
        this.add(check);

        JTextField field = new JTextField(10);

        ((AbstractDocument) field.getDocument()).setDocumentFilter(numberFilter);

        this.add(field);

        raise.setBounds(20, 20, 400, 100);
        call.setBounds(520, 20, 400, 100);
        raise.setBounds(1020, 20, 400, 100);
        check.setBounds(1520, 20, 400, 100);
        
    }
}