File Organizer Utility
A simple Java-based desktop application to automatically organize files in a selected folder by category and file extension. Built with Swing, this GUI utility helps declutter your directories in just one click.

Features
Organize files by category (Images, Documents, Music, etc.)

Automatically sort into subfolders by file extension

Undo the last organization operation

Edit and customize file type-to-category mappings

Real-time logging of all file moves

Lightweight and easy to use interface

Screenshot
(Add a screenshot here if you'd like)

How to Run
Clone the repository


git clone https://github.com/Generalbloopers/FileOrganizerUtility.git

cd FileOrganizerUtility

Open in NetBeans or any Java IDE

Run FileOrganizerApp.java

Make sure you’re using Java 8 or higher.

Build Instructions
To create a .jar file:

javac -d bin src/fileorganizer/*.java

jar cfe FileOrganizerUtility.jar fileorganizer.FileOrganizerApp -C bin .

Configuration

Click "Manage Categories" in the app to customize which file extensions map to which categories. For example:

Extension	Category
.jpg	Images
.pdf	PDFs
.docx	Documents

Planned Features
Save/load custom mappings to a file

Redo functionality

Theme customization (light/dark mode)

Cross-platform .jar or installer

License
MIT License – free to use, modify, and share.

