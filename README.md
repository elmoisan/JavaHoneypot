# JavaHoneypot

[![Java](https://img.shields.io/badge/Java-17+-orange?style=flat&logo=java)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-blue?style=flat&logo=apache-maven)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)
[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)]()

A **low-interaction honeypot system** written in Java designed to detect, log, and analyze network intrusion attempts. Perfect for cybersecurity students, researchers, and security professionals who want to understand attacker behavior patterns.

## Features

- **Multi-Service Simulation**: Emulate SSH, HTTP, and other common network services
- **Real-Time Logging**: Capture and log all connection attempts with detailed metadata
- **JSON Export**: Export attack logs in structured JSON format for analysis
- **Lightweight**: Low-interaction design minimizes system resource usage
- **Modular Architecture**: Easy to extend with additional services
- **Thread-Safe**: Handle multiple concurrent connections efficiently

## Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.8+
- Linux/macOS/Windows

### Installation

```bash
# Clone the repository
git clone https://github.com/YOUR_USERNAME/JavaHoneypot.git
cd JavaHoneypot

# Build the project
mvn clean package

# Run the honeypot
java -jar target/honeypot.jar
```

## Usage

### Basic Configuration

Edit `src/main/resources/config.properties`:

```properties
# Services to enable
services.ssh.enabled=true
services.ssh.port=2222

services.http.enabled=true
services.http.port=8080

# Logging configuration
logs.directory=./logs
logs.format=json
```

### Running the Honeypot

```bash
# Run with default configuration
java -jar target/honeypot.jar

# Run with custom config
java -jar target/honeypot.jar --config /path/to/config.properties

# Run with specific services
java -jar target/honeypot.jar --services ssh,http
```

### Example Output

```
[2025-02-22 14:30:15] [INFO] JavaHoneypot v1.0.0 starting...
[2025-02-22 14:30:15] [INFO] SSH Service started on port 2222
[2025-02-22 14:30:15] [INFO] HTTP Service started on port 8080
[2025-02-22 14:30:42] [ALERT] Connection attempt from 192.168.1.105:52341 to SSH (port 2222)
[2025-02-22 14:30:42] [INFO] Logged attack: ssh_brute_force_001.json
```

## Architecture

```
JavaHoneypot/
├── src/main/java/com/honeypot/
│   ├── core/              # Core honeypot engine
│   │   ├── HoneypotServer.java
│   │   ├── ConnectionHandler.java
│   │   └── Logger.java
│   ├── services/          # Service implementations
│   │   ├── Service.java
│   │   ├── SSHService.java
│   │   └── HTTPService.java
│   ├── models/            # Data models
│   │   ├── AttackLog.java
│   │   └── ConnectionInfo.java
│   ├── storage/           # Log storage
│   │   └── JsonLogStorage.java
│   └── utils/             # Utilities
│
├── logs/                  # Generated logs
├── docs/                  # Documentation
└── pom.xml               # Maven configuration
```

### Class Diagram

```
┌─────────────────────┐
│   HoneypotServer    │
│ ─────────────────── │
│ - services: List    │
│ + start()           │
│ + stop()            │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│   <<interface>>     │
│      Service        │
│ ─────────────────── │
│ + start()           │
│ + handleConnection()│
└─────────────────────┘
```

## Sample Log Output

```json
{
  "timestamp": "2025-02-22T14:30:42.123Z",
  "sourceIP": "192.168.1.105",
  "sourcePort": 52341,
  "destinationPort": 2222,
  "service": "SSH",
  "protocol": "TCP",
  "attackType": "brute_force",
  "payloadSize": 256,
  "payload": "SSH-2.0-OpenSSH_8.2p1 Ubuntu-4ubuntu0.5",
  "geolocation": {
    "country": "Unknown",
    "city": "Unknown"
  }
}
```

## Testing

```bash
# Run all tests
mvn test

# Run with coverage report
mvn test jacoco:report

# Test specific service
mvn test -Dtest=SSHServiceTest
```

## Security Considerations

**Important Security Notes:**

- **Isolation**: Always run honeypots in isolated environments (VMs, containers)
- **Firewall Rules**: Configure firewall to prevent honeypot from attacking other systems
- **Legal Compliance**: Understand local laws regarding network monitoring
- **Ethical Use**: Never use honeypots to counter-attack or harm attackers
- **Data Privacy**: Be aware of data protection regulations (GDPR, etc.)

## Roadmap

### Version 1.0 (Current)
- [x] Basic SSH service emulation
- [x] HTTP service emulation
- [x] JSON logging
- [x] Multi-threaded connection handling

### Version 2.0 (Planned)
- [ ] Web dashboard for log visualization
- [ ] IP geolocation integration
- [ ] Email/Slack notifications
- [ ] Attack pattern analysis
- [ ] Additional services (FTP, Telnet, MySQL)
- [ ] Docker containerization

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Author

- GitHub: [@elmoisan](https://github.com/elmoisan)
- LinkedIn: [Elodie Moisan](www.linkedin.com/in/elodie-moisan-233115276)

## Acknowledgments

- Inspired by modern honeypot systems like Cowrie and Kippo
- Built as a learning project for cybersecurity education
- Special thanks to the open-source security community

## Resources

- [Honeypot Concepts](https://en.wikipedia.org/wiki/Honeypot_(computing))
- [Java Network Programming](https://docs.oracle.com/javase/tutorial/networking/)
- [OWASP Security Guidelines](https://owasp.org/)

---

**Star this repository if you find it useful!**
