# About this project
This is a Kotlin To-Do app (single application project) for KTU module "Object-Oriented Programme Design".

# Features
- Add a new task 
- Mark a task as active or done
- Delete a task
- Filter tasks by:
  - Task progress
  - Assigned task urgency
  - Assigned task impact (how important the task is)
- See a summary of all performed actions and all tasks

# Possible task filters

## Progress
Inactive, Active, Done, Deleted

## Urgency (suggested tagging)
Low - flexible timing,

Moderate - can wait up to a week,

High - should be addressed within 2 days,

Immediate - needs resolution now,

## Impact
Low, Moderate, High, Critical

# Run locally (tested on Windows 11)

1. **clone the project**

2. **ensure JDK 27 is installed**
```
  https://www.oracle.com/apac/java/technologies/downloads/#jdk27-windows
```

3. **ensure gradle is installed. I used scoop package manager**
```bash
irm get.scoop.io | iex
```
```bash
scoop install gradle
```

4. **open project folder through terminal or vscode**

5. **run program**
```bash
.\gradlew :run
```

6. **if tests are needed, run this**
```bash
.\gradlew test

```
