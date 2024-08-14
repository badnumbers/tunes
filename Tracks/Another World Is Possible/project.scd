(
var project = Project();
project.includeFile("variables", openInIde: false);
project.includeFile("buses", openInIde: false);
project.includeFile("groups", openInIde: false);
project.includeFile("hardware", openInIde: false);
project.includeFile("synthdefs", openInIde: false);
project.includeFile("synths", openInIde: false);
project.includeFile("sequences", openInIde: true);
project.includeFile("play", openInIde: true, runContents: false);
project.setupProject();
)