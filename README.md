# Rosenrudel Manual Download

## Kommandozeilen-Tool

Lädt die (meisten) Dateien für das "Standard" oder "WW2" repository herunter. Kann helfen, wenn das Sync Tool sich im Download aufhängt.

## Benutzung
Starte das Tool mit `java -jar RosenrudelManualDownload.jar`. Es wird nach den [Einstellungen](https://github.com/Alf-Melmac/RosenrudelManualDownload#einstellungen) gefragt.

Nach Beendigung des Downloads, dass Tool gibt darüber Bescheid und beendet sich, muss das Sync Tool neu gestartet werden und das gewünschte Repository ausgewählt werden.
Dann erkennt Sals die geladenen Dateien von selbst und fügt oder updated noch letzte Dateien. Dieser zweite Download sollte ziemlich klein sein.

## Einstellungen
Bei jedem Start des Tools werden folgende Einstellungen abgefragt:

| Einstellung | Standard | Erklärung |
| :---------- | :------- | :-------- |
| Speicherpfad  | - | Wie im Sals Launcher konfiguriert. Standardmäßig unter C:\Users\\{USERNAME}\AppData\Roaming\sals-rosenrudel\gSync |
| Sollen bereits vorhandene Dateien ersetzt werden? | Nein | Durch einen vorhergegangen Download oder bereits von Sals gedownloadete Dateien werden bei "Ja" mit dem neuen Download überschrieben. |
| Sollen Dateien die keinem Unterordner zugeordnet werden können übersprungen werden? | Nein | Beim Download werden Unterordner (0, 2, 3, 5[, 6]) angelegt. Potenziell können nicht alle Downloads in einen dieser Unterordner eingeordnet werden. Falls "Ja" wird ein Ordner *manualDownload* angelegt aus dem die Ordner/Dateien dann manuell verschoben werden müssen. |
| Repository oder Bucket Urls | - | Die Auswahl der unterstützen Repositories aus Sals oder die Angabe eigener Urls.

## Download
Letztes Release: 19.06.2020

https://alf.codes/rrDownload

Alternative: https://www.dropbox.com/s/dhvd0ys2fcjxilg/RosenrudelManualDownload-1.1.jar?dl=0

## Debugging
**:warning: Diese Einstellungen können die Anwendung sehr langsam machen oder unerwartet beenden. Sollte nur von erfahrenen Anwendern verwendet werden.**

Mit dem Aufruf `java -jar RosenrudelManualDownload.jar dev` können die Dev-Optionen aktiviert werden.

| Zahl | Option | Eingabemöglichkeiten |
| :--- | :---------- | :-------- |
| 0 | Set debug level | [OFF], SEVERE, WARNING, INFO, ALL |
| 1 | print know subdirectories and mods in them | Gibt alle Unterordnernamen und den darin bekannten Unterordnern aus |
| Alles andere | Exit dev-options | - |
