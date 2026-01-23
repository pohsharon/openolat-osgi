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

### Course Management Commands (UC1 - UC5)
| Description                   | Command                                                                 | Example                                                                 |
| :---------------------------- | :---------------------------------------------------------------------- | :---------------------------------------------------------------------- |
| Create a new course           | `course:create <title> <ownerId>`                                      | `course:create "Machine Learning" OWNER001`                             |
| Copy an existing course       | `course:copy <sourceCourseId> <ownerId>`                               | `course:copy CS101 OWNER002`                                            |
| Delete a course               | `course:delete <courseId> <userId>`                                    | `course:delete CS103 OWNER001`                                          |
| Update course basic info      | `course:update_info <courseId> <title> <description> <courseCode> <userId>` | `course:update_info CS101 "Database Systems Advanced" "Updated description" "CS101-V2" COACH001` |
| Update course metadata        | `course:update_metadata <courseId> <semester> <credits> <license> <institution> <userId>` | `course:update_metadata CS101 "2024-2" 4 "MIT-License" "University of Computing" COACH001` |
| Add member to course          | `course:add_member <courseId> <userId> <userName> <role> <addedBy>`    | `course:add_member CS101 USER001 "John Doe" COACH OWNER001`             |
| Update member role            | `course:update_role <courseId> <userId> <newRole> <updatedBy>`         | `course:update_role CS101 USER001 OWNER OWNER001`                       |
| Remove member from course     | `course:remove_member <courseId> <userId> <removedBy>`                | `course:remove_member CS101 USER002 OWNER001`                           |
| Add course element            | `course:add_element <courseId> <title> <type> <userId>`                | `course:add_element CS101 "Week 1 - Introduction" STRUCTURE OWNER001`   |
| Move course element           | `course:move_element <courseId> <elementId> <newParentId> <newOrder> <userId>` | `course:move_element CS101 ELEM001 ROOT 0 OWNER001`            |
| Delete course element         | `course:delete_element <courseId> <elementId> <userId>`               | `course:delete_element CS101 ELEM001 OWNER001`                          |
| Create learning resource      | `course:create_resource <title> <type> <ownerId>`                     | `course:create_resource "Lecture Video 1" VIDEO OWNER001`               |
| Link resource to element      | `course:link_resource <courseId> <elementId> <resourceId> <userId>`   | `course:link_resource CS101 ELEM001 RES001 OWNER001`                    |
| Delete learning resource      | `course:delete_resource <resourceId> <userId>`                        | `course:delete_resource RES001 OWNER001`                                |




### Assessment Management Commands (UC6 - UC10)

| Description                     | Command                                                          | Example                                                   |
| :------------------------------ | :--------------------------------------------------------------- | :-------------------------------------------------------- |
| Create a new assessment         | `cbse:createAssessment <courseId> <title> <maxScore> <deadline>` | `cbse:createAssessment CS101 "Final Exam" 100 2026-01-10` |
| Submit an assignment            | `cbse:submitAssignment <assessmentId> <studentId> <content>`     | `cbse:submitAssignment A001 STU01 "My final answers"`     |
| Grade a submission              | `cbse:gradeSubmission <submissionId> <score> <feedback>`         | `cbse:gradeSubmission S001 85 "Good effort"`              |
| View submissions for assessment | `cbse:listSubmissions <assessmentId>`                            | `cbse:listSubmissions A001`                               |
| View grades for an assessment   | `cbse:viewGrades <assessmentId>`                                 | `cbse:viewGrades A001`                                    |
| View assessment details         | `cbse:viewAssessment <assessmentId>`                             | `cbse:viewAssessment A001`                                |

### Enrollment Commands (UC11 - UC)

| Description | Command | Example |
| :----------------------- | :----------------------------------------------- | :----------------------------------------------- |
| Enrol a student | `cbse:enrol <courseId> <studentId> <role> <addedBy>` | `cbse:enrol CS101 STU002 STUDENT OWNER001` |
| Manually enrol (admin/lecturer) | `cbse:manualEnrol <studentId> <courseId> <assigneeId> <role>` | `cbse:manualEnrol STU002 CS101 LEC001 LECTURER` |
| Unenrol a student | `cbse:unenrol <courseId> <studentId> <removedBy>` | `cbse:unenrol CS101 STU002 OWNER001` |
| Manual unenrol | `cbse:manualUnenrol <studentId> <courseId> <removedBy>` | `cbse:manualUnenrol STU002 CS101 ADMIN001` |
| Create a student group | `cbse:createGroup <courseId> <groupId> <name> <createdBy>` | `cbse:createGroup CS101 G1 "Project Group 1" LEC001` |
| Assign student to group | `cbse:assignStudentToGroup <courseId> <groupId> <studentId> <assignedBy>` | `cbse:assignStudentToGroup CS101 G1 STU002 LEC001` |
| View groups for a course | `cbse:viewGroups <courseId>` | `cbse:viewGroups CS101` |
| View course enrollments | `cbse:viewCourseEnrollments <courseId>` | `cbse:viewCourseEnrollments CS101` |
| Find student | `cbse:findStudent <studentId|name>` | `cbse:findStudent STU002` |
| Show available spots | `cbse:availableSpots <courseId>` | `cbse:availableSpots CS101` |
| Mark attendance | `cbse:markAttendance <courseId> <sessionId> <studentId> <present|absent> <markedBy>` | `cbse:markAttendance CS101 S1 STU002 present LEC001` |
| Generate attendance report | `cbse:attendanceReport <courseId> <sessionId>` | `cbse:attendanceReport CS101 S1` |
| Update participation score | `cbse:updateParticipation <courseId> <studentId> <score> <updatedBy>` | `cbse:updateParticipation CS101 STU002 8 LEC001` |
| View participation score | `cbse:participationScore <courseId> <studentId>` | `cbse:participationScore CS101 STU002` |
| Enrollment info | `cbse:enrollmentInfo <courseId> <studentId>` | `cbse:enrollmentInfo CS101 STU002` |
| Enrollment history | `cbse:enrollmentHistory <studentId>` | `cbse:enrollmentHistory STU002` |

### Communication Commands (UC18 - UC20)

| Description              | Command                                          | Example                                          |
| :----------------------- | :----------------------------------------------- | :----------------------------------------------- |
| Create a forum topic     | `cbse:createTopic <courseId> <title> <author>`   | `cbse:createTopic CS101 "OSGi Exam Tips" Eugene` |
| List topics for a course | `cbse:listTopics <courseId>`                     | `cbse:listTopics CS101`                          |
| Add a reply to a topic   | `cbse:replyToTopic <topicId> <message> <author>` | `cbse:replyToTopic topic_1 "Great advice" Alice` |
| View replies for a topic | `cbse:viewReplies <topicId>`                     | `cbse:viewReplies topic_1`                       |
| Lock/unlock a topic      | `cbse:lockTopic <topicId> <true\|false>`         | `cbse:lockTopic topic_1 true`                    |
| Edit a topic title       | `cbse:editTopic <topicId> <newTitle>`            | `cbse:editTopic topic_1 "Updated Title"`         |
| Delete a reply           | `cbse:deleteReply <topicId> <replyIndex>`        | `cbse:deleteReply topic_1 0`                     |
| Delete a topic           | `cbse:deleteTopic <topicId>`                     | `cbse:deleteTopic topic_1`                       |

### Scheduling Management Commands (UC - UC)
