## Welcome to GitHub Pages

UltimateShell is a fully open-source, ultimate toolbox for remote management server.

UltimateShell is your ultimate toolbox for remote management server. Its goal is to provide a large number of customized features for penetration test engineers, programmers, webmasters, IT administrators, and almost all users who need to handle remote work in a simpler way. And, it also supports multiple operating system platforms and theme skin switching based on FlatLaf.

### Security

See [the security file](SECURITY.md)!

### Features

- [x] Support multi-platform
- [x] Support 63 theme skins
- [x] Support sessions manager
- [x] Support local terminal
- [x] Support ssh, sftp
- [x] Support serial terminal
- [x] Support Telnet
- [x] Support RDP
- [x] Support VNC
- [ ] <del>Support FTP</del>

### Build

```
# 1. JDK 11+
# 2. Install dependencies to LocalRepository: jediterm-pty-2.49.jar, jediterm-ssh-2.49.jar, terminal-2.54.jar, jediterm-typeahead-2.54.jar
mvn install:install-file -Dfile=C:\Users\G3G4X5X6\IdeaProjects\ultimateshell\src\main\resources\libs/jediterm-pty-2.49.jar -DgroupId=com.g3g4x5x6  -DartifactId=jediterm-pty -Dversion=2.49 -Dpackaging=jar
mvn install:install-file -Dfile=C:\Users\G3G4X5X6\IdeaProjects\ultimateshell\src\main\resources\libs/jediterm-ssh-2.49.jar -DgroupId=com.g3g4x5x6  -DartifactId=jediterm-ssh -Dversion=2.49 -Dpackaging=jar
mvn install:install-file -Dfile=C:\Users\Security\IdeaProjects\UltimateShell\src\main\resources\libs/terminal-2.54.jar -DgroupId=com.g3g4x5x6  -DartifactId=terminal -Dversion=2.54 -Dpackaging=jar
mvn install:install-file -Dfile=C:\Users\Security\IdeaProjects\UltimateShell\src\main\resources\libs/jediterm-typeahead-2.54.jar -DgroupId=com.g3g4x5x6  -DartifactId=jediterm-typeahead -Dversion=2.54 -Dpackaging=jar
mvn install:install-file -Dfile=C:\Users\Security\IdeaProjects\UltimateShell\src\main\resources\libs/tightvnc-jviewer.jar -DgroupId=com.g3g4x5x6  -DartifactId=tightvnc-jviewer -Dversion=2.8.3 -Dpackaging=jar 
```

### Usage

```
# JDK 11+ 
java -jar UltimateShell-${version}-SNAPSHOT-jar-with-dependencies.jar
# Or double click
```
See [Project wiki](https://github.com/G3G4X5X6/ultimateshell/wiki)

### Libraries
- JediTerm: [https://github.com/JetBrains/jediterm](https://github.com/JetBrains/jediterm)
- FlatLaf: [https://github.com/JFormDesigner/FlatLaf](https://github.com/JFormDesigner/FlatLaf)
- Apache MINA SSHD: [https://github.com/apache/mina-sshd](https://github.com/apache/mina-sshd)
- RSyntaxTextArea: [https://github.com/bobbylight/RSyntaxTextArea](https://github.com/bobbylight/RSyntaxTextArea)
- More...


### Maintainers

[@G3G4X5X6](https://github.com/G3G4X5X6)

### Contributing

See [the contributing file](contributing.md)!

PRs accepted.

Small note: If editing the README, please conform to the [standard-readme](https://github.com/RichardLitt/standard-readme) specification.

### License

MIT © 2021 勾三股四弦五小六


### Stargazers over time

[![Stargazers over time](https://starchart.cc/G3G4X5X6/ultimateshell.svg)](https://starchart.cc/G3G4X5X6/ultimateshell)

### Support or Contact

Having trouble with Pages? Check out our [documentation](https://docs.github.com/categories/github-pages-basics/) or [contact support](https://support.github.com/contact) and we’ll help you sort it out.
