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
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class FileOrganizerApp extends JFrame {

    private static final Map<String, String> extensionMap = new HashMap<>();
    static {
        extensionMap.put("jpg", "Images");
        extensionMap.put("png", "Images");
        extensionMap.put("jpeg", "Images");
        extensionMap.put("gif", "Images");

        extensionMap.put("txt", "Text Files");
        extensionMap.put("doc", "Documents");
        extensionMap.put("docx", "Documents");
        extensionMap.put("pdf", "PDFs");

        extensionMap.put("mp3", "Music");
        extensionMap.put("wav", "Music");

        extensionMap.put("mp4", "Videos");
        extensionMap.put("mov", "Videos");
        extensionMap.put("avi", "Videos");

        extensionMap.put("zip", "Archives");
        extensionMap.put("rar", "Archives");
    }

    private final JTextField pathField;
    private final JLabel statusLabel;
    private final JTextArea logArea;
    private final Stack<Map<Path, Path>> undoStack = new Stack<>();

    public FileOrganizerApp() {
        setTitle("File Organizer Utility");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton browseButton = new JButton("Choose Folder");
        JButton organizeButton = new JButton("Organize Files");
        JButton categoryButton = new JButton("Manage Categories");
        JButton undoButton = new JButton("Undo Last Action");

        pathField = new JTextField(35);
        pathField.setEditable(false);
        statusLabel = new JLabel(" ");

        logArea = new JTextArea(12, 50);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        browseButton.addActionListener(this::chooseFolder);
        organizeButton.addActionListener(this::organizeFiles);
        categoryButton.addActionListener(e -> new CategoryManagerDialog(this, extensionMap).setVisible(true));
        undoButton.addActionListener(e -> undoLastMove());

        add(browseButton);
        add(pathField);
        add(organizeButton);
        add(categoryButton);
        add(undoButton);
        add(statusLabel);
        add(scrollPane);
    }

    private void chooseFolder(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selected = chooser.getSelectedFile();
            pathField.setText(selected.getAbsolutePath());
            log("Selected folder: " + selected.getAbsolutePath());
        }
    }

    private void organizeFiles(ActionEvent e) {
        String path = pathField.getText();
        if (path.isEmpty()) {
            statusLabel.setText("Please choose a folder first.");
            return;
        }

        File folder = new File(path);
        if (!folder.exists() || !folder.isDirectory()) {
            statusLabel.setText("Invalid folder.");
            return;
        }

        File[] files = folder.listFiles();
        if (files == null) {
            statusLabel.setText("No files found.");
            return;
        }

        log("Organizing files in: " + path);
        Map<Path, Path> currentMove = new HashMap<>();

        for (File file : files) {
            if (file.isFile()) {
                String ext = getFileExtension(file);
                String category = extensionMap.getOrDefault(ext, "Other");

                File categoryDir = new File(folder, category);
                File extSubDir = new File(categoryDir, ext); // Subfolder for extension

                if (!extSubDir.exists()) {
                    extSubDir.mkdirs();
                    log("Created: " + extSubDir.getAbsolutePath());
                }

                try {
                    Path sourcePath = file.toPath();
                    Path targetPath = new File(extSubDir, file.getName()).toPath();
                    Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    currentMove.put(targetPath, sourcePath);
                    log("Moved: " + file.getName() + " → " + category + "/" + ext);
                } catch (IOException ex) {
                    log("❌ Failed to move: " + file.getName());
                }
            }
        }

        if (!currentMove.isEmpty()) {
            undoStack.push(currentMove);
            log("🔁 You can undo this action.");
        }

        statusLabel.setText("Files organized successfully.");
        log("✅ Done organizing.\n");
    }

    private void undoLastMove() {
        if (undoStack.isEmpty()) {
            log("No action to undo.");
            return;
        }

        Map<Path, Path> lastMove = undoStack.pop();
        for (Map.Entry<Path, Path> entry : lastMove.entrySet()) {
            try {
                Files.move(entry.getKey(), entry.getValue(), StandardCopyOption.REPLACE_EXISTING);
                log("Undid: " + entry.getKey().getFileName());
            } catch (IOException ex) {
                log("❌ Failed to undo: " + entry.getKey().getFileName());
            }
        }

        log("✅ Undo complete.\n");
    }

    private void log(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int dot = name.lastIndexOf('.');
        return (dot > 0) ? name.substring(dot + 1).toLowerCase() : "";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FileOrganizerApp app = new FileOrganizerApp();
            app.setVisible(true);
        });
    }
}
