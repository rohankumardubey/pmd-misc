/*
 * User: tom
 * Date: Jul 8, 2002
 * Time: 4:29:19 PM
 */
package net.sourceforge.pmd.jedit;

import org.gjt.sp.jedit.OptionPane;
import org.gjt.sp.jedit.AbstractOptionPane;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import net.sourceforge.pmd.RuleSetNotFoundException;
import net.sourceforge.pmd.RuleSet;
import net.sourceforge.pmd.Rule;

public class PMDOptionPane extends AbstractOptionPane implements OptionPane {

    public static class CheckboxList extends JList {
        public class CheckboxListCellRenderer implements ListCellRenderer {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JCheckBox box = (JCheckBox)value;
                box.setEnabled(isEnabled());
                box.setFont(getFont());
                box.setFocusPainted(false);
                box.setBorderPainted(true);
                box.setBorder(isSelected ? UIManager.getBorder("List.focusCellHighlightBorder") : new EmptyBorder(1,1,1,1));
                return box;
            }
        }

        public CheckboxList(Object[] args) {
            super(args);
            setCellRenderer(new CheckboxListCellRenderer());
            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    int index = locationToIndex(e.getPoint());
                    if (index != -1) {
                        JCheckBox box = (JCheckBox)getModel().getElementAt(index);
                        box.setSelected(!box.isSelected());
                        repaint();
                    }
                }
            });
        }
    }

    private SelectedRules rules;

    public PMDOptionPane() {
        super(PMDJEditPlugin.NAME);
        try {
            rules = new SelectedRules();
        } catch (RuleSetNotFoundException rsne) {
            rsne.printStackTrace();
        }
    }

    public void init() {
        removeAll();
        addComponent(new JLabel("Please see http://pmd.sf.net/ for more information"));
        JList list = new CheckboxList(rules.getAllBoxes());
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        addComponent(new JScrollPane(list));
    }

    public void save() {
        rules.save();
    }
}
