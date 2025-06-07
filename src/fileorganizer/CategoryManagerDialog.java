/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fileorganizer;

/**
 *
 * @author gener
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;

public class CategoryManagerDialog extends JDialog {

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final Map<String, String> extensionMap;

    public CategoryManagerDialog(JFrame parent, Map<String, String> extensionMap) {
        super(parent, "Manage Categories", true);
        this.extensionMap = extensionMap;

        setLayout(new BorderLayout());
        setSize(400, 300);
        setLocationRelativeTo(parent);

        JList<String> list = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(list);
        updateList();

        JButton addButton = new JButton("Add");
        JButton removeButton = new JButton("Remove");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener((ActionEvent e) -> {
            String ext = JOptionPane.showInputDialog(this, "Enter file extension (e.g., txt):");
            if (ext == null || ext.isBlank()) return;

            String category = JOptionPane.showInputDialog(this, "Enter category name (e.g., Text Files):");
            if (category == null || category.isBlank()) return;

            extensionMap.put(ext.toLowerCase(), category);
            updateList();
        });

        removeButton.addActionListener((ActionEvent e) -> {
            String selected = list.getSelectedValue();
            if (selected != null) {
                String ext = selected.split(" -> ")[0];
                extensionMap.remove(ext);
                updateList();
            }
        });
    }

    private void updateList() {
        listModel.clear();
        extensionMap.forEach((ext, cat) -> listModel.addElement(ext + " -> " + cat));
    }
}

