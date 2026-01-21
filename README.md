# OpenOLAT OSGi System (CBSE Assignment)

This repository contains the **OSGi-based implementation** of the system for the CBSE assignment, using **Apache Karaf** and **OSGi Declarative Services (DS)**.


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
**Step 2 – Install all components**

```bash
bundle:install -s file:/ABS_PATH/openolat-osgi/course-component/target/assessmcourseent-component-1.0.0-SNAPSHOT.jar
bundle:install -s file:/ABS_PATH/openolat-osgi/assessment-component/target/assessment-component-1.0.0-SNAPSHOT.jar
bundle:install -s file:/ABS_PATH/openolat-osgi/enrollment-component/target/enrollment-component-1.0.0-SNAPSHOT.jar
bundle:install -s file:/ABS_PATH/openolat-osgi/scheduling-component/target/scheduling-component-1.0.0-SNAPSHOT.jar
bundle:install -s file:/ABS_PATH/openolat-osgi/communication-component/target/communication-component-1.0.0-SNAPSHOT.jar
```

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

## 🧑🏻‍💻 Command Reference

### Assessment Management Commands (UC6 - UC10)
| Description                     | Command                                                        | Example                                         |
|:--------------------------------|:---------------------------------------------------------------|:------------------------------------------------|
| Create a new assessment         | `cbse:createAssessment <courseId> <title> <maxScore> <deadline>`| `cbse:createAssessment CS101 "Final Exam" 100 2026-01-10` |
| Submit an assignment            | `cbse:submitAssignment <assessmentId> <studentId> <content>`    | `cbse:submitAssignment A001 STU01 "My final answers"` |
| Grade a submission              | `cbse:gradeSubmission <submissionId> <score> <feedback>`        | `cbse:gradeSubmission S001 85 "Good effort"`     |
| View submissions for assessment | `cbse:listSubmissions <assessmentId>`                           | `cbse:listSubmissions A001`                      |
| View grades for an assessment   | `cbse:viewGrades <assessmentId>`                                | `cbse:viewGrades A001`                           |
| View assessment details         | `cbse:viewAssessment <assessmentId>`                            | `cbse:viewAssessment A001`                       |
