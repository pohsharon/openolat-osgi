#!/usr/bin/env python3
import re
import glob

# Pattern to match @Component annotation with properties
component_pattern = re.compile(
    r'@Component\(\s*service\s*=\s*Action\.class,?\s*property\s*=\s*\{[^}]*\}\s*\)',
    re.MULTILINE | re.DOTALL
)

# Pattern to match public method declarations (like public void list() throws Exception {)
public_method_pattern = re.compile(
    r'\n\s+public void \w+\([^\)]*\) throws Exception \{\s*\n(?:\s+this\.\w+ = \w+;\s*\n)*\s+execute\(\);\s*\n\s+\}\s*\n',
    re.MULTILINE
)

# Pattern to match OPTIONAL cardinality
optional_pattern = re.compile(
    r'ReferenceCardinality\.OPTIONAL',
    re.MULTILINE
)

# Pattern to match null check blocks
null_check_pattern = re.compile(
    r'\n\s+if \(courseService == null\) \{\s*\n\s+System\.out\.println\("ERROR: CourseService is not available"\);\s*\n\s+return null;\s*\n\s+\}\s*\n',
    re.MULTILINE
)

command_files = glob.glob('/Users/lichee/Documents/GitHub/openolat-osgi/course-component/src/main/java/my/um/cbse/course/command/*Command.java')

for filepath in command_files:
    with open(filepath, 'r') as f:
        content = f.read()
    
    original_content = content
    
    # Replace @Component annotation
    content = component_pattern.sub('@Component(service = Action.class)', content)
    
    # Remove public methods
    content = public_method_pattern.sub('\n', content)
    
    # Replace OPTIONAL with MANDATORY
    content = optional_pattern.sub('ReferenceCardinality.MANDATORY', content)
    
    # Remove null checks
    content = null_check_pattern.sub('\n', content)
    
    if content != original_content:
        with open(filepath, 'w') as f:
            f.write(content)
        print(f"Updated: {filepath}")
    else:
        print(f"No changes: {filepath}")

print("\nDone!")
