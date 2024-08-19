(
var project = Project();
project.includeFile("variables", openInIde: true);
project.includeFile("buses", openInIde: false);
project.includeFile("groups", openInIde: true);
project.includeFile("hardware", openInIde: false);
project.includeFile("synthdefs", openInIde: true);
project.includeFile("synths", openInIde: true);
project.includeFile("sequences", openInIde: true);
project.includeFile("play", openInIde: true, runContents: false);
project.setupProject();
)