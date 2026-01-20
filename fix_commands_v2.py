#!/usr/bin/env python3
import re
import glob

def fix_command_file(filepath):
    with open(filepath, 'r') as f:
        lines = f.readlines()
    
    new_lines = []
    i = 0
    skip_until_execute = False
    
    while i < len(lines):
        line = lines[i]
        
        # Fix @Component annotation - keep it simple
        if '@Component(' in line:
            # Find the closing parenthesis
            annotation = line
            paren_count = line.count('(') - line.count(')')
            j = i + 1
            while paren_count > 0 and j < len(lines):
                annotation += lines[j]
                paren_count += lines[j].count('(') - lines[j].count(')')
                j += 1
            
            # Replace with simple version
            new_lines.append('@Component(service = Action.class)\n')
            i = j
            continue
        
        # Skip public method declarations (they call execute())
        if re.match(r'\s+public void \w+\([^\)]*\) throws Exception \{', line):
            skip_until_execute = True
            i += 1
            continue
        
        if skip_until_execute:
            if 'execute();' in line:
                # Skip until closing brace
                while i < len(lines) and '}' not in lines[i]:
                    i += 1
                i += 1  # Skip the closing brace line
                skip_until_execute = False
                continue
            i += 1
            continue
        
        # Fix ReferenceCardinality
        if 'ReferenceCardinality.OPTIONAL' in line:
            line = line.replace('ReferenceCardinality.OPTIONAL', 'ReferenceCardinality.MANDATORY')
        
        # Remove null check blocks for courseService
        if 'if (courseService == null)' in line:
            # Skip this if block
            while i < len(lines):
                i += 1
                if i >= len(lines):
                    break
                if lines[i].strip() == '}':
                    i += 1
                    # Skip empty line after if block
                    if i < len(lines) and lines[i].strip() == '':
                        i += 1
                    break
            continue
        
        new_lines.append(line)
        i += 1
    
    with open(filepath, 'w') as f:
        f.writelines(new_lines)

command_files = glob.glob('/Users/lichee/Documents/GitHub/openolat-osgi/course-component/src/main/java/my/um/cbse/course/command/*Command.java')

for filepath in command_files:
    print(f"Processing: {filepath}")
    fix_command_file(filepath)

print("\nDone!")
