# Course Component Testing Guide

## Test the course component in Karaf console

### 1. Check Bundle Status
```bash
bundle:list | grep course
# Should show: 59 │ Active │  80 │ 1.0.0.SNAPSHOT │ course-component
```

### 2. Restart Course Bundle to Trigger Activation
```bash
bundle:restart 59
```

After restart, you should see:
```
=== CourseServiceImpl ACTIVATED ===
✓ Course registered: CS101 - Introduction to Programming
✓ Course registered: CS102 - Data Structures
✓ Sample courses initialized: CS101, CS102
```

### 3. Check SCR Component Status
```bash
scr:list | grep -i course
```

CourseServiceImpl should show `State:ACTIVE` (not SATISFIED)

### 4. Test Course Commands

#### UC1: Create Course
```bash
course:create "Web Development" LEARNING_PATH COACH001
course:copy CS101 "Intro to Programming - Copy" COACH001
```

#### UC2: List and View Courses
```bash
course:list
course:list --owner COACH001
course:info CS101
```

#### UC3: Manage Members
```bash
course:add-member CS101 USER001 "John Doe" john@example.com PARTICIPANT COACH001
course:members CS101
course:update-role CS101 USER001 COACH COACH001
course:remove-member CS101 USER001 COACH001
```

#### UC4: Course Structure
```bash
course:structure CS101
course:add-element CS101 null "Week 1" STRUCTURE COACH001
course:validate CS101
```

#### UC5: Learning Resources
```bash
course:create-resource "Java Tutorial" "Comprehensive Java guide" TEST COACH001
course:resources
course:link-resource CS101 ELEM001 RES001 COACH001
```

### 5. Alternative: Test via Service
If commands don't work, test the service directly using a Java client or by examining logs:

```bash
log:display | grep -i course
```

### Troubleshooting

If commands show "Cannot invoke getClass() because target is null":
1. The CourseService reference is not being injected
2. Try: `bundle:restart 59`
3. Check: `service:list | grep CourseService` - should show the service registered
4. The issue is likely that CourseServiceImpl needs to be ACTIVE, not just SATISFIED

The difference between SATISFIED and ACTIVE:
- SATISFIED = All dependencies met, but service not activated
- ACTIVE = Service actually running and available

To force activation, the `immediate = true` should work, but if not, try uninstalling and reinstalling:
```bash
bundle:uninstall 59
bundle:install -s file:/Users/lichee/Documents/GitHub/openolat-osgi/course-component/target/course-component-1.0.0-SNAPSHOT.jar
```
