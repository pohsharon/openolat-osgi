#!/usr/bin/env python3
import re
import glob

def fix_command_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()
    
    # Extract command scope and name from @Command annotation
    command_match = re.search(r'@Command\(scope = "(\w+)", name = "([^"]+)"', content)
    if not command_match:
        print(f"Skipping {filepath} - no @Command found")
        return
    
    scope = command_match.group(1)
    name = command_match.group(2)
    function_name = name.replace('-', '_')  # Convert kebab-case to snake_case for function name
    
    # Replace @Component(service = Action.class) with full version including properties
    content = re.sub(
        r'@Component\(service = Action\.class\)',
        f'@Component(\n    service = Action.class,\n    property = {{\n        "osgi.command.scope={scope}",\n        "osgi.command.function={function_name}"\n    }}\n)',
        content
    )
    
    # Change MANDATORY back to OPTIONAL
    content = content.replace('ReferenceCardinality.MANDATORY', 'ReferenceCardinality.OPTIONAL')
    
    # Add null check back if missing
    if 'if (courseService == null)' not in content:
        # Find @Override line and add null check after it
        content = re.sub(
            r'(@Override\s+public Object execute\(\) throws Exception \{\s*\n)',
            r'\1        if (courseService == null) {\n            System.out.println("ERROR: CourseService is not available");\n            return null;\n        }\n\n',
            content
        )
    
    with open(filepath, 'w') as f:
        f.write(content)
    
    print(f"Fixed: {filepath} -> {scope}:{function_name}")

command_files = glob.glob('/Users/lichee/Documents/GitHub/openolat-osgi/course-component/src/main/java/my/um/cbse/course/command/*Command.java')

for filepath in command_files:
    fix_command_file(filepath)

print("\nDone! Rebuild and the commands should work with @Option support.")
