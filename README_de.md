[🇬🇧 Switch to English](README.md)

---

# 📚 VOEBB Abschlussprojekt – Bibliotheksverwaltungssystem

Eine **monolithische Spring-Boot**-Anwendung zur Verwaltung eines Bibliothekskatalogs (OPAC – Open Public Access Catalog), konzipiert für **Bibliotheksnutzer\*innen** und **Administrator\*innen**.

---

## 👥 Team

Dieses Projekt wurde entwickelt von:

* [Natalie Lazarev](https://github.com/nat-laz)
* [Mitali Soti](https://github.com/mitalisoti)
* [Alex Bruch](https://github.com/bruch-alex)
* [Marc Stiehm](https://github.com/Rikupp17)

---

## ✨ Überblick

Diese Anwendung simuliert ein vollständig ausgestattetes **Bibliothekssystem**, das alle zentralen Funktionen einer modernen öffentlichen Bibliothek abdeckt. Nutzer*innen können **Bücher durchsuchen, ausleihen und ihre Konten verwalten**, während Administrator*innen den Katalog pflegen, Ausleihen überwachen und Benutzer\*innen verwalten können.

---

## 🎯 Funktionen

### 👤 Nutzerbereich

* 📖 Bibliothekskatalog durchsuchen
* ✅ Benutzerkonto erstellen
* 📚 Bücher ausleihen
* ⏰ Überfällige Bücher einsehen und verwalten
* 🧾 Persönliche Ausleihhistorie anzeigen

### 🛠 Admin-Bereich

* ➕ Bücher hinzufügen, bearbeiten oder löschen
* 👥 Benutzer\*innen verwalten
* 📊 Übersicht über alle Ausleihen
* ❌ Verwaltung überfälliger Rückgaben
* 📚 und vieles mehr...

---

## 🧱 Technologie-Stack

| Ebene          | Technologie                        |
| -------------- | ---------------------------------- |
| **Backend**    | Java 17, Spring Boot               |
| **Frontend**   | Thymeleaf, Bootstrap 5, JavaScript |
| **Datenbank**  | PostgreSQL                         |
| **Build-Tool** | Maven                              |
| **Tests**      | JUnit                              |

> Umfangreiche Nutzung von benutzerdefiniertem JavaScript zur Erweiterung der Bootstrap-Funktionalitäten.

---

## 📁 Projektstruktur

```bash
voebb-final-project/
├── src/
│   ├── main/
│   │   ├── java/com/voebb/
│   │   │   ├── config/                 # Sicherheits- und App-Konfiguration
│   │   │   ├── controller/             # Web-Controller (Nutzer, Admin, Auth)
│   │   │   ├── model/                  # JPA-Entitäten (User, Book, Borrow, etc.)
│   │   │   ├── repository/             # Spring Data Repositories
│   │   │   ├── service/                # Geschäftslogik
│   │   │   ├── dto/                    # Data Transfer Objects für Formulare & Views
│   │   │   ├── exceptions/             # Eigene Ausnahmen und Fehlerbehandlung
│   │   │   └── job/                    # Geplante Tasks (z. B. Cron-Jobs)
│   │   ├── resources/
│   │   │   ├── templates/              # Thymeleaf-Templates
│   │   │   ├── static/                 # JS, CSS, Bilder
│   │   │   └── application.properties  # Spring-Boot-Konfiguration
│   └── test/                           # Unit- und Integrationstests
├── pom.xml                             # Maven-Konfiguration & Abhängigkeiten
└── README.md                           # Projektdokumentation
```

---

## 🔐 Sicherheit & Zugriff

* Keine öffentlichen API-Endpunkte
* Interne Endpunkte nur für Admins zugänglich (geschützt)
* Rollenbasierte Zugriffskontrolle für Admin-Funktionen

---

## 🧭 Entwicklungshinweise

* Die Anwendung ist monolithisch aufgebaut – für einfache Verwaltung und Übersicht.
* Cron-Jobs überprüfen regelmäßig überfällige Bücher und versenden ggf. Benachrichtigungen.
* Eigene Ausnahmen verbessern das Fehlermanagement auf Nutzer- und Admin-Seite.
* Interne Endpunkte sind über Spring Security rollenbasiert abgesichert.
* Bootstrap wurde durch individuelles JavaScript funktional erweitert.
