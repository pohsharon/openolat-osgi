#!/usr/bin/env python3
import re
import glob

def remove_options(filepath):
    with open(filepath, 'r') as f:
        content = f.read()
    
    # Remove @Option import
    content = re.sub(r'import org\.apache\.karaf\.shell\.api\.action\.Option;\n', '', content)
    
    # Remove @Option annotations and their fields (multi-line)
    content = re.sub(
        r'    @Option\([^)]+\)\s+private [^;]+;\s*\n\s*\n',
        '',
        content,
        flags=re.MULTILINE | re.DOTALL
    )
    
    # Also handle single-line @Option
    content = re.sub(
        r'    @Option\([^)]+\)\s+private [^;]+;\s*\n',
        '',
        content,
        flags=re.MULTILINE
    )
    
    with open(filepath, 'w') as f:
        f.write(content)
    
    print(f"Processed: {filepath}")

command_files = glob.glob('/Users/lichee/Documents/GitHub/openolat-osgi/course-component/src/main/java/my/um/cbse/course/command/*Command.java')

for filepath in command_files:
    remove_options(filepath)

print("\nDone! You'll need to manually fix any execute() methods that reference removed fields.")
