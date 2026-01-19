# OpenOLAT OSGi System (CBSE Assignment)

This repository contains the **OSGi-based implementation** of the system for the CBSE assignment, using **Apache Karaf** and **OSGi Declarative Services (DS)**.

Each team member is responsible for implementing **one component** as an OSGi bundle.

---

## 📦 System Components

- `common-api` – Shared interfaces and DTOs
- `course-component` - Chai Li Chee
- `assessment-component` - Poh Sharon
- `enrollment-component` - Goh Kah Kheng
- `scheduling-component` - Al Rubab Ibn Yeahyea
- `communication-component` - Eugene See Yi Le

---

## 🛠 Prerequisites

Make sure you have the following installed:

- **Java 17**
  ```bash
  java -version
  ```
- **Maven 3.8+**
  ```bash
  mvn -version
  ```
- **Apache Karaf 4.4.x**  
  [Download here](https://karaf.apache.org/download.html) (Choose Karaf OSGi Runtime 4.4.x – Binary Distribution)

---

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/pohsharon/openolat-osgi.git
cd openolat-osgi
```

### 2. Build the Project
From the project root:
```bash
mvn clean package
```
✔️ This should complete without errors.  
✔️ It builds all components and generates OSGi bundles (`.jar` files).

### 3. Start Apache Karaf
Navigate to your Karaf installation:
```bash
cd <KARAF_HOME>/bin
./karaf
```
You should see:
```
karaf@root()>
```

### 4. Enable OSGi Declarative Services (**IMPORTANT**)
Inside the Karaf shell, run:
```bash
feature:install scr
```
Verify:
```bash
scr:list
```
Declarative Services are required for `@Component` and `@Reference` to work.

### 5. Install Bundles into Karaf
⚠️ **Bundle installation order matters!**

**Step 1 – Install `common-api`**
```bash
bundle:install -s file:/ABS_PATH/openolat-osgi/common-api/target/common-api-1.0.0-SNAPSHOT.jar
```
**Step 2 – Install your component only**

Example for `assessment-component`:
```bash
bundle:install -s file:/ABS_PATH/openolat-osgi/assessment-component/target/assessment-component-1.0.0-SNAPSHOT.jar
```
Replace `assessment-component` with the component you are responsible for.

---

### 6. Verify Installation
In Karaf:
```bash
bundle:list
scr:list
```
Your component should be:
- **Installed**
- **Active**
- **Registered as a service** (if implemented)

---

### 7. Start Implementing Your Component

- **Interfaces** → `common-api`
- **Implementation** → your own component module  
  (e.g. `assessment-component/src/main/java/...`)

#### OSGi Service Example
```java
@Component(service = AssessmentService.class)
public class AssessmentServiceImpl implements AssessmentService {
    // implementation
}
```

